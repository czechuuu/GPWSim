package simulation;

import investors.InvestorManagement;
import stocks.StockManagement;
import utilities.Parser;

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
