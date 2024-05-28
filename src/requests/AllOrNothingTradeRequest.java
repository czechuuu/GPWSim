package requests;

import investors.AInvestor;
import stocks.Stock;

public class AllOrNothingTradeRequest extends ATradeRequest {
    public AllOrNothingTradeRequest(AInvestor investor, Stock stock, int quantity, int priceLimit, TradeType tradeType) {
        super(investor, stock, quantity, priceLimit, tradeType);
    }

    // Must be realised fully within one round
    // If not, the trade is cancelled
    // But it can be split among different sellers/buyers
    // Implement the trade logic here
}
