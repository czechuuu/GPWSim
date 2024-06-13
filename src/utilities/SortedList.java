package utilities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SortedList<T> implements Iterable<T> {
    private final Comparator<T> comparator;
    private final ArrayList<T> list;

    /**
     * Creates a new sorted list with the given comparator.
     *
     * @param comparator the comparator
     */
    public SortedList(Comparator<T> comparator) {
        this.comparator = comparator;
        this.list = new ArrayList<>();
    }

    /**
     * Creates a copy of the given sorted list.
     * The new sorted list will have the same comparator and elements as the given sorted list.
     *
     * @param sortedList the sorted list to copy
     */
    public SortedList(SortedList<T> sortedList) {
        this.comparator = sortedList.getComparator();
        this.list = new ArrayList<>();
        for (T element : sortedList.getList()) {
            add(element);
        }
    }

    /**
     * Returns the list of elements in this sorted list.
     *
     * @return the list of elements in this sorted list
     */
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

    /**
     * Removes the element at the specified position in this list.
     *
     * @param index the index of the element to be removed
     */
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

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     */
    public T get(int index) {
        return list.get(index);
    }

    /**
     * Returns the comparator of this list.
     *
     * @return the comparator of this list
     */
    public Comparator<T> getComparator() {
        return comparator;
    }

    /**
     * Returns the iterator over the elements in this list.
     *
     * @return the iterator over the elements in this list
     */
    @Override
    public Iterator<T> iterator() {
        return new SortedListIterator();
    }

    /**
     * Inner class for the iterator over the elements in this list.
     * Does not support the remove operation while iterating.
     * For changing while iterating create a copy of the list.
     */
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
