package project3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Author: baojianfeng
 * Date: 2018-11-16
 * Description: Node data structure
 */
public class Node<T> {
    int key; // item's hashcode value
    T item;
    boolean marked; // set to true if the node is logically deleted
    Node<T> next;
    Node<T> removingNode; // used for storing the current removing node in replace operation, make the operation atomic
    private Lock lock = new ReentrantLock();

    /**
     * constructor, only used for sentinel node initialization
     * @param key key
     */
    public Node(int key) {
        this.key = key;
    }

    /**
     * constructor, pass item to initialize
     * @param item node element
     */
    public Node(T item) {
        this.item = item;
        this.key = item.hashCode();
    }

    /**
     * constructor, pass item and the node being removed in replace operation
     * @param item node element
     * @param removingNode node currently being removed
     */
    public Node(T item, Node<T> removingNode) {
        this.item = item;
        this.key = item.hashCode();
        this.removingNode = removingNode;
    }

    /**
     * lock the node
     */
    public void lock() {
        lock.lock();
    }

    /**
     * unlock the node
     */
    public void unlock() {
        lock.unlock();
    }


}
