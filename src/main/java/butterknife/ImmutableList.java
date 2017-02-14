package butterknife;

import java.util.AbstractList;
import java.util.RandomAccess;

final class ImmutableList<T> extends AbstractList<T> implements RandomAccess {
    private final T[] views;

    ImmutableList(T[] views) {
        this.views = views;
    }

    public T get(int index) {
        return this.views[index];
    }

    public int size() {
        return this.views.length;
    }

    public boolean contains(Object o) {
        for (T view : this.views) {
            if (view == o) {
                return true;
            }
        }
        return false;
    }
}
