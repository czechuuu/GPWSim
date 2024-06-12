package simulation;

import stocks.Stock;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class LastTradeData {
    private final Map<Stock, Queue<Integer>> lastTradeDataMap;
    private final int maxTradesRemembered;

    /**
     * Creates a new last trade data with the given number of trades remembered.
     *
     * @param maxTradesRemembered the number of trades remembered
     */
    public LastTradeData(int maxTradesRemembered) {
        this.lastTradeDataMap = new HashMap<>();
        this.maxTradesRemembered = maxTradesRemembered;
    }

    /**
     * Creates a new last trade data with a default of 10 trades remembered.
     */
    public LastTradeData() {
        this(10);
    }

    /**
     * Adds the given trade data to the last trade data of the given stock.
     *
     * @param stock the stock
     * @param price the price of the trade
     */
    public void addTradeData(Stock stock, int price) {
        lastTradeDataMap.computeIfAbsent(stock, k -> new LinkedList<>()).add(price);
        if (lastTradeDataMap.get(stock).size() > maxTradesRemembered) {
            lastTradeDataMap.get(stock).poll(); // weird name - pops head
        }
    }

    /**
     * Returns the simple moving average of the last n trades of the given stock.
     *
     * @param stock the stock
     * @param n     the number of trades to consider
     * @return the simple moving average of the last n trades of the given stock
     */
    public double getSMA(Stock stock, int n) {
        if (!lastTradeDataMap.containsKey(stock)) {
            return 0;
        }
        // TODO check if streams preserve order
        return lastTradeDataMap.get(stock).stream().limit(n).mapToInt(Integer::intValue).average().orElse(0);
    }
}
