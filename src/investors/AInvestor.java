package investors;

import requests.ATradeRequest;
import simulation.StockExchangeSimulation;
import stocks.Stock;

import java.util.HashMap;
import java.util.Map;

abstract public class AInvestor {
    private final int id;
    // TODO consider deleting when 0
    private final Map<Stock, Integer> stocksPortfolio;
    private int balance;

    public AInvestor(int id, int balance) {
        this.id = id;
        this.balance = balance;
        this.stocksPortfolio = new HashMap<>();
    }

    public AInvestor(int id, int balance, Map<Stock, Integer> stocksPortfolio) {
        this.id = id;
        this.balance = balance;
        this.stocksPortfolio = new HashMap<>(stocksPortfolio); // copy the map!!
    }

    public Map<Stock, Integer> getStocksPortfolio() {
        return stocksPortfolio;
    }

    public int getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    private void deposit(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot deposit a negative amount");
        }
        balance += amount;
    }

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

    public void buyStock(Stock stock, int quantity, int price) {
        int totalCost = quantity * price;
        withdraw(totalCost);
        stocksPortfolio.put(stock, stocksPortfolio.getOrDefault(stock, 0) + quantity);
    }

    public boolean canSellStock(Stock stock, int quantity, int price) {
        if (!stocksPortfolio.containsKey(stock)) {
            return false;
        }
        return stocksPortfolio.get(stock) >= quantity;
    }

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

    public int getStockQuantity(Stock stock) {
        return stocksPortfolio.getOrDefault(stock, 0);
    }

    abstract public ATradeRequest makeTradeDecision(StockExchangeSimulation simulation);

}
