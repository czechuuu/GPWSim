package tests;

import investors.RandomChoiceInvestor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import requests.ATradeRequest;
import simulation.StockExchangeSimulation;
import stocks.Stock;
import utilities.Parser;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class RandomChoiceInvestorTest {
    static final int TEST_REPEATS = 100;
    static final double NULL_THRESHOLD = 0.3;
    static StockExchangeSimulation simulation;
    static Parser parser;

    @BeforeAll
    public static void setUp() throws IOException {
        parser = new Parser(TestPaths.MOODLE_TEST_PATH);
        simulation = new StockExchangeSimulation(10, parser);

    }

    @AfterAll
    public static void checkNullCount() {
        double nullRate = (double) NullCountHolder.getNullCount() / TEST_REPEATS;
        System.out.println("Null return rate: " + nullRate);
        assertTrue(nullRate <= NULL_THRESHOLD, "Null return rate exceeded threshold");
    }

    /**
     * Test the makeTradeDecision method of the RandomChoiceInvestor class
     * also checks how often the method returns null
     */
    @RepeatedTest(TEST_REPEATS)
    public void testMakeTradeDecision() {
        // Create a RandomChoiceInvestor instance
        RandomChoiceInvestor investor = createRandomChoiceInvestor();

        // Call the makeTradeDecision method
        ATradeRequest tradeRequest = investor.makeTradeDecision(simulation);

        // If tradeRequest is null, the investor didn't have enough money or stocks to trade, which is a valid scenario
        if (tradeRequest != null) {
            // Check if the tradeRequest is valid
            Stock stock = tradeRequest.getStock();
            int quantity = tradeRequest.getQuantity();
            int price = tradeRequest.getPriceLimit();


            if (tradeRequest.isBuyRequest()) {
                assertTrue(investor.canBuyStock(stock, quantity, price));
            } else {
                assertTrue(investor.canSellStock(stock, quantity, price));
            }
        } else {
            NullCountHolder.incrementNullCount();
        }
    }

    private RandomChoiceInvestor createRandomChoiceInvestor() {
        // Create a RandomChoiceInvestor instance
        return simulation.getInvestorManagement().createRandomChoiceInvestor(10000,
                simulation.getInvestorManagement().convertStocksPortfolio(parser.getInitialPortfolio()));
    }


    static class NullCountHolder {
        private static int nullCount = 0;

        static void incrementNullCount() {
            nullCount++;
        }

        static int getNullCount() {
            return nullCount;
        }
    }
}