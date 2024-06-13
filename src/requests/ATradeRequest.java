package requests;

import investors.AInvestor;
import stocks.Stock;
import utilities.EventLogging;

abstract public class ATradeRequest {
    private final AInvestor investor;
    private final Stock stock;
    private final TradeType tradeType;
    private final int id;
    /**
     * The price limit for the trade request. If the trade request is a BUY request, the price limit is the maximum price the buyer is willing to pay.
     * If the trade request is a SELL request, the price limit is the minimum price the seller is willing to accept.
     */
    private final int priceLimit;
    private int quantity;

    /**
     * Creates a new trade request with the given investor, stock, quantity, price limit, trade type, and id.
     *
     * @param investor   the investor
     * @param stock      the stock
     * @param quantity   the quantity
     * @param priceLimit the price limit
     * @param tradeType  the trade type
     * @param id         the id
     */
    public ATradeRequest(AInvestor investor, Stock stock, int quantity, int priceLimit, TradeType tradeType, int id) {
        this.investor = investor;
        this.stock = stock;
        this.quantity = quantity;
        this.tradeType = tradeType;
        this.priceLimit = priceLimit;
        this.id = id;
    }

    /**
     * Get the id of the trade request.
     *
     * @return the id of the trade request
     */
    public int getId() {
        return id;
    }

    /**
     * Check if the trade request has expired and should be deleted.
     *
     * @param currentRound the current round
     * @return true if the trade request has expired and should be deleted, false otherwise
     */
    abstract public boolean expiredAndShouldBeDeleted(int currentRound);

    /**
     * Reduce the quantity of the trade request by the given quantity.
     *
     * @param quantity the quantity to reduce by
     */
    public void reduceQuantity(int quantity) {
        if (quantity > this.quantity) {
            throw new IllegalArgumentException("Cannot reduce quantity by more than the current quantity");
        }
        this.quantity -= quantity;
    }

    /**
     * Check if the trade request can be considered for a trade with the given other trade request.
     *
     * @param other the other trade request
     * @return true if the trade request can be considered for a trade with the other trade request, false otherwise
     */
    public boolean considerTrade(ATradeRequest other) {
        if (this.isBuyRequest() && other.isSellRequest() && this.getPriceLimit() >= other.getPriceLimit()) {
            return true;
        } else if (this.isSellRequest() && other.isBuyRequest() && this.getPriceLimit() <= other.getPriceLimit()) {
            return true;
        }
        return false;
    }

    /**
     * Get the stock of the trade request.
     *
     * @return the stock of the trade request
     */
    public Stock getStock() {
        return stock;
    }

    /**
     * Get the quantity of the trade request.
     *
     * @return the quantity of the trade request
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Check if the trade request is a buy request.
     *
     * @return true if the trade request is a buy request, false otherwise
     */
    public boolean isBuyRequest() {
        return tradeType == TradeType.BUY;
    }

    /**
     * Check if the trade request is a sell request.
     *
     * @return true if the trade request is a sell request, false otherwise
     */
    public boolean isSellRequest() {
        return tradeType == TradeType.SELL;
    }

    /**
     * Get the price limit of the trade request.
     *
     * @return the price limit of the trade request
     */
    public int getPriceLimit() {
        return priceLimit;
    }

    /**
     * Get the investor of the trade request.
     *
     * @return the investor of the trade request
     */
    public AInvestor getInvestor() {
        return investor;
    }

    /**
     * Returns the string representation of the trade request.
     * Uses colors for the stock identifier, quantity, and price limit.
     *
     * @return the string representation of the trade request
     */
    @Override
    public String toString() {
        return "Trade request for " + EventLogging.Color.blue(stock.getIdentifier()) + " by "
                + investor + " for " + EventLogging.Color.yellow(String.valueOf(quantity))
                + " stocks at " + EventLogging.Color.green(String.valueOf(priceLimit));
    }

    public enum TradeType {
        BUY,
        SELL
    }
}
