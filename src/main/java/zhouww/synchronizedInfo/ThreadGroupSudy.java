package zhouww.synchronizedInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadGroupSudy {
    static AtomicInteger atomicInteger=new AtomicInteger(1);

    public static void main(String[] args) throws InterruptedException {
        //创建10个线程
        List<Thread> threads=new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new ThreadGroupRunnable());
            threads.add(thread)  ;
            thread.start();

        }
        while (true){
            Thread.sleep(300);
            for (int i = 0; i < threads.size(); i++) {
                System.out.println(threads.get(i).getName()+" 线程状态："+threads.get(i).getState());
            }
            ThreadGroup threadGroup=threads.get(1).getThreadGroup();
            if(threadGroup!=null && threadGroup.activeCount()>0){
                System.out.println("存活"+threadGroup.activeCount());
                threadGroup.interrupt();
            }
        }
    }



    static class ThreadGroupRunnable implements Runnable{

        @Override
        public void run() {
            if(atomicInteger.get()==1){
                atomicInteger.addAndGet(1);
                try {
                    System.out.println(Thread.currentThread().getName()+"要阻塞一秒");
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName()+"阻塞结束 将要根据线程组停用所有线程");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }else {
                try {

                    System.out.println(Thread.currentThread().getName()+"要阻塞");
                    Thread.sleep(100000);
                    System.out.println(Thread.currentThread().getName()+"要阻塞");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }










    /*static class CalledImpl implements Callable<String>{

        @Override
        public String call() throws Exception {
            Thread.sleep();
            return null;
        }
    }*/
}
