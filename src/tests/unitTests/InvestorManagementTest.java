package tests.unitTests;

import investors.InvestorManagement;
import investors.RandomChoiceInvestor;
import investors.SMAInvestor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.StockManagement;
import utilities.Parser;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InvestorManagementTest {
    private Parser parser;

    @BeforeEach
    public void setUp() throws IOException {
        parser = new Parser(TestPaths.MOODLE_TEST_PATH);
    }

    @Test
    public void testCreationFromParser() {
        StockManagement stockManagement = new StockManagement(parser);
        // this is tested in StockManagementTest so assume its correct
        InvestorManagement investorManagement = new InvestorManagement(stockManagement, parser);
        // 4 random choice investors and 2 SMA investors
        assertEquals(6, investorManagement.getInvestors().size());
        assertEquals(4,
                investorManagement.getInvestors().stream()
                        .filter(investor -> investor.getClass() == RandomChoiceInvestor.class).count());
        assertEquals(2,
                investorManagement.getInvestors().stream()
                        .filter(investor -> investor.getClass() == SMAInvestor.class).count());
        // checking portfolios
        assertTrue(investorManagement.getInvestors().stream()
                .allMatch(investor -> investor.getBalance() == 100000));
        assertTrue(investorManagement.getInvestors().stream().allMatch(investor -> investor.getStockQuantity(stockManagement.getStock("APL")) == 5));
        assertTrue(investorManagement.getInvestors().stream().allMatch(investor -> investor.getStockQuantity(stockManagement.getStock("MSFT")) == 15));
        assertTrue(investorManagement.getInvestors().stream().allMatch(investor -> investor.getStockQuantity(stockManagement.getStock("GOOGL")) == 3));

    }
}