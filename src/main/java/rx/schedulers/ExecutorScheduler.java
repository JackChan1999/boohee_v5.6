package rx.schedulers;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscription;
import rx.functions.Action0;
import rx.internal.schedulers.GenericScheduledExecutorService;
import rx.internal.schedulers.ScheduledAction;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.MultipleAssignmentSubscription;
import rx.subscriptions.Subscriptions;

final class ExecutorScheduler extends Scheduler {
    final Executor executor;

    static final class ExecutorSchedulerWorker extends Worker implements Runnable {
        final Executor executor;
        final ConcurrentLinkedQueue<ScheduledAction> queue = new ConcurrentLinkedQueue();
        final CompositeSubscription tasks = new CompositeSubscription();
        final AtomicInteger wip = new AtomicInteger();

        public ExecutorSchedulerWorker(Executor executor) {
            this.executor = executor;
        }

        public Subscription schedule(Action0 action) {
            if (isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            Subscription ea = new ScheduledAction(action, this.tasks);
            this.tasks.add(ea);
            this.queue.offer(ea);
            if (this.wip.getAndIncrement() != 0) {
                return ea;
            }
            try {
                this.executor.execute(this);
                return ea;
            } catch (RejectedExecutionException t) {
                this.tasks.remove(ea);
                this.wip.decrementAndGet();
                RxJavaPlugins.getInstance().getErrorHandler().handleError(t);
                throw t;
            }
        }

        public void run() {
            do {
                ScheduledAction sa = (ScheduledAction) this.queue.poll();
                if (!sa.isUnsubscribed()) {
                    sa.run();
                }
            } while (this.wip.decrementAndGet() > 0);
        }

        public Subscription schedule(final Action0 action, long delayTime, TimeUnit unit) {
            if (delayTime <= 0) {
                return schedule(action);
            }
            if (isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            ScheduledExecutorService service;
            if (this.executor instanceof ScheduledExecutorService) {
                service = this.executor;
            } else {
                service = GenericScheduledExecutorService.getInstance();
            }
            MultipleAssignmentSubscription first = new MultipleAssignmentSubscription();
            final MultipleAssignmentSubscription mas = new MultipleAssignmentSubscription();
            mas.set(first);
            this.tasks.add(mas);
            final Subscription removeMas = Subscriptions.create(new Action0() {
                public void call() {
                    ExecutorSchedulerWorker.this.tasks.remove(mas);
                }
            });
            ScheduledAction ea = new ScheduledAction(new Action0() {
                public void call() {
                    if (!mas.isUnsubscribed()) {
                        Subscription s2 = ExecutorSchedulerWorker.this.schedule(action);
                        mas.set(s2);
                        if (s2.getClass() == ScheduledAction.class) {
                            ((ScheduledAction) s2).add(removeMas);
                        }
                    }
                }
            });
            first.set(ea);
            try {
                ea.add(service.schedule(ea, delayTime, unit));
                return removeMas;
            } catch (RejectedExecutionException t) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(t);
                throw t;
            }
        }

        public boolean isUnsubscribed() {
            return this.tasks.isUnsubscribed();
        }

        public void unsubscribe() {
            this.tasks.unsubscribe();
        }
    }

    public ExecutorScheduler(Executor executor) {
        this.executor = executor;
    }

    public Worker createWorker() {
        return new ExecutorSchedulerWorker(this.executor);
    }
}
