package project1;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: baojianfeng
 * Date: 2018-09-14
 * Description: Implementation of Bakery Algorithm
 */
public class BakeryLock extends AbstractLock {
    private AtomicBoolean[] flag;
    private AtomicInteger[] label;

    public BakeryLock(int n) {
        flag = new AtomicBoolean[n];
        label = new AtomicInteger[n];
        for (int i = 0; i < n; i++) {
            flag[i] = new AtomicBoolean();
            label[i] = new AtomicInteger();
        }
    }

    /**
     * lock function of bakery algorithm
     * @param ic IncrementCounter is a runnable object, related to each thread
     */
    public void lock(IncrementCounter ic) {
        int id = ic.getThreadId();
        flag[id].set(true);
        label[id].set(getMaxLabel() + 1);
        for (int i = 0; i < flag.length; i++) {
            if (i == id) continue;

            // only allow the thread with minimum label value or minimum label index to
            // enter into critical section
            while (flag[i].get() && (label[i].get() < label[id].get() || (label[i].get() == label[id].get() && i < id))) {}
        }
    }

    /**
     * unlock function of bakery algorithm
     * @param ic IncrementCounter is a runnable object, related to each thread
     */
    public void unlock(IncrementCounter ic) {

        int id = ic.getThreadId();
        flag[id].set(false);
    }

    /**
     * find current labels maximum value
     * @return maximum value
     */
    private int getMaxLabel() {
        int max = label[0].get();
        for (int i = 1; i < label.length; i++) {
            if (label[i].get() > max)
                max = label[i].get();
        }
        return max;
    }
}
