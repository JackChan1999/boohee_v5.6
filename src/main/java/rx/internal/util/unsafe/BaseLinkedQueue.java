package rx.internal.util.unsafe;

import android.support.v7.widget.ActivityChooserView.ActivityChooserViewAdapter;
import java.util.Iterator;
import rx.internal.util.atomic.LinkedQueueNode;

abstract class BaseLinkedQueue<E> extends BaseLinkedQueueConsumerNodeRef<E> {
    long p00;
    long p01;
    long p02;
    long p03;
    long p04;
    long p05;
    long p06;
    long p07;
    long p30;
    long p31;
    long p32;
    long p33;
    long p34;
    long p35;
    long p36;
    long p37;

    BaseLinkedQueue() {
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
