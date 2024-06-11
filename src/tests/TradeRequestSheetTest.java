package tests;

import investors.AInvestor;
import investors.InvestorManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.ATradeRequest;
import simulation.StockExchangeSimulation;
import stocks.Stock;
import stocks.StockManagement;
import stocks.TradeRequestSheet;
import utilities.Parser;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TradeRequestSheetTest {
    private Parser parser;
    private StockManagement stockManagement;
    private InvestorManagement investorManagement;
    private AInvestor investor1;
    private AInvestor investor2;
    private TradeRequestSheet tradeRequestSheet;
    private Stock aplStock;
    private StockExchangeSimulation simulation;

    @BeforeEach
    public void setUp() throws IOException {
        parser = new Parser("src/tests/testFiles/moodle.txt");
        stockManagement = new StockManagement(parser);
        investorManagement = new InvestorManagement(stockManagement, parser);
        List<AInvestor> investors = investorManagement.getInvestorsInRandomOrder();
        investor1 = investors.get(0);
        investor2 = investors.get(1);
        // we will test trades between two different investors
        // although that should not change anything
        assertNotSame(investor1, investor2);
        tradeRequestSheet = new TradeRequestSheet();
        aplStock = stockManagement.getStock("APL");
        simulation = new StockExchangeSimulation(10);
    }

    @Test
    public void testSimpleTrade() {
        int initialBalance = parser.getInitialCash();
        int initialQty = 5;
        int qty = 5;
        int buyUpperLimit = 150;
        int sellLowerLimit = 140;
        int moneyDifference = qty * sellLowerLimit;
        // Add a buy request
        ATradeRequest aplBuyRequest = aplStock.createIndefiniteBuyRequest(investor1, qty, buyUpperLimit);
        tradeRequestSheet.addRequest(aplBuyRequest);

        // check if the request was added correctly to the buy list
        assertEquals(1, tradeRequestSheet.getBuyRequestsMap().get(aplStock).getList().size());
        assertEquals(aplBuyRequest, tradeRequestSheet.getBuyRequestsMap().get(aplStock).getList().get(0));
        // check if the sell list is empty but initialized under the stock key
        assertDoesNotThrow(() -> tradeRequestSheet.getSellRequestsMap().get(aplStock).getList());
        assertEquals(0, tradeRequestSheet.getSellRequestsMap().get(aplStock).getList().size());

        // Add a sell request
        ATradeRequest aplSellRequest = aplStock.createIndefiniteSellRequest(investor2, qty, sellLowerLimit);
        tradeRequestSheet.addRequest(aplSellRequest);

        // check if the request was added correctly to the sell list
        assertEquals(1, tradeRequestSheet.getSellRequestsMap().get(aplStock).getList().size());
        assertEquals(aplSellRequest, tradeRequestSheet.getSellRequestsMap().get(aplStock).getList().get(0));
        // check if the buy list is still the same
        assertEquals(1, tradeRequestSheet.getBuyRequestsMap().get(aplStock).getList().size());
        assertEquals(aplBuyRequest, tradeRequestSheet.getBuyRequestsMap().get(aplStock).getList().get(0));

        // commence the trade
        tradeRequestSheet.realiseSubmittedTrades(simulation);

        // check if the buy list is empty
        assertEquals(0, tradeRequestSheet.getBuyRequestsMap().get(aplStock).getList().size());
        // check if the sell list is empty
        assertEquals(0, tradeRequestSheet.getSellRequestsMap().get(aplStock).getList().size());
        //check if the stock price has changed
        assertEquals(140, aplStock.getLastPrice());
        // check if the investors' balances have changed
        assertEquals(initialBalance - moneyDifference, investor1.getBalance());
        assertEquals(initialBalance + moneyDifference, investor2.getBalance());
        // check if the investors' stock portfolios have changed
        assertEquals(initialQty + qty, investor1.getStockQuantity(aplStock));
        assertEquals(initialQty - qty, investor2.getStockQuantity(aplStock));
    }

    // TODO DOESNT PASS
    // PROLLY wrong lop nestings or idk
    // sold only the first one?
    @Test
    public void testPartialTrade() {
        int initialBalance = parser.getInitialCash();
        int initialQty = 5;
        int buyQty = 10;
        int sellQty1 = 1;
        int sellQty2 = 2;
        int buyUpperLimit = 150;
        int sellLowerLimit1 = 135;
        int sellLowerLimit2 = 140;

        int moneyDifference = sellQty1 * sellLowerLimit1 + sellQty2 * sellLowerLimit2;
        // Add a buy request
        ATradeRequest aplBuyRequest = aplStock.createIndefiniteBuyRequest(investor1, buyQty, buyUpperLimit);
        tradeRequestSheet.addRequest(aplBuyRequest);

        // Add a sell request 1
        ATradeRequest aplSellRequest1 = aplStock.createIndefiniteSellRequest(investor2, sellQty1, sellLowerLimit1);
        tradeRequestSheet.addRequest(aplSellRequest1);
        // Add a sell request 2
        ATradeRequest aplSellRequest2 = aplStock.createIndefiniteSellRequest(investor2, sellQty2, sellLowerLimit2);
        tradeRequestSheet.addRequest(aplSellRequest2);

        // commence the trade
        tradeRequestSheet.realiseSubmittedTrades(simulation);

        // check if the buy request quantity has been reduced
        assertEquals(buyQty - sellQty1 - sellQty2, aplBuyRequest.getQuantity());
        // check if the sell list is empty
        assertEquals(0, tradeRequestSheet.getSellRequestsMap().get(aplStock).getList().size());
        //check if the stock price has changed
        assertEquals(140, aplStock.getLastPrice());
        // check if the investors' balances have changed
        assertEquals(initialBalance - moneyDifference, investor1.getBalance());
        assertEquals(initialBalance + moneyDifference, investor2.getBalance());
        // check if the investors' stock portfolios have changed
        assertEquals(initialQty + sellQty1 + sellQty2, investor1.getStockQuantity(aplStock));
        assertEquals(initialQty - sellQty1 - sellQty2, investor2.getStockQuantity(aplStock));
    }


}
