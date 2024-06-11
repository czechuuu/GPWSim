package tests;

import org.junit.jupiter.api.Test;
import utilities.SortedList;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SortedListTest {

    @Test
    public void testAlwaysSorted() {
        SortedList<Integer> sortedList = new SortedList<>(Integer::compareTo);
        sortedList.add(5);
        sortedList.add(3);
        sortedList.add(4);
        sortedList.add(1);
        sortedList.add(2);

        for (int i = 0; i < sortedList.getList().size() - 1; i++) {
            assertTrue(sortedList.get(i) <= sortedList.get(i + 1));
        }
    }


    @Test
    public void testIterator() {
        SortedList<Integer> sortedList = new SortedList<>(Integer::compareTo);
        sortedList.add(5);
        sortedList.add(3);
        sortedList.add(4);
        sortedList.add(1);
        sortedList.add(2);

        Iterator<Integer> iterator = sortedList.iterator();
        int previous = iterator.next();

        while (iterator.hasNext()) {
            int current = iterator.next();
            assertTrue(previous <= current);
            previous = current;
        }
    }

    @Test
    public void testPreservingOrder() {
        // Sorted by last digit
        SortedList<Integer> sortedList = new SortedList<>((a, b) -> Integer.compare(a % 10, b % 10));
        sortedList.add(15);
        sortedList.add(13); // first occurrence of last digit 3
        sortedList.add(14);
        sortedList.add(11);
        sortedList.add(23); // second occurrence of last digit 3
        Integer[] expected = new Integer[]{11, 13, 23, 14, 15};
        for (int i = 0; i < sortedList.getList().size(); i++) {
            assertEquals(expected[i], sortedList.get(i));
        }
    }
}
