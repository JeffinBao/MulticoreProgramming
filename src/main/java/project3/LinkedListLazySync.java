package project3;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: baojianfeng
 * Date: 2018-11-16
 */
public class LinkedListLazySync<T> {
    private Node<T> head, tail;
    private volatile int expectSize = 0;

    public LinkedListLazySync() {
        head = new Node<>(Integer.MIN_VALUE);
        tail = new Node<>(Integer.MAX_VALUE);
        head.next = tail;
    }

    /**
     * get the actual size of the list, exclude sentinel nodes
     * @return size
     */
    public int getSize() {
        Node<T> node = head;
        int size = 0;
        while (node != null) {
            size++;
            node = node.next;
        }
        return size - 2;
    }

    public int getExpectSize() {
        return expectSize;
    }

    /**
     * add operation
     * @param item item
     * @return if the key doesn't exist, add into list and return true, otherwise return false
     */
    public boolean add(T item) {
        int key = item.hashCode();
        System.out.println("Enter add operation");
        while (true) {
            List<Node<T>> list = locateWindow(key);
            Node<T> pred = list.get(0);
            Node<T> cur = list.get(1);
            pred.lock();
            try {
                cur.lock();
                try {
                    if (validate(pred, cur)) {
                        if (cur.key == key)
                            return false;
                        else {
                            // store the node which is currently being removed
                            // if it exists, otherwise passing null instead
                            Node<T> node = new Node<>(item);
                            node.next = cur;
                            pred.next = node;
                            increExpectSize();
                            return true;
                        }
                    }
                } finally {
                    cur.unlock();
                }
            } finally {
                pred.unlock();
                System.out.println("Exit add operation");
            }
        }
    }

    /**
     * remove operation
     * @param item item
     * @return true if the item exists and been deleted, otherwise return false
     */
    public boolean remove(T item) {
        int key = item.hashCode();
        System.out.println("Enter remove operation");
        while (true) {
            List<Node<T>> list = locateWindow(key);
            Node<T> pred = list.get(0);
            Node<T> cur = list.get(1);
            pred.lock();
            try {
                cur.lock();
                try {
                    if (validate(pred, cur)) {
                        if (cur.key != key)
                            return false;
                        else {
                            cur.marked = true;
                            pred.next = cur.next;
                            decreExpectSize();
                            return true;
                        }
                    }
                } finally {
                    cur.unlock();
                }
            } finally {
                pred.unlock();
                System.out.println("Exit remove operation");
            }
        }
    }

    /**
     * replace operation
     * @param oldItem old item to be deleted if exists
     * @param newItem new item to be added if not exists
     * @return true if the list is modified in any way
     */
    public boolean replace(T oldItem, T newItem) {
        int oldKey = oldItem.hashCode();
        int newKey = newItem.hashCode();
        while (true) {
            List<Node<T>> oldWindowList = locateWindow(oldKey);
            Node<T> removePred = oldWindowList.get(0);
            Node<T> removeCur = oldWindowList.get(1);
            List<Node<T>> newWindowList = locateWindow(newKey);
            Node<T> addPred = newWindowList.get(0);
            Node<T> addCur = newWindowList.get(1);
            // when two windows overlap
            if (removePred.key == addPred.key && removeCur.key == addCur.key) {
                removePred.lock();
                try {
                    removeCur.lock();
                    try {
                        if (validate(removePred, removeCur))
                            return singleWindowReplace(removePred, removeCur, oldKey, newKey, newItem);
                    } finally {
                        removeCur.unlock();
                    }
                } finally {
                    removePred.unlock();
                }
            } else if (removeCur.key <= addPred.key) {
                // oldItem window locates before newItem window
                removePred.lock();
                try {
                    removeCur.lock();
                    try {
                        addPred.lock();
                        try {
                            addCur.lock();
                            try {
                                if (validate(removePred, removeCur) && validate(addPred, addCur)) {
                                    return doubleWindowReplace(removePred, removeCur, addPred, addCur, oldKey, newKey, newItem);
                                }
                            } finally {
                                addCur.unlock();
                            }
                        } finally {
                            addPred.unlock();
                        }
                    } finally {
                        removeCur.unlock();
                    }
                } finally {
                    removePred.unlock();
                }

            } else if (addCur.key <= removePred.key) {
                // newItem window locates before oldItem window
                addPred.lock();
                try {
                    addCur.lock();
                    try {
                        removePred.lock();
                        try {
                            removeCur.lock();
                            try {
                                if (validate(addPred, addCur) && validate(removePred, removeCur)) {
                                    return doubleWindowReplace(removePred, removeCur, addPred, addCur, oldKey, newKey, newItem);
                                }
                            } finally {
                                removeCur.unlock();
                            }
                        } finally {
                            removePred.unlock();
                        }
                    } finally {
                        addCur.unlock();
                    }
                } finally {
                    addPred.unlock();
                }
            }
        }
    }

    /**
     * check whether an item is in the list
     * @param item element
     * @return true or false
     * 2 scenarios:
     * 1. node is not involved in replace operation
     * 2. node is involved in replace operation, need to check removingNode is marked,
     *    if it is mark, means the replace operation took effect, cur node presented in
     *    the list.
     */
    public boolean contains(T item) {
        int key = item.hashCode();
        System.out.println("Enter contains operation");
        Node<T> cur = head;
        while (cur.key < key) {
            cur = cur.next;
        }

        boolean result = cur.key == key && !cur.marked;
        if (cur.removingNode != null)
            result = result && cur.removingNode.marked;
        System.out.println("Exit contains operation");
        return result;
    }

    /**
     * check whether the linkedlist is legal or not
     * if it's legal, it should not contain duplicate keys
     * and it should arrange in ascending order by keys
     * @return true if the linkedlist is legal, otherwise return false
     */
    public boolean checkLegal() {
        Node<T> pred = head;
        Node<T> cur = head.next;
        System.out.print(pred.key + " ");
        while (pred != null && cur != null) {
            if (cur.key <= pred.key)
                return false;
            System.out.print(cur.key + " ");
            pred = cur;
            cur = cur.next;
        }
        return true;
    }

    /**
     * locate a window of predecessor and current nodes using given key
     * @param key key
     * @return a list of two nodes: predecessor and current nodes
     */
    private List<Node<T>> locateWindow(int key) {
        Node<T> pred = head;
        Node<T> cur = head.next;
        while (cur.key < key) {
            pred = cur;
            cur = cur.next;
        }
        List<Node<T>> list = new ArrayList<>();
        list.add(pred);
        list.add(cur);
        return list;
    }

    /**
     * validate the pred, cur window
     * @param pred predecessor node
     * @param cur current node
     * @return true if the window is still valid
     */
    private boolean validate(Node<T> pred, Node<T> cur) {
        return !pred.marked && pred.next == cur;
    }

    /**
     * there is only one window in replace operation
     * @param pred predecessor node
     * @param cur current node
     * @param oldKey key to be removed
     * @param newKey key to be added
     * @param newItem new item
     * @return true if the list is modified
     */
    private boolean singleWindowReplace(Node<T> pred, Node<T> cur, int oldKey, int newKey, T newItem) {
        System.out.println("Enter single window replace");
        boolean result = false;
        Node<T> node = null;
        if (cur.key != newKey) {
            if (cur.key == oldKey) {
                // replace oldItem with newItem
                cur.key = newKey;
                cur.item = newItem;
            } else {
                // only add newItem
                node = new Node<T>(newItem);
                node.next = cur;
                pred.next = node;
                increExpectSize();
            }
            result = true;
        } else {
            if (cur.key == oldKey) {
                // only remove oldItem
                cur.marked = true;
                pred.next = cur.next;
                result = true;
                decreExpectSize();
            }
        }
        System.out.println("Exit single window replace");
        return result;
    }

    /**
     * there are two windows in replace operation
     * @param removePred old key window's predecessor node
     * @param removeCur old key window's current node
     * @param addPred new key window's predecessor node
     * @param addCur new key window's current node
     * @param removeKey old key
     * @param addKey new key
     * @param addItem new item
     * @return true if the list is modified
     */
    private boolean doubleWindowReplace(Node<T> removePred, Node<T> removeCur, Node<T> addPred,
                                        Node<T> addCur, int removeKey, int addKey, T addItem) {
        System.out.println("Enter double window replace");
        boolean result = false;
        Node<T> node = null;
        // if add key not exits
        if (addCur.key != addKey) {
            if (removeCur.key == removeKey) {
                // keep track of the removing node's information if exists
                node = new Node<T>(addItem, removeCur);
            } else {
                node = new Node<T>(addItem);
            }
            node.next = addCur;
            addPred.next = node;
            result = true;
            increExpectSize();
        }
        // if remove key exists
        if (removeCur.key == removeKey) {
            removeCur.marked = true;
            removePred.next = removeCur.next;
            // clear the removing node information in the newly added node
            if (node != null)
                node.removingNode = null;
            result = true;
            decreExpectSize();
        }
        System.out.println("Exit double window replace");
        return result;
    }

    /**
     * atomically increase expectSize field
     */
    private synchronized void increExpectSize() {
        expectSize++;
    }

    /**
     * atomically decrease expectSize field
     */
    private synchronized void decreExpectSize() {
        expectSize--;
    }

}
