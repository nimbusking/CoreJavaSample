package cc.nimbusk.corejava.concurent;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 增强版生产者-消费者模型
 * 特性：
 * 1. 双缓冲队列减少锁竞争
 * 2. 动态速率控制
 * 3. 生产消费平衡监控
 */
public class AdvancedProducerConsumer {

    // 主缓冲区及锁控制
    private final Queue<Integer> buffer = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    // 备份缓冲区用于平滑流量
    private final Queue<Integer> backupBuffer = new LinkedList<>();

    // 流控参数
    private static final int BUFFER_CAPACITY = 10;
    private static final int BACKUP_CAPACITY = 5;
    private volatile boolean isRunning = true;
    private long produceCount = 0;
    private long consumeCount = 0;

    // 生产速率控制器
    private volatile int produceIntervalMs = 100;
    private volatile int consumeIntervalMs = 150;

    // 生产者任务
    class Producer implements Runnable {
        @Override
        public void run() {
            try {
                while (isRunning) {
                    // 流控检查
                    if (produceCount - consumeCount > BUFFER_CAPACITY * 2) {
                        TimeUnit.MILLISECONDS.sleep(produceIntervalMs * 2);
                        continue;
                    }

                    int product = (int) (Math.random() * 100);
                    lock.lock();
                    try {
                        // 主队列满时使用备用队列
                        while (buffer.size() >= BUFFER_CAPACITY) {
                            if (backupBuffer.size() < BACKUP_CAPACITY) {
                                backupBuffer.offer(product);
                                System.out.println("转存备用队列: " + product);
                                break;
                            }
                            System.out.println("缓冲区满，生产者等待");
                            notFull.await();
                        }

                        if (buffer.size() < BUFFER_CAPACITY) {
                            buffer.offer(product);
                            produceCount++;
                            System.out.println("生产: " + product + " 主队列大小: " + buffer.size());
                            notEmpty.signal();
                        }
                    } finally {
                        lock.unlock();
                    }
                    TimeUnit.MILLISECONDS.sleep(produceIntervalMs);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // 消费者任务
    class Consumer implements Runnable {
        @Override
        public void run() {
            try {
                while (isRunning || !buffer.isEmpty()) {
                    lock.lock();
                    try {
                        // 主队列空时检查备用队列
                        while (buffer.isEmpty() && !backupBuffer.isEmpty()) {
                            int product = backupBuffer.poll();
                            buffer.offer(product);
                            System.out.println("从备用恢复: " + product);
                        }

                        while (buffer.isEmpty()) {
                            if (!isRunning) return;
                            System.out.println("缓冲区空，消费者等待");
                            notEmpty.await(500, TimeUnit.MILLISECONDS);
                        }

                        int product = buffer.poll();
                        consumeCount++;
                        System.out.println("消费: " + product + " 队列剩余: " + buffer.size());
                        notFull.signal();

                        // 动态调整消费速率
                        if (buffer.size() < BUFFER_CAPACITY / 2) {
                            consumeIntervalMs = Math.max(50, consumeIntervalMs - 10);
                        }
                    } finally {
                        lock.unlock();
                    }
                    TimeUnit.MILLISECONDS.sleep(consumeIntervalMs);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // 监控线程
    class Monitor implements Runnable {
        @Override
        public void run() {
            while (isRunning) {
                System.out.println("\n==== 监控报告 ====");
                System.out.println("生产总量: " + produceCount);
                System.out.println("消费总量: " + consumeCount);
                System.out.println("主队列大小: " + buffer.size());
                System.out.println("备用队列大小: " + backupBuffer.size());
                System.out.println("生产间隔: " + produceIntervalMs + "ms");
                System.out.println("消费间隔: " + consumeIntervalMs + "ms");
                System.out.println("不平衡值: " + (produceCount - consumeCount));
                System.out.println("=================\n");

                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    // 优雅停机
    public void shutdown() {
        isRunning = false;
        lock.lock();
        try {
            notEmpty.signalAll();
            notFull.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AdvancedProducerConsumer pc = new AdvancedProducerConsumer();

        // 创建2个生产者，3个消费者
        Thread[] producers = new Thread[2];
        Thread[] consumers = new Thread[3];

        for (int i = 0; i < producers.length; i++) {
            producers[i] = new Thread(pc.new Producer());
            producers[i].start();
        }

        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Thread(pc.new Consumer());
            consumers[i].start();
        }

        // 启动监控线程
        Thread monitor = new Thread(pc.new Monitor());
        monitor.start();

        // 运行30秒后停机
        TimeUnit.SECONDS.sleep(30);
        pc.shutdown();

        // 等待线程结束
        for (Thread p : producers) p.join();
        for (Thread c : consumers) c.join();
        monitor.interrupt();
    }
}
