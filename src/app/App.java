package app;

import simulation.StockExchangeSimulation;
import utilities.EventLogging;
import utilities.Parser;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
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
        // TODO 1: implement price choice method to avoid prices sinking dramatically
        // TODO 2: fix SMA: currently he never does anything

    }

    private static void printFinalState(StockExchangeSimulation simulation) {
        for (var investor : simulation.getInvestorManagement().getInvestors()) {
            System.out.println(investor + " has a balance of " + EventLogging.Color.green(String.valueOf(investor.getBalance())));
            System.out.println("and the following stocks:");
            for (var stock : investor.getStocksPortfolio().keySet()) {
                System.out.println("    " + EventLogging.Color.blue(stock.getIdentifier()) + ":"
                        + EventLogging.Color.yellow(String.valueOf(investor.getStocksPortfolio().get(stock))));
            }
        }
    }
}
