package zhouww.synchronizedInfo;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchStudy {
    static  CountDownLatch countDownLatch=new CountDownLatch(4);

    public static void main(String[] args) {
        countDownLatch.countDown();
    }
}
