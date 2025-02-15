package cc.nimbusk.corejava.concurent;

public class OrderedPrintingWithWaitNotify {
    private static final Object lock = new Object();
    private static boolean isOddTurn = true;

    public static void main(String[] args) {
        int[] odds = {1, 3, 5, 7, 9};
        int[] evens = {2, 4, 6, 8, 10};

        Thread oddThread = new Thread(() -> {
            for (int num : odds) {
                synchronized (lock) {
                    while (!isOddTurn) { // 检查是否轮到自己
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(num);
                    isOddTurn = false;
                    lock.notifyAll(); // 唤醒偶数线程
                }
            }
        });

        Thread evenThread = new Thread(() -> {
            for (int num : evens) {
                synchronized (lock) {
                    while (isOddTurn) { // 检查是否轮到自己
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(num);
                    isOddTurn = true;
                    lock.notifyAll(); // 唤醒奇数线程
                }
            }
        });

        oddThread.start();
        evenThread.start();
    }
}
