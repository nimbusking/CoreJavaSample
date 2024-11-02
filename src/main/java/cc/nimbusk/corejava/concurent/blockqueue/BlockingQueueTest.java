package cc.nimbusk.corejava.concurent.blockqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueTest {

    public static void main(String[] args) {
        addTest();
//        takeTest();

    }

    /**
     * add 方法是往队列里添加一个元素，如果队列满了，就会抛出异常来提示队列已满。
     */
    private static void addTest() {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(2);
        System.out.println(blockingQueue.add(1));
        System.out.println(blockingQueue.add(2));
        System.out.println(blockingQueue.add(3));
    }

    /**
     * remove 方法的作用是删除元素并返回队列的头节点，如果删除的队列是空的， remove 方法就会抛出异常。
     */
    private static void removeTest() {
        ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(2);
        blockingQueue.add(1);
        blockingQueue.add(2);
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
    }

    /**
     * element 方法是返回队列的头部节点，但是并不删除。如果队列为空，抛出异常
     */
    private static void elementTest() {
        ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(2);
        blockingQueue.element();
    }

    /**
     * offer 方法用来插入一个元素。如果添加成功会返回 true，而如果队列已经满了，返回false
     */
    private static void offerTest() {
        ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(2);
        System.out.println(blockingQueue.offer(1));
        System.out.println(blockingQueue.offer(2));
        System.out.println(blockingQueue.offer(3));
    }

    /**
     * poll 方法作用也是移除并返回队列的头节点。 如果队列为空，返回null
     */
    private static void pollTest() {
        ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(3);
        blockingQueue.offer(1);
        blockingQueue.offer(2);
        blockingQueue.offer(3);
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
    }

    /**
     * peek 方法返回队列的头元素但并不删除。 如果队列为空，返回null
     */
    private static void peekTest() {
        ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(2);
        System.out.println(blockingQueue.peek());
    }

    /**
     * put 方法的作用是插入元素。如果队列已满就无法继续插入,阻塞插入线程，直至队列空出位置
     */
    private static void putTest() {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(2);
        try {
            blockingQueue.put(1);
            blockingQueue.put(2);
            blockingQueue.put(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * take 方法的作用是获取并移除队列的头结点。如果执队列里无数据，则阻塞，直到队列里有数据
     */
    private static void takeTest() {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(2);
        try {
            blockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}