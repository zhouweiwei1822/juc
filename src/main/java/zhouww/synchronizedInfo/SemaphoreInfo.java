package zhouww.synchronizedInfo;

import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;

public class SemaphoreInfo {
   static Exchanger<Integer> semaphore=new Exchanger();


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


                while (true) {
                 //   synchronized (WaitInfo.class) {
                        while (true) {
                            try {
                                semaphore.acquire();
                                System.out.println(77);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }finally {
                                semaphore.release();
                            }

                        }
                   // }





            }
        }
    }

    static class tasker2 implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    semaphore.acquire();
                    System.out.println(88);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();
                }

            }
        }
    }


        static class tasker3 implements Runnable {

            @Override
            public void run() {
                while (true) {
                    try {
                        semaphore.acquire();
                        System.out.println(55);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        semaphore.release();
                    }

                }
            }
        }
    }