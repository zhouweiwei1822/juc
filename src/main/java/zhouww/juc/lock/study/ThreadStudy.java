package zhouww.juc.lock.study;

import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.yield;

/**
 *
 * 线程基础内容
 * 线程 的状态：创建（new ），就绪(runable),运行（running）,阻塞(blocked){时间等待timewaiting、waiting}，消亡（dead）
 * thread中几个重要的方法:
 * 1）start方法
 *
 * 　　start()用来启动一个线程，当调用start方法后，系统才会开启一个新的线程来执行用户定义的子任务，在这个过程中，会为相应的线程分配需要的资源。
 *
 * 　　2）run方法
 *
 * 　　run()方法是不需要用户来调用的，当通过start方法启动一个线程之后，当线程获得了CPU执行时间，便进入run方法体去执行具体的任务。注意，继承Thread类必须重写run方法，在run方法中定义具体要执行的任务
 * 　3）sleep方法
 *
 * 　　sleep方法有两个重载版本：
 * sleep(long millis)     //参数为毫秒
 *
 * sleep(long millis,int nanoseconds)    //第一参数为毫秒，第二个参数为纳秒
 * 　　sleep相当于让线程睡眠，交出CPU，让CPU去执行其他的任务。
 *
 * 　　但是有一点要非常注意，sleep方法不会释放锁，也就是说如果当前线程持有对某个对象的锁，则即使调用sleep方法，其他线程也无法访问这个对象
 * 可运行SleepThread 查看现象
 * 4）yield方法
 *
 * 　　调用yield方法会让当前线程交出CPU权限，让CPU去执行其他的线程。它跟sleep方法类似，同样不会释放锁。
 * 但是yield不能控制具体的交出CPU的时间，另外，yield方法只能让拥有相同优先级的线程有获取CPU执行时间的机会。
 *
 * 　　注意，调用yield方法并不会让线程进入阻塞状态(就是说当前线程直接由运行（running）状态切换到 就绪（runnable）状态)，
 * 而是让线程重回就绪状态，它只需要等待重新获取CPU执行时间，这一点是和sleep方法不一样的
 * 可运行YieldRunnable 查看现象
 *
 * 5）join方法
 *
 * 　　join方法有三个重载版本：
 * join()
 * join(long millis)     //参数为毫秒
 * join(long millis,int nanoseconds)    //第一参数为毫秒，第二个参数为纳秒
 *  　　假如在main线程中，调用thread.join方法，则main方法会等待thread线程执行完毕或者等待一定的时间。
 *  如果调用的是无参join方法，则等待thread执行完毕，如果调用的是指定了时间参数的join方法，则等待一定的事件(实际上调用join方法是调用了Object的wait方法，这个可以通过查看源码得知：)
 *  可运行 JoinRunnable
 *  wait方法会让线程进入阻塞状态，并且会释放线程占有的锁，并交出CPU执行权限。
 *由于wait方法会让线程释放对象锁，所以join方法同样会让线程释放对一个对象持有的锁
 *
 *
 * 本文学 可参考 https://www.cnblogs.com/dolphin0520/p/3920357.html
 */
public class ThreadStudy {
    public static void main(String[] args) {
        Thread t=new Thread(new myThread());
        t.start();

        Thread.currentThread().interrupt();
        System.out.println(t.getName()+"1"+t.interrupted());
        System.out.println(t.getName()+"2"+t.interrupted());
        Thread.currentThread().interrupt();
        System.out.println(t.getName()+"3"+t.isInterrupted());
        System.out.println(t.getName()+"4"+t.isInterrupted());
       // sleep
        //runRunnable(new SleepThread(),"SleepRunnable");

        // YieldRunnable 线程
       // runRunnable(new YieldRunnable(),"YieldRunnable");


        // JoinRunnable
/*        for(int i=0;i<10;i++){
            if(i==4){
                Long g=System.currentTimeMillis();

                System.out.println("主函数等待一会");
                Thread sleepT=new Thread(new JoinRunnable());

                sleepT.start();
                try {
                    sleepT.join(1000,200);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("主函数等待一会"+(System.currentTimeMillis()-g)/1000);
            }
            System.out.println("main-"+i);


        }*/
        /*NotifyWaitUse notifyWaitUse=new NotifyWaitUse();
        Thread thread=new Thread(notifyWaitUse);
        thread.start();
        Thread thread2=new Thread(new NotifyWaitUse());
        thread2.setPriority(5);
        thread2.start();
        Thread thread3=new Thread(new NotifyWaitUse());
        thread3.start();
        Thread thread4=new Thread(new NotifyWaitUse());
        thread4.setPriority(5);
        thread4.start();
        Thread thread5=new Thread(new NotifyWaitUse());
        thread5.start();*/






    }
    private static void runRunnable(Runnable task,String threadName){
        for(int i=0;i<2;i++){
            Thread sleepT=new Thread(task);
            sleepT.setName(threadName+"-"+i);
            sleepT.start();
        }
    }
    public static class myThread implements Runnable{

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

        public void run() {
            for(int i=0;i<5000;i++){
                System.out.println("i=="+i);

                    Thread.currentThread().interrupt();


            }
        }
    }
    //sleep 线程表现
    public static class SleepThread implements Runnable{

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
        public void run() {
            synchronized (this){

                System.out.println(Thread.currentThread().getName()+"  开始运行 然后休息 1秒 进入阻塞 锁不释放，其它线程等待获取锁");
                try {
                    long startTime=System.currentTimeMillis()/1000;
                    TimeUnit.SECONDS.sleep(1);
                    //Thread.sleep(1000);
                    long endTime= System.currentTimeMillis()/1000;
                    System.out.println(Thread.currentThread().getName()+"  运行结束 运行耗时:"+(endTime-startTime)+"秒 线程即将释放锁，其它线程可获取锁");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    //yield 线程表现
    public static class YieldRunnable implements Runnable{

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
        public void run() {
            synchronized (this){

                System.out.println(Thread.currentThread().getName()+"  开始运行  进入就绪状态锁不释放，其它线程等待获取锁");

                    long startTime=System.currentTimeMillis()/1000;
                    yield();
                    //Thread.sleep(1000);
                    long endTime= System.currentTimeMillis()/1000;
                    System.out.println(Thread.currentThread().getName()+"  运行结束 运行耗时:"+(endTime-startTime)+"秒 线程即将释放锁，其它线程可获取锁");


            }
        }
    }
    public static class JoinRunnable implements Runnable{

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
            System.out.println("执行join线程 执行10秒");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public  static  class NotifyWaitUse implements Runnable{

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
            noty();

        }

        private     void noty(){
            synchronized(this){
                int i=0;
                while (true){
                    i++;
                    System.out.println("noty()"+i+Thread.currentThread().getName());
                    try {
                        wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }




                }
            }


        }
        private synchronized void waity(){
            System.out.println("waity()"+Thread.currentThread().getName());

                notify();

        }
    }
}
