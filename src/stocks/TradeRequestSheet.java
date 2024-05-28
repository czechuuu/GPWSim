package stocks;

import requests.ATradeRequest;
import utilities.SortedList;

import java.util.Comparator;

public class TradeRequestSheet {
    /**
     * Comparator for buy requests.
     * Used for sorting in descending order by price limit.
     */
    static Comparator<ATradeRequest> buyComparator = (a, b) -> Integer.compare(b.getPriceLimit(), a.getPriceLimit());

    /**
     * Comparator for sell requests.
     * Used for sorting in ascending order by price limit.
     */
    static Comparator<ATradeRequest> sellComparator = (a, b) -> Integer.compare(a.getPriceLimit(), b.getPriceLimit());

    private final SortedList<ATradeRequest> buyRequests;
    private final SortedList<ATradeRequest> sellRequests;

    public TradeRequestSheet() {
        buyRequests = new SortedList<>(buyComparator);
        sellRequests = new SortedList<>(sellComparator);
    }

    public void addRequest(ATradeRequest request) {
        if (request.isBuyRequest()) {
            addBuyRequest(request);
        } else {
            addSellRequest(request);
        }
    }

    private void addBuyRequest(ATradeRequest request) {
        buyRequests.add(request);
    }

    private void addSellRequest(ATradeRequest request) {
        sellRequests.add(request);
    }

    private void checkForTrades() {
        outer:
        for (ATradeRequest buyRequest : buyRequests) {
            for (ATradeRequest sellRequest : sellRequests) {
                if (buyRequest.getPriceLimit() >= sellRequest.getPriceLimit()) {
                    realiseTrade(buyRequest, sellRequest);
                } else {
                    // Since the sell requests are sorted in ascending order by price limit,
                    // if the price limit of the current sell request is less than the price limit of the current buy request,
                    // then there will be no more sell requests with price limits greater than the price limit of the current buy request.
                    break outer;
                }
            }
        }
    }

    private void realiseTrade(ATradeRequest buyRequest, ATradeRequest sellRequest) {
        // Implement the trade logic here
        // Assume that this will delete from the list of requests
        int quantity = Math.min(buyRequest.getQuantity(), sellRequest.getQuantity());
        // write the buystock metheod in the investor class
        buyRequest.getInvestor().buyStock(sellRequest.getStock(), quantity, sellRequest.getPriceLimit());
        sellRequest.getInvestor().sellStock(sellRequest.getStock(), quantity, sellRequest.getPriceLimit());

        if (buyRequest.getQuantity() == quantity) {
            // If the buy request has been completely fulfilled, remove it from the list of buy requests
            // Should work if the list is implemented correctly -> make sure it deletes the correct request
            buyRequests.remove(buyRequest);
        } else {
            buyRequest.reduceQuantity(quantity);
        }
    }


}
