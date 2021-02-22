package zhouww.juc.lock.study;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchStudy {
   static final CountDownLatch latch=new CountDownLatch(2);


    public static void main(String[] args) throws InterruptedException {
        latch.await();
        latch.countDown();




    }
}
