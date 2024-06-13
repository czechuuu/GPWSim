package tests.simulationTests;

import investors.AInvestor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import simulation.StockExchangeSimulation;
import stocks.Stock;
import utilities.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleInvariantTest {
    private static final int ROUNDS = 100;

    static Stream<Parser> provideParsersForTests() throws IOException {
        Path testFilesPath = Path.of("src", "tests", "testFiles");
        return Files.walk(testFilesPath)
                .filter(Files::isRegularFile)
                .map(path -> {
                    try {
                        return new Parser(path.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                });
    }

    /**
     * Test the simple invariant of the simulation.
     * That is:
     * - The total money in the system is constant
     * - The total number of stocks in the system is constant
     *
     * @param parser the parser
     */
    @ParameterizedTest
    @MethodSource("provideParsersForTests")
    public void testSimpleInvariant(Parser parser) {
        // Create a StockExchangeSimulation instance
        StockExchangeSimulation simulation = new StockExchangeSimulation(ROUNDS, parser);
        // Run the simulation
        simulation.run();
        // Check if the total money in the system is constant
        checkConstantMoney(simulation, parser);
        // Check if the total number of stocks in the system is constant
        checkConstantStocks(simulation, parser);

    }

    private void checkConstantMoney(StockExchangeSimulation simulation, Parser parser) {
        // Sum the balances across all investors
        int totalMoneyAfterSimulation = simulation.getInvestorManagement().getInvestors().stream()
                .mapToInt(AInvestor::getBalance).sum();
        // Sum initial balances
        int numberOfInvestors = parser.getNumberOfInvestors();
        int totalMoneyBeforeSimulation = parser.getInitialCash() * numberOfInvestors;
        assertEquals(totalMoneyBeforeSimulation, totalMoneyAfterSimulation);
    }

    private void checkConstantStocks(StockExchangeSimulation simulation, Parser parser) {
        Map<Stock, Integer> initialPortfolio = simulation.getInvestorManagement()
                .convertStocksPortfolio(parser.getInitialPortfolio());
        // For each stock in the initial portfolio
        // Check if the total number of stocks in the system is constant
        for (Stock stock : initialPortfolio.keySet()) {
            // Sum the quantities of each stock across all investors
            int totalStocksAfterSimulation = simulation.getInvestorManagement().getInvestors().stream()
                    .mapToInt(investor -> investor.getStocksPortfolio().getOrDefault(stock, 0)).sum();
            // Sum initial quantities
            int totalStocksBeforeSimulation = initialPortfolio.get(stock) * parser.getNumberOfInvestors();
            assertEquals(totalStocksBeforeSimulation, totalStocksAfterSimulation);
        }
    }
}
