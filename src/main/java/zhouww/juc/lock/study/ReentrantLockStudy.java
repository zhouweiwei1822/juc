package zhouww.juc.lock.study;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock 锁的学习
 * 该锁的特点：
 * 1、它一个可重入独占锁，内部实现了aqs的功能，
 * 2、可重入锁的实现是通过aqs中state的计数来体现的
 *
 */
public class ReentrantLockStudy {
    private  static final ReentrantLock lock=new ReentrantLock();// 初始化

}
