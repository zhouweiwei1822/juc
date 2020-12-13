package zhouww.juc.lock.study;

import java.util.ArrayList;

/**
 * 此处研究 ThreadLocal的使用与原理
 * 1、从名字上我们可以理解为它为一个本地线程
 */
public class ThreadLocalStudy {
    private static final ThreadLocal<String> y=new ThreadLocal<String>();

    public static void main(String[] args) {
//        y.set("--kaisThreadLocal");
//        ThreadLocalRunnable threadLocalRunnable=new ThreadLocalRunnable(y);
//        for(long i=0;i<100000000;i++){
//            Thread t=new Thread(threadLocalRunnable);
//            t.setName("t-"+i);
//
//            System.out.println(y.get()+"=========");
//            t.start();
//        }

        ArrayList list=new ArrayList();

        while(true)

        {

            list.add(new ThreadLocalStudy());

        }
    }

    public static class ThreadLocalRunnable implements Runnable{
        private  final  ThreadLocal<String> threadLocal;
        ThreadLocalRunnable(ThreadLocal<String> threadLocal){
            this.threadLocal=threadLocal;

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
            threadLocal.set(Thread.currentThread().getName() + "-threadlocal-"+Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getName() + "set-threadlocal-" + threadLocal.get());
        }
        private synchronized void getThreadLocal(){
            System.out.println(Thread.currentThread().getName() + "get-threadlocal-" + threadLocal.get());
        }
    }
}
