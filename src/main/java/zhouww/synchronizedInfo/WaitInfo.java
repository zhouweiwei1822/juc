package zhouww.synchronizedInfo;

import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;

/**
 * 场景 三个线程交替打印
 * 任务 1执行完 任务二执行 然后三 交替输入 9个数字
 * 同步实现
 */
public class WaitInfo {
   static int po=0;
   static  int flag=1;
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(new tasker1());
            thread.setName("task1-"+i);
            Thread thread1 = new Thread(new tasker2());
            thread1.setName("task2-"+i);
            Thread thread2 = new Thread(new tasker3());
            thread2.setName("task3-"+i);
            thread.start();
            thread1.start();
            thread2.start();

        }
        Semaphore g=new Semaphore(4);





    }
    /**
     *
     */
    static class tasker1 implements Runnable{


        @Override
        public void run() {
            synchronized (WaitInfo.class){

                while (true){
                    if(po>=9){
                        break;
                    }
                    if(flag==1){

                        po++;
                        System.out.println(Thread.currentThread().getName()+"-- 该任务二执行工作"+po);
                        flag=2;
                        WaitInfo.class.notifyAll();


                    }

                    try {
                        WaitInfo.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }



            }


        }
    }

    static class tasker2 implements Runnable{

        @Override
        public void run() {
            synchronized (WaitInfo.class){

                while (true){
                    if(po>=9){
                        WaitInfo.class.notifyAll();
                        break;
                    }
                    if(flag==2){

                        po++;
                        System.out.println(Thread.currentThread().getName()+"--该任务三执行工作"+po);
                        flag=3;
                        WaitInfo.class.notifyAll();


                    }

                    try {
                        WaitInfo.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
    static class tasker3 implements Runnable{

        @Override
        public void run() {
            synchronized (WaitInfo.class){
             while (true){
                 if(po>=9){
                     WaitInfo.class.notifyAll();
                     break;
                 }
                 if(flag==3){

                     po++;
                     System.out.println(Thread.currentThread().getName()+"--该任务一执行工作"+po);
                     flag=1;
                     WaitInfo.class.notifyAll();


                 }

                 try {
                     WaitInfo.class.wait();
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }

            }

        }
    }
}
