package requests;

import investors.AInvestor;
import stocks.Stock;

public class InstantTradeRequest extends ValidUntilNthRoundTradeRequest {
    public InstantTradeRequest(AInvestor investor, Stock stock, int quantity, int priceLimit, TradeType tradeType) {
        // this way round check will always fail and thus the request will expire at the end of the round
        super(investor, stock, quantity, priceLimit, tradeType, -1);
    }
    
}
