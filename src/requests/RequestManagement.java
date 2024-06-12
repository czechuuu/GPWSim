package requests;

import investors.AInvestor;
import stocks.Stock;

public class RequestManagement {
    public static ATradeRequest createTradeRequest(AInvestor investor, Stock stock, int quantity, int priceLimit, ATradeRequest.TradeType tradeType, int lastRound) {
        if (lastRound == -1) {
            return new InstantTradeRequest(investor, stock, quantity, priceLimit, tradeType);
        } else if (lastRound == -2) {
            return new IndefiniteTradeRequest(investor, stock, quantity, priceLimit, tradeType);
        } else {
            return new ValidUntilNthRoundTradeRequest(investor, stock, quantity, priceLimit, tradeType, lastRound);
        }
    }
}
