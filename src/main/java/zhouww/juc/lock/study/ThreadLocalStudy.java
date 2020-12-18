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
 *    接着我们继续看 createMap(Thread t, T firstValue)  方法 ，方法中将新创建的ThreadLocalMap 对象赋值值给了当前线程 threadlocals副本属性；
 *    ThreadLocalMap 类是 threadLocal中的一个内部静态类 如果当前线程 的副本threadLocals对象为空是 系统就会为当前线程创建一个ThreadLocalMap对象
 *      void createMap(Thread t, T firstValue) {
 *         t.threadLocals = new ThreadLocalMap(this, firstValue); // 这里的this 同样是是指当前对象 的threadLocal对象
 *     }
 *     下面是ThreadLocalMap(this, firstValue)构造函数，在构造函数，在下面的构造函数中有一个 数组Entry的对象，那这个是什么呢？这个数组是ThreadLocalMap中的一个
 *     静态类实体 并且是一个Threadlocal弱引用的类
 *
 *      ThreadLocalMap(ThreadLocal<?> firstKey, Object firstValue) {
 *             table = new Entry[INITIAL_CAPACITY]; // 创建一个Entry数组 用于存放值信息
 *             int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);// 计算存储位置的的下标
 *             table[i] = new Entry(firstKey, firstValue); // 创建一个Entry实体存放到 Entry数组中
 *             size = 1;// 记录数组大小
 *             setThreshold(INITIAL_CAPACITY);// 设置下次库容数组的加载因子
 *         }
 *
 *
 *         到这里我们就将 ThreadLocal 是如何将信息设置到当前线程中的流程看完了。（说明：对于线程来说只有共享资源才存在线程，对于局部变量和方法的调用如果不存在共享资源都是线程安全的）
 *         从上面的源码分析我们可以简单总结表达 下面我们来回答一下上面的问题吧：
 *         什么是threadLocal? -实际就是用来给每个线程创建特定 副本的类（官方文档都建议 将该类声明为类静态变量使用）
 *         在多线程中如何使用Threadlocal实现线程隔离的？--由于每个线程ThreadLocals 都是每个线程独有的，对其他线程是不可见的 所以他可以实现线程隔离
 *         threadlocal 使用过程中是否会出现 内存泄露？--从使用场景来说时可能会出现内存泄露，一般在使用线程池时 如果创建了一个很大的类 如果创建的线程池在某种业务场景下是不允许停止 这个时候线程副本会
 *         一直存在并且 弱引用的threadlocal 已经被gc回收 这个时候可能调用set，或get方法无法读取该内存信息，那么该部分对象就无法被gc进行回收，如果无法被回收的对象多了就会出现内存泄露的可能（一般普通的线程 是可以避免这种
 *         问题的，因为线程执行完成 对应的副本也会跟着线程结束 被内存进行回收的-- 不存在了强引用）
 *         那么如何避免Threadlocal的内存泄露呢？---为了避免出现这种内存泄露的风险 在业务中 应在合适的位置调用 threadLocal的remove（）方法进行操作
 *
 *
 *
 *         ThreadLocal特性
 * ThreadLocal和Synchronized都是为了解决多线程中相同变量的访问冲突问题，不同的点是
 *
 * Synchronized是通过线程等待，牺牲时间来解决访问冲突
 * ThreadLocal是通过每个线程单独一份存储空间，牺牲空间来解决冲突，并且相比于Synchronized，ThreadLocal具有线程隔离的效果，只有在线程内才能获取到对应的值，线程外则不能访问到想要的值。
 * 正因为ThreadLocal的线程隔离特性，使他的应用场景相对来说更为特殊一些。在android中Looper、ActivityThread以及AMS中都用到了ThreadLocal。当某些数据是以线程为作用域并且不同线程具有不同的数据副本的时候，就可以考虑采用ThreadLocal
 * 链接：https://www.jianshu.com/p/3c5d7f09dfbd
 *
 *  总结如下：
 *
 * 对于某一ThreadLocal来讲，他的索引值i是确定的，在不同线程之间访问时访问的是不同的table数组的同一位置即都为table[i]，只不过这个不同线程之间的table是独立的。
 * 对于同一线程的不同ThreadLocal来讲，这些ThreadLocal实例共享一个table数组，然后每个ThreadLocal实例在table中的索引i是不同的
 *
 *
 *
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
