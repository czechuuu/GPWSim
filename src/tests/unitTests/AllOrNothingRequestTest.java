package tests.unitTests;

import investors.AInvestor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.ATradeRequest;
import requests.AllOrNothingTradeRequest;
import requests.RequestManagement;
import simulation.StockExchangeSimulation;
import stocks.Stock;
import stocks.StockManagement;
import stocks.TradeRequestSheet;
import utilities.Parser;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AllOrNothingRequestTest {
    private StockManagement stockManagement;
    private Stock stock;
    private AllOrNothingTradeRequest tradeRequest;
    private AInvestor investor;
    private TradeRequestSheet tradeRequestSheet;
    private StockExchangeSimulation sim;

    @BeforeEach
    public void setUp() throws IOException {
        Parser parser = new Parser(TestPaths.MOODLE_TEST_PATH);
        sim = new StockExchangeSimulation(10, parser);
        investor = sim.getInvestorManagement().getInvestors().stream().findFirst().orElse(null);
        stockManagement = sim.getStockManagement();
        stock = stockManagement.getStocks().stream().findFirst().orElse(null);
        tradeRequestSheet = new TradeRequestSheet();
    }

    @Test
    public void testTradeCanBeRealizedFully() {
        // we want to buy 5 stocks
        AllOrNothingTradeRequest allBuyRequest = RequestManagement.createAllOrNothingTradeRequest(investor, stock, 5, 100, ATradeRequest.TradeType.BUY);
        tradeRequestSheet.addRequest(allBuyRequest);
        // and have 2 + 3 stocks to sell
        tradeRequestSheet.addRequest(RequestManagement.createIndefiniteTradeRequest(investor, stock, 2, 100, ATradeRequest.TradeType.SELL));
        tradeRequestSheet.addRequest(RequestManagement.createIndefiniteTradeRequest(investor, stock, 3, 100, ATradeRequest.TradeType.SELL));
        tradeRequestSheet.realiseSubmittedTrades(sim);

        // the buy request should be realized fully
        assertTrue(tradeRequestSheet.getBuyRequestsMap().get(stock).getList().isEmpty());
        // the sell requests should be realized fully
        assertTrue(tradeRequestSheet.getSellRequestsMap().get(stock).getList().isEmpty());
    }

    @Test
    public void testTradeCanNotBeRealizedFullyBecauseTooFewSellRequests() {
        // we want to buy 5 stocks
        AllOrNothingTradeRequest allBuyRequest = RequestManagement.createAllOrNothingTradeRequest(investor, stock, 5, 100, ATradeRequest.TradeType.BUY);
        tradeRequestSheet.addRequest(allBuyRequest);
        // and have only 2 stocks to sell
        tradeRequestSheet.addRequest(RequestManagement.createIndefiniteTradeRequest(investor, stock, 2, 100, ATradeRequest.TradeType.SELL));
        // so the trade shouldn't happen
        tradeRequestSheet.realiseSubmittedTrades(sim);

        // but the buy request should be deleted
        assertTrue(tradeRequestSheet.getBuyRequestsMap().get(stock).getList().isEmpty());
        // the sell requests should be left untouched
        assertEquals(1, tradeRequestSheet.getSellRequestsMap().get(stock).getList().size());
    }

    @Test
    public void testTradeCanNotBeRealizedFullyBecauseTooExpensiveSellRequests() {
        // we want to buy 5 stocks
        AllOrNothingTradeRequest allBuyRequest = RequestManagement.createAllOrNothingTradeRequest(investor, stock, 5, 100, ATradeRequest.TradeType.BUY);
        tradeRequestSheet.addRequest(allBuyRequest);
        // we have 2+3 stocks to sell
        // but 3 of them are too expensive
        tradeRequestSheet.addRequest(RequestManagement.createIndefiniteTradeRequest(investor, stock, 2, 100, ATradeRequest.TradeType.SELL));
        tradeRequestSheet.addRequest(RequestManagement.createIndefiniteTradeRequest(investor, stock, 2, 150, ATradeRequest.TradeType.SELL));
        // so the trade shouldn't happen
        tradeRequestSheet.realiseSubmittedTrades(sim);

        // but the buy request should be deleted
        assertTrue(tradeRequestSheet.getBuyRequestsMap().get(stock).getList().isEmpty());
        // the sell requests should be left untouched
        assertEquals(2, tradeRequestSheet.getSellRequestsMap().get(stock).getList().size());
    }
}