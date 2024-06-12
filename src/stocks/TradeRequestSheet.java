package stocks;

import requests.ATradeRequest;
import simulation.StockExchangeSimulation;
import utilities.SortedList;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TradeRequestSheet {
    /**
     * Comparator for buy requests.
     * Used for sorting in descending order by price limit.
     */
    static final Comparator<ATradeRequest> buyComparator = (a, b) -> Integer.compare(b.getPriceLimit(), a.getPriceLimit());

    /**
     * Comparator for sell requests.
     * Used for sorting in ascending order by price limit.
     */
    static final Comparator<ATradeRequest> sellComparator = (a, b) -> Integer.compare(a.getPriceLimit(), b.getPriceLimit());

    private final Map<Stock, SortedList<ATradeRequest>> buyRequestsMap;
    private final Map<Stock, SortedList<ATradeRequest>> sellRequestsMap;

    public TradeRequestSheet() {


        buyRequestsMap = new HashMap<>();
        sellRequestsMap = new HashMap<>();
    }

    public void addRequest(ATradeRequest request) {
        if (request.isBuyRequest()) {
            addBuyRequest(request);
        } else {
            addSellRequest(request);
        }
    }

    private void addBuyRequest(ATradeRequest request) {
        // make sure there are lists for the stock
        buyRequestsMap.putIfAbsent(request.getStock(), new SortedList<>(buyComparator));
        sellRequestsMap.putIfAbsent(request.getStock(), new SortedList<>(sellComparator));
        // add the request to the list of buy requests for the stock
        buyRequestsMap.get(request.getStock()).add(request);
    }

    private void addSellRequest(ATradeRequest request) {
        // make sure there are lists for the stock
        buyRequestsMap.putIfAbsent(request.getStock(), new SortedList<>(buyComparator));
        sellRequestsMap.putIfAbsent(request.getStock(), new SortedList<>(sellComparator));
        // add the request to the list of sell requests for the stock
        sellRequestsMap.get(request.getStock()).add(request);
    }

    public void realiseSubmittedTrades(StockExchangeSimulation simulation) {
        checkForTrades(simulation.getRound());
        removeExpiredRequests(simulation);
    }

    private void checkForTrades(int round) {
        for (Stock stock : buyRequestsMap.keySet()) {
            checkForTradesForStock(stock, round);
        }
    }

    private void checkForTradesForStock(Stock stock, int round) {
        SortedList<ATradeRequest> buyRequests = buyRequestsMap.get(stock);
        SortedList<ATradeRequest> sellRequests = sellRequestsMap.get(stock);

        outer:
        for (ATradeRequest buyRequest : new SortedList<>(buyRequests)) {
            for (ATradeRequest sellRequest : new SortedList<>(sellRequests)) {
                System.out.println("considering: " + sellRequest);
                if (buyRequest.getPriceLimit() >= sellRequest.getPriceLimit()) {
                    boolean buyRequestFullfilled = realiseTrade(buyRequest, sellRequest, round);
                    if (buyRequestFullfilled) {
                        // If the buy request has been completely fulfilled, move on to the next buy request
                        continue;
                    }
                } else {
                    // Since the sell requests are sorted in ascending order by price limit,
                    // if the price limit of the current sell request is less than the price limit of the current buy request,
                    // then there will be no more sell requests with price limits greater than the price limit of the current buy request.
                    break;
                }
            }
        }
    }

    /**
     * Realises a trade between a buy request and a sell request.
     * If the buy request is completely fulfilled, it is removed from the list of buy requests.
     *
     * @param buyRequest  the buy request
     * @param sellRequest the sell request
     * @return true if the buy request has been completely fulfilled or cancelled, false otherwise
     */
    private boolean realiseTrade(ATradeRequest buyRequest, ATradeRequest sellRequest, int round) {
        // Implement the trade logic here
        // Assume that this will delete from the list of requests
        int quantity = Math.min(buyRequest.getQuantity(), sellRequest.getQuantity());

        if (buyRequest.getInvestor().canBuyStock(buyRequest.getStock(), quantity, buyRequest.getPriceLimit())) {
            if (sellRequest.getInvestor().canSellStock(sellRequest.getStock(), quantity, sellRequest.getPriceLimit())) {
                buyRequest.getInvestor().buyStock(buyRequest.getStock(), quantity, sellRequest.getPriceLimit());
                sellRequest.getInvestor().sellStock(sellRequest.getStock(), quantity, sellRequest.getPriceLimit());
                reduceQuantityOrRemove(buyRequest, quantity);
                reduceQuantityOrRemove(sellRequest, quantity);
                buyRequest.getStock().updateLastTransactionInformation(sellRequest.getPriceLimit(), buyRequest.getStock().getLastTradeRound());
                return !buyRequestsMap.get(buyRequest.getStock()).getList().contains(buyRequest);
            }
        }

        // we don't consider realizing trade possibly even more partially
        // if one of the investors can afford only part of the trade
        removeRequestIfCancelledDueToInsufficientFunds(sellRequest);
        return removeRequestIfCancelledDueToInsufficientFunds(buyRequest);
    }

    /**
     * Reduces the quantity of a trade request.
     * If the quantity is reduced to 0, the request is removed from the list of requests.
     *
     * @param request  the trade request
     * @param quantity the quantity to reduce by
     */
    private void reduceQuantityOrRemove(ATradeRequest request, int quantity) {
        if (request.getQuantity() < quantity) {
            throw new IllegalArgumentException("Cannot reduce quantity by more than the current quantity");
        }
        if (request.getQuantity() == quantity) {
            removeRequest(request);
        } else {
            request.reduceQuantity(quantity);
        }
    }

    /**
     * Removes a trade request from the list of requests.
     *
     * @param request the trade request to remove
     */
    private void removeRequest(ATradeRequest request) {
        if (request.isBuyRequest()) {
            buyRequestsMap.get(request.getStock()).remove(request);
        } else {
            sellRequestsMap.get(request.getStock()).remove(request);
        }
    }

    /**
     * Removes a trade request from the list of requests if it had to be cancelled due to insufficient funds.
     *
     * @param request the trade request to remove
     * @return true if the request was removed, false otherwise
     */
    private boolean removeRequestIfCancelledDueToInsufficientFunds(ATradeRequest request) {
        if (request.isBuyRequest()) {
            if (!request.getInvestor().canBuyStock(request.getStock(), request.getQuantity(), request.getPriceLimit())) {
                buyRequestsMap.get(request.getStock()).remove(request);
                return true;
            }
        } else {
            if (!request.getInvestor().canSellStock(request.getStock(), request.getQuantity(), request.getPriceLimit())) {
                sellRequestsMap.get(request.getStock()).remove(request);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes expired requests from the list of requests.
     *
     * @param simulation the simulation (current round needed to check for expired requests)
     */
    private void removeExpiredRequests(StockExchangeSimulation simulation) {
        /*
          We only iterate over the key set of buyRequestsMap because the keys of buyRequestsMap and sellRequestsMap
          are the same. This is because we add a stock to both maps at the same time when we add a request.
         */
        for (Stock stock : buyRequestsMap.keySet()) {
            removeExpiredRequestsForStock(stock, simulation);
        }
    }

    /**
     * Removes expired requests for a specific stock from the list of requests.
     *
     * @param stock      the stock
     * @param simulation the simulation (current round needed to check for expired requests)
     */
    private void removeExpiredRequestsForStock(Stock stock, StockExchangeSimulation simulation) {
        SortedList<ATradeRequest> buyRequests = buyRequestsMap.get(stock);
        SortedList<ATradeRequest> sellRequests = sellRequestsMap.get(stock);
        int currentRound = simulation.getRound();

        for (ATradeRequest buyRequest : buyRequests) {
            if (buyRequest.expiredAndShouldBeDeleted(currentRound)) {
                buyRequests.remove(buyRequest);
            }
        }

        for (ATradeRequest sellRequest : sellRequests) {
            if (sellRequest.expiredAndShouldBeDeleted(currentRound)) {
                sellRequests.remove(sellRequest);
            }
        }
    }

    public Map<Stock, SortedList<ATradeRequest>> getBuyRequestsMap() {
        return buyRequestsMap;
    }

    public Map<Stock, SortedList<ATradeRequest>> getSellRequestsMap() {
        return sellRequestsMap;
    }

}
