package investors;

import stocks.Stock;

import java.util.Map;


public class SMAInvestor extends AInvestor {
    public SMAInvestor(int name, int balance) {
        super(name, balance);
    }

    public SMAInvestor(int name, int balance, Map<Stock, Integer> stocksPortfolio) {
        super(name, balance, stocksPortfolio);
    }

}
