package investors;

import stocks.Stock;

import java.util.HashMap;
import java.util.Map;

abstract public class AInvestor {
    private final String name;
    private final Map<Stock, Integer> stocksPortfolio;
    private int balance;

    public AInvestor(String name, int balance) {
        this.name = name;
        this.balance = balance;
        this.stocksPortfolio = new HashMap<>();
    }

    public String getName() {
        return name;
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

    public boolean canBuyStock(Stock stock, int quantity, int price) {
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


}
