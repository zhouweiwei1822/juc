package zhouww.juc.lock.study;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockStudy {
    public static void main(String[] args) {
        ReentrantReadWriteLock lock=new ReentrantReadWriteLock();
        lock.readLock();
    }
}
