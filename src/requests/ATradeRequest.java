package requests;

import investors.AInvestor;
import stocks.Stock;

abstract public class ATradeRequest {
    private final AInvestor investor;
    private final Stock stock;
    private final TradeType tradeType;
    /**
     * The price limit for the trade request. If the trade request is a BUY request, the price limit is the maximum price the buyer is willing to pay.
     * If the trade request is a SELL request, the price limit is the minimum price the seller is willing to accept.
     */
    private final int priceLimit;
    private int quantity;


    // Constructor for the investor
    public ATradeRequest(AInvestor investor, Stock stock, int quantity, int priceLimit, TradeType tradeType) {
        this.investor = investor;
        this.stock = stock;
        this.quantity = quantity;
        this.tradeType = tradeType;
        this.priceLimit = priceLimit;
    }

    public void reduceQuantity(int quantity) {
        if (quantity > this.quantity) {
            throw new IllegalArgumentException("Cannot reduce quantity by more than the current quantity");
        }
        this.quantity -= quantity;
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

    public enum TradeType {
        BUY,
        SELL
    }
}
