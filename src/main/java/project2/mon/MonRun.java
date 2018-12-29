package project2.mon;

/**
 * Author: baojianfeng
 * Date: 2018-10-02
 * Usage: test various vehicle arriving situations
 */
public class MonRun {

    public static void main(String[] args) {
        int testCaseNum = Integer.parseInt(args[0]);
        switch (testCaseNum) {
            case 1:
                testVehicleAllFromLeft();
                break;
            case 2:
                testVehicleAllFromRight();
                break;
            case 3:
                testVehicleFromBothDirection1();
                break;
            case 4:
                testVehicleFromBothDirection2();
                break;
            case 5:
                testVehicleFromBothDirection3();
                break;
            case 6:
                testVehicleFromBothDirection4();
                break;
            case 7:
                testVehicleFromBothDirection5();
                break;
        }


    }

    /**
     * test vehicles are all from left direction passing the bridge
     */
    public static void testVehicleAllFromLeft() {
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

    /**
     * test vehicles are all from right direction passing the bridge
     */
    public static void testVehicleAllFromRight() {
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

    /**
     * vehicle arriving situation:
     * 3 from left come first, then 2 from left and 2 from
     * right arrive at the bridge and wait
     */
    public static void testVehicleFromBothDirection1() {
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

    /**
     * vehicle arriving situation:
     * 5 from right come first, then 5 from left and 2 from
     * right arrive at the bridge and wait
     */
    public static void testVehicleFromBothDirection2() {
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

    /**
     * vehicle arriving situation:
     * vehicles come from both directions in random order
     */
    public static void testVehicleFromBothDirection3() {
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

    /**
     * vehicle arriving situation:
     * 4 vehicle left arrive,
     * then after some time 6 vehicle right arrive,
     * and 3 vehicle left arrive
     */
    public static void testVehicleFromBothDirection4() {
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

    /**
     * vehicle arriving situation:
     * 4 vehicle right arrive,
     * then after some time 6 vehicle left arrive,
     * and 3 vehicle right arrive
     */
    public static void testVehicleFromBothDirection5() {
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
