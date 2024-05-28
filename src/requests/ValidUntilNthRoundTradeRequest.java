package requests;

import investors.AInvestor;
import stocks.Stock;

public class ValidUntilNthRoundTradeRequest extends ATradeRequest {
    private final int lastRound;
    

    // Constructor for the investor
    public ValidUntilNthRoundTradeRequest(AInvestor investor, Stock stock, int quantity, int priceLimit, TradeType tradeType, int lastRound) {
        super(investor, stock, quantity, priceLimit, tradeType);
        this.lastRound = lastRound;
    }

    public int getLastRound() {
        return lastRound;
    }
}
