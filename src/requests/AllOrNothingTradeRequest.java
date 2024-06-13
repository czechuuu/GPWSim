package requests;

import investors.AInvestor;
import stocks.Stock;

/**
 * Represents a trade request that must be realised fully within one round.
 * NOT IMPLEMENTED
 */
public class AllOrNothingTradeRequest extends ATradeRequest {
    public AllOrNothingTradeRequest(AInvestor investor, Stock stock, int quantity, int priceLimit, TradeType tradeType, int id) {
        super(investor, stock, quantity, priceLimit, tradeType, id);
    }

    @Override
    public boolean expiredAndShouldBeDeleted(int currentRound) {
        return true; // Must be realised fully within one round
    }

    /**
     * Check if the trade request can be considered for a trade with another trade request.
     * The trade request can be considered for a trade if the other trade request is an AllOrNothingTradeRequest.
     *
     * @param other the other trade request
     * @return true if the trade request can be considered for a trade with the other trade request, false otherwise
     */
    @Override
    public boolean considerTrade(ATradeRequest other) {
        // should check if this request can be realised fully
        // TODO Implement this logic
        return false;
    }
}
