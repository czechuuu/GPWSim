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
    // Constructor for the investor
    public ATradeRequest(AInvestor investor, Stock stock, int quantity, int priceLimit, TradeType tradeType, int id) {
        this.investor = investor;
        this.stock = stock;
        this.quantity = quantity;
        this.tradeType = tradeType;
        this.priceLimit = priceLimit;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    abstract public boolean expiredAndShouldBeDeleted(int currentRound);

    public void reduceQuantity(int quantity) {
        if (quantity > this.quantity) {
            throw new IllegalArgumentException("Cannot reduce quantity by more than the current quantity");
        }
        this.quantity -= quantity;
    }

    public boolean considerTrade(ATradeRequest other) {
        if (this.isBuyRequest() && other.isSellRequest() && this.getPriceLimit() >= other.getPriceLimit()) {
            return true;
        } else if (this.isSellRequest() && other.isBuyRequest() && this.getPriceLimit() <= other.getPriceLimit()) {
            return true;
        }
        return false;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isBuyRequest() {
        return tradeType == TradeType.BUY;
    }

    public boolean isSellRequest() {
        return tradeType == TradeType.SELL;
    }

    public int getPriceLimit() {
        return priceLimit;
    }

    public AInvestor getInvestor() {
        return investor;
    }

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
