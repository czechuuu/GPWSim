package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.Stock;
import stocks.StockManagement;
import utilities.Parser;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class StockManagementTest {
    private Parser parser;

    @BeforeEach
    public void setUp() throws IOException {
        parser = new Parser("src/tests/testFiles/moodle.txt");
    }

    @Test
    public void testStockCreation() {
        StockManagement stockManagement = new StockManagement();

        Stock stock = stockManagement.createStock("APL", 145, 0);

        // created stock verification
        assertEquals("APL", stock.getIdentifier());
        assertEquals(145, stock.getLastPrice());
        assertEquals(0, stock.getLastTradeRound());
        // stock management information verification
        assertEquals(1, stockManagement.getStocks().size());
        assertEquals(stock, stockManagement.getStock("APL"));
        assertTrue(stockManagement.getStocks().contains(stock));
        // changing stock information directly
        stock.updateLastTransactionInformation(150, 1);
        assertEquals(150, stock.getLastPrice());
        assertEquals(1, stock.getLastTradeRound());
        // changing stock information through stock management
        stockManagement.updateStockPriceByName("APL", 155, 2);
        assertEquals(155, stock.getLastPrice());
        assertEquals(2, stock.getLastTradeRound());
        // cant create duplicate stock
        assertThrows(IllegalArgumentException.class, () -> stockManagement.createStock("APL", 145, 0));
        // stock doesnt exist
        assertThrows(NoSuchElementException.class, () -> stockManagement.getStock("TSLA"));
    }

    @Test
    public void testStockManagementCreation() {
        StockManagement stockManagement = new StockManagement(parser.getStockPrices());
        // verification of creating from file data
        assertEquals(3, stockManagement.getStocks().size());
        assertEquals(145, stockManagement.getStock("APL").getLastPrice());
        assertEquals(300, stockManagement.getStock("MSFT").getLastPrice());
        assertEquals(2700, stockManagement.getStock("GOOGL").getLastPrice());
    }


}
