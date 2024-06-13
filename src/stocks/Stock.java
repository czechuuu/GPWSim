package stocks;

public class Stock {
    private final String identifier;
    private int lastPrice;
    private int lastTradeRound;

    /**
     * Creates a new stock with the given identifier, last price, and last trade round.
     * Shouldn't be used directly, use stock management instead.
     *
     * @param identifier the identifier of the stock
     * @param lastPrice  the last price of the stock
     */
    public Stock(String identifier, int lastPrice, int lastTradeRound) {
        this.identifier = identifier;
        this.lastPrice = lastPrice;
        // set appropriate value for lastTradeRound at the beginning
        this.lastTradeRound = 0; // or -1?
    }

    /**
     * Updates the last transaction information of the stock.
     *
     * @param price      the price of the stock
     * @param tradeRound the trade round of the stock
     */
    public void updateLastTransactionInformation(int price, int tradeRound) {
        this.lastPrice = price;
        this.lastTradeRound = tradeRound;
    }

    /**
     * Get the identifier of the stock.
     *
     * @return the identifier of the stock
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Get the last price of the stock.
     *
     * @return the last price of the stock
     */
    public int getLastPrice() {
        return lastPrice;
    }

    /**
     * Get the last trade round of the stock.
     *
     * @return the last trade round of the stock
     */
    public int getLastTradeRound() {
        return lastTradeRound;
    }

    /**
     * Returns the price of the stock changed by up to the given maximum change.
     * Making sure that the price is always positive.
     *
     * @param maxChange the maximum change
     * @return the price of the stock changed by up to the given maximum change
     */
    public int priceChangedByUpTo(int maxChange) {
        if (lastPrice + maxChange <= 0)
            return lastPrice;
        else
            return lastPrice + maxChange;
    }
}
