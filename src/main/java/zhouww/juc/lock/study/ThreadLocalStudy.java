package zhouww.juc.lock.study;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 局部变量和局部方法 不存在线程安全，线程安全是针对 共享资源来说的
 * 此处研究 ThreadLocal的使用与原理
 *
 * 1、从名字上我们可以理解为它为一个本地线程
 * 2、Threadlocal 在一些比较优秀的框架中都有使用 ，在平时业务处理中很少能用到
 *
 * Q：a-他是如何实现 在系统任何地方获得 当前线程信息的?
 *    b-使用它的场景在哪呢？----(1)保存线程上下文信息，在任意需要的地方可以获取！！！
 *                              (2)线程安全的，避免某些情况需要考虑线程安全必须同步带来的性能损失！！
 *    c-ThreadLocal使用一些细节
 *    带着对应问题我们来研究一下 源码：
 *    首先看下set方法：
 *    看下 该方法的上面的解释：设置当前线程ThreadLocal 变量 通过拷贝赋值，对于所有的子类都不需要重写 该方法，可以依赖{@link #initialValue}方法设置线程局部变量的值
 *    set 给线程设置 数据的流程-1、首先获取 当前线程对象，2、获取当前线程的threadLocals信息 如果获取到 就进行数据的更新（ThreadLocalMap），
 *    如果获取不到就进行创建ThreadLocalMap并且将新创建的对象赋值给当前线程对象threadLocals（注意创建的这个 ThreadLocalMap是一个新的对象 属于当前线程的内部副本，对其他线程是不可见的
 *    ，由于在每个线程内部都有一个ThreadLocalMap副本，所以线程在任何地方都可以通过当前线程副本信息获取想要信息，保证了线程间的安全）
 *
 *    注意：一个ThreadLocal只能存储一个Object对象，如果需要存储多个Object对象那么就需要多个ThreadLocal！！！
 *
 *    参考文章： http://www.jiangxinlingdu.com/interview/2019/06/19/threadlocal.html
 *
 */
public class ThreadLocalStudy {
    private static final ThreadLocal<ThreadLocalStudy> y=new ThreadLocal<ThreadLocalStudy>();
    //private static final ThreadLocal<Integer> y2=new ThreadLocal<Integer>();

    public static void main(String[] args) {
        ThreadLocalRunnable threadLocalRunnable=new ThreadLocalRunnable(y);
        ThreadLocalRunnable2 threadLocalRunnable2=new ThreadLocalRunnable2(y);
        ExecutorService service=Executors.newFixedThreadPool(2);
        for(long i=0;i<10;i++){
            if(i<=5){
                service.execute(threadLocalRunnable);
            }else {
                service.execute(threadLocalRunnable2);
            }

    }
        service.shutdown();


    }

    public static class ThreadLocalRunnable implements Runnable{
        private  final  ThreadLocal<ThreadLocalStudy> threadLocal;
        //private  final  ThreadLocal<Integer> threadLocal2;
        ThreadLocalRunnable(ThreadLocal<ThreadLocalStudy> threadLocal/*,ThreadLocal<Integer> threadLocal2*/){
            this.threadLocal=threadLocal;
           // this.threadLocal2=threadLocal2;

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

                setThreadLocal();

                getThreadLocal();

        }
        private synchronized void setThreadLocal(){
            threadLocal.set(new ThreadLocalStudy());
          //  threadLocal2.set(2);
            //threadLocal.set(Thread.currentThread().getName() + "-threadlocal-"+Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getName() + "set-threadlocal-" + threadLocal.get());
        }
        private synchronized void getThreadLocal(){
            threadLocal.remove();
           // System.out.println(Thread.currentThread().getName() + "get-threadlocal-" + threadLocal.get());
           // System.out.println(Thread.currentThread().getName() + "get-threadlocal2-" + threadLocal2.get());
        }
    }


    public static class ThreadLocalRunnable2 implements Runnable{
        private  final  ThreadLocal<ThreadLocalStudy> threadLocal;
        //private  final  ThreadLocal<Integer> threadLocal2;
        ThreadLocalRunnable2(ThreadLocal<ThreadLocalStudy> threadLocal/*,ThreadLocal<Integer> threadLocal2*/){
            this.threadLocal=threadLocal;
            // this.threadLocal2=threadLocal2;

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
          //  setThreadLocal();
            getThreadLocal();


        }

        private synchronized void getThreadLocal(){
            System.out.println(Thread.currentThread().getName() + "get-threadlocal-" + threadLocal.get());

        }
    }
}
