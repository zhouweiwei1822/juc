package zhouww.juc.lock.study;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * q:
 * 1、什么是threadLocal?
 * 2、threadlocal 在多线程中有啥作用
 * 3、在多线程中如何使用Threadlocal实现线程隔离的
 * 4、threadlocal 在使用过程中实现线程隔离的原理是什么
 * 5、threadlocal 使用过程中是否会出现 内存泄露
 * 6、threadlocal 一般使用场景有哪些
 * 7、你在哪些框架中看到 有使用过 它
 * 下面我们将带着以上问题 一一的去研究和学习 Threadlocal
 * as:
 * 我们先回答一下 什么是threadLocal: 从名字上来看 我们可能认为他是一个本地线程 ，其实呢它在线程内部存储一块信息副本
 * 这块信息副本就是我们使用线程想在线程中存储的信息，这块副本对于线程来说是线程隔离的; 这里所说的副本呢并不是Threadlocal实体 而是通过threadlocal
 * 创建 threadMap对象信息存储在当前线程中 这里仅仅是个人理解内容
 * 我们看下官方 解释：
 *  This class provides thread-local variables.  These variables differ from
 *  their normal counterparts in that each thread that accesses one (via its
 *  {@code get} or {@code set} method) has its own, independently initialized
 *  copy of the variable.  {@code ThreadLocal} instances are typically private
 *  static fields in classes that wish to associate state with a thread (e.g.,
 *  a user ID or Transaction ID) 翻译：这个类提供了线程本地变量，这些变量和其他的副本区别在于每个线程 都用他们自己的存取数据的方法（get或set）
 *  独立的初始化线程副本， ThreadLocal实例通常是类中的私有静态字段，它们希望将线程state关联起来
 *  2、下面我们讲下threadLocal 是如何将信息副本保存在 当前线程的；
 *  保存流程：1、首先在类中创建一个静态ThreadLocal 静态变量对象
 *            2、给对应线程创建 线程threadmap副本信息，通过 创建的静态ThreadLocal 类对象变量 调用Threadlocal中的 set方法进行给当前线程设置线程副本信息
 *            3、在获取当前副本信息内容时，通过get方法获取
 *  3、使用threadlocal 重点就set和get方法 下面我们就通个 源码来研究一下 threadLocal 使用和原理：
 *    我们先看下threadlocal 的set方法：
 *
*
*     public void set(T value){
 *      Thread t=Thread.currentThread();
 *      ThreadLocalMap map=getMap(t);
 *      if(map!=null)
 *     map.set(this,value);
 *     else
 *     createMap(t,value);
 *    }
 *我们可以看到源码中，首先获得当前线程 对象，然后通过getMap(Thread t)方法获取当前线程对象中副本 threadLocalMap对象
 *     ThreadLocalMap getMap(Thread t) {
 *         return t.threadLocals;
 *     }
 *   接着判断 当前线程中的threadlocalMap 是否已经存在
 *   如果已经存在 就调用  threadlocalMap的set进行数据更新 map.set(this,value);中的this是指当前threadlocal对象
 *
 *
 *      void createMap(Thread t, T firstValue) {
 *         t.threadLocals = new ThreadLocalMap(this, firstValue);
 *     }
 *
 *      ThreadLocalMap(ThreadLocal<?> firstKey, Object firstValue) {
 *             table = new Entry[INITIAL_CAPACITY];
 *             int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
 *             table[i] = new Entry(firstKey, firstValue);
 *             size = 1;
 *             setThreshold(INITIAL_CAPACITY);
 *         }
 *
 *
 *
 *
 *
 *
 *
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
//        for(long i=0;i<2;i++){
//            if(i<=0){
//                service.execute(threadLocalRunnable);
//                try {
//                    TimeUnit.SECONDS.sleep(30);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }else {
//                service.execute(threadLocalRunnable2);
//            }
//
//
//    }
        /*Thread t=new Thread(threadLocalRunnable);
        t.start();
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        Thread t2=new Thread(threadLocalRunnable2);
        t2.start();
     ///   y.set(new ThreadLocalStudy());


       // y.set(new ThreadLocalStudy());
        //service.shutdown();


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

             //   getThreadLocal();

        }
        private synchronized void setThreadLocal(){
            threadLocal.set(new ThreadLocalStudy());
          //  threadLocal2.set(2);
            //threadLocal.set(Thread.currentThread().getName() + "-threadlocal-"+Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getName() + "set-threadlocal-" + threadLocal.get());
        }
        private synchronized void getThreadLocal(){
           // threadLocal.remove();
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
           // getThreadLocal();
            threadLocal.set(new ThreadLocalStudy());
            ThreadLocal<ThreadLocalStudy> kk=new ThreadLocal<>();
            kk.set(new ThreadLocalStudy());

        }

        private synchronized void getThreadLocal(){
            //threadLocal.set(new ThreadLocalStudy());
            //System.out.println(Thread.currentThread().getName() + "get-threadlocal-" + threadLocal.get());
           // System.out.println(Thread.currentThread().getName() + "set-threadlocal-2-" + threadLocal.get());

        }
    }
}
