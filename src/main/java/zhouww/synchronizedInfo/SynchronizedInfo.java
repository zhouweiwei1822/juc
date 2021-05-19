package zhouww.synchronizedInfo;

public class SynchronizedInfo {
    private static int shareData=0;
    public static synchronized void increate(){
        shareData++;
        System.out.println(Thread.currentThread().getName()+" "+shareData);
    }
    public void increate1(){
        synchronized(SynchronizedInfo.class){
            shareData++;
            System.out.println(Thread.currentThread().getName()+" "+shareData);
        }

    }
    static class ThreadS implements Runnable{

        @Override
        public void run() {
            increate();
        }
    }
    static class ThreadS1 implements Runnable{
        private SynchronizedInfo synchronizedInfo;
        ThreadS1(SynchronizedInfo synchronizedInfo){
            this.synchronizedInfo=synchronizedInfo;

        }

        @Override
        public void run() {
            synchronizedInfo.increate1();

        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10000; i++) {
            new Thread(new ThreadS()).start();
            new Thread(new ThreadS1(new SynchronizedInfo())).start();

        }
        Thread.sleep(1000);
        int shareData5=0;
        for (int i = 0; i < 20000; i++) {
            shareData5++;


        }
        System.out.println("mian:sum"+shareData5);
    }
}
