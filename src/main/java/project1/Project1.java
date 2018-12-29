package project1;

/**
 * Author: baojianfeng
 * Date: 2018-09-14
 */
public class Project1 {
    private static volatile int counter = 0;
    private static AbstractLock lock;
    private static int maxValue = 100000; // max request of each thread

    public static void main(String[] args) {
        for (int threadNum = 2; threadNum <= 32; threadNum++) {
            long bakeryTime = 0;
            long tournamentTime = 0;
            for (int round = 1; round <= 5; round++) {
                IncrementCounter[] ics = new IncrementCounter[threadNum];
                Thread[] threads = new Thread[threadNum];

                // Bakery algorithm
                lock = new BakeryLock(threadNum);
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < threads.length; i++) {
                    ics[i] = new IncrementCounter(maxValue, i, threadNum, lock, counter);
                    threads[i] = new Thread(ics[i], String.valueOf(i));
                }
                for (int i = 0; i < threads.length; i++) {
                    threads[i].start();
                }

                for (int i = 0; i < threadNum; i++) {
                    try {
                        threads[i].join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                long endTime = System.currentTimeMillis();
                bakeryTime += (endTime - startTime);
                System.out.println("lock algorithm: BAKERY" +
                        ",     threadNum: " + threadNum +
                        ", round: " + round +
                        ", time elapsed: " + String.valueOf(endTime - startTime));

                // Tournament algorithm
                lock = new TournamentLock(threadNum);
                counter = 0;
                startTime = System.currentTimeMillis();
                for (int i = 0; i < threads.length; i++) {
                    ics[i] = new IncrementCounter(maxValue, i, threadNum, lock, counter);
                    threads[i] = new Thread(ics[i], String.valueOf(i));
                }
                for (int i = 0; i < threads.length; i++) {
                    threads[i].start();
                }

                for (int i = 0; i < threadNum; i++) {
                    try {
                        threads[i].join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                endTime = System.currentTimeMillis();
                tournamentTime += (endTime - startTime);
                System.out.println("lock algorithm: TOURNAMENT" +
                        ", threadNum: " + threadNum +
                        ", maxValue: " + maxValue +
                        ", time elapsed: " + String.valueOf(endTime - startTime));
                System.out.println("---------------------------------------------------");

            }

            long averageBakery = bakeryTime / 5;
            long averageTournament = tournamentTime / 5;
            System.out.println("threadNum: " + threadNum +
                    "average time of BAKERY Algorithm:     " + averageBakery);
            System.out.println("threadNum: " + threadNum +
                    "average time of TOURNAMENT Algorithm: " + averageTournament);
            System.out.println("---------------------------------------------------");
            System.out.println("---------------------------------------------------");
        }

    }
}
