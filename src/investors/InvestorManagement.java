package investors;


import stocks.Stock;
import stocks.StockManagement;
import utilities.Parser;

import java.util.*;

public class InvestorManagement {
    private final Set<AInvestor> investors;
    private final StockManagement stockManagement;
    private int nextID;

    public InvestorManagement(StockManagement stockManagement) {
        this.investors = new HashSet<>();
        this.stockManagement = stockManagement;
        nextID = 0;
    }

    public InvestorManagement(StockManagement stockManagement, Parser parser) {
        this.investors = new HashSet<>();
        this.stockManagement = stockManagement;
        this.nextID = 0;

        Map<Stock, Integer> converted = convertStocksPortfolio(parser.getInitialPortfolio());
        for (int i = 0; i < parser.getNumberOfRandomInvestors(); i++) {
            createRandomChoiceInvestor(parser.getInitialCash(), converted);
        }
        for (int i = 0; i < parser.getNumberOfSMAInvestors(); i++) {
            createSMAInvestor(parser.getInitialCash(), converted);
        }
    }

    public Set<AInvestor> getInvestors() {
        return investors;
    }

    public AInvestor createSMAInvestor(int balance, Map<Stock, Integer> stocksPortfolio) {
        AInvestor investor = new SMAInvestor(nextID++, balance, stocksPortfolio);
        investors.add(investor);
        return investor;
    }

    public AInvestor createRandomChoiceInvestor(int balance, Map<Stock, Integer> stocksPortfolio) {
        AInvestor investor = new RandomChoiceInvestor(nextID++, balance, stocksPortfolio);
        investors.add(investor);
        return investor;
    }

    public Map<Stock, Integer> convertStocksPortfolio(Map<String, Integer> stocksPortfolio) {
        Map<Stock, Integer> convertedStocksPortfolio = new HashMap<>();
        for (Map.Entry<String, Integer> entry : stocksPortfolio.entrySet()) {
            Stock stock = stockManagement.getStock(entry.getKey());
            convertedStocksPortfolio.put(stock, entry.getValue());
        }
        return convertedStocksPortfolio;
    }

    public List<AInvestor> getInvestorsInRandomOrder() {
        List<AInvestor> investorsList = new ArrayList<>(investors);
        Collections.shuffle(investorsList);
        return investorsList;
    }

}