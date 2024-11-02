package cc.nimbusk.corejava.concurent.disruptor.event;


import lombok.Data;

/**
 * 消息载体(事件)
 */
@Data
public class OrderEvent {

    private long value;
    private String name;

}
