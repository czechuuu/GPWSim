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

    public Parser(Path filePath) throws IOException {
        this(filePath.toString());
    }

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
    }

    public int getNumberOfRandomInvestors() {
        return numberOfRandomInvestors;
    }

    public int getNumberOfSMAInvestors() {
        return numberOfSMAInvestors;
    }

    public int getNumberOfInvestors() {
        return numberOfRandomInvestors + numberOfSMAInvestors;
    }

    public Map<String, Integer> getStockPrices() {
        return stockPrices;
    }

    public Map<String, Integer> getInitialPortfolio() {
        return initialPortfolio;
    }

    public int getInitialCash() {
        return initialCash;
    }

}
