package rx.internal.util.atomic;

public final class SpscLinkedAtomicQueue<E> extends BaseLinkedAtomicQueue<E> {
    public SpscLinkedAtomicQueue() {
        LinkedQueueNode<E> node = new LinkedQueueNode();
        spProducerNode(node);
        spConsumerNode(node);
        node.soNext(null);
    }

    public boolean offer(E nextValue) {
        if (nextValue == null) {
            throw new NullPointerException("null elements not allowed");
        }
        LinkedQueueNode<E> nextNode = new LinkedQueueNode(nextValue);
        lpProducerNode().soNext(nextNode);
        spProducerNode(nextNode);
        return true;
    }

    public E poll() {
        LinkedQueueNode<E> nextNode = lpConsumerNode().lvNext();
        if (nextNode == null) {
            return null;
        }
        E nextValue = nextNode.getAndNullValue();
        spConsumerNode(nextNode);
        return nextValue;
    }

    public E peek() {
        LinkedQueueNode<E> nextNode = lpConsumerNode().lvNext();
        if (nextNode != null) {
            return nextNode.lpValue();
        }
        return null;
    }
}
