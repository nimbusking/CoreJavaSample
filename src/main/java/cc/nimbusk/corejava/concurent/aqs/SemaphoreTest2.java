package cc.nimbusk.corejava.concurent.aqs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SemaphoreTest2 {

    /**
     * 实现一个同时只能处理5个请求的限流器
     */
    private static Semaphore semaphore = new Semaphore(5);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 定义一个线程池
     */
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor
            (10, 50, 60,
                    TimeUnit.SECONDS, new LinkedBlockingDeque<>(200));

    /**
     * 模拟执行方法
     */
    public static void exec() {
        try {
            //占用1个资源
            semaphore.acquire(1);
            //TODO  模拟业务执行
            System.out.println(Thread.currentThread().getName() + ":" + sdf.format(new Date()) + ":执行exec方法");
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放一个资源
            semaphore.release(1);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (; ; ) {
            Thread.sleep(100);
            // 模拟请求以10个/s的速度
            executor.execute(() -> exec());
        }
    }
}