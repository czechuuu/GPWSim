package utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    Map<String, Integer> stockPrices;
    Map<String, Integer> initialPortfolio;
    private int numberOfRandomInvestors;
    private int numberOfSMAInvestors;
    private int initialCash;

    /**
     * Creates a new parser with the given file path.
     *
     * @param filePath the file path
     * @throws IOException              if an I/O error occurs
     * @throws IllegalArgumentException if the file format is invalid
     */
    public Parser(Path filePath) throws IOException, IllegalArgumentException {
        this(filePath.toString());
    }

    /**
     * Creates a new parser with the given String of a file path.
     *
     * @param filePath the file path as a String
     * @throws IOException              if an I/O error occurs
     * @throws IllegalArgumentException if the file format is invalid
     */
    public Parser(String filePath) throws IOException, IllegalArgumentException {
        String fileError = " in file" + filePath;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean investorTypesParsed = false;
            boolean stockPricesParsed = false;
            boolean initialPortfolioParsed = false;
            while ((line = br.readLine()) != null) {
                // Ignore comment lines
                if (line.startsWith("#")) {
                    continue;
                }

                // Parse investor types
                if (!investorTypesParsed) {
                    numberOfRandomInvestors = 0;
                    numberOfSMAInvestors = 0;
                    String[] types = line.split(" ");
                    for (String type : types) {
                        if (type.equals("R")) {
                            numberOfRandomInvestors++;
                        } else if (type.equals("S")) {
                            numberOfSMAInvestors++;
                        } else {
                            throw new IllegalArgumentException("Invalid investor type: " + type + fileError);
                        }
                    }
                    investorTypesParsed = true;
                }
                // Parse stock prices
                else if (!stockPricesParsed) {
                    stockPrices = new HashMap<>();
                    String[] stocks = line.split(" ");
                    for (String stock : stocks) {
                        String[] stockInfo = stock.split(":");
                        if (stockInfo.length != 2)
                            throw new IllegalArgumentException("Invalid stock price format" + fileError);
                        stockPrices.put(stockInfo[0], Integer.parseInt(stockInfo[1]));
                    }
                    stockPricesParsed = true;
                }
                // Parse initial portfolio
                else {
                    initialPortfolio = new HashMap<>();
                    String[] portfolio = line.split(" ");
                    initialCash = Integer.parseInt(portfolio[0]);
                    for (int i = 1; i < portfolio.length; i++) {
                        String[] stockInfo = portfolio[i].split(":");
                        if (stockInfo.length != 2)
                            throw new IllegalArgumentException("Invalid stock quantity format" + fileError);
                        initialPortfolio.put(stockInfo[0], Integer.parseInt(stockInfo[1]));
                    }
                }
            }
        }

        if (!stockPrices.keySet().containsAll(initialPortfolio.keySet()))
            throw new IllegalArgumentException("Invalid stock identifier in initial portfolio" + fileError);
    }

    /**
     * Returns the number of random investors.
     *
     * @return the number of random investors
     */
    public int getNumberOfRandomInvestors() {
        return numberOfRandomInvestors;
    }

    /**
     * Returns the number of SMA investors.
     *
     * @return the number of SMA investors
     */
    public int getNumberOfSMAInvestors() {
        return numberOfSMAInvestors;
    }

    /**
     * Returns the total number of investors.
     *
     * @return the total number of investors
     */
    public int getNumberOfInvestors() {
        return numberOfRandomInvestors + numberOfSMAInvestors;
    }

    /**
     * Returns the stock prices.
     *
     * @return the stock prices
     */
    public Map<String, Integer> getStockPrices() {
        return stockPrices;
    }

    /**
     * Returns the initial portfolio.
     *
     * @return the initial portfolio
     */
    public Map<String, Integer> getInitialPortfolio() {
        return initialPortfolio;
    }

    /**
     * Returns the initial cash.
     *
     * @return the initial cash
     */
    public int getInitialCash() {
        return initialCash;
    }

}
