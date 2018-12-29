package project2;

import org.junit.Test;
import project2.sem.Bridge;
import project2.sem.Vehicle;

/**
 * Author: baojianfeng
 * Date: 2018-10-18
 */
public class SemaRunTest {
    @Test
    public void testVehicleAllFromLeft() {
        int totalVehicle = 10;
        Vehicle[] vehicles = new Vehicle[totalVehicle];
        Thread[] threads = new Thread[totalVehicle];

        Bridge bridge = new Bridge();
        for (int i = 0; i < totalVehicle; i++) {
            vehicles[i] = new Vehicle(bridge, 0);
            threads[i] = new Thread(vehicles[i], String.valueOf(i));
            threads[i].start();
        }

        // release threads
        for (int i = 0; i < totalVehicle; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.exit(0);
    }

    @Test
    public void testVehicleAllFromRight() {
        int totalVehicle = 5;
        Vehicle[] vehicles = new Vehicle[totalVehicle];
        Thread[] threads = new Thread[totalVehicle];

        Bridge bridge = new Bridge();
        for (int i = 0; i < totalVehicle; i++) {
            vehicles[i] = new Vehicle(bridge, 1);
            threads[i] = new Thread(vehicles[i], String.valueOf(i));
            threads[i].start();
        }

        // release threads
        for (int i = 0; i < totalVehicle; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.exit(0);
    }

    @Test
    public void testVehicleFromBothDirection1() {
        // 3 from left come first, then 2 from left and 2 from
        // right arrive at the bridge and wait
        int totalVehicle = 7;
        Vehicle[] vehicles = new Vehicle[totalVehicle];
        Thread[] threads = new Thread[totalVehicle];

        Bridge bridge = new Bridge();
        int i = 0;
        for (; i <= 2; i++) {
            vehicles[i] = new Vehicle(bridge, 0);
            threads[i] = new Thread(vehicles[i], String.valueOf(i));
            threads[i].start();
        }

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (; i <= 4; i++) {
            vehicles[i] = new Vehicle(bridge, 1);
            threads[i] = new Thread(vehicles[i], String.valueOf(i));
            threads[i].start();
        }
        for (; i <= 6; i++) {
            vehicles[i] = new Vehicle(bridge, 0);
            threads[i] = new Thread(vehicles[i], String.valueOf(i));
            threads[i].start();
        }

        // release threads
        for (int j = 0; j < totalVehicle; j++) {
            try {
                threads[j].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.exit(0);
    }

    @Test
    public void testVehicleFromBothDirection2() {
        // 5 from right come first, then 5 from left and 2 from
        // right arrive at the bridge and wait
        int totalVehicle = 12;
        Vehicle[] vehicles = new Vehicle[totalVehicle];
        Thread[] threads = new Thread[totalVehicle];

        Bridge bridge = new Bridge();
        int i = 0;
        for (; i <= 4; i++) {
            vehicles[i] = new Vehicle(bridge, 1);
            threads[i] = new Thread(vehicles[i], String.valueOf(i));
            threads[i].start();
        }

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (; i <= 9; i++) {
            vehicles[i] = new Vehicle(bridge, 0);
            threads[i] = new Thread(vehicles[i], String.valueOf(i));
            threads[i].start();
        }
        for (; i <= 11; i++) {
            vehicles[i] = new Vehicle(bridge, 1);
            threads[i] = new Thread(vehicles[i], String.valueOf(i));
            threads[i].start();
        }

        // release threads
        for (int j = 0; j < totalVehicle; j++) {
            try {
                threads[j].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.exit(0);
    }

    @Test
    public void testVehicleFromBothDirection3() {
        // vehicles come from both directions in random order
        int totalVehicle = 15;

        Vehicle[] vehicles = new Vehicle[totalVehicle];
        Thread[] threads = new Thread[totalVehicle];

        Bridge bridge = new Bridge();
        for (int i = 0; i < totalVehicle; i++) {
            vehicles[i] = new Vehicle(bridge, (int) Math.round(Math.random()));
            threads[i] = new Thread(vehicles[i], String.valueOf(i));
            threads[i].start();
        }

        // release threads
        for (int i = 0; i < totalVehicle; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.exit(0);
    }

    @Test
    public void testVehicleFromBothDirection4() {
        // 4 vehicle left arrive,
        // then after some time 6 vehicle right arrive,
        // and 3 vehicle left arrive
        int totalVehicle = 13;
        Vehicle[] vehicles = new Vehicle[totalVehicle];
        Thread[] threads = new Thread[totalVehicle];

        Bridge bridge = new Bridge();
        int i = 0;
        for (; i <= 3; i++) {
            vehicles[i] = new Vehicle(bridge, 0);
            threads[i] = new Thread(vehicles[i], String.valueOf(i));
            threads[i].start();
        }

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (; i <= 9; i++) {
            vehicles[i] = new Vehicle(bridge, 1);
            threads[i] = new Thread(vehicles[i], String.valueOf(i));
            threads[i].start();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (; i <= 12; i++) {
            vehicles[i] = new Vehicle(bridge, 0);
            threads[i] = new Thread(vehicles[i], String.valueOf(i));
            threads[i].start();
        }

        // release threads
        for (int j = 0; j < totalVehicle; j++) {
            try {
                threads[j].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.exit(0);
    }

    @Test
    public void testVehicleFromBothDirection5() {
        // 4 vehicle right arrive,
        // then after some time 6 vehicle left arrive,
        // and 3 vehicle right arrive
        int totalVehicle = 13;
        Vehicle[] vehicles = new Vehicle[totalVehicle];
        Thread[] threads = new Thread[totalVehicle];

        Bridge bridge = new Bridge();
        int i = 0;
        for (; i <= 3; i++) {
            vehicles[i] = new Vehicle(bridge, 1);
            threads[i] = new Thread(vehicles[i], String.valueOf(i));
            threads[i].start();
        }

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (; i <= 9; i++) {
            vehicles[i] = new Vehicle(bridge, 0);
            threads[i] = new Thread(vehicles[i], String.valueOf(i));
            threads[i].start();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (; i <= 12; i++) {
            vehicles[i] = new Vehicle(bridge, 1);
            threads[i] = new Thread(vehicles[i], String.valueOf(i));
            threads[i].start();
        }

        // release threads
        for (int j = 0; j < totalVehicle; j++) {
            try {
                threads[j].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.exit(0);
    }
}
