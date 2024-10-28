package cc.nimbusk.corejava.concurent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class DebugReentrantLockDemo {

    private static int total = 0;
    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(() -> {
                // 加锁 下面一行设置断点
                // 在idea中将断点模式设置为thread（下同），方便对指定线程执行操作
                lock.lock();
                try {
                    // 临界区代码，下面一行设置断点
                    for (int j = 0; j < 10000; j++) {
                        total++;
                    }
                } finally {
                    // 解锁
                    lock.unlock();
                }
            });
            thread.start();
        }

        Thread.sleep(2000);
        log.info("summary result is:[{}]", total);
    }
}