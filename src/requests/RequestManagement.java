package requests;

import investors.AInvestor;
import stocks.Stock;

public class RequestManagement {
    private static int nextID = 0;

    public static int chooseOlderPrice(ATradeRequest trade1, ATradeRequest trade2) {
        if (trade1.getId() < trade2.getId()) {
            return trade1.getPriceLimit();
        } else {
            return trade2.getPriceLimit();
        }
    }

    public static ValidUntilNthRoundTradeRequest createValidUntilNthRoundTradeRequest
            (AInvestor investor, Stock stock, int quantity, int priceLimit, ATradeRequest.TradeType tradeType, int lastRoundValid) {
        return new ValidUntilNthRoundTradeRequest(investor, stock, quantity, priceLimit, tradeType, lastRoundValid, nextID++);
    }

    public static AllOrNothingTradeRequest createAllOrNothingTradeRequest
            (AInvestor investor, Stock stock, int quantity, int priceLimit, ATradeRequest.TradeType tradeType) {
        return new AllOrNothingTradeRequest(investor, stock, quantity, priceLimit, tradeType, nextID++);
    }

    public static InstantTradeRequest createInstantTradeRequest
            (AInvestor investor, Stock stock, int quantity, int priceLimit, ATradeRequest.TradeType tradeType) {
        return new InstantTradeRequest(investor, stock, quantity, priceLimit, tradeType, nextID++);
    }

    public static IndefiniteTradeRequest createIndefiniteTradeRequest
            (AInvestor investor, Stock stock, int quantity, int priceLimit, ATradeRequest.TradeType tradeType) {
        return new IndefiniteTradeRequest(investor, stock, quantity, priceLimit, tradeType, nextID++);
    }
}
