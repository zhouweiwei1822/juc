package zhouww.juc.lock.study;

/**
 * 本文进行 线程协作通信学习 wait()--让线程放弃对象锁并且添加到线程阻塞队列中 notify()/notifyAll()--线程唤醒和condition（await()/signal(),signalAll()）
 * https://www.cnblogs.com/dolphin0520/p/3920385.html 说明 与意义可参考
 * 在使用 wait()/notify/notifyAll时 必须使用在synchronized 方法和synchronized块中，condition（await()/signal(),signalAll()）必须使用在lock锁中
 * 下面 我们来学习一下
 *
 */
public class ThreadCommunication {
   static volatile boolean flag=false;
   static WaitNotifyTestMethod waitNotifyTestMethod=new WaitNotifyTestMethod();
    public static void main(String[] args) {


        Thread t1=new Thread(new WaitNotifyStudy(waitNotifyTestMethod));

        t1.start();
        Thread t2=new Thread(new WaitNotifyStudy2(waitNotifyTestMethod));
        t2.start();
        Thread t3=new Thread(new WaitNotifyStudy2(waitNotifyTestMethod));
        t3.start();


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


    public static class WaitNotifyTestMethod {
        public   synchronized void getWaitNotify() {
          /*  while (true){
                   notifyAll();
                try {
                    flag=true;
                   wait();
                    System.out.println(Thread.currentThread().getName()+"获得了锁11");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/


            }



            public synchronized void setWaitNotify() {

                while (true){
                    notifyAll();
                        flag=false;
                        try {
                            wait();
                            System.out.println(Thread.currentThread().getName()+"获得了锁22");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }



                    flag=true;

                }


            }

        }
    }

