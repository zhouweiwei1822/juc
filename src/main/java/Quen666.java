

import org.openjdk.jol.info.ClassLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Quen666 {
    public static void main(String[] args) throws InterruptedException {



        LinkedBlockingQueue<Integer>  ip=new LinkedBlockingQueue<>();
        ip.put(1);

        Map<String,Thread> jk=new HashMap<>();
        jk.put("fist",new Thread());
        jk.put("two",new Thread());
        Thread k=jk.get("two");
       /* System.out.println(-1 << 29);
        System.out.println(0 << 29);
        System.out.println(1 << 29);
        System.out.println(2 << 29);
        System.out.println(3 << 29);*/

        System.out.println(-1 << 29|0);




        //ExecutorService executorService=Executors.newCachedThreadPool();

        Runnable w   =  new Runnable() {
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
                //synchronized(this){

                System.out.println(System.currentTimeMillis());
                    System.out.println(Thread.currentThread().getName()+"===============ppppppp");
                String  str="zhangsankk";
               System.out.println(ClassLayout.parseInstance(str.getClass()).toPrintable());
                    for (int i=0;i<str.length();i++)
                        System.out.print(str.substring(i,i+1));

            }
            // }

        };
        /*executorService.execute(w);
        executorService.shutdown();*/
   //   BlockingQueue<Runnable>  work= new SynchronousQueue<Runnable>();

    /*    new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    work.take().run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
        /*work.put(w);*/

       /* ExecutorService executorService1= new ThreadPoolExecutor(2, Integer.MAX_VALUE,
                60000L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
        executorService1.execute(w);
Thread.sleep(30000);
       executorService1.execute(w);*/
        ThreadPoolExecutor po= new ThreadPoolExecutor(4, Integer.MAX_VALUE,
                9000000L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
        po.allowCoreThreadTimeOut(true);
       // for(int i=0;i<5;i++){

        po .execute(w);
       System.out.println("总线程数："+po.getPoolSize()+"当前活跃线程数："+po.getActiveCount());
        //po .execute(w);


           po .execute(w);
        po .execute(w);

        po .execute(w);

        Thread.sleep(9000);
        po .execute(w);
      // }
      //  po.shutdown();

       /* po .execute(w);
        Thread.sleep(3000);
        po .execute(w);*/
        //ReentrantLock reentrantLock=new ReentrantLock();
       // Condition condition=reentrantLock.newCondition();

           // new Thread(w).start();





    }
}
