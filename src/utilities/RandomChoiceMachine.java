package utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RandomChoiceMachine {
    /**
     * Returns a random element from the given collection.
     *
     * @param collection the collection
     * @param <T>        the type of elements in the collection
     * @return a random element from the given collection
     */
    public <T> T getRandomElement(Collection<T> collection) {
        List<T> list = new ArrayList<>(collection);
        return list.get((int) (Math.random() * list.size()));
    }

    public boolean getRandomBoolean() {
        return Math.random() < 0.5;
    }
}
