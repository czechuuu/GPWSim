package requests;

abstract public class TradeRequest {
    private final String stockSymbol;
    private final int quantity;
    private final TradeType tradeType;

    /**
     * The price limit for the trade request. If the trade request is a BUY request, the price limit is the maximum price the buyer is willing to pay.
     * If the trade request is a SELL request, the price limit is the minimum price the seller is willing to accept.
     */
    private final int priceLimit;

    public TradeRequest(String stockSymbol, int quantity, int priceLimit, TradeType tradeType) {
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.tradeType = tradeType;
        this.priceLimit = priceLimit;
    }

    public String getStockSymbol() {
        return stockSymbol;
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

    public enum TradeType {
        BUY,
        SELL
    }
}
