package cc.nimbusk.corejava.concurent;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static cc.nimbusk.corejava.concurent.SeqPrint.THREAD_NUMS;
import static cc.nimbusk.corejava.concurent.SeqPrint.TOTAL_NUMS;

class SharedData {
    private int number = 1; // 起始数字
    private final Lock lock = new ReentrantLock(); // 独占锁控制打印资源访问
    private final Condition[] conditions = new Condition[THREAD_NUMS]; // Condition精准唤醒对应线程
    private int currentId = 1; // 当前应执行的线程ID

    public SharedData() {
        for (int i = 0; i < conditions.length; i++) {
            conditions[i] = lock.newCondition();
        }
    }

    public void print(int threadId) throws InterruptedException {
        lock.lock();
        try {
            while (number <= TOTAL_NUMS) {
                // 检查是否轮到当前线程执行
                while (currentId != threadId) {
                    // 不是直接阻塞
                    conditions[threadId - 1].await();
                }
                if (number > TOTAL_NUMS) {
                    // 轮转至下一个线程，计算下一个线程号
                    currentId = (currentId % THREAD_NUMS) + 1;
                    // 唤醒下一个线程
                    conditions[currentId - 1].signal();
                    break;
                }

                System.out.println(Thread.currentThread().getName() + " : " + number);
                number++;
                // 轮转至下一个线程，计算下一个线程号
                currentId = (currentId % THREAD_NUMS) + 1;
                // 唤醒下一个线程
                conditions[currentId - 1].signal();
            }
            // 确保所有线程最终都能退出
//            for (Condition cond : conditions) cond.signal();
        } finally {
            lock.unlock();
        }
    }
}

class PrintThread extends Thread {
    private final SharedData sharedData;
    private final int threadId;

    public PrintThread(SharedData sharedData, int threadId) {
        this.sharedData = sharedData;
        this.threadId = threadId;
    }

    @Override
    public void run() {
        try {
            sharedData.print(threadId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class SeqPrint {
    public static final int TOTAL_NUMS = 47;
    public static final int THREAD_NUMS = 2;
    public static void main(String[] args) {
        SharedData sharedData = new SharedData();
        new PrintThread(sharedData, 1).start();
        new PrintThread(sharedData, 2).start();
//        new PrintThread(sharedData, 3).start();
//        new PrintThread(sharedData, 4).start();
//        new PrintThread(sharedData, 5).start();
//        new PrintThread(sharedData, 6).start();
    }
}
