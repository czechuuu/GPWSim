package simulation;

import stocks.Stock;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class LastTradeData {
    private final Map<Stock, Queue<Integer>> lastTradeDataMap;
    private final Map<Stock, Double> lastSMA5Map;
    private final Map<Stock, Double> lastSMA10Map;
    private final int maxTradesRemembered;

    /**
     * Creates a new last trade data with the given number of trades remembered.
     *
     * @param maxTradesRemembered the number of trades remembered
     */
    public LastTradeData(int maxTradesRemembered) {
        this.lastTradeDataMap = new HashMap<>();
        this.maxTradesRemembered = maxTradesRemembered;
        this.lastSMA5Map = new HashMap<>();
        this.lastSMA10Map = new HashMap<>();
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
    private double getSMA(Stock stock, int n) {
        if (!lastTradeDataMap.containsKey(stock)) {
            return 0;
        }
        // TODO check if streams preserve order
        return lastTradeDataMap.get(stock).stream().limit(n).mapToInt(Integer::intValue).average().orElse(0);
    }

    public boolean checkIfSMABuySignal(Stock stock) {
        double currentSMA5 = getSMA(stock, 5);
        double currentSMA10 = getSMA(stock, 10);
        double lastSMA5 = lastSMA5Map.get(stock);
        double lastSMA10 = lastSMA10Map.get(stock);
        return currentSMA5 - currentSMA10 > 0 && lastSMA5 - lastSMA10 < 0;
    }

    public boolean checkIfSMASellSignal(Stock stock) {
        double currentSMA5 = getSMA(stock, 5);
        double currentSMA10 = getSMA(stock, 10);
        double lastSMA5 = lastSMA5Map.get(stock);
        double lastSMA10 = lastSMA10Map.get(stock);
        return currentSMA5 - currentSMA10 < 0 && lastSMA5 - lastSMA10 > 0;
    }

    public void updateSMA() {
        for (Stock stock : lastTradeDataMap.keySet()) {
            double currentSMA5 = getSMA(stock, 5);
            double currentSMA10 = getSMA(stock, 10);
            lastSMA5Map.put(stock, currentSMA5);
            lastSMA10Map.put(stock, currentSMA10);
        }
    }

    public int getSMA5(Stock stock) {
        return lastSMA5Map.get(stock).intValue();
    }

    public int getSMA10(Stock stock) {
        return lastSMA10Map.get(stock).intValue();
    }
}
