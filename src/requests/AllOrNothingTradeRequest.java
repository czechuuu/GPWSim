package requests;

import investors.AInvestor;
import stocks.Stock;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a trade request that must be realised fully within one round.
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
     * Check if this trade request can be traded with the given list of other trade requests.
     * This trade request can be traded with other trade requests if the sum of the quantities of the other trade requests
     * is greater than or equal to the quantity of this trade request.
     *
     * @param otherRequests the other trade requests
     * @return true if this trade request can be traded with the other trade requests, false otherwise
     */
    @Override
    public boolean considerTrade(List<ATradeRequest> otherRequests) {
        if (otherRequests.isEmpty())
            return false;
        if (otherRequests.get(0) instanceof AllOrNothingTradeRequest)
            return false;

        Predicate<ATradeRequest> isNotAllOrNothing = request -> request.getClass() != AllOrNothingTradeRequest.class;
        Predicate<ATradeRequest> isWithinPriceRange = request -> {
            if (isBuyRequest()) {
                return request.getPriceLimit() <= getPriceLimit();
            } else {
                return request.getPriceLimit() >= getPriceLimit();
            }
        };
        // We don't consider trading with other AllOrNothing requests since that involves recursive logic
        // We only allow AllOrNothing requests to be traded with non-AllOrNothing requests
        return otherRequests.stream().filter(isNotAllOrNothing).filter(isWithinPriceRange) // Only consider non-AllOrNothing requests
                .mapToInt(ATradeRequest::getQuantity).sum() >= getQuantity();
    }
}
