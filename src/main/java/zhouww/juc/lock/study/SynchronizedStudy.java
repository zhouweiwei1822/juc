package zhouww.juc.lock.study;

import java.util.concurrent.TimeUnit;

/**
 *  本章 学习 synchronized 锁
 *  synchronized 可以修饰在 方法上、可以是一个同步代码块、可以修饰在静态方法上
 *  synchronized 方法上 学习：
 *  使用 synchronized 是必须保证 使用锁为同一把锁 例如 让当前的类对象作为锁，如果 不同线程创建了不同的 对象 此时在进行使用类的共享变量 就可能出现线程安全问题 举例 使用方法使用synchronized来演示
 *  见：MethodSynchronized类的演示
 *  MethodSynchronized 中setBlance() 和中getBlance()在未添加synchronized 修饰时 运行 结果 会出现你各种结果 下面这个结果就是 不对的
 Thread-1setBlance:2001000
 Thread-0setBlance:2001000
 Thread-0getBlance:2001000
 Thread-1getBlance:2001000
 Thread-2setBlance:3406000
 Thread-3setBlance:3805000
 Thread-2getBlance:3805000
 Thread-3getBlance:3805000
 * 下面再看下 方法添加了synchronized 修饰的 setBlanceSynchronized()和 getBlanceSynchronized() 在多个线程 不使用同一个线程锁 也存在线程安全 下面就是没用使用相同对象锁出现的 多运行几次“使用不是同一个 对象锁”线程运行结果可能就会出现
 * 你意想不到的结果 如下面这个就是
 Thread-0setBlanceSynchronized:1001000
 Thread-0getBlanceSynchronized:1001000
 Thread-2setBlanceSynchronized:2292000
 Thread-1setBlanceSynchronized:2984000
 Thread-2getBlanceSynchronized:2984000
 Thread-1getBlanceSynchronized:2984000
 Thread-3setBlanceSynchronized:3984000
 Thread-3getBlanceSynchronized:3984000
 *
 *下面再看下 方法添加了synchronized 修饰的 setBlanceSynchronized()和 getBlanceSynchronized() 在多个线程 使用同一个线程锁 就可解决线程安全 下面就是使用相同对象锁出现的 无论运行多少次线程运行结果 都是正确如下结果
 Thread-0setBlanceSynchronized:1001000
 Thread-0getBlanceSynchronized:1001000
 Thread-1setBlanceSynchronized:2001000
 Thread-1getBlanceSynchronized:2001000
 Thread-2setBlanceSynchronized:3001000
 Thread-2getBlanceSynchronized:3001000
 Thread-3setBlanceSynchronized:4001000
 Thread-3getBlanceSynchronized:4001000
 *
 *  synchronized 静态方法上 学习： 对于静态方法来说 这个方法属于这个类 并不是单独归属 某个对象，被static 修饰的方法 在类加载是就进行了加载 了 并不是在创建对象时才进行加载的，接下来看下使用吧
 *  我们通过 MethodSynchronized类 中 setStaticBlanceSynchronized()和 getStaticBlanceSynchronized() 进行研究
 *  1、从 使用不同的对象 来观察运行现象 ，无论下面程序运行运行很多次 运行结果都是 下面正确结果
 Thread-0setStaticBlanceSynchronized:1001000
 Thread-0getStaticBlanceSynchronized:1001000
 Thread-1setStaticBlanceSynchronized:2001000
 Thread-1getStaticBlanceSynchronized:2001000
 Thread-2setStaticBlanceSynchronized:3001000
 Thread-2getStaticBlanceSynchronized:3001000
 Thread-3setStaticBlanceSynchronized:4001000
 Thread-3getStaticBlanceSynchronized:4001000 *
 从上面的运行结果 可以看出 被静态修饰 synchronized方法 持有的锁class，并不是 创建的类对象而是当前class ,也就是说无论多少线程访问当类中被static修饰的synchronized的方法都是线程独占的，都是同步的线程安全的
 *
 * synchronized 一个同步代码块上的学习：
 * 先研究一下 使用指定对象 作为对象锁来操作研究也从两方面 a、多个线程使用不同Runnable对象创建线程 b、多个线程使用相同的Runnable对象创建
 * MethodSynchronized类 中 setBlanceSynchronizedBlockObject()和 getBlanceSynchronizedBlockObject()  进行研究
 *
 * 下面看下运行结果出现 不安全数据 不同对象导致不同锁产生了 线程安全问题
 *
 *  运行代码 ：
 *  Thread thread=new Thread(new MethodSynhronized(new MethodSynhronized()));
 *         thread.start();
 *         Thread thread1=new Thread(new MethodSynhronized(new MethodSynhronized()));
 *         thread1.start();
 *         Thread thread2=new Thread(new MethodSynhronized(new MethodSynhronized()));
 *         thread2.start();
 *         Thread thread7=new Thread(new MethodSynhronized(new MethodSynhronized()));
 *         thread7.start();
 *         运行结果：
 Thread-0getStaticBlanceSynchronized:1001000
 Thread-1setStaticBlanceSynchronized:2001000
 Thread-1getStaticBlanceSynchronized:2001000
 Thread-2setStaticBlanceSynchronized:3001000
 Thread-2getStaticBlanceSynchronized:3001000
 Thread-3setStaticBlanceSynchronized:4001000
 Thread-3getStaticBlanceSynchronized:4001000
 * 看下多个线程使用相同的Runnable对象创建
 * 启动线程代码：
 *  Object methodSynhronizedObj=new Object();// 作为同步代码块中的对象锁（锁为自定义的对象锁）
 *     // 使用同一个 对象锁  代码同步块
 *
 *     MethodSynhronized methodSynhronized=new MethodSynhronized(methodSynhronizedObj);
 *     Thread thread4=new Thread(new MethodSynhronized(methodSynhronizedObj));
 *     thread4.start();
 *     Thread thread5=new Thread(new MethodSynhronized(methodSynhronizedObj));
 *     thread5.start();
 *     Thread thread6=new Thread(new MethodSynhronized(methodSynhronizedObj));
 *     thread6.start();
 *     Thread thread8=new Thread(new MethodSynhronized(methodSynhronizedObj));
 *     thread8.start();
 *     运行结果
 *Thread-0setStaticBlanceSynchronized:1001000
 * Thread-0getStaticBlanceSynchronized:1001000
 * Thread-1setStaticBlanceSynchronized:2001000
 * Thread-1getStaticBlanceSynchronized:2001000
 * Thread-2setStaticBlanceSynchronized:3001000
 * Thread-2getStaticBlanceSynchronized:3001000
 * Thread-3setStaticBlanceSynchronized:4001000
 * Thread-3getStaticBlanceSynchronized:4001000
 *synchronized 一个同步代码块上使用this的的学习：
 * 通过 MethodSynchronized类 中 setBlanceSynchronizedBlockThis() 和getBlanceSynchronizedBlockThis() 进行研究
 * 研究也从两方面 a、多个线程使用不同Runnable对象创建线程
 * 运行代码：
 *        Thread thread=new Thread(new MethodSynhronized());
 *         thread.start();
 *         Thread thread1=new Thread(new MethodSynhronized());
 *         thread1.start();
 *         Thread thread2=new Thread(new MethodSynhronized());
 *         thread2.start();
 *         Thread thread7=new Thread(new MethodSynhronized());
 *         thread7.start();
 * 运行结果 看下运行结果 从运行结果来看 也是使用不同对象Runnable创建的线程也是不安全
 * Thread-1setBlanceSynchronizedBlockThis:2001000
 * Thread-2setBlanceSynchronizedBlockThis:3001000
 * Thread-0setBlanceSynchronizedBlockThis:1909000
 * Thread-2getBlanceSynchronizedBlockThis:3001000
 * Thread-1getBlanceSynchronizedBlockThis:3001000
 * Thread-0getBlanceSynchronizedBlockThis:3001000
 * Thread-3setBlanceSynchronizedBlockThis:4001000
 * Thread-3getBlanceSynchronizedBlockThis:4001000
 *
 *
 * b、多个线程使用相同的Runnable对象创建
 * 和上面的synchronized放在方法上、代码块对象锁都是等价的 ，要保证线程安全不行保证使用相同的对象锁（锁为当前类的对象和方法上的synchronized等等价）
 *运行结果：
 *  MethodSynhronized methodSynhronized1=new MethodSynhronized(methodSynhronizedObj);
 *     Thread thread4=new Thread(methodSynhronized1);
 *     thread4.start();
 *     Thread thread5=new Thread(methodSynhronized1);
 *     thread5.start();
 *     Thread thread6=new Thread(methodSynhronized1);
 *     thread6.start();
 *     Thread thread8=new Thread(methodSynhronized1);
 *     thread8.start();
 *运行结果：
 * Thread-0setBlanceSynchronizedBlockThis:1001000
 * Thread-0getBlanceSynchronizedBlockThis:1001000
 * Thread-2setBlanceSynchronizedBlockThis:2001000
 * Thread-2getBlanceSynchronizedBlockThis:2001000
 * Thread-1setBlanceSynchronizedBlockThis:3001000
 * Thread-1getBlanceSynchronizedBlockThis:3001000
 * Thread-3setBlanceSynchronizedBlockThis:4001000
 * Thread-3getBlanceSynchronizedBlockThis:4001000
 *
 * synchronized 一个同步代码块上使用Object.class的的学习：
 * 通过 MethodSynchronized类 中 setBlanceSynchronizedBlockClass() 和 getBlanceSynchronizedBlockClass() 进行研究
 * 研究也从两方面 a、多个线程使用不同Runnable对象创建线程
 * 代码：
 *
 Thread thread=new Thread(new MethodSynhronized());
 thread.start();
 Thread thread1=new Thread(new MethodSynhronized());
 thread1.start();
 Thread thread2=new Thread(new MethodSynhronized());
 thread2.start();
 Thread thread7=new Thread(new MethodSynhronized());
 thread7.start();
 运行结果： 无论运行多次都是一样的（从结果可以看出这里的锁是针对class的并不是针对 类对象的 这个和静态synchronized等价的）
 Thread-0setBlanceSynchronizedBlockClass:1001000
 Thread-0getBlanceSynchronizedBlockClass:1001000
 Thread-1setBlanceSynchronizedBlockClass:2001000
 Thread-1getBlanceSynchronizedBlockClass:2001000
 Thread-2setBlanceSynchronizedBlockClass:3001000
 Thread-2getBlanceSynchronizedBlockClass:3001000
 Thread-3setBlanceSynchronizedBlockClass:4001000
 Thread-3getBlanceSynchronizedBlockClass:4001000

 * * b、多个线程使用相同的Runnable对象创建
 *  * 代码：
 * MethodSynhronized methodSynhronized1=new MethodSynhronized(methodSynhronizedObj);
 *     Thread thread4=new Thread(methodSynhronized1);
 *     thread4.start();
 *     Thread thread5=new Thread(methodSynhronized1);
 *     thread5.start();
 *     Thread thread6=new Thread(methodSynhronized1);
 *     thread6.start();
 *     Thread thread8=new Thread(methodSynhronized1);
 *     thread8.start();
 *
 *运行结果：
 * Thread-0setBlanceSynchronizedBlockClass:1001000
 * Thread-0getBlanceSynchronizedBlockClass:1001000
 * Thread-2setBlanceSynchronizedBlockClass:2001000
 * Thread-2getBlanceSynchronizedBlockClass:2001000
 * Thread-1setBlanceSynchronizedBlockClass:3001000
 * Thread-1getBlanceSynchronizedBlockClass:3001000
 * Thread-3setBlanceSynchronizedBlockClass:4001000
 * Thread-3getBlanceSynchronizedBlockClass:4001000
 *
 *
 */
public  class SynchronizedStudy {


    public static void main(String[] args) {



//        Thread thread=new Thread(new MethodSynhronized());
//        thread.start();
//        Thread thread1=new Thread(new MethodSynhronized());
//        thread1.start();
//        Thread thread2=new Thread(new MethodSynhronized());
//        thread2.start();
//        Thread thread7=new Thread(new MethodSynhronized());
//        thread7.start();


        Object methodSynhronizedObj=new Object();// 作为同步代码块中的对象锁
    // 使用同一个 对象锁  代码同步块

    MethodSynhronized methodSynhronized=new MethodSynhronized(methodSynhronizedObj);

        MethodSynhronized methodSynhronized1=new MethodSynhronized(methodSynhronizedObj);
    Thread thread4=new Thread(methodSynhronized1);
    thread4.start();
    Thread thread5=new Thread(methodSynhronized1);
    thread5.start();
    Thread thread6=new Thread(methodSynhronized1);
    thread6.start();
    Thread thread8=new Thread(methodSynhronized1);
    thread8.start();


    }
    public static class MethodSynhronized implements Runnable{
        static String yy="6666666";
        private static int blance=1000;
        private  Object methodSynhronized;
        MethodSynhronized (Object methodSynhronized){
            this.methodSynhronized=methodSynhronized;

        }
        MethodSynhronized(){}


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
            // 未添加synchronized 修饰的方法 演示 存在线程安全
            //setBlance();

            //添加synchronized 修饰的方法 演示 存在两种情况 1使用同一个对象锁不同线程能够保证线程安全，2多个线程不使用同一个对象锁 线程不安全
           // setBlanceSynchronized();

            // setStaticBlanceSynchronized();//静态方法修饰的synchronized 修饰的方法 演示
           //setBlanceSynchronizedBlockObject();// 同步代码块 使用对象最为 锁
            //setBlanceSynchronizedBlockThis();// 同步代码块 使用this作为锁
            setBlanceSynchronizedBlockClass();;// 同步代码块 使用xx.class作为锁
        }
        private   void setBlance(){// 未添加synchronized 修饰的方法
            for(int i=0;i<1000;i++)
            blance+=1000;
            System.out.println(Thread.currentThread().getName()+"setBlance:"+blance);
            getBlance();

        }
        private   int getBlance(){// 未添加synchronized 修饰的方法
            System.out.println(Thread.currentThread().getName()+"getBlance:"+blance);
            return blance;
        }

        private  synchronized void setBlanceSynchronized(){// 添加synchronized 修饰的方法
            for(int i=0;i<1000;i++)
                blance+=1000;
            System.out.println(Thread.currentThread().getName()+"setBlanceSynchronized:"+blance);
            getBlanceSynchronized();


        }
        private  synchronized int getBlanceSynchronized(){// 添加synchronized 修饰的方法
            System.out.println(Thread.currentThread().getName()+"getBlanceSynchronized:"+blance);
            return blance;
        }
        private static synchronized void setStaticBlanceSynchronized(){// 静态方法修饰synchronized 的方法
            for(int i=0;i<1000;i++)
                blance+=1000;
            System.out.println(Thread.currentThread().getName()+"setStaticBlanceSynchronized:"+blance);
            getStaticBlanceSynchronized();

        }
        private static synchronized int getStaticBlanceSynchronized(){// 静态方法修饰synchronized 的方法
            System.out.println(Thread.currentThread().getName()+"getStaticBlanceSynchronized:"+blance);
            return blance;

        }
        private  void setBlanceSynchronizedBlockObject() {// 使用同步块 对象锁
            synchronized (methodSynhronized) {
                try {
                    TimeUnit.MICROSECONDS.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < 1000; i++)
                    blance += 1000;
                System.out.println(Thread.currentThread().getName()+"setBlanceSynchronizedBlockObject:"+blance);
                getBlanceSynchronizedBlockObject();
            }
        }

        private  int getBlanceSynchronizedBlockObject() {// 使用同步块 对象锁
            synchronized (methodSynhronized) {
                System.out.println(Thread.currentThread().getName()+"getBlanceSynchronizedBlockObject:"+blance);
                return blance;
            }
        }
            private  void setBlanceSynchronizedBlockClass() {// 使用同步块 class 锁
                synchronized (MethodSynhronized.class) {
                    for (int i = 0; i < 1000; i++)
                        blance += 1000;
                    System.out.println(Thread.currentThread().getName()+"setBlanceSynchronizedBlockClass:"+blance);
                    getBlanceSynchronizedBlockClass();
                }
            }
        private  int getBlanceSynchronizedBlockClass() {// 使用同步块 class 锁
            synchronized (MethodSynhronized.class) {
                System.out.println(Thread.currentThread().getName()+"getBlanceSynchronizedBlockClass:"+blance);
                return blance;
            }
        }
        private  void setBlanceSynchronizedBlockThis() {// 使用同步块 This 锁
            synchronized (this) {
                for (int i = 0; i < 1000; i++)
                    blance += 1000;
                System.out.println(Thread.currentThread().getName()+"setBlanceSynchronizedBlockThis:"+blance);
                getBlanceSynchronizedBlockThis();
            }
        }
        private  int getBlanceSynchronizedBlockThis() {// 使用同步块 This 锁
            synchronized (this) {
                System.out.println(Thread.currentThread().getName()+"getBlanceSynchronizedBlockThis:"+blance);
                return blance;
            }
        }


        }

    }

