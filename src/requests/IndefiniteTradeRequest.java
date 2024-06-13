package requests;

import investors.AInvestor;
import stocks.Stock;

/**
 * Represents a trade request that never expires.
 * for performance reasons, we don't want to use it too much
 */
public class IndefiniteTradeRequest extends ATradeRequest {
    /**
     * Create a new indefinite trade request with the given investor, stock, quantity, price limit, trade type and id.
     *
     * @param investor   the investor that makes the trade request
     * @param stock      the stock that is traded
     * @param quantity   the quantity of the stock that is traded
     * @param priceLimit the price limit of the stock that is traded
     * @param tradeType  the type of the trade request
     * @param id         the id of the trade request
     */
    public IndefiniteTradeRequest(AInvestor investor, Stock stock, int quantity, int priceLimit, TradeType tradeType, int id) {
        super(investor, stock, quantity, priceLimit, tradeType, id);
    }

    /**
     * Check if the trade request has expired and should be deleted.
     *
     * @param currentRound the current round
     * @return false, as this trade request never expires
     */
    @Override
    public boolean expiredAndShouldBeDeleted(int currentRound) {
        return false; // never expires
    }
}
