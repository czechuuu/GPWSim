package investors;


import stocks.Stock;
import stocks.StockManagement;
import utilities.Parser;

import java.util.*;

public class InvestorManagement {
    private final Set<AInvestor> investors;
    private final StockManagement stockManagement;
    private int nextID;

    /**
     * Create a new investor management with the given stock management.
     *
     * @param stockManagement the stock management to use
     */
    public InvestorManagement(StockManagement stockManagement) {
        this.investors = new HashSet<>();
        this.stockManagement = stockManagement;
        nextID = 0;
    }

    /**
     * Create a new investor management with the given stock management and parser.
     *
     * @param stockManagement the stock management to use
     * @param parser          the parser to use
     */
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

    /**
     * Get the investors.
     *
     * @return the investors
     */
    public Set<AInvestor> getInvestors() {
        return investors;
    }

    /**
     * Create a new SMA investor with the given balance and stocks portfolio.
     *
     * @param balance         the initial balance of the investor
     * @param stocksPortfolio the initial stocks portfolio of the investor
     * @return the created investor
     */
    public SMAInvestor createSMAInvestor(int balance, Map<Stock, Integer> stocksPortfolio) {
        SMAInvestor investor = new SMAInvestor(nextID++, balance, stocksPortfolio);
        investors.add(investor);
        return investor;
    }

    /**
     * Create a new random choice investor with the given balance and stocks portfolio.
     *
     * @param balance         the initial balance of the investor
     * @param stocksPortfolio the initial stocks portfolio of the investor
     * @return the created investor
     */
    public RandomChoiceInvestor createRandomChoiceInvestor(int balance, Map<Stock, Integer> stocksPortfolio) {
        RandomChoiceInvestor investor = new RandomChoiceInvestor(nextID++, balance, stocksPortfolio);
        investors.add(investor);
        return investor;
    }

    /**
     * Create a new random choice investor with the given balance and stocks portfolio.
     *
     * @param balance the initial balance of the investor
     * @return the created investor
     */
    public RandomChoiceInvestor createRandomChoiceInvestor(int balance) {
        return createRandomChoiceInvestor(balance, new HashMap<>());
    }

    /**
     * Create a new random choice investor with the given balance and stocks portfolio.
     *
     * @param balance the initial balance of the investor
     * @return the created investor
     */
    public SMAInvestor createSMAInvestor(int balance) {
        return createSMAInvestor(balance, new HashMap<>());
    }

    /**
     * Get the stock management used.
     *
     * @return the stock management used
     */
    public StockManagement getStockManagement() {
        return stockManagement;
    }

    /**
     * Get the next investor ID.
     *
     * @return the next investor ID
     */
    public int getNextID() {
        return nextID;
    }

    /**
     * Set the next investor ID.
     *
     * @param nextID the next investor ID to set
     */
    public void setNextID(int nextID) {
        this.nextID = nextID;
    }

    /**
     * Get the investor with the given ID.
     *
     * @param id the ID of the investor to get
     * @return the investor with the given ID, or null if no such investor exists
     */
    public AInvestor getInvestor(int id) {
        for (AInvestor investor : investors) {
            if (investor.getId() == id) {
                return investor;
            }
        }
        return null;
    }

    /**
     * Convert the given stocks portfolio to the stock-integer map representation.
     *
     * @param stocksPortfolio the stocks portfolio to convert
     * @return the stock-integer map representation of the given stocks portfolio
     */
    public Map<Stock, Integer> convertStocksPortfolio(Map<String, Integer> stocksPortfolio) {
        Map<Stock, Integer> convertedStocksPortfolio = new HashMap<>();
        for (Map.Entry<String, Integer> entry : stocksPortfolio.entrySet()) {
            Stock stock = stockManagement.getStock(entry.getKey());
            convertedStocksPortfolio.put(stock, entry.getValue());
        }
        return convertedStocksPortfolio;
    }

    /**
     * Get the investors in random order.
     *
     * @return list of the investors in random order
     */
    public List<AInvestor> getInvestorsInRandomOrder() {
        List<AInvestor> investorsList = new ArrayList<>(investors);
        Collections.shuffle(investorsList);
        return investorsList;
    }

}