package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulation.LastTradeData;
import stocks.Stock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LastTradeDataTest {
    private static final int[] TRADE_DATA = {5, 9, 9, 9, 9, 6, 7, 8, 9, 10, 11};
    private LastTradeData lastTradeData;
    private Stock stock;

    @BeforeEach
    public void setup() {
        lastTradeData = new LastTradeData(10);
        stock = new Stock("Test", 100, 0);
        for (int i = 0; i < 10; i++) {
            lastTradeData.addTradeData(stock, TRADE_DATA[i]);
        }
        lastTradeData.updateSMA();
    }

    @Test
    public void testGetSMA() {
        assertEquals(8.1, lastTradeData.getSMA(stock, 10));
        assertEquals(8.0, lastTradeData.getSMA(stock, 5));
    }

    @Test
    public void testCheckIfSMABuySignal() {
        lastTradeData.addTradeData(stock, TRADE_DATA[10]);
        assertTrue(lastTradeData.checkIfSMABuySignal(stock));
    }

}