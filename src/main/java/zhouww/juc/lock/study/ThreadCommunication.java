package zhouww.juc.lock.study;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 本文进行 线程协作通信学习 wait()--让线程放弃对象锁并且添加到线程阻塞队列中 notify()/notifyAll()--线程唤醒和condition（await()/signal(),signalAll()）
 * https://www.cnblogs.com/dolphin0520/p/3920385.html 说明 与意义可参考
 * 在使用 wait()/notify/notifyAll时 必须使用在synchronized 方法和synchronized块中，condition（await()/signal(),signalAll()）必须使用在lock锁中
 * 下面 我们来学习一下
 *
 */
public class ThreadCommunication {
   static volatile boolean flag=false;
   static volatile  int pflg=1;
   static WaitNotifyTestMethod waitNotifyTestMethod=new WaitNotifyTestMethod();
    public static void main(String[] args) {


      //  while (true){
        // 传统的 通知内容
           /* Thread t1=new Thread(new WaitNotifyStudy(waitNotifyTestMethod));
            t1.start();
            Thread t2=new Thread(new WaitNotifyStudy2(waitNotifyTestMethod));
            t2.start();
            Thread t3=new Thread(new WaitNotifyStudy3(waitNotifyTestMethod));
            t3.start();*/


            // 使用 condition的操作
        ConditionTestMethod conditionTestMethod=new ConditionTestMethod();

        Thread t3=new Thread(new ConditionStudy3(conditionTestMethod));
        t3.start();
        Thread t1=new Thread(new ConditionStudy(conditionTestMethod));
        t1.start();
        Thread t2=new Thread(new ConditionStudy2(conditionTestMethod));
        t2.start();







    }
    /**
     * 使用传统的 wait和notify
     */
    public static class WaitNotifyStudy implements Runnable{
        private final  WaitNotifyTestMethod waitNotifyTestMethod;
        WaitNotifyStudy(WaitNotifyTestMethod waitNotifyTestMethod){
            this.waitNotifyTestMethod=waitNotifyTestMethod;

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
            waitNotifyTestMethod.getWaitNotify();

        }
    }
    public static class WaitNotifyStudy2 implements Runnable{
        private final  WaitNotifyTestMethod waitNotifyTestMethod;
        WaitNotifyStudy2(WaitNotifyTestMethod waitNotifyTestMethod){
            this.waitNotifyTestMethod=waitNotifyTestMethod;

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
            waitNotifyTestMethod.setWaitNotify();

        }
    }
    public static class WaitNotifyStudy3 implements Runnable{
        private final  WaitNotifyTestMethod waitNotifyTestMethod;
        WaitNotifyStudy3(WaitNotifyTestMethod waitNotifyTestMethod){
            this.waitNotifyTestMethod=waitNotifyTestMethod;

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
            waitNotifyTestMethod.setWaitNotify3();

        }
    }

    public static class WaitNotifyTestMethod {
        public   synchronized void getWaitNotify() {
                        System.out.println(Thread.currentThread().getName()+"到了王五开始运行 setWaitNotify获得了锁");
                        for(int i=0;i<10;i++){
                            System.out.println(Thread.currentThread().getName()+"getWaitNotify-----------"+i);
                        }
                        System.out.println(Thread.currentThread().getName()+"王五运行 结束 该李四运行");
                        notifyAll();
                      pflg++;

            }



            public synchronized void setWaitNotify() {

            while (true){
                try {
                    while(pflg==2 || pflg==1) {
                        System.out.println(Thread.currentThread().getName()+"还未到张三运行呢即将进入等待 释放锁");
                        waitNotifyTestMethod.notifyAll();
                        waitNotifyTestMethod.wait();
                    }
                        System.out.println(Thread.currentThread().getName()+"到了张三开始运行 setWaitNotify获得了锁");
                        for(int i=0;i<10;i++){
                            System.out.println(Thread.currentThread().getName()+"setWaitNotify-----------"+i);
                        }
                        System.out.println(Thread.currentThread().getName()+"到了张三运行 结束");
                    waitNotifyTestMethod.notify();
                    flag=true;
                    pflg=1;
                        break;
                    }
                 catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            }

        public synchronized void setWaitNotify3() {

            while (true){
                try {

                    while (pflg==1){
                        System.out.println(Thread.currentThread().getName()+"还未到李四运行呢即将进入等待 释放锁");
                        waitNotifyTestMethod. notifyAll();
                        waitNotifyTestMethod.wait();
                    }
                        System.out.println(Thread.currentThread().getName()+"到了李四开始运行 setWaitNotify获得了锁11");
                        for(int i=0;i<10;i++){
                            System.out.println(Thread.currentThread().getName()+"setWaitNotify3-----------"+i);
                        }
                        System.out.println(Thread.currentThread().getName()+"到了李四运行 结束");
                    waitNotifyTestMethod.notify();
                      pflg=6;
                        break;


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }


        }

        }





    /**
     * 使用传统的 condition await signal
     */
    public static class ConditionStudy implements Runnable{
        private final  ConditionTestMethod conditionTestMethod;
        ConditionStudy(ConditionTestMethod conditionTestMethod){
            this.conditionTestMethod=conditionTestMethod;

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
            conditionTestMethod.getCondition();

        }
    }
    public static class ConditionStudy2 implements Runnable{
        private final  ConditionTestMethod conditionTestMethod;
        ConditionStudy2(ConditionTestMethod conditionTestMethod){
            this.conditionTestMethod=conditionTestMethod;

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
            conditionTestMethod.setCondition();

        }
    }
    public static class ConditionStudy3 implements Runnable{
        private final  ConditionTestMethod conditionTestMethod;
        ConditionStudy3(ConditionTestMethod conditionTestMethod){
            this.conditionTestMethod=conditionTestMethod;

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
            conditionTestMethod.setCondition3();

        }
    }
    public static class ConditionTestMethod {
        private Lock lock = new ReentrantLock();
        private Condition condition=lock.newCondition();
        public    void getCondition() {
            try{
                lock.lock();
                System.out.println(Thread.currentThread().getName()+"到了王五开始运行 getCondition获得了锁");
                for(int i=0;i<10;i++){
                    System.out.println(Thread.currentThread().getName()+"getCondition-----------"+i);
                }
                System.out.println(Thread.currentThread().getName()+"王五运行 结束 该李四运行");
                condition.signalAll();
                pflg++;
            }finally {
                lock.unlock();
            }



        }



        public  void setCondition() {
            try{
                lock.lock();
            while (true){
                try {
                    while(pflg==2 || pflg==1) {
                        System.out.println(Thread.currentThread().getName()+"还未到张三运行呢即将进入等待 释放锁");
                        condition.signal();
                        condition.await();
                    }
                    System.out.println(Thread.currentThread().getName()+"到了张三开始运行 setWaitNotify获得了锁");
                    for(int i=0;i<10;i++){
                        System.out.println(Thread.currentThread().getName()+"setWaitNotify-----------"+i);
                    }
                    System.out.println(Thread.currentThread().getName()+"到了张三运行 结束");
                    condition.signal();
                    flag=true;
                    pflg=1;
                    break;
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            }finally {
                lock.unlock();
            }
        }

        public synchronized void setCondition3() {
            try{
                lock.lock();
            while (true){
                try {

                    while (pflg==1){
                        System.out.println(Thread.currentThread().getName()+"还未到李四运行呢即将进入等待 释放锁");
                        condition.signal();
                        condition.await();
                    }
                    System.out.println(Thread.currentThread().getName()+"到了李四开始运行 setWaitNotify获得了锁11");
                    for(int i=0;i<10;i++){
                        System.out.println(Thread.currentThread().getName()+"setWaitNotify3-----------"+i);
                    }
                    System.out.println(Thread.currentThread().getName()+"到了李四运行 结束");
                    condition.signal();
                    pflg=6;
                    break;


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }finally {
            lock.unlock();
        }

        }

    }
    }

