package cc.nimbusk.corejava.concurent;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class OrderedPrinting {
    public static void main(String[] args) {
        // 初始化信号量，奇数先执行
        Semaphore semOdd = new Semaphore(1);
        Semaphore semEven = new Semaphore(0);
        AtomicBoolean oddEnd = new AtomicBoolean(false);
        AtomicBoolean evenEnd = new AtomicBoolean(false);

        int[] odds = {1, 3, 5, 7, 9, 11, 13};
        int[] evens = {2, 4, 6, 8, 10, 12, 14, 16};

        Thread oddThread = new Thread(() -> {
            for (int num : odds) {
                try {
                    if (!evenEnd.get()) {
                        semOdd.acquire(); // 等待奇数信号量
                    }
                    System.out.println(num);
                    if (!evenEnd.get()) {
                        semEven.release(); // 释放偶数信号量
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (oddEnd.compareAndSet(false, true)) {
                // 确保最终释放
                semOdd.release();
            }
        });

        Thread evenThread = new Thread(() -> {
            for (int num : evens) {
                try {
                    if (!oddEnd.get()) {
                        semEven.acquire(); // 等待偶数信号量
                    }
                    System.out.println(num);
                    if (!oddEnd.get()) {
                        semOdd.release(); // 释放奇数信号量
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (evenEnd.compareAndSet(false, true)) {
                semEven.release();
            }
        });

        oddThread.start();
        evenThread.start();
    }
}
