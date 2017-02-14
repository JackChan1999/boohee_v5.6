package rx.schedulers;

public class TimeInterval<T> {
    private final long intervalInMilliseconds;
    private final T value;

    public TimeInterval(long intervalInMilliseconds, T value) {
        this.value = value;
        this.intervalInMilliseconds = intervalInMilliseconds;
    }

    public long getIntervalInMilliseconds() {
        return this.intervalInMilliseconds;
    }

    public T getValue() {
        return this.value;
    }

    public int hashCode() {
        return ((((int) (this.intervalInMilliseconds ^ (this.intervalInMilliseconds >>> 32))) + 31) * 31) + (this.value == null ? 0 : this.value.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TimeInterval<?> other = (TimeInterval) obj;
        if (this.intervalInMilliseconds != other.intervalInMilliseconds) {
            return false;
        }
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
            return true;
        } else if (this.value.equals(other.value)) {
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        return "TimeInterval [intervalInMilliseconds=" + this.intervalInMilliseconds + ", value=" + this.value + "]";
    }
}
