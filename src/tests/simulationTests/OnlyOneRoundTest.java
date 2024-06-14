package tests.simulationTests;

import org.junit.jupiter.api.RepeatedTest;
import simulation.StockExchangeSimulation;
import stocks.Stock;
import tests.unitTests.TestPaths;
import utilities.Parser;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OnlyOneRoundTest {
    private static final int TEST_REPETITIONS = 100;

    @RepeatedTest(100)
    public void testOnlyOneRound() throws IOException {
        Parser parser = new Parser(TestPaths.BIG_TEST_PATH);
        // Create a StockExchangeSimulation instance
        StockExchangeSimulation simulation = new StockExchangeSimulation(1, parser);
        // Run the simulation
        simulation.run();
        // Check if the total money in the system is constant
        checkPriceDifferenceWithinBounds(simulation, parser);
    }

    public void checkPriceDifferenceWithinBounds(StockExchangeSimulation simulation, Parser parser) {
        for (Stock stock : simulation.getStockManagement().getStocks()) {
            int priceAfterRound = stock.getLastPrice();
            int priceBeforeRound = parser.getStockPrices().get(stock.getIdentifier());
            assertTrue(Math.abs(priceAfterRound - priceBeforeRound) <= 10);
        }
    }
}
