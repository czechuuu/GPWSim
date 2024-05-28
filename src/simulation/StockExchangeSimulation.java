package simulation;

public class StockExchangeSimulation {
    private final int totalRounds;
    private final int round;

    public StockExchangeSimulation(int totalRounds) {
        this.totalRounds = totalRounds;
        this.round = 0;
    }

    public int getRound() {
        return round;
    }
}
