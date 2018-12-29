package project2.mon;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Author: baojianfeng
 * Date: 2018-10-02
 */
public class Bridge {
    // wait and onBridge counters
    private int leftWait = 0, rightWait = 0;
    private int leftOnBridge = 0, rightOnBridge = 0;
    // use a lock to protect varies counters
    private Lock lock = new ReentrantLock();
    // conditions to decide whether two direction vehicles can cross bridge or not
    private Condition leftCanEnter = lock.newCondition();
    private Condition rightCanEnter = lock.newCondition();

    public Bridge() {

    }

    /**
     * when vehicle arrives at a bridge in two different directions.
     * Two scenarios(now only consider when the direction is left):
     * 1. rightOnBridge > 0 || rightWait > 0 means there are vehicle in the right direction
     *    waiting or already on the bridge, hence left direction vehicle must wait and leftWait++
     * 2. otherwise, left direction vehicle can cross the bridge immediately by increasing leftOnBridge counter.
     *
     * For right direction, the scenarios are similar
     * @param direction two directions: left and right. Using 0 as left direction and 1 as right direction
     */
    public void arriveBridge(int direction) {
        if (direction == 0) {
            lock.lock();
            try {
                if (rightOnBridge > 0 || rightWait > 0) {
                    leftWait++;
                    System.out.println("Vehicle Left  " + Thread.currentThread().getName() + " arrived the bridge");
                    leftCanEnter.await();
                    leftWait--;
                    leftOnBridge++;
                    System.out.println("Vehicle Left  " + Thread.currentThread().getName() + " is crossing the bridge");
                } else {
                    System.out.println("Vehicle Left  " + Thread.currentThread().getName() + " arrived the bridge");
                    leftOnBridge++;
                    System.out.println("Vehicle Left  " + Thread.currentThread().getName() + " is crossing the bridge");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        } else {
            lock.lock();
            try {
                if (leftOnBridge > 0 || leftWait > 0) {
                    rightWait++;
                    System.out.println("Vehicle Right  " + Thread.currentThread().getName() + " arrived the bridge");
                    rightCanEnter.await();
                    rightWait--;
                    rightOnBridge++;
                    System.out.println("Vehicle Right  " + Thread.currentThread().getName() + " is crossing the bridge");
                } else {
                    System.out.println("Vehicle Right  " + Thread.currentThread().getName() + " arrived the bridge");
                    rightOnBridge++;
                    System.out.println("Vehicle Right  " + Thread.currentThread().getName() + " is crossing the bridge");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * when vehicle leaves a bridge
     * only consider when the direction is left:
     * 1. decreasing leftOnBridge by one and check the remaining left direction vehicles on the bridge, if
     *    no left direction vehicle is currently on the bridge, then check whether right waiting vehicles' counter
     *    is greater than zero. If it is, then check left waiting vehicles' counter. If leftWait > 0, only signal one
     *    right direction vehicle to cross; otherwise, signal all right direction vehicles.
     *
     * For right direction, the condition is similar
     * @param direction two directions: left and right. Using 0 as left direction and 1 as right direction
     */
    public void leaveBridge(int direction) {
        if (direction == 0) {
            lock.lock();
            try {
                leftOnBridge--;
                System.out.println("Vehicle Left " + Thread.currentThread().getName() + " left the bridge");
                if (leftOnBridge == 0) {
                    if (rightWait > 0) {
                        if (leftWait > 0) {
                            // release one vehicle facing right
                            rightCanEnter.signal();
                        } else {
                            // release all vehicles facing right
                            rightCanEnter.signalAll();
                        }
                    }
                }
            } finally {
                lock.unlock();
            }
        } else {
            lock.lock();
            try {
                rightOnBridge--;
                System.out.println("Vehicle Right " + Thread.currentThread().getName() + " left the bridge");
                if (rightOnBridge == 0) {
                    if (leftWait > 0) {
                        if (rightWait > 0) {
                            // release one vehicle facing left
                            leftCanEnter.signal();
                        } else {
                            // release all vehicles facing left
                            leftCanEnter.signalAll();
                        }
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
