package kankan.wheel.widget;

public class ItemsRange {
    private int count;
    private int first;

    public ItemsRange() {
        this(0, 0);
    }

    public ItemsRange(int first, int count) {
        this.first = first;
        this.count = count;
    }

    public int getFirst() {
        return this.first;
    }

    public int getLast() {
        return (getFirst() + getCount()) - 1;
    }

    public int getCount() {
        return this.count;
    }

    public boolean contains(int index) {
        return index >= getFirst() && index <= getLast();
    }
}
