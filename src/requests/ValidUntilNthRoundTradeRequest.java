package requests;

import investors.AInvestor;
import stocks.Stock;

/**
 * Represents a trade request that expires after the given round.
 * For performance reasons that's the trade request type used the most in the simulation.
 */
public class ValidUntilNthRoundTradeRequest extends ATradeRequest {
    private final int lastRound;


    /**
     * Create a new trade request that expires after the given round with the given investor, stock, quantity, price limit, trade type and id.
     *
     * @param investor   the investor that makes the trade request
     * @param stock      the stock that is traded
     * @param quantity   the quantity of the stock that is traded
     * @param priceLimit the price limit of the stock that is traded
     * @param tradeType  the type of the trade request
     * @param lastRound  the round after which the trade request expires
     * @param id         the id of the trade request
     */
    public ValidUntilNthRoundTradeRequest(AInvestor investor, Stock stock, int quantity, int priceLimit, TradeType tradeType, int lastRound, int id) {
        super(investor, stock, quantity, priceLimit, tradeType, id);
        this.lastRound = lastRound;
    }

    /**
     * Get the last round the trade request is valid.
     *
     * @return the last round the trade request is valid
     */
    public int getLastRound() {
        return lastRound;
    }

    /**
     * Check if the trade request has expired and should be deleted.
     *
     * @param currentRound the current round
     * @return true if the trade request has expired and should be deleted, false otherwise
     */
    @Override
    public boolean expiredAndShouldBeDeleted(int currentRound) {
        // we allow it to be greater for easy InstantTradeRequest implementation
        return currentRound >= lastRound;
    }
}
