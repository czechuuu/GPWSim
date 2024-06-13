package requests;

import investors.AInvestor;
import stocks.Stock;

public class ValidUntilNthRoundTradeRequest extends ATradeRequest {
    private final int lastRound;


    // Constructor for the investor
    public ValidUntilNthRoundTradeRequest(AInvestor investor, Stock stock, int quantity, int priceLimit, TradeType tradeType, int lastRound, int id) {
        super(investor, stock, quantity, priceLimit, tradeType, id);
        this.lastRound = lastRound;
    }

    public int getLastRound() {
        return lastRound;
    }

    @Override
    public boolean expiredAndShouldBeDeleted(int currentRound) {
        // we allow it to be greater for easy InstantTradeRequest implementation
        return currentRound >= lastRound;
    }
}
