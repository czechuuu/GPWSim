package investors;

import requests.ATradeRequest;
import requests.RequestManagement;
import simulation.StockExchangeSimulation;
import stocks.Stock;

import java.util.Collection;
import java.util.Map;

import static requests.ATradeRequest.TradeType.BUY;
import static requests.ATradeRequest.TradeType.SELL;


public class SMAInvestor extends AInvestor {
    private static final int SMA_DIFFERENCE = 5;
    private static final int SINGAL_LENGTH = 10;

    /**
     * Creates a new SMA investor with the given name and balance and an empty portfolio.
     *
     * @param name    the name of the investor
     * @param balance the balance of the investor
     */
    public SMAInvestor(int name, int balance) {
        super(name, balance);
    }

    /**
     * Creates a new SMA investor with the given name, balance, and stocks portfolio.
     *
     * @param name            the name of the investor
     * @param balance         the balance of the investor
     * @param stocksPortfolio the stocks portfolio of the investor
     */
    public SMAInvestor(int name, int balance, Map<Stock, Integer> stocksPortfolio) {
        super(name, balance, stocksPortfolio);
    }

    /**
     * Makes a trade decision based on the given stock exchange simulation, using the SMA data.
     *
     * @param stockExchangeSimulation the stock exchange simulation
     * @return the trade request, or null if no trade should be made
     */
    @Override
    public ATradeRequest makeTradeDecision(StockExchangeSimulation stockExchangeSimulation) {
        if (stockExchangeSimulation.getRound() <= 10) {
            return null; // he doesn't have enough data to make a decision
        }


        // filer out stocks that are not in the portfolio
        // (the ones that the investor has 0 of)
        Collection<Stock> stocksInPossesion = getStocksPortfolio().entrySet().stream()
                .filter(entry -> entry.getValue() > 0).map(Map.Entry::getKey).toList();
        for (Stock stock : stocksInPossesion) {
            if (stockExchangeSimulation.getLastTradeData().checkIfSMASellSignal(stock)) {
                int qty = getStocksPortfolio().get(stock);
                int price = stock.priceChangedByUpTo(-SMA_DIFFERENCE); // to make it easier to sell
                int lastRoundValid = stockExchangeSimulation.getRound() + SINGAL_LENGTH;
                return RequestManagement.createValidUntilNthRoundTradeRequest(this, stock, qty, price, SELL, lastRoundValid);
            }
        }

        // filter out stocks that the investor cannot afford
        int balance = getBalance();
        Collection<Stock> stockThatCanBeAfforded = stockExchangeSimulation.getStockManagement().getStocks().stream()
                .filter(stock -> stock.getLastPrice() <= balance).toList();
        for (Stock stock : stockThatCanBeAfforded) {
            if (stockExchangeSimulation.getLastTradeData().checkIfSMABuySignal(stock)) {
                int qty = balance / stock.getLastPrice();
                int price = stock.getLastPrice(); // he wants to buy really fast, so he doesn't haggle
                int lastRoundValid = stockExchangeSimulation.getRound() + SINGAL_LENGTH;
                return RequestManagement.createValidUntilNthRoundTradeRequest(this, stock, qty, price, BUY, lastRoundValid);
            }
        }

        return null; // no signal received
    }

    @Override
    public String toString() {
        return "SMAInvestor " + getId();
    }
}
