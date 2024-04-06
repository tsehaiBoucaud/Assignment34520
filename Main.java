import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.io.*;

class Node {
    int giftTag;
    Node next;
    ReentrantLock nodeLock = new ReentrantLock();

    public Node() {
    }

    public Node(int giftTag) {
        this.giftTag = giftTag;
    }
}

class LinkedList {
    Node dummyNode = new Node();

    void addNode(int giftTag) {
        Node temp = new Node(giftTag);
        Node current = dummyNode;
        current.nodeLock.lock();
        try {
            while (current.next != null && current.next.giftTag < giftTag) {
                Node nextNode = current.next;
                nextNode.nodeLock.lock();
                current.nodeLock.unlock();
                current = nextNode;
            }
            temp.next = current.next;
            current.next = temp;
        } finally {
            current.nodeLock.unlock();
        }
    }

    void removeNode() {
        if (dummyNode.next != null) {
            Node temp = dummyNode.next;
            temp.nodeLock.lock();
            dummyNode.next = temp.next;
            temp.nodeLock.unlock();
        }
    }
}

public class Main {
    static Stack<Integer> bagOfPresents = new Stack<>();
    static LinkedList list = new LinkedList();

    static class ServantWork implements Runnable {
        Random r = new Random();
        int id;
        private PrintWriter pw;

        ServantWork(int id, PrintWriter pw) {
            this.id = id;
            this.pw = pw;
        }

        @Override
        public void run() {
            while (!bagOfPresents.empty() || list.dummyNode.next != null) {
                int task = r.nextInt(3);
                switch (task) {
                    case 0:
                        if (!bagOfPresents.empty()) {
                            int tag = bagOfPresents.pop();
                            list.addNode(tag);
                            pw.println("Servant " + id + " added gift " + tag);
                        }
                        break;
                    case 1:
                        if (list.dummyNode.next != null) {
                            pw.println("Servant " + id + " wrote a thank you note and removed gift.");
                            list.removeNode();
                        }
                        break;
                    case 2:
                        if (list.dummyNode.next != null) {
                            int searchTag = r.nextInt(500000) + 1;
                            Node temp = list.dummyNode.next;
                            while (temp != null) {
                                temp.nodeLock.lock();
                                if (temp.giftTag == searchTag) {
                                    pw.println("Servant " + id + " found gift" + searchTag);
                                    temp.nodeLock.unlock();
                                    break;
                                }
                                Node nextNode = temp.next;
                                temp.nodeLock.unlock();
                                temp = nextNode;
                            }
                            if (temp == null)
                                pw.println("Servant " + id + " couldn't find gift " + searchTag);
                        }
                        break;
                }
            }
            pw.println("Servant " + id + "  has completed their work");

        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(System.out);
        for (int i = 1; i <= 500000; ++i) {
            bagOfPresents.push(i);
        }
        Thread[] threads = new Thread[4];
        for (int i = 0; i < 4; ++i) {
            (threads[i] = new Thread(new ServantWork(i, pw))).start();
        }
        for (Thread th : threads) {
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        pw.close();
    }
}