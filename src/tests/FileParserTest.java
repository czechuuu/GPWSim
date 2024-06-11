package tests;

import org.junit.jupiter.api.Test;
import utilities.Parser;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileParserTest {
    private static String getAbsolutePath(String filePath) {
        return Path.of(filePath).toAbsolutePath().toString();
    }


    @Test
    public void parseFileFromMoodleTest() throws IOException {
        Parser parser = new Parser(getAbsolutePath("src/tests/testFiles/moodle.txt"));
        // first line parsed
        // R R R R S S
        assertEquals(4, parser.getNumberOfRandomInvestors());
        assertEquals(2, parser.getNumberOfSMAInvestors());

        // Second line parsed
        // APL:145 MSFT:300 GOOGL:2700
        assertEquals(3, parser.getStockPrices().size());
        assertEquals(145, parser.getStockPrices().get("APL"));
        assertEquals(300, parser.getStockPrices().get("MSFT"));
        assertEquals(2700, parser.getStockPrices().get("GOOGL"));

        // Third line parsed
        // 100000 APL:5 MSFT:15 GOOGL:3
        assertEquals(100000, parser.getInitialCash());
        assertEquals(3, parser.getInitialPortfolio().size());
        assertEquals(5, parser.getInitialPortfolio().get("APL"));
        assertEquals(15, parser.getInitialPortfolio().get("MSFT"));
        assertEquals(3, parser.getInitialPortfolio().get("GOOGL"));
    }
}
