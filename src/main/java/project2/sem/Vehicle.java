package project2.sem;
/**
 * Author: baojianfeng
 * Date: 2018-10-01
 * Usage: Vehicle class, can be initialized as many vehicle threads
 */
public class Vehicle implements Runnable {
    private Bridge bridge;
    private int direction;

    public Vehicle(Bridge bridge, int direction) {
        this.bridge = bridge;
        this.direction = direction;
    }

    /**
     * two operations of vehicle: arrive at a bridge and leave a bridge
     */
    public void run() {
        bridge.arriveBridge(direction);
        bridge.leaveBridge(direction);
    }
}
