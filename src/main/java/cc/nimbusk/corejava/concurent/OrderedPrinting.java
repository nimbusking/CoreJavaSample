package cc.nimbusk.corejava.concurent;

import java.util.concurrent.Semaphore;

public class OrderedPrinting {
    public static void main(String[] args) {
        // 初始化信号量，奇数先执行
        Semaphore semOdd = new Semaphore(1);
        Semaphore semEven = new Semaphore(0);

        int[] odds = {1, 3, 5, 7, 9};
        int[] evens = {2, 4, 6, 8, 10};

        Thread oddThread = new Thread(() -> {
            for (int num : odds) {
                try {
                    semOdd.acquire(); // 等待奇数信号量
                    System.out.println(num);
                    semEven.release(); // 释放偶数信号量
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread evenThread = new Thread(() -> {
            for (int num : evens) {
                try {
                    semEven.acquire(); // 等待偶数信号量
                    System.out.println(num);
                    semOdd.release(); // 释放奇数信号量
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        oddThread.start();
        evenThread.start();
    }
}
