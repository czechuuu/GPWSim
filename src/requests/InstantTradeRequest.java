package requests;

import investors.AInvestor;
import stocks.Stock;

public class InstantTradeRequest extends ValidUntilNthRoundTradeRequest {
    /**
     * Create a new instant trade request with the given investor, stock, quantity, price limit, trade type and id.
     *
     * @param investor   the investor that makes the trade request
     * @param stock      the stock that is traded
     * @param quantity   the quantity of the stock that is traded
     * @param priceLimit the price limit of the stock that is traded
     * @param tradeType  the type of the trade request
     * @param id         the id of the trade request
     */
    public InstantTradeRequest(AInvestor investor, Stock stock, int quantity, int priceLimit, TradeType tradeType, int id) {
        // this way round check will always fail and thus the request will expire at the end of the round
        super(investor, stock, quantity, priceLimit, tradeType, -1, id);
    }

}
