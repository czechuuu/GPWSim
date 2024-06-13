package app;

import simulation.StockExchangeSimulation;
import utilities.EventLogging;
import utilities.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class App {
    public static void main(String[] args) {
        runSimulationFromAFile(args);
//        EventLogging.setLoggingEnabled(false);
//        try {
//            runExampleSimulations();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private static void runSimulationFromAFile(String[] args) {
        assert args.length == 2 : "<executable> <file path> <simulation length>";
        String filePath = args[0];
        int simulationLength = Integer.parseInt(args[1]);
        // Create a new Parser instance
        Parser parser;
        try {
            parser = new Parser(filePath);
        } catch (IOException e) {
            System.out.println("File opening error: " + e.getMessage());
            System.exit(1);
            return; // to avoid the uninitialized variable warning
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid file format: " + e.getMessage());
            System.exit(1);
            return; // to avoid the uninitialized variable warning
        }

        // Create a StockExchangeSimulation instance
        StockExchangeSimulation simulation = new StockExchangeSimulation(simulationLength, parser);

        // Run the simulation
        simulation.run();

        // Print the final state after the simulation
        printFinalState(simulation);
        // LOOKING AT THE RESULTS
        // TODO 2: random exceptions? division by zero and withdrawing negative amounts?
        // TODO test single SMA
        // TODO some tests for the test files
        // maybe integer overflows?
    }

    private static void printFinalState(StockExchangeSimulation simulation) {
        // Stock prices
        System.out.println("Stock prices: ");
        System.out.print("    ");
        for (var stock : simulation.getStockManagement().getStocks()) {
            System.out.print(EventLogging.Color.blue(stock.getIdentifier()) + ":"
                    + EventLogging.Color.green(String.valueOf(stock.getLastPrice())) + " ");
        }
        System.out.println();
        // Investor balances and stocks
        for (var investor : simulation.getInvestorManagement().getInvestors()) {
            int netWorth = investor.getStocksPortfolio().keySet().stream()
                    .mapToInt(stock -> stock.getLastPrice() * investor.getStocksPortfolio().get(stock)).sum()
                    + investor.getBalance();
            System.out.println(investor + " has a net worth of " + EventLogging.Color.purple(String.valueOf(netWorth)));
            System.out.println("with " + EventLogging.Color.green(String.valueOf(investor.getBalance())) + " in cash and the following stocks:");
            for (var stock : investor.getStocksPortfolio().keySet()) {
                System.out.println("    " + EventLogging.Color.blue(stock.getIdentifier()) + ":"
                        + EventLogging.Color.yellow(String.valueOf(investor.getStocksPortfolio().get(stock))));
            }
        }
        // Calculated net worth of each investor


    }

    private static void runExampleSimulations() throws IOException {
        // simulations can't be too long or we can cause integer overflows
        final int ROUNDS = 1000;
        Path testFilesPath = Path.of("src", "tests", "testFiles");
        List<Path> paths = Files.walk(testFilesPath).filter(Files::isRegularFile).toList();
        for (Path path : paths) {
            Parser parser = new Parser(path.toString());
            StockExchangeSimulation simulation = new StockExchangeSimulation(ROUNDS, parser);
            simulation.run();
            System.out.println(EventLogging.Color.red("Results for " + path.getFileName()) + ": ");
            printFinalState(simulation);
        }

    }
}
