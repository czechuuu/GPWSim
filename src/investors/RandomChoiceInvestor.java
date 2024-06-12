package investors;

import requests.ATradeRequest;
import requests.IndefiniteTradeRequest;
import simulation.StockExchangeSimulation;
import stocks.Stock;
import utilities.RandomChoiceMachine;

import java.util.Collection;
import java.util.Map;

public class RandomChoiceInvestor extends AInvestor {
    private final RandomChoiceMachine randomChoiceMachine;

    /**
     * Creates a new random choice investor with the given name and balance and an empty portfolio.
     *
     * @param name    the name of the investor
     * @param balance the balance of the investor
     */
    public RandomChoiceInvestor(int name, int balance) {
        super(name, balance);
        this.randomChoiceMachine = new RandomChoiceMachine();
    }

    /**
     * Creates a new random choice investor with the given name, balance, and stocks portfolio.
     *
     * @param name            the name of the investor
     * @param balance         the balance of the investor
     * @param stocksPortfolio the stocks portfolio of the investor
     */
    public RandomChoiceInvestor(int name, int balance, Map<Stock, Integer> stocksPortfolio) {
        super(name, balance, stocksPortfolio);
        this.randomChoiceMachine = new RandomChoiceMachine();
    }

    @Override
    public ATradeRequest makeTradeDecision(StockExchangeSimulation stockExchangeSimulation) {

        // buy or sell
        if (randomChoiceMachine.getRandomBoolean()) {
            Stock stock = randomChoiceMachine.getRandomElement(stockExchangeSimulation.getStockManagement().getStocks());
            int priceChange = (int) (Math.random() * 10) - 5; // [-5, 5]
            int price = stockExchangeSimulation.getStockManagement().getStock(stock.getIdentifier()).getLastPrice() + priceChange;
            int maxQuantity = getBalance() / price;
            if (maxQuantity == 0)
                return null; // if the investor hasn't enough money, return null
            int quantity = (int) (Math.random() * maxQuantity) + 1; // [1, maxQuantity]
            assert canBuyStock(stock, quantity, price);
            return new IndefiniteTradeRequest(this, stock, quantity, price, ATradeRequest.TradeType.BUY);
        } else {
            Collection<Stock> stocksInPortfolio = getStocksPortfolio().entrySet().stream()
                    .filter(entry -> entry.getValue() > 0).map(Map.Entry::getKey).toList();
            if (stocksInPortfolio.isEmpty()) {
                return null;
            }
            Stock stock = randomChoiceMachine.getRandomElement(stocksInPortfolio);
            int maxQuantity = getStocksPortfolio().get(stock);
            int quantity = (int) (Math.random() * maxQuantity) + 1; // [1, maxQuantity]
            int priceChange = (int) (Math.random() * 10) - 5; // [-5, 5]
            int price = stockExchangeSimulation.getStockManagement().getStock(stock.getIdentifier()).getLastPrice() + priceChange;
            assert canSellStock(stock, quantity, price);
            return new IndefiniteTradeRequest(this, stock, quantity, price, ATradeRequest.TradeType.SELL);
        }

    }


}
