package okio;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

class Timeout$1 extends Timeout {
    Timeout$1() {
    }

    public Timeout timeout(long timeout, TimeUnit unit) {
        return this;
    }

    public Timeout deadlineNanoTime(long deadlineNanoTime) {
        return this;
    }

    public void throwIfReached() throws IOException {
    }
}
