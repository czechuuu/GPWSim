package investors;

import requests.ATradeRequest;
import requests.IndefiniteTradeRequest;
import simulation.StockExchangeSimulation;
import stocks.Stock;

import java.util.Map;


public class SMAInvestor extends AInvestor {
    // The difference between the 5-day and 10-day Simple Moving Average (SMA) of the stock
    private int lastSMADifference;

    public SMAInvestor(int name, int balance) {
        super(name, balance);
    }

    public SMAInvestor(int name, int balance, Map<Stock, Integer> stocksPortfolio) {
        super(name, balance, stocksPortfolio);
        lastSMADifference = 0;
    }

    @Override
    public ATradeRequest makeTradeDecision(StockExchangeSimulation stockExchangeSimulation) {
        if (stockExchangeSimulation.getRound() < 10) {
            return null;
        }
        for (Stock stock : getStocksPortfolio().keySet()) {
            int currentSMADifference = (int) (stockExchangeSimulation.getLastTradeData().getSMA(stock, 5) - stockExchangeSimulation.getLastTradeData().getSMA(stock, 10));
            if (currentSMADifference * lastSMADifference < 0 && Math.abs(currentSMADifference) < 0) {
                return new IndefiniteTradeRequest(this, stock, stock, 1);
            }
        }
    }

}
