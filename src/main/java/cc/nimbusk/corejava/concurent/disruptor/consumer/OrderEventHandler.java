package cc.nimbusk.corejava.concurent.disruptor.consumer;

import cc.nimbusk.corejava.concurent.disruptor.event.OrderEvent;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 消费者
 */
@Slf4j
public class OrderEventHandler implements EventHandler<OrderEvent>, WorkHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        // TODO 消费逻辑
        log.info("消费者" + Thread.currentThread().getName()
                + "获取数据value:" + event.getValue() + ",name:" + event.getName());
    }

    @Override
    public void onEvent(OrderEvent event) throws Exception {
        // TODO 消费逻辑
        log.info("消费者" + Thread.currentThread().getName()
                + "获取数据value:" + event.getValue() + ",name:" + event.getName());
    }
}
