package project1;


/**
 * Author: baojianfeng
 * Date: 2018-09-14
 * Description: IncrementCounter class, run the critical section, record the thread id and lock path
 */
public class IncrementCounter implements Runnable {
    private long maxValue;
    private int threadId;
    private int[] lockPath; // to record the lock path of each thread, used for find the path when unlocking
    private AbstractLock lock;
    private volatile int counter;

    public IncrementCounter(long maxValue, int threadId, int threadNum, AbstractLock lock, int counter) {
        this.maxValue = maxValue;
        this.threadId = threadId;
        this.lock = lock;
        this.counter = counter;

        if (lock instanceof TournamentLock) {
            int maxLevel = MathUtil.log2Ceil(threadNum);
            lockPath = new int[maxLevel];
        }
    }

    /**
     * critical section
     */
    public void run() {
        for (int i = 0; i < maxValue; i++) {
            lock.lock(this);
            try {
                counter++;
            } finally {
                lock.unlock(this);
            }
        }

    }

    /**
     * get current thread id
     * @return thread id
     */
    public int getThreadId() {
        return threadId;
    }

    /**
     * get current thread lock path,
     * which is the locks current thread used at each level
     * to enter the critical section at last
     * @return thread lock path array
     */
    public int[] getLockPath() {
        return lockPath;
    }
}
