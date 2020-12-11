package zhouww.juc.lock.study;

import java.util.concurrent.TimeUnit;

/**
 * 线程基础内容
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
        @Override
        public void run() {
            for(int i=0;i<5000;i++){
                System.out.println("i=="+i);

                    Thread.currentThread().interrupt();


            }
        }
    }
}
