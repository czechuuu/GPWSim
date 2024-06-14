package investors;

import requests.ATradeRequest;
import requests.RequestManagement;
import simulation.StockExchangeSimulation;
import stocks.Stock;
import utilities.RandomChoiceMachine;

import java.util.Collection;
import java.util.Map;

/**
 * For performance reasons, we don't want to use indefinite trade requests too much.
 */
public class RandomChoiceInvestor extends AInvestor {
    private final static int MAX_TRADE_VALIDITY = 10;
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

    /**
     * Randomly makes a trade decision for the current round.
     * For performance reasons all are valid for a random number of rounds between 1 and MAX_TRADE_VALIDITY.
     *
     * @param stockExchangeSimulation the stock exchange simulation
     * @return a trade request if the investor decides to make a trade, null otherwise
     */
    @Override
    public ATradeRequest makeTradeDecision(StockExchangeSimulation stockExchangeSimulation) {
        int round = stockExchangeSimulation.getRound();
        int howLongValid = (int) (Math.random() * MAX_TRADE_VALIDITY) + 1; // [1, MAX_TRADE_VALIDITY]
        int expiryRound = round + howLongValid;
        // buy or sell
        if (randomChoiceMachine.getRandomBoolean()) {
            Stock stock = randomChoiceMachine.getRandomElement(stockExchangeSimulation.getStockManagement().getStocks());
            int priceChange = (int) (Math.random() * 10) - 5; // [-5, 5]
            int price = stock.priceChangedByUpTo(priceChange);
            int maxQuantity = getBalance() / price;
            if (maxQuantity == 0)
                return null; // if the investor hasn't enough money, return null
            int quantity = (int) (Math.random() * maxQuantity) + 1; // [1, maxQuantity]
            assert canBuyStock(stock, quantity, price);
            return RequestManagement.createValidUntilNthRoundTradeRequest(this, stock, quantity, price, ATradeRequest.TradeType.BUY, expiryRound);
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
            int price = stock.priceChangedByUpTo(priceChange);
            assert canSellStock(stock, quantity, price);
            return RequestManagement.createValidUntilNthRoundTradeRequest(this, stock, quantity, price, ATradeRequest.TradeType.SELL, expiryRound);
        }

    }

    @Override
    public String toString() {
        return "Random Investor " + getId();
    }
}
