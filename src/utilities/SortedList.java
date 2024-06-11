package utilities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SortedList<T> implements Iterable<T> {
    private final Comparator<T> comparator;
    private final ArrayList<T> list;

    public SortedList(Comparator<T> comparator) {
        this.comparator = comparator;
        this.list = new ArrayList<>();
    }

    public ArrayList<T> getList() {
        return list;
    }

    /**
     * Adds the specified element to this list in a sorted manner.
     * should preserve the order of elements that compare as equal (first added is first returned by the iterator)
     *
     * @param element element to be added to this list
     */
    public void add(T element) {
        int index = 0;
        while (index < list.size() && comparator.compare(list.get(index), element) <= 0) {
            index++;
        }
        list.add(index, element);
    }

    public void remove(int index) {
        list.remove(index);
    }

    /**
     * Removes the first occurrence of the specified element from this list, if it is present.
     *
     * @param element element to be removed from this list, if present
     */
    public void remove(T element) {
        list.remove(element);
    }

    public T get(int index) {
        return list.get(index);
    }

    public Comparator<T> getComparator() {
        return comparator;
    }

    @Override
    public Iterator<T> iterator() {
        return new SortedListIterator();
    }

    private class SortedListIterator implements Iterator<T> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < list.size();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return list.get(currentIndex++);
        }
    }
}
