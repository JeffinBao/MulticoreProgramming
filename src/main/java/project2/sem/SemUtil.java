package project2.sem;

import java.util.concurrent.Semaphore;

/**
 * Author: baojianfeng
 * Date: 2018-10-01
 */
public class SemUtil {

    /**
     * wait on a semaphore
     * @param sem input semaphore
     */
    public static void wait(Semaphore sem) {
        try {
            sem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * signal a semaphore
     * @param sem input semaphore
     */
    public static void signal(Semaphore sem) {
        sem.release();
    }
}
