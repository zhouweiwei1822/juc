package zhouww.juc.lock.study;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantLock 锁的学习
 * 该锁的特点：
 * 1、它一个可重入独占锁，内部实现了aqs的功能，
 * 2、可重入锁的实现是通过aqs中state的计数来体现的,就是同一个线程可以多次获取该锁，并且每获得一次state就加一 在释放锁时需要根据获取重入锁的次数进行释放，就是获得多少次锁就释放多少次
 * Uses AQS state to represent the number of holds on the lock --ReentrantLock 注解翻译：使用AQS状态表示锁上持有的数量（这里指可重入持有锁的数量）
 * 3、该锁和synchronize性质相同 都是可重入独占锁 ，ReentrantLock 需要进行手动释放锁
 * 4、ReentrantLock 内部实现了两种锁 公平锁、非公平锁  默认使用非公平锁
 *
 *
 */
public class  ReentrantLockStudy {
    private  static final ReentrantLock lockNoFir=new ReentrantLock();// 非公平锁
    private  static final ReentrantLock lockFir=new ReentrantLock(true);// 公平锁

    public static void main(String[] args) {
        Thread t1=null;
        ReentranLockNoFir op=  new ReentranLockNoFir(lockNoFir);
        for(int i=0;i<3;i++){
            // 公平锁的 案例
           /* Thread t=  new Thread(new ReentranLockFir(lockFir));
                  t.setName("firLock-"+i);
                  t.start();*/
                  // 非公平锁的案例
             t1=  new Thread(op);
            t1.setName("nofir-"+i);
            t1.start();


        }
        try {
            TimeUnit.SECONDS.sleep(10);
            System.out.println( t1.isAlive()+""+t1.getName());
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LockSupport.unpark(t1);
    }
    private  static class ReentranLockNoFir implements Runnable{// 非公平锁的实现
        final  ReentrantLock lockNoFir;
        ReentranLockNoFir(ReentrantLock lockNoFir){
            this.lockNoFir=lockNoFir;
        }
        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            LockSupport.park(this);
        /*    lockNoFir.lock();
            while(true){
                System.out.println(Thread.currentThread().getName());
            }*/
            System.out.println(Thread.currentThread().getName());
           // LockSupport.park(this);
           /* System.out.println(Thread.currentThread().getName());
            LockSupport.park(this);
            System.out.println(Thread.interrupted());*/
          //  lockNoFir.unlock();
        }
    }
   public static class ReentranLockFir implements Runnable{// 公平锁的实现
        final  ReentrantLock lockFir;
        ReentranLockFir(ReentrantLock lockFir){
            this.lockFir=lockFir;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {

            lockFir.lock();
            System.out.println(Thread.currentThread().getName());
            lockFir.unlock();
        }
    }
    }



