package zhouww.juc.lock.study;

import java.util.concurrent.TimeUnit;

/**
 *  本章 学习 synchronized 锁
 *  synchronized 可以修饰在 方法上、可以是一个同步代码块、可以修饰在静态方法上
 *  synchronized 方法上 学习：
 *  使用 synchronized 是必须保证 使用锁为同一把锁 例如 让当前的类对象作为锁，如果 不同线程创建了不同的 对象 此时在进行使用类的共享变量 就可能出现线程安全问题 举例 使用方法使用synchronized来演示
 *  见：MethodSynchronized类的演示
 *  MethodSynchronized 中setBlance() 在未添加synchronized 修饰时 运行 结果 会出现你各种结果 下面这个结果就是 不对的
 * thread---sum:1770000  ---正确的应该是 thread---sum:3001000
 * main---sum:3001000
 *
 * 下面再看下 方法添加了synchronized 修饰的 setBlanceSynchronized() 在多个线程 不使用同一个线程锁 也存在线程安全 下面就是没用使用相同对象锁出现的 多运行几次“使用不是同一个 对象锁”线程运行结果可能就会出现
 * 你意想不到的结果 如下面这个就是
 * thread---sum:2001000 ---正确的应该是 thread---sum:3001000
 * main---sum:3001000
 *
 *下面再看下 方法添加了synchronized 修饰的 setBlanceSynchronized() 在多个线程 使用同一个线程锁 就可解决线程安全 下面就是使用相同对象锁出现的 无论运行多少次线程运行结果 都是正确如下结果
 * thread---sum:3001000
 * main---sum:3001000
 *
 *  synchronized 静态方法上 学习： 对于静态方法来说 这个方法属于这个类 并不是单独归属 某个对象，被static 修饰的方法 在类加载是就进行了加载 了 并不是在创建对象时才进行加载的，接下来看下使用吧
 *  我们通过 MethodSynchronized类 中 setStaticBlanceSynchronized() 进行研究
 *  1、从 使用不同的对象 来观察运行现象 ，无论下面程序运行运行很多次 运行结果都是 下面正确结果
 *thread--staticsynchronized-diffObjectLock-sum:3001000
 * main--staticsynchronized-diffObjectLock-sum:3001000
 * thread--staticsynchronized-sameObjectLock---sum:3001000
 * main-staticsynchronized- sameObjectLock-sum:3001000
 * 从上面的运行结果 可以看出 被静态修饰 synchronized方法 持有的锁class，并不是 创建的类对象而是当前class ,也就是说无论多少线程访问当类中被static修饰的synchronized的方法都是线程独占的，都是同步的线程安全的
 *
 * synchronized 一个同步代码块上的学习：
 * 先研究一下 使用指定对象 作为对象锁来操作研究也从两方面 a、多个线程使用不同Runnable对象创建线程 b、多个线程使用相同的Runnable对象创建
 * MethodSynchronized类 中 setBlanceSynchronizedBlockObject() 进行研究 下面看下运行结果出现 不安全数据 不同对象导致不同锁产生了 线程安全问题
 * 这个和方法上 添加synchronized 结果是一样 的并且这两种方式 也是等价的
 * thread--synchronizedBlock-diffObjectLock-sum:6001000
 * main--synchronizedBlock-diffObjectLock-sum:3001000
 * main--synchronizedBlock-diffObjectLock-sum:出现线程 不安全了
 * thread--synchronizedBlock-sameObjectLock---sum:3001000
 * main-synchronizedBlock- sameObjectLock-sum:3001000
 * main-synchronizedBlock- sameObjectLock-sum:正确
 *
 *synchronized 一个同步代码块上使用this的的学习：
 * 通过 MethodSynchronized类 中 setBlanceSynchronizedBlockThis() 进行研究
 * 研究也从两方面 a、多个线程使用不同Runnable对象创建线程 b、多个线程使用相同的Runnable对象创建 看下运行结果 从运行结果来看 也是使用不同对象Runnable创建的线程也是不安全
 * 和上面的synchronized放在方法上、代码块对象锁都是等价的 ，要保证线程安全不行保证使用相同的对象锁
 *thread--synchronizedBlock-diffObjectLock-sum:6001000
 * main--synchronizedBlock-diffObjectLock-sum:3001000
 * main--synchronizedBlock-diffObjectLock-sum:出现线程 不安全了
 * thread--synchronizedBlock-sameObjectLock---sum:3001000
 * main-synchronizedBlock- sameObjectLock-sum:3001000
 * main-synchronizedBlock- sameObjectLock-sum:正确
 *
 *
 * synchronized 一个同步代码块上使用Object.class的的学习：
 * 通过 MethodSynchronized类 中 setBlanceSynchronizedBlockThis() 进行研究
 * 研究也从两方面 a、多个线程使用不同Runnable对象创建线程 b、多个线程使用相同的Runnable对象创建
 *
 *
 *
 *
 *
 *
 */
public  class SynchronizedStudy {
    private static int blance=1000;
    static MethodSynhronized methodSynhronizedObj=new MethodSynhronized();// 作为同步代码块中的对象锁
    public static void main(String[] args) {
        // 使用不是同一个 对象锁 非静修饰的方法
/*        Thread thread=new Thread(new MethodSynhronized());
        thread.start();
        Thread thread1=new Thread(new MethodSynhronized());
        thread1.start();
        Thread thread2=new Thread(new MethodSynhronized());
        thread2.start();
        try {
            thread1.join();
            thread.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("thread---sum:"+blance);
        blance=1000;
        for(int i=0;i<3000;i++)
            blance+=1000;
        System.out.println("main---sum:"+blance);



        // 使用同一个 对象锁 非静修饰的方法
        MethodSynhronized methodSynhronized=new MethodSynhronized();
        Thread thread4=new Thread(methodSynhronized);
        thread4.start();
        Thread thread5=new Thread(methodSynhronized);
        thread5.start();
        Thread thread6=new Thread(methodSynhronized);
        thread6.start();
        try {
            thread4.join();
            thread5.join();
            thread6.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("thread---sum:"+blance);
        System.out.println("thread--staticsynchronized-sameObjectLock---sum:"+blance);
        blance=1000;
        for(int i=0;i<3000;i++)
            blance+=1000;
        //System.out.println("main---sum:"+blance);
        System.out.println("main-staticsynchronized- sameObjectLock-sum:"+blance);*/



        // 使用不是同一个 对象锁 静修饰的方法
/*        Thread thread=new Thread(new MethodSynhronized());
        thread.start();
        Thread thread1=new Thread(new MethodSynhronized());
        thread1.start();
        Thread thread2=new Thread(new MethodSynhronized());
        thread2.start();
        try {
            thread1.join();
            thread.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("thread--staticsynchronized-diffObjectLock-sum:"+blance);
        blance=1000;
        for(int i=0;i<3000;i++)
            blance+=1000;
        System.out.println("main--staticsynchronized-diffObjectLock-sum:"+blance);


        // 使用同一个 对象锁  静修饰的方法
        blance=1000;// 这里设置这个值是为了演示 静态修饰synchronized方法 多个对象和通过对象结果是否相同
        MethodSynhronized methodSynhronized=new MethodSynhronized();
        Thread thread4=new Thread(methodSynhronized);
        thread4.start();
        Thread thread5=new Thread(methodSynhronized);
        thread5.start();
        Thread thread6=new Thread(methodSynhronized);
        thread6.start();
        try {
            thread4.join();
            thread5.join();
            thread6.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("thread--staticsynchronized-sameObjectLock---sum:"+blance);
        blance=1000;
        for(int i=0;i<3000;i++)
            blance+=1000;
        System.out.println("main-staticsynchronized- sameObjectLock-sum:"+blance);*/
while (true){
    System.out.println();
    System.out.println();
    System.out.println("===================================================================================================================");
    // 使用不是同一个 对象锁 代码同步块
    Thread thread=new Thread(new MethodSynhronized());
    thread.start();
    Thread thread1=new Thread(new MethodSynhronized());
    thread1.start();
    Thread thread2=new Thread(new MethodSynhronized());
    thread2.start();
    Thread thread7=new Thread(new MethodSynhronized());
    thread7.start();
    try {
        thread.join();
        thread1.join();
        thread2.join();
        thread7.join();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    int su=blance;
    System.out.println("thread--synchronizedBlock-diffObjectLock-sum:"+su);
    blance=1000;
    for(int i=0;i<4000;i++)
        blance+=1000;
    System.out.println("main--synchronizedBlock-diffObjectLock-sum:"+blance);
    System.out.println("main--synchronizedBlock-diffObjectLock-sum:"+(blance!=su?"出现线程 不安全了":"正确"));


    // 使用同一个 对象锁  代码同步块
/*    blance=1000;// 初始化进行对比
    MethodSynhronized methodSynhronized=new MethodSynhronized();
    Thread thread4=new Thread(methodSynhronizedObj);
    thread4.start();
    Thread thread5=new Thread(methodSynhronizedObj);
    thread5.start();
    Thread thread6=new Thread(methodSynhronizedObj);
    thread6.start();
    Thread thread8=new Thread(methodSynhronizedObj);
    thread8.start();
    try {
        thread4.join();
        thread5.join();
        thread6.join();
        thread8.join();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    int su1=blance;
    System.out.println("thread--synchronizedBlock-sameObjectLock---sum:"+su1);
    blance=1000;
    for(int i=0;i<4000;i++)
        blance+=1000;
    System.out.println("main-synchronizedBlock- sameObjectLock-sum:"+su1);
    System.out.println("main-synchronizedBlock- sameObjectLock-sum:"+(blance!=su1?"出现线程 不安全了":"正确"));
    */
    System.out.println("blance:"+blance);
    System.out.println("su:"+su);
    if(su!=blance){
        break;

    }
    blance=1000;
}

    }
    public static class MethodSynhronized implements Runnable{


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
           // setBlance();// 未添加synchronized 修饰的方法 演示 存在线程安全
           // setBlanceSynchronized(); //添加synchronized 修饰的方法 演示 存在两种情况 1使用同一个对象锁不同线程能够保证线程安全，2多个线程不使用同一个对象锁 线程不安全
           // setStaticBlanceSynchronized();//静态方法修饰的synchronized 修饰的方法 演示
            setBlanceSynchronizedBlockObject();// 同步代码块 使用对象最为 锁
            //setBlanceSynchronizedBlockThis();// 同步代码块 使用this作为锁
            //setBlanceSynchronizedBlockClass();;// 同步代码块 使用xx.class作为锁
        }
        private   void setBlance(){// 未添加synchronized 修饰的方法
            for(int i=0;i<1000;i++)
            blance+=1000;

        }
        private  synchronized void setBlanceSynchronized(){// 添加synchronized 修饰的方法
            for(int i=0;i<1000;i++)
                blance+=1000;

        }
        private static synchronized void setStaticBlanceSynchronized(){// 静态方法修饰synchronized 的方法
            for(int i=0;i<1000;i++)
                blance+=1000;

        }
        private  void setBlanceSynchronizedBlockObject() {// 使用同步块 对象锁
            synchronized (methodSynhronizedObj) {
                try {
                    TimeUnit.MICROSECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < 1000; i++)
                    blance += 1000;
            }
        }
            private  void setBlanceSynchronizedBlockClass() {// 使用同步块 class 锁
                synchronized (SynchronizedStudy.MethodSynhronized.class) {
                    for (int i = 0; i < 1000; i++)
                        blance += 1000;
                }
            }
        private  void setBlanceSynchronizedBlockThis() {// 使用同步块 This 锁
            synchronized (this) {
                for (int i = 0; i < 1000; i++)
                    blance += 1000;
            }
        }


        }

    }

