package stocks;

import investors.AInvestor;
import requests.ATradeRequest;
import requests.IndefiniteTradeRequest;
import requests.InstantTradeRequest;

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


    public void updateLastTransactionInformation(int price, int tradeRound) {
        this.lastPrice = price;
        this.lastTradeRound = tradeRound;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getLastPrice() {
        return lastPrice;
    }

    public int getLastTradeRound() {
        return lastTradeRound;
    }

    public ATradeRequest createIndefiniteBuyRequest(AInvestor investor, int quantity, int priceLimit) {
        return new IndefiniteTradeRequest(investor, this, quantity, priceLimit, ATradeRequest.TradeType.BUY);
    }

    public ATradeRequest createIndefiniteSellRequest(AInvestor investor, int quantity, int priceLimit) {
        return new IndefiniteTradeRequest(investor, this, quantity, priceLimit, ATradeRequest.TradeType.SELL);
    }

    public ATradeRequest createInstantBuyRequest(AInvestor investor, int quantity, int priceLimit) {
        return new InstantTradeRequest(investor, this, quantity, priceLimit, ATradeRequest.TradeType.BUY);
    }

    public ATradeRequest createInstantSellRequest(AInvestor investor, int quantity, int priceLimit) {
        return new InstantTradeRequest(investor, this, quantity, priceLimit, ATradeRequest.TradeType.SELL);
    }

    // TODO rest od creators or maybe better way tyo not write duplicate code


}
