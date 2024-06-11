package investors;

import stocks.Stock;

import java.util.Map;

public class RandomChoiceInvestor extends AInvestor {
    public RandomChoiceInvestor(int name, int balance) {
        super(name, balance);
    }

    public RandomChoiceInvestor(int name, int balance, Map<Stock, Integer> stocksPortfolio) {
        super(name, balance, stocksPortfolio);
    }
}
