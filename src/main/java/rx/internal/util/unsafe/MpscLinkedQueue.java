package rx.internal.util.unsafe;

import rx.internal.util.atomic.LinkedQueueNode;

public final class MpscLinkedQueue<E> extends BaseLinkedQueue<E> {
    public MpscLinkedQueue() {
        this.consumerNode = new LinkedQueueNode();
        xchgProducerNode(this.consumerNode);
    }

    protected final LinkedQueueNode<E> xchgProducerNode(LinkedQueueNode<E> newVal) {
        LinkedQueueNode oldVal;
        do {
            oldVal = this.producerNode;
        } while (!UnsafeAccess.UNSAFE.compareAndSwapObject(this, P_NODE_OFFSET, oldVal, newVal));
        return oldVal;
    }

    public final boolean offer(E nextValue) {
        if (nextValue == null) {
            throw new NullPointerException("null elements not allowed");
        }
        LinkedQueueNode<E> nextNode = new LinkedQueueNode(nextValue);
        xchgProducerNode(nextNode).soNext(nextNode);
        return true;
    }

    public final E poll() {
        LinkedQueueNode<E> currConsumerNode = lpConsumerNode();
        LinkedQueueNode<E> nextNode = currConsumerNode.lvNext();
        E nextValue;
        if (nextNode != null) {
            nextValue = nextNode.getAndNullValue();
            spConsumerNode(nextNode);
            return nextValue;
        } else if (currConsumerNode == lvProducerNode()) {
            return null;
        } else {
            do {
                nextNode = currConsumerNode.lvNext();
            } while (nextNode == null);
            nextValue = nextNode.getAndNullValue();
            this.consumerNode = nextNode;
            return nextValue;
        }
    }

    public final E peek() {
        LinkedQueueNode<E> currConsumerNode = this.consumerNode;
        LinkedQueueNode<E> nextNode = currConsumerNode.lvNext();
        if (nextNode != null) {
            return nextNode.lpValue();
        }
        if (currConsumerNode == lvProducerNode()) {
            return null;
        }
        do {
            nextNode = currConsumerNode.lvNext();
        } while (nextNode == null);
        return nextNode.lpValue();
    }
}
