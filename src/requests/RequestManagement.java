package requests;

import investors.AInvestor;
import stocks.Stock;

public class RequestManagement {
    private static int nextID = 0;

    /**
     * Compares two trade requests and returns the price limit of the older one.
     */
    public static int chooseOlderPrice(ATradeRequest trade1, ATradeRequest trade2) {
        if (trade1.getId() < trade2.getId()) {
            return trade1.getPriceLimit();
        } else {
            return trade2.getPriceLimit();
        }
    }

    /**
     * Create a new Valid until n-th round trade request with the given investor, stock, quantity, price limit, trade type.
     *
     * @param investor       the investor that makes the trade request
     * @param stock          the stock that is traded
     * @param quantity       the quantity of the stock that is traded
     * @param priceLimit     the price limit of the stock that is traded
     * @param tradeType      the type of the trade request
     * @param lastRoundValid the last round the trade request is valid
     * @return the new indefinite trade request
     */
    public static ValidUntilNthRoundTradeRequest createValidUntilNthRoundTradeRequest
    (AInvestor investor, Stock stock, int quantity, int priceLimit, ATradeRequest.TradeType tradeType, int lastRoundValid) {
        return new ValidUntilNthRoundTradeRequest(investor, stock, quantity, priceLimit, tradeType, lastRoundValid, nextID++);
    }

    /**
     * Create a new All or nothing trade request with the given investor, stock, quantity, price limit, trade type.
     *
     * @param investor   the investor that makes the trade request
     * @param stock      the stock that is traded
     * @param quantity   the quantity of the stock that is traded
     * @param priceLimit the price limit of the stock that is traded
     * @param tradeType  the type of the trade request
     * @return the new indefinite trade request
     */
    public static AllOrNothingTradeRequest createAllOrNothingTradeRequest
    (AInvestor investor, Stock stock, int quantity, int priceLimit, ATradeRequest.TradeType tradeType) {
        return new AllOrNothingTradeRequest(investor, stock, quantity, priceLimit, tradeType, nextID++);
    }

    /**
     * Create a new Instant trade request with the given investor, stock, quantity, price limit, trade type.
     *
     * @param investor   the investor that makes the trade request
     * @param stock      the stock that is traded
     * @param quantity   the quantity of the stock that is traded
     * @param priceLimit the price limit of the stock that is traded
     * @param tradeType  the type of the trade request
     * @return the new indefinite trade request
     */
    public static InstantTradeRequest createInstantTradeRequest
    (AInvestor investor, Stock stock, int quantity, int priceLimit, ATradeRequest.TradeType tradeType) {
        return new InstantTradeRequest(investor, stock, quantity, priceLimit, tradeType, nextID++);
    }

    /**
     * Create a new Indefinite trade request with the given investor, stock, quantity, price limit, trade type.
     *
     * @param investor   the investor that makes the trade request
     * @param stock      the stock that is traded
     * @param quantity   the quantity of the stock that is traded
     * @param priceLimit the price limit of the stock that is traded
     * @param tradeType  the type of the trade request
     * @return the new indefinite trade request
     */
    public static IndefiniteTradeRequest createIndefiniteTradeRequest
    (AInvestor investor, Stock stock, int quantity, int priceLimit, ATradeRequest.TradeType tradeType) {
        return new IndefiniteTradeRequest(investor, stock, quantity, priceLimit, tradeType, nextID++);
    }
}
