package rx.internal.schedulers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;
import rx.internal.util.RxThreadFactory;

public final class GenericScheduledExecutorService implements SchedulerLifecycle {
    public static final GenericScheduledExecutorService INSTANCE = new GenericScheduledExecutorService();
    static final ScheduledExecutorService NONE = Executors.newScheduledThreadPool(0);
    private static final RxThreadFactory THREAD_FACTORY = new RxThreadFactory(THREAD_NAME_PREFIX);
    private static final String THREAD_NAME_PREFIX = "RxScheduledExecutorPool-";
    private final AtomicReference<ScheduledExecutorService> executor = new AtomicReference(NONE);

    static {
        NONE.shutdownNow();
    }

    private GenericScheduledExecutorService() {
        start();
    }

    public void start() {
        int count = Runtime.getRuntime().availableProcessors();
        if (count > 4) {
            count /= 2;
        }
        if (count > 8) {
            count = 8;
        }
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(count, THREAD_FACTORY);
        if (!this.executor.compareAndSet(NONE, exec)) {
            exec.shutdownNow();
        } else if (!NewThreadWorker.tryEnableCancelPolicy(exec) && (exec instanceof ScheduledThreadPoolExecutor)) {
            NewThreadWorker.registerExecutor((ScheduledThreadPoolExecutor) exec);
        }
    }

    public void shutdown() {
        ScheduledExecutorService exec;
        do {
            exec = (ScheduledExecutorService) this.executor.get();
            if (exec == NONE) {
                return;
            }
        } while (!this.executor.compareAndSet(exec, NONE));
        NewThreadWorker.deregisterExecutor(exec);
        exec.shutdownNow();
    }

    public static ScheduledExecutorService getInstance() {
        return (ScheduledExecutorService) INSTANCE.executor.get();
    }
}
