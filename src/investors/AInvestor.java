package investors;

import requests.ATradeRequest;
import simulation.StockExchangeSimulation;
import stocks.Stock;

import java.util.HashMap;
import java.util.Map;

abstract public class AInvestor {
    private final int id;
    private final Map<Stock, Integer> stocksPortfolio;
    private int balance;

    /**
     * Create a new investor with the given id and balance.
     *
     * @param id      the id of the investor
     * @param balance the initial balance of the investor
     */
    public AInvestor(int id, int balance) {
        this.id = id;
        this.balance = balance;
        this.stocksPortfolio = new HashMap<>();
    }

    /**
     * Create a new investor with the given id, balance and stocks portfolio.
     *
     * @param id              the id of the investor
     * @param balance         the initial balance of the investor
     * @param stocksPortfolio the initial stocks portfolio of the investor
     */
    public AInvestor(int id, int balance, Map<Stock, Integer> stocksPortfolio) {
        this.id = id;
        this.balance = balance;
        this.stocksPortfolio = new HashMap<>(stocksPortfolio); // copy the map!!
    }

    /**
     * Get the stocks portfolio of the investor.
     *
     * @return the stocks portfolio of the investor
     */
    public Map<Stock, Integer> getStocksPortfolio() {
        return stocksPortfolio;
    }

    /**
     * Get the id of the investor.
     *
     * @return the id of the investor
     */
    public int getId() {
        return id;
    }

    /**
     * Get the balance of the investor.
     *
     * @return the balance of the investor
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Deposit the given amount to the investor's balance.
     *
     * @param amount the amount to deposit
     */
    private void deposit(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot deposit a negative amount");
        }
        balance += amount;
    }

    /**
     * Withdraw the given amount from the investor's balance.
     *
     * @param amount the amount to withdraw
     */
    private void withdraw(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot withdraw a negative amount");
        }
        if (amount > balance) {
            throw new InsufficientBalanceException("Cannot withdraw more than the current balance");
        }
        balance -= amount;
    }

    /**
     * Check if the investor can buy the stock with the given quantity and price.
     *
     * @param stock    the stock to buy (in case we will add anti Money Laundering or antitrust checks)
     * @param quantity the quantity of the stock to buy
     * @param price    the price of the stock
     * @return true if the investor can buy the stock, false otherwise
     */
    public boolean canBuyStock(Stock stock, int quantity, int price) {
        // check if the investor has enough balance to buy the stock
        int totalCost = quantity * price;
        return totalCost <= balance;
    }

    /**
     * Buy the stock with the given quantity and price.
     *
     * @param stock    the stock to buy
     * @param quantity the quantity of the stock to buy
     * @param price    the price of the stock
     */
    public void buyStock(Stock stock, int quantity, int price) {
        int totalCost = quantity * price;
        withdraw(totalCost);
        stocksPortfolio.put(stock, stocksPortfolio.getOrDefault(stock, 0) + quantity);
    }

    /**
     * Check if the investor can sell the stock with the given quantity and price.
     *
     * @param stock    the stock to sell
     * @param quantity the quantity of the stock to sell
     * @param price    the price of the stock
     * @return true if the investor can sell the stock, false otherwise
     */
    public boolean canSellStock(Stock stock, int quantity, int price) {
        if (!stocksPortfolio.containsKey(stock)) {
            return false;
        }
        return stocksPortfolio.get(stock) >= quantity;
    }

    /**
     * Sell the stock with the given quantity and price.
     *
     * @param stock    the stock to sell
     * @param quantity the quantity of the stock to sell
     * @param price    the price of the stock
     */
    public void sellStock(Stock stock, int quantity, int price) throws InsufficientBalanceException {
        if (!stocksPortfolio.containsKey(stock)) {
            throw new IllegalArgumentException("Cannot sell stock that is not in the portfolio");
        }
        if (stocksPortfolio.get(stock) < quantity) {
            throw new InsufficientBalanceException("Cannot sell more stock than is in the portfolio");
        }
        int totalCost = quantity * price;
        deposit(totalCost);
        stocksPortfolio.put(stock, stocksPortfolio.get(stock) - quantity);
    }

    /**
     * Get the quantity of the stock in the investor's portfolio.
     *
     * @param stock the stock to get the quantity of
     * @return the quantity of the stock in the investor's portfolio
     */
    public int getStockQuantity(Stock stock) {
        return stocksPortfolio.getOrDefault(stock, 0);
    }

    /**
     * Make a trade decision based on the given simulation.
     *
     * @param simulation the simulation to make the trade decision based on
     * @return the trade request to make, or null if no trade should be made
     */
    abstract public ATradeRequest makeTradeDecision(StockExchangeSimulation simulation);

}
