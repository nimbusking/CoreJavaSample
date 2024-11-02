package cc.nimbusk.corejava.concurent.disruptor.event;

import com.lmax.disruptor.EventFactory;

/**
 * 事件工厂
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {

    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();
    }
}
