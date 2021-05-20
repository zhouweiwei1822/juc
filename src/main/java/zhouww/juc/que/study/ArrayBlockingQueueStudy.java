package zhouww.juc.que.study;

import java.util.concurrent.*;

public class ArrayBlockingQueueStudy {
  static final ArrayBlockingQueue<String> strings = new ArrayBlockingQueue<String>(4);
    static  final ExecutorService service = Executors.newFixedThreadPool(5);
    static  final  LinkedBlockingQueue<Integer> f=new LinkedBlockingQueue<>(3);

    public static void main(String[] args) throws InterruptedException {
      final   getIm getIm = new getIm();
        //getIm.run();
     /*   strings.add("rr"); //
        strings.add("rr2"); //
        strings.add("rr3"); //
        System.out.println(strings.peek()); //
        System.out.println(strings.peek()); //
        System.out.println(strings.peek()); //*/
      //  LinkedBlockingQueue<Integer> f=new LinkedBlockingQueue<>(3);
        long l = System.currentTimeMillis();
        System.out.println("即将进入阻塞 10s "+l);
        f.poll(10,TimeUnit.SECONDS);
        System.out.println("阻塞结束耗时 "+(System.currentTimeMillis()-l));
       new Thread(new setIm()).start();
        new Thread(new getIm()).start();


        //  service.shutdown();
       //for (int i = 0; i < 10; i++) {

     /*       service.execute(new setIm());
      //  }
        // 数组集合同步队列
        Thread.sleep(1000);
        for (int i = 0; i < 10; i++) {

            service.execute(new getIm());
        }
        service.shutdown();*/
    }
  static class getIm implements Runnable{

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

          try {
              Thread.sleep(1000);
              System.out.println(f.take());
              Thread.sleep(1000);
              System.out.println(f.take());
              Thread.sleep(1000);
              System.out.println(f.take());
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
          /*try {
              ArrayBlockingQueueStudy.getOBj(strings);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }*/
      }
  }
 static class setIm implements Runnable{

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

         try {
             f.put(1);
             System.out.println("put==="+1);
             f.put(1);
             System.out.println("put==="+2);
             f.put(1);
             System.out.println("put==="+3);
             f.put(1);
             System.out.println("put==="+4);
             f.put(1);
             System.out.println("put==="+5);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
        /* try {
             ArrayBlockingQueueStudy.setOBj(strings);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }*/
     }
 }
    public  static void getOBj(ArrayBlockingQueue<String> strings) throws InterruptedException {
        // 直接返回 如果队列为空直接返回 null 仅获取对首信息
        System.out.println("==="+strings.peek());// 只获取takeIndex 位置的数据
        /*取走 BlockingQueue 里排在首位的对象,若 BlockingQueue 为空,阻断进入等待状
        态直到 BlockingQueue 有新的数据被加入*/
        System.out.println("==="+strings.take());
        /*取走 BlockingQueue 里排在首位的对象,若不能立即取出,则可以等 time 参数
        规定的时间,取不到时返回 null*/
        System.out.println("==="+strings.poll());

    }
    public static  void setOBj(ArrayBlockingQueue<String> strings) throws InterruptedException {
       /* 将指定元素插
        入此队列中，将等待可用的空间（如果有必要）*/
        strings.put("33");
/*
        将指定元素插入此队列中（如果立即可行
        且不会违反容量限制），成功时返回 true，如果当前没有可用的空间，则抛
        出 IllegalStateException。如果该元素是 NULL，则会抛出 NullPointerException 异常*/
        strings.add("rr"); //
        /*将指定元素插入此队列中（如果立即可行
        且不会违反容量限制），成功时返回 true，如果当前没有可用的空间，则返回 false。*/
        strings.offer("tt");

    }
}
