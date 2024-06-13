package stocks;

import utilities.Parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class StockManagement {
    private final Map<String, Stock> stockIdentifiers;

    /**
     * Creates a new stock management.
     */
    public StockManagement() {
        this.stockIdentifiers = new HashMap<>();
    }

    /**
     * Creates a new stock management with the given identifier-price map.
     *
     * @param identifierPriceMap the identifier-price map
     */
    public StockManagement(Map<String, Integer> identifierPriceMap) {
        this.stockIdentifiers = new HashMap<>();
        createStocksFromIdentifierPriceMap(identifierPriceMap);
    }

    /**
     * Creates a new stock management with the given parser.
     *
     * @param parser the parser
     */
    public StockManagement(Parser parser) {
        this(parser.getStockPrices());
    }

    /**
     * Creates a new stock with the given identifier, last price, and last trade round.
     *
     * @param identifier     the identifier of the stock
     * @param lastPrice      the last price of the stock
     * @param lastTradeRound the last trade round of the stock
     * @return the created stock
     * @throws IllegalArgumentException if a stock with the given identifier already exists
     */
    public Stock createStock(String identifier, int lastPrice, int lastTradeRound) {
        if (stockIdentifiers.containsKey(identifier)) {
            throw new IllegalArgumentException("Stock with identifier " + identifier + " already exists");
        }
        Stock stock = new Stock(identifier, lastPrice, lastTradeRound);
        stockIdentifiers.put(identifier, stock);
        return stock;
    }

    /**
     * Returns the stock with the given identifier.
     *
     * @param identifier the identifier of the stock
     * @return the stock with the given identifier
     * @throws NoSuchElementException if a stock with the given identifier does not exist
     */
    public Stock getStock(String identifier) {
        if (stockIdentifiers.containsKey(identifier)) {
            return stockIdentifiers.get(identifier);
        }
        throw new NoSuchElementException("Stock with identifier " + identifier + " does not exist");
    }

    /**
     * Returns all stocks.
     *
     * @return collection of all stocks
     */
    public Collection<Stock> getStocks() {
        return stockIdentifiers.values();
    }

    /**
     * Creates stocks from the given identifier-price map.
     *
     * @param identifierPriceMap the identifier-price map
     */
    private void createStocksFromIdentifierPriceMap(Map<String, Integer> identifierPriceMap) {
        for (Map.Entry<String, Integer> entry : identifierPriceMap.entrySet()) {
            createStock(entry.getKey(), entry.getValue(), 0);
        }
    }

    /**
     * Updates the stock price by the given identifier.
     *
     * @param identifier the identifier of the stock
     * @param price      the price of the stock
     * @param tradeRound the trade round of the stock
     */
    public void updateStockPriceByName(String identifier, int price, int tradeRound) {
        Stock stock = getStock(identifier);
        stock.updateLastTransactionInformation(price, tradeRound);
    }


}
