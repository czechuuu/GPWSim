package requests;

import investors.AInvestor;
import stocks.Stock;

public class InstantTradeRequest extends ATradeRequest {
    public InstantTradeRequest(AInvestor investor, Stock stock, int quantity, int priceLimit, TradeType tradeType) {
        super(investor, stock, quantity, priceLimit, tradeType);
    }

    @Override
    public boolean expiredAndShouldBeDeleted(int currentRound) {
        return true; // expires at the end of the round
    }

}
