package simulation;

import investors.AInvestor;
import investors.InvestorManagement;
import requests.ATradeRequest;
import stocks.Stock;
import stocks.StockManagement;
import stocks.TradeRequestSheet;
import utilities.Parser;

import java.util.Collection;

public class StockExchangeSimulation {
    private final int totalRounds;
    private final StockManagement stockManagement;
    private final InvestorManagement investorManagement;
    private final LastTradeData lastTradeData;
    private int round;

    public StockExchangeSimulation(int totalRounds) {
        this.totalRounds = totalRounds;
        this.round = 0;
        this.stockManagement = new StockManagement();
        this.lastTradeData = new LastTradeData();
        this.investorManagement = new InvestorManagement(stockManagement);
    }

    public StockExchangeSimulation(int totalRounds, Parser parser) {
        this.totalRounds = totalRounds;
        this.round = 0;
        this.stockManagement = new StockManagement(parser);
        this.lastTradeData = new LastTradeData();
        this.investorManagement = new InvestorManagement(stockManagement, parser);
    }

    public void run() {
        TradeRequestSheet tradeRequestSheet = new TradeRequestSheet();

        while (round < totalRounds) {
            // We randomly shuffle the investors to avoid any bias
            Collection<AInvestor> investorsInRandomOrder = investorManagement.getInvestorsInRandomOrder();
            for (AInvestor investor : investorsInRandomOrder) {
                // We ask each investor to make a trade decision
                ATradeRequest possibleTradeRequest = investor.makeTradeDecision(this);
                if (possibleTradeRequest != null) {
                    // If the investor wants to make a trade, we add the request to the trade request sheet
                    tradeRequestSheet.addRequest(possibleTradeRequest);
                }
            }
            // If everyone has made their decisions, we realise the trades
            tradeRequestSheet.realiseSubmittedTrades(this);
            // We update the last trade data
            for (Stock stock : stockManagement.getStocks()) {
                // we treat the last trade data of a round as the price of the stock at the end of the round
                lastTradeData.addTradeData(stock, stock.getLastPrice());
            }
            // and we update the SMA data
            lastTradeData.updateSMA();
            round++;
        }
    }

    public InvestorManagement getInvestorManagement() {
        return investorManagement;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public StockManagement getStockManagement() {
        return stockManagement;
    }

    public int getRound() {
        return round;
    }

    public LastTradeData getLastTradeData() {
        return lastTradeData;
    }
}
