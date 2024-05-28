package stocks;

public class Stock {
    private final String identifier;
    private int lastPrice;
    private int lastTradeRound;

    public Stock(String identifier, int lastPrice, int lastTradeRound) {
        this.identifier = identifier;
        this.lastPrice = lastPrice;
        // set appropriate value for lastTradeRound at the beginning
        this.lastTradeRound = 0; // or -1?
    }

    public void updateLastTransactionInformation(int price, int tradeRound) {
        this.lastPrice = price;
        this.lastTradeRound = tradeRound;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getLastPrice() {
        return lastPrice;
    }

    public int getLastTradeRound() {
        return lastTradeRound;
    }

}
