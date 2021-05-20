package zhouww.synchronizedInfo;

import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;

public class SemaphoreInfo {
   static Semaphore semaphore=new Semaphore(1,true);


    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(new SemaphoreInfo.tasker1());
            thread.setName("task1-"+i);
            Thread thread1 = new Thread(new SemaphoreInfo.tasker2());
            thread1.setName("task2-"+i);
            Thread thread2 = new Thread(new SemaphoreInfo.tasker3());
            thread2.setName("task3-"+i);
            thread.start();
            thread1.start();
            thread2.start();

        }
    }


    /**
     *
     */
    static class tasker1 implements Runnable {


        @Override
        public void run() {


              //  while (true) {
                 //   synchronized (WaitInfo.class) {
                      //  while (true) {
                            try {
                                System.out.println(Thread.currentThread().getName()+"将要争取打印资源。。。。。");
                                semaphore.acquire();
                                System.out.println(Thread.currentThread().getName()+"获得了打印机  正在打印。。。。。");
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }finally {
                                System.out.println(Thread.currentThread().getName()+"打印完成 即将放弃打印机资源");
                                semaphore.release();

                         //   }

                     //   }
                   // }





            }
        }
    }

    static class tasker2 implements Runnable {

        @Override
        public void run() {
        //    while (true) {
                try {
                    System.out.println(Thread.currentThread().getName()+"将要争取打印资源。。。。。");
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+"获得了打印机  正在打印。。。。。");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println(Thread.currentThread().getName()+"打印完成 即将放弃打印机资源");
                    semaphore.release();
                    System.out.println(Thread.currentThread().getName()+"正在打印完成");
                }

          //  }
        }
    }


        static class tasker3 implements Runnable {

            @Override
            public void run() {
              //  while (true) {
                    try {
                        System.out.println(Thread.currentThread().getName()+"将要争取打印资源。。。。。");
                        semaphore.acquire();
                        System.out.println(Thread.currentThread().getName()+"获得了打印机  正在打印。。。。。");
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        System.out.println(Thread.currentThread().getName()+"打印完成 即将放弃打印机资源");
                        semaphore.release();
                    }

               // }
            }
        }
    }