package rx.internal.util.atomic;

import android.support.v7.widget.ActivityChooserView.ActivityChooserViewAdapter;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

abstract class BaseLinkedAtomicQueue<E> extends AbstractQueue<E> {
    private final AtomicReference<LinkedQueueNode<E>> consumerNode = new AtomicReference();
    private final AtomicReference<LinkedQueueNode<E>> producerNode = new AtomicReference();

    protected final LinkedQueueNode<E> lvProducerNode() {
        return (LinkedQueueNode) this.producerNode.get();
    }

    protected final LinkedQueueNode<E> lpProducerNode() {
        return (LinkedQueueNode) this.producerNode.get();
    }

    protected final void spProducerNode(LinkedQueueNode<E> node) {
        this.producerNode.lazySet(node);
    }

    protected final LinkedQueueNode<E> xchgProducerNode(LinkedQueueNode<E> node) {
        return (LinkedQueueNode) this.producerNode.getAndSet(node);
    }

    protected final LinkedQueueNode<E> lvConsumerNode() {
        return (LinkedQueueNode) this.consumerNode.get();
    }

    protected final LinkedQueueNode<E> lpConsumerNode() {
        return (LinkedQueueNode) this.consumerNode.get();
    }

    protected final void spConsumerNode(LinkedQueueNode<E> node) {
        this.consumerNode.lazySet(node);
    }

    public final Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    public final int size() {
        LinkedQueueNode<E> chaserNode = lvConsumerNode();
        LinkedQueueNode<E> producerNode = lvProducerNode();
        int size = 0;
        while (chaserNode != producerNode && size < ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) {
            LinkedQueueNode<E> next;
            do {
                next = chaserNode.lvNext();
            } while (next == null);
            chaserNode = next;
            size++;
        }
        return size;
    }

    public final boolean isEmpty() {
        return lvConsumerNode() == lvProducerNode();
    }
}
