package project1;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: baojianfeng
 * Date: 2018-09-21
 * Description: Implement of Peterson Algorithm
 */
public class PetersonLock {

    private AtomicBoolean[] flag = new AtomicBoolean[2];
    private AtomicInteger victim = new AtomicInteger();

    public PetersonLock() {
        for (int i = 0; i < flag.length; i++) {
            flag[i] = new AtomicBoolean();
        }
    }

    /**
     * lock function
     * @param threadId thread id, value can only be 0 or 1,
     *                 since peterson algorithm is used for 2 threads mutual exclusion
     */
    public void lock(int threadId) {
        flag[threadId].set(true);
        victim.set(threadId);

        while (flag[1 - threadId].get() && victim.get() == threadId) {}
    }

    /**
     * unlock function
     * @param threadId thread id, value can only be 0 or 1,
     *                 since peterson algorithm is used for 2 threads mutual exclusion
     */
    public void unlock(int threadId) {
        flag[threadId].set(false);
    }
}
