package rx.schedulers;

public final class Timestamped<T> {
    private final long timestampMillis;
    private final T value;

    public Timestamped(long timestampMillis, T value) {
        this.value = value;
        this.timestampMillis = timestampMillis;
    }

    public long getTimestampMillis() {
        return this.timestampMillis;
    }

    public T getValue() {
        return this.value;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Timestamped)) {
            return false;
        }
        Timestamped<?> other = (Timestamped) obj;
        if (this.timestampMillis != other.timestampMillis) {
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

    public int hashCode() {
        return ((((int) (this.timestampMillis ^ (this.timestampMillis >>> 32))) + 31) * 31) + (this.value == null ? 0 : this.value.hashCode());
    }

    public String toString() {
        return String.format("Timestamped(timestampMillis = %d, value = %s)", new Object[]{Long.valueOf(this.timestampMillis), this.value.toString()});
    }
}
