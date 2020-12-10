import java.util.HashSet;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 简单的 线程池的实现 和 ReentranLock 的简单实用
 *
 * 实现 线程池的流程（简单点说下基本思想：通过线程池创建出有限的线程 通过线程池创建的线程去执行 我们业务线程Runable的run方法-- 实际上就是run方法的串行执行）
 * 1、如何理解串行执行呢？ 就是线程池创建的线程进行启动，在线程池创建的线程的run 方法中去调用我们 实现的Runable中 run方法来执行时间的业务
 * 2、线程池中的线程是如何实现复用的呢？ 基本思想就是通过循环调用我们 实现的Runable中 run方法;在线程池的实现内部定义了 一个阻塞队列 用于存放我们定义的业务对象的Runable ，
 * 另外定义了一个集合 用于存放 线程池实现Runable对象在线程池创建的线程在创建时就调用start()方法启动的线程，线程池中实现的Runable中run方法使用了循环获取阻塞队列中的业务对象run方法，
 * 直到任务被全部从队列中取出并且执行完毕 （jdk中的线程池实现功能比较严谨）线程池的实现的基本实现就是这样 实际上也算的上是一种“生产者和消费者的一种模式”
 * 消费者就是线程池创建的 线程集合，生产者我们可以认为使我们业务创建的任务对象，其中队列就像中间的消息中心一样
 *
 *
 */
public class ReentrantLockClassThreadPool {
    static  volatile int nmb=0;
    static  final HashSet<ThreadRunable1> workers = new HashSet<ThreadRunable1>();// worker-任务执行对象集合 （线程池创建Runable对象）
    static  final LinkedBlockingDeque<ThreadRunable> ls=new LinkedBlockingDeque<ThreadRunable>(); //  task-任务对象 实际要执行的业务对象实体（业务创建的Runable对象）
    static ReentrantLock lock=new ReentrantLock();
    public static void main(String[] args) {
        ExecutorService service= Executors.newFixedThreadPool(10);

        // 创建线程池的任务 添加到队列中
        for(int i=0;i<100;i++) { // 多线程共享一个对象 在未使用同步锁时 易出现数据错误
            ThreadRunable tt=new ThreadRunable(lock);
            ls.add(tt);
           //service.execute(tt);
        }
        for(int k=0;k<2;k++){// 给线程池 创建线程 并且添加到 集合中
            ThreadRunable1 yy=new ThreadRunable1(null);
            Thread ts = new Thread(yy);
            ts.setName("myThreadPool-Thread-"+k);
            ts.start();
            workers.add(yy);
        }
        service.shutdown();
       int nmb1=0;
        for(int i=0;i<10;i++){
            for(int j=0;j<5;j++) {
                nmb1++;

            }
        }

        System.out.println("System.out.println(nmb1);"+nmb1);


    }




    public static class ThreadRunable implements Runnable{

      //  private int nmb=0;
        private final ReentrantLock lock;
        String str ="zhangsan";
        ThreadRunable(ReentrantLock lock){
           // this.nmb=nmb;
            this.lock=lock;

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

            lock.lock();
            System.out.println(Thread.currentThread().getName()+"    当前还有线程");
            for(int i=0;i<str.length();i++){
               // lock.lock();
                System.out.print(str.charAt(i));
               // lock.unlock();

            }

            System.out.println();
            for(int i=0;i<5;i++){

                nmb++;
                System.out.println(Thread.currentThread().getName()+"  nmb="+nmb);


            }
            lock.unlock();
        }

    }

    // 真正多线程 实现操作功能
    public static class ThreadRunable1 extends Thread{
        private volatile boolean flag=true;
        private   Runnable task;
        private final  Thread work;
        ThreadRunable1(){
            this.work=new Thread(this);
        }
        ThreadRunable1(Runnable task){
            this.task=task;
            this.work=new Thread(this);

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

                ru(this);

        }
        // 创建的实际线程去 执行相关实际任务
        private  void ru(ThreadRunable1 gg){
            Runnable task=gg.task;
            gg.task=null;

            gg.yield();// 此处放弃线程 获取CPU执行权限， 让线程都有机会获取 执行的机会

            while(task!=null || (task=getTask())!=null){

                task.run();
                task=null;

            }

        }
        // 从任务中获取 要执行的任务
        private  Runnable getTask() {

            Runnable r = null;
               if(ls.size()>0){
                   try {

                       r = ls.take();
                       ls.remove( r);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }


            if(r!=null){
                return r;
            }
            return null;



    }
}
}
