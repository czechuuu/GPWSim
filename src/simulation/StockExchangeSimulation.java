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

    /**
     * Creates a new stock exchange simulation with the given number of rounds.
     *
     * @param totalRounds the total number of rounds
     */
    public StockExchangeSimulation(int totalRounds) {
        this.totalRounds = totalRounds;
        this.round = 0;
        this.stockManagement = new StockManagement();
        this.lastTradeData = new LastTradeData();
        this.investorManagement = new InvestorManagement(stockManagement);
    }

    /**
     * Creates a new stock exchange simulation with the given number of rounds and parser.
     *
     * @param totalRounds the total number of rounds
     * @param parser      the parser
     */
    public StockExchangeSimulation(int totalRounds, Parser parser) {
        this.totalRounds = totalRounds;
        this.round = 0;
        this.stockManagement = new StockManagement(parser);
        this.lastTradeData = new LastTradeData();
        this.investorManagement = new InvestorManagement(stockManagement, parser);
    }

    /**
     * Runs the stock exchange simulation.
     */
    public void run() {
        TradeRequestSheet tradeRequestSheet = new TradeRequestSheet();

        while (round < totalRounds) {
            // We update the last trade data
            for (Stock stock : stockManagement.getStocks()) {
                // we treat the last trade data of a round as the price of the stock at the end of the round
                lastTradeData.addTradeData(stock, stock.getLastPrice());
            }
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

            // and we update the SMA data
            lastTradeData.updateSMA();
            round++;
        }
    }

    /**
     * Get the investor management.
     *
     * @return the investor management
     */
    public InvestorManagement getInvestorManagement() {
        return investorManagement;
    }

    /**
     * Get the total number of rounds.
     *
     * @return the total number of rounds
     */
    public int getTotalRounds() {
        return totalRounds;
    }

    /**
     * Get the stock management.
     *
     * @return the stock management
     */
    public StockManagement getStockManagement() {
        return stockManagement;
    }

    /**
     * Get the current round.
     *
     * @return the current round
     */
    public int getRound() {
        return round;
    }

    /**
     * Get the last trade data.
     *
     * @return the last trade data
     */
    public LastTradeData getLastTradeData() {
        return lastTradeData;
    }
}
