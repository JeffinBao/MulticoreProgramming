package project2.sem;

import java.util.concurrent.Semaphore;

/**
 * Author: baojianfeng
 * Date: 2018-10-01
 */
public class Bridge {

    // wait and onBridge counters
    private int leftWait = 0, rightWait = 0;
    private int leftOnBridge = 0, rightOnBridge = 0;
    // use one mutex to protect varies counters
    private Semaphore mutex = new Semaphore(1);

    // TODO write into summary: no need to use semaphore array,
    // TODO instead use two semaphores for left and right direction are enough
    private Semaphore leftWaitSem = new Semaphore(0);
    private Semaphore rightWaitSem = new Semaphore(0);

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
            SemUtil.wait(mutex);
            if (rightOnBridge > 0 || rightWait > 0) {
                leftWait++;
                System.out.println("Vehicle Left  " + Thread.currentThread().getName() + " arrived the bridge");
                SemUtil.signal(mutex);
                // this is where the difference between using monitor and semaphore
                SemUtil.wait(leftWaitSem);
                SemUtil.wait(mutex);
                leftWait--;
                leftOnBridge++;
                System.out.println("Vehicle Left  " + Thread.currentThread().getName() + " is crossing the bridge");
                SemUtil.signal(mutex);
            } else {
                System.out.println("Vehicle Left  " + Thread.currentThread().getName() + " arrived the bridge");
                leftOnBridge++;
                System.out.println("Vehicle Left  " + Thread.currentThread().getName() + " is crossing the bridge");
                SemUtil.signal(mutex);
            }

        } else {
            SemUtil.wait(mutex);
            if (leftOnBridge > 0 || leftWait > 0) {
                rightWait++;
                System.out.println("Vehicle Right  " + Thread.currentThread().getName() + " arrived the bridge");
                SemUtil.signal(mutex);
                // this is where the difference between using monitor and semaphore
                SemUtil.wait(rightWaitSem);
                SemUtil.wait(mutex);
                rightWait--;
                rightOnBridge++;
                System.out.println("Vehicle Right  " + Thread.currentThread().getName() + " is crossing the bridge");
                SemUtil.signal(mutex);
            } else {
                System.out.println("Vehicle Right  " + Thread.currentThread().getName() + " arrived the bridge");
                rightOnBridge++;
                System.out.println("Vehicle Right  " + Thread.currentThread().getName() + " is crossing the bridge");
                SemUtil.signal(mutex);
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
            SemUtil.wait(mutex);
            leftOnBridge--;
            System.out.println("Vehicle Left " + Thread.currentThread().getName() + " left the bridge");
            if (leftOnBridge == 0) {
                if (rightWait > 0) {
                    if (leftWait > 0) {
                        // release one vehicle facing right
                        SemUtil.signal(rightWaitSem);
                    } else {
                        // release all vehicles facing right
                        for (int i = 0; i < rightWait; i++) {
                            SemUtil.signal(rightWaitSem);
                        }
                    }
                }
            }
            SemUtil.signal(mutex);
        } else {
            SemUtil.wait(mutex);
            rightOnBridge--;
            System.out.println("Vehicle Right " + Thread.currentThread().getName() + " left the bridge");
            if (rightOnBridge == 0) {
                if (leftWait > 0) {
                    if (rightWait > 0) {
                        // release one vehicle facing left
                        SemUtil.signal(leftWaitSem);
                    } else {
                        // release all vehicles facing left
                        for (int i = 0; i < leftWait; i++) {
                            SemUtil.signal(leftWaitSem);
                        }
                    }
                }
            }
            SemUtil.signal(mutex);
        }
    }

}
