package cn.willon.pcms.pcmsmidware.thred;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThrealPoolManager
 *
 * @author Willon
 * @since 2019-04-06
 */
public enum ThreadPoolManager {

    /**
     * 单例
     */
    INSTANCE;

    private static final int CORE_POOL_SIZE = 30;
    private static final int MAXIMUM_POOL_SIZE = 100;
    private static final int KEEP_ALIVE_TIME = 180;
    private static final int BLOCK_QUEUE_SIZE = 200;
    private final ListeningExecutorService listeningExecutorService;

    ThreadPoolManager() {
        final ThreadPoolExecutor executor =
                new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<>(BLOCK_QUEUE_SIZE), new PcmsMidwareThreadFactory("pcms_thredPool"), new ThreadPoolExecutor.DiscardPolicy());
        listeningExecutorService = MoreExecutors.listeningDecorator(executor);
    }

    /**
     * 添加异步任务
     *
     * @param task 任务
     * @param <T>  返回参数
     * @return 返回空
     */
    public <T> ListenableFuture<T> addTask(Callable<T> task) {
        return listeningExecutorService.submit(task);
    }

    private class PcmsMidwareThreadFactory implements ThreadFactory {

        private final ThreadGroup threadGroup;
        private final String prefix;
        private final AtomicInteger atomicInteger = new AtomicInteger();

        PcmsMidwareThreadFactory(String poolName) {
            SecurityManager securityManager = System.getSecurityManager();
            threadGroup = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            prefix = "pool-" + poolName + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {

            Thread t = new Thread(threadGroup, r, prefix + atomicInteger.getAndIncrement());
            // 不允许守护线程
            if (t.isDaemon()) {
                t.setDaemon(false);
            }

            // 不允许优先级
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

}

