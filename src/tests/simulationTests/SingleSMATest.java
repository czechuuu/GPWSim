package tests.simulationTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulation.StockExchangeSimulation;
import utilities.Parser;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SingleSMATest {
    private Parser parser;

    @BeforeEach
    public void setUp() {
        // Create a new Parser instance
        try {
            parser = new Parser("src/tests/testFiles/genericTestFiles/singleSMA.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSingleSMA() {
        // Create a StockExchangeSimulation instance
        StockExchangeSimulation simulation = new StockExchangeSimulation(100, parser);
        // Run the simulation
        simulation.run();
        // Check if anything changed
        // It shouldn't because the SMA investor should not buy or sell anything without a signal
        // and since he's the only investor, the stock prices should not change, so he should not have any signals
        checkMoneyUnchanged(simulation, parser);
        checkStocksUnchanged(simulation, parser);
    }

    private void checkMoneyUnchanged(StockExchangeSimulation simulation, Parser parser) {
        // Check if he has the same amount of money as the initial cash
        int balanceAfterSimulation = simulation.getInvestorManagement().getInvestors().stream()
                .toList().get(0).getBalance();
        int totalMoneyBeforeSimulation = parser.getInitialCash();
        assertEquals(totalMoneyBeforeSimulation, balanceAfterSimulation);
    }

    private void checkStocksUnchanged(StockExchangeSimulation simulation, Parser parser) {
        // Check if he has the same stocks as the initial portfolio
        assertTrue(simulation.getInvestorManagement().getInvestors().stream().toList().get(0) // getting the only investor
                .getStocksPortfolio().entrySet().stream() // stream of his portfolio
                .allMatch(entry -> Objects.equals(entry.getValue(), parser.getInitialPortfolio().get(entry.getKey().getIdentifier()))));
    }
}
