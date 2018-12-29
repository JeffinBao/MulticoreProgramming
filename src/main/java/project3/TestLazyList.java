package project3;

import java.util.Random;

/**
 * Author: baojianfeng
 * Date: 2018-11-21
 * Usage: test lazy synchronization linkedlist with 10 threads
 *        and one million operations each thread
 */
public class TestLazyList {

    public static void main(String[] args) {
        LinkedListLazySync<Integer> list = new LinkedListLazySync<>();
        int[] keySpace = {3, 4, 1, 5, 8, 11, 7, 13, 2, 15};
        int operationNum = 1000000;
        LazyListOps[] lazyListOpses = new LazyListOps[10];
        for (int i = 0; i < 10; i++) {
            if (i < 3) {
                lazyListOpses[i] = new LazyListOps(list, "add", operationNum, keySpace);
            } else if (i < 6) {
                lazyListOpses[i] = new LazyListOps(list, "remove", operationNum, keySpace);
            } else if (i < 9) {
                lazyListOpses[i] = new LazyListOps(list, "replace", operationNum, keySpace);
            } else {
                lazyListOpses[i] = new LazyListOps(list, "contains", operationNum, keySpace);
            }
        }
        for (int i = 0; i < 10; i++) {
            lazyListOpses[i].start();
        }

        for (int i = 0; i < 10; i++) {
            try {
                lazyListOpses[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        boolean legalCheck = list.checkLegal();
        System.out.println();
        System.out.println("The list is legal? " + legalCheck);
        System.out.println("Actual list size: " + list.getSize());
        System.out.println("Expect list size: " + list.getExpectSize());

    }

    private static class LazyListOps extends Thread {
        private LinkedListLazySync<Integer> list;
        private String operationType;
        private int operationNum;
        private int[] keySpace;
        private int keySpaceSize;
        private Random random;

        LazyListOps(LinkedListLazySync<Integer> list, String operationType, int operationNum, int[] keySpace) {
            this.list = list;
            this.operationType = operationType;
            this.operationNum = operationNum;
            this.keySpace = keySpace;
            this.keySpaceSize = keySpace.length;
            this.random = new Random();
        }

        @Override
        public void run() {
            switch (operationType) {
                case "add":
                    testAdd();
                    break;
                case "remove":
                    testRemove();
                    break;
                case "replace":
                    testReplace();
                    break;
                case "contains":
                    testContains();
                    break;
                default:
                    break;
            }
        }

        /**
         * test add operation
         */
        private void testAdd() {
            for (int i = 0; i < operationNum; i++) {
                int randIdx = random.nextInt(keySpaceSize);
                list.add(keySpace[randIdx]);
            }
        }

        /**
         * test remove operation
         */
        private void testRemove() {
            for (int i = 0; i < operationNum; i++) {
                int randIdx = random.nextInt(keySpaceSize);
                list.remove(keySpace[randIdx]);
            }
        }

        /**
         * test replace operation
         */
        private void testReplace() {
            for (int i = 0; i < operationNum; i++) {
                int randIdxOld = random.nextInt(keySpaceSize);
                int randIdxNew = random.nextInt(keySpaceSize);
                list.replace(keySpace[randIdxOld], keySpace[randIdxNew]);
            }
        }

        /**
         * test contains operation
         */
        private void testContains() {
            for (int i = 0; i < operationNum; i++) {
                int randIdx = random.nextInt(keySpaceSize);
                list.contains(keySpace[randIdx]);
            }
        }
    }
}
