package project1;

/**
 * Author: baojianfeng
 * Date: 2018-09-14
 * Description: Implementation of Tournament Algorithm
 */
public class TournamentLock extends AbstractLock {
    private PetersonLock[] pLocks;
    private int k; // number of levels

    public TournamentLock(int n) {
        k = MathUtil.log2Ceil(n);

        // create PetersonLock tree using an array
        pLocks = new PetersonLock[(int) Math.pow(2, k)];
        for (int i = 1; i < pLocks.length; i++) {
            pLocks[i] = new PetersonLock();
        }
    }

    /**
     * lock function, at each level(from down to top),
     * different threads will compete with its "sibling" thread
     * to get the lock and finally enter into critical section
     * @param ic IncrementCounter object
     */
    public void lock(IncrementCounter ic) {
        int id = ic.getThreadId() + (int) Math.pow(2, k);
        for (int i = 0; i < k; i++) {
            ic.getLockPath()[i] = id % 2;
            id /= 2;
            pLocks[id].lock(ic.getLockPath()[i]);
        }
    }

    /**
     * unlock function, unlock the acquired locks from top to down
     * @param ic IncrementCounter object
     */
    public void unlock(IncrementCounter ic) {
        int id = 1;
        for (int i = k - 1; i >= 0; i--) {
            pLocks[id].unlock(ic.getLockPath()[i]);
            id = 2 * id + ic.getLockPath()[i];
        }
    }
}
