package tests;

import investors.AInvestor;
import investors.InsufficientBalanceException;
import investors.RandomChoiceInvestor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.Stock;

import static org.junit.jupiter.api.Assertions.*;

public class AInvestorTest {
    private AInvestor investor;
    private Stock stock;

    @BeforeEach
    public void setUp() {
        // cant create an instance of an abstract class
        // so we create an instance of a subclass but test only methods of the abstract class
        investor = new RandomChoiceInvestor(0, 1000);
        stock = new Stock("AAPL", 150, 1);
    }

    @Test
    public void canBuyStock_withSufficientBalance_returnsTrue() {
        assertTrue(investor.canBuyStock(stock, 5, 100));
    }

    @Test
    public void canBuyStock_withInsufficientBalance_returnsFalse() {
        assertFalse(investor.canBuyStock(stock, 15, 100));
    }

    @Test
    public void buyStock_withSufficientBalance_increasesStockQuantity() {
        investor.buyStock(stock, 5, 100);
        assertEquals(5, investor.getStockQuantity(stock));
    }

    @Test
    public void buyStock_withInsufficientBalance_throwsException() {
        assertThrows(InsufficientBalanceException.class, () -> investor.buyStock(stock, 15, 100));
    }

    @Test
    public void canSellStock_withSufficientQuantity_returnsTrue() {
        investor.buyStock(stock, 5, 100);
        assertTrue(investor.canSellStock(stock, 3, 100));
    }

    @Test
    public void canSellStock_withInsufficientQuantity_returnsFalse() {
        investor.buyStock(stock, 5, 100);
        assertFalse(investor.canSellStock(stock, 7, 100));
    }

    @Test
    public void sellStock_withSufficientQuantity_decreasesStockQuantity() {
        investor.buyStock(stock, 5, 100);
        investor.sellStock(stock, 3, 100);
        assertEquals(2, investor.getStockQuantity(stock));
    }

    @Test
    public void sellStock_withInsufficientQuantity_throwsException() {
        investor.buyStock(stock, 5, 100);
        assertThrows(InsufficientBalanceException.class, () -> investor.sellStock(stock, 7, 100));
    }
}