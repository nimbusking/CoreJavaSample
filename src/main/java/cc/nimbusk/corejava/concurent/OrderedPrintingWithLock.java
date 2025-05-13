package cc.nimbusk.corejava.concurent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class OrderedData {
    private final Lock lock = new ReentrantLock();
    private final Condition[] conditions = new Condition[2];
    private final int[] odds = {1, 3, 5, 7, 9};
    private int oddIndex = 0;
    private final int[] evens = {2, 4, 6, 8, 10};
    private int evenIndex = 0;
    private int currentId = 1;

    public OrderedData() {
        for (int i = 0; i < conditions.length; i++) {
            conditions[i] = lock.newCondition();
        }
    }

    public void print(int threadId) throws InterruptedException {
        lock.lock();
        try {
            for (int i = 0; i < odds.length + evens.length; i++) {
                while (currentId != threadId) {
                    // 不是直接阻塞
                    conditions[threadId - 1].await();
                }
                if (threadId == 1 && oddIndex < odds.length) {
                    System.out.println(Thread.currentThread().getName() + " : " + odds[oddIndex]);
                    oddIndex++;

                } else if (threadId == 2 && evenIndex < evens.length) {
                    System.out.println(Thread.currentThread().getName() + " : " + evens[evenIndex]);
                    evenIndex++;
                }
                // 轮转至下一个线程，计算下一个线程号
                currentId = (currentId % 2) + 1;
                // 唤醒下一个线程
                conditions[currentId - 1].signal();
            }
            for (Condition cond : conditions) cond.signal();
        } finally {
            lock.unlock();
        }
    }

}

class TestPrintThread extends Thread {
    private final OrderedData orderedData;
    private final int threadId;

    public TestPrintThread(OrderedData orderedData, int threadId) {
        this.orderedData = orderedData;
        this.threadId = threadId;
    }

    @Override
    public void run() {
        try {
            orderedData.print(threadId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class OrderedPrintingWithLock {
    public static void main(String[] args) {
        OrderedData orderedData = new OrderedData();
        new TestPrintThread(orderedData, 1).start();
        new TestPrintThread(orderedData, 2).start();
    }
}
