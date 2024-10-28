package cc.nimbusk.corejava.concurent.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一个小的“人满发车”场景的demo
 */
@Slf4j
public class CyclicBarrierTest3 {

    public static void main(String[] args) throws InterruptedException {

        AtomicInteger counter = new AtomicInteger(); // 计数标识选手序号
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5, 5, 1000, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                (r) -> new Thread(r, "第" + counter.addAndGet(1) + "号 "),
                new ThreadPoolExecutor.AbortPolicy());

        int cbInitSize = 5;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(cbInitSize,
                () -> System.out.println("===========裁判：比赛开始==========="));

        for (int i = 0; i < cbInitSize * 4; i++) {
            threadPoolExecutor.submit(new Runner(cyclicBarrier));
        }
        threadPoolExecutor.shutdown();

    }

    static class Runner extends Thread {
        private CyclicBarrier cyclicBarrier;

        public Runner(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                int sleepMills = ThreadLocalRandom.current().nextInt(1000);
                Thread.sleep(sleepMills);
                System.out.println(Thread.currentThread().getName() + " 选手已就位, 准备共用时:" + sleepMills + " ms" + ", 准备完成序号:" + cyclicBarrier.getNumberWaiting());
                cyclicBarrier.await();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

}