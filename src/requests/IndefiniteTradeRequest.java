package requests;

import investors.AInvestor;
import stocks.Stock;

public class IndefiniteTradeRequest extends ATradeRequest {
    public IndefiniteTradeRequest(AInvestor investor, Stock stock, int quantity, int priceLimit, TradeType tradeType) {
        super(investor, stock, quantity, priceLimit, tradeType);
    }

    @Override
    public boolean expiredAndShouldBeDeleted(int currentRound) {
        return false; // never expires
    }
}
