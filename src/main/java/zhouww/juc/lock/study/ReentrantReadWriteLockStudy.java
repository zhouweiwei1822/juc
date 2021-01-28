package zhouww.juc.lock.study;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockStudy {
    private static Map<String,String> tempMap=new HashMap<String,String>();
    public static void main(String[] args) {
        ReentrantReadWriteLock lock=new ReentrantReadWriteLock();
        ReentrantReadWriteLockStudy pm=new ReentrantReadWriteLockStudy();

        ReentrantReadWriteLock.ReadLock readLock=lock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock=lock.writeLock();
        WriterTempMapInfo writerTempMapInfo=new WriterTempMapInfo(pm,lock,writeLock);
        ReadTempMapInfo readTempMapInfo=new ReadTempMapInfo(pm,lock,readLock);

        for(int i=0;i<10;i++){

            Thread tW=new Thread(writerTempMapInfo);
            tW.setName(i+"tW");
            tW.start();

        }
        for(int i=0;i<10;i++){
            Thread tR=new Thread(readTempMapInfo);
            tR.setName(i+"tR");
            tR.start();

         //   System.out.println("==============================================================   "+lock.getQueueLength());
        }
        System.exit(0);
     /*   try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //System.out.println("==============================================================   "+lock.getQueueLength());

    }
    // 向临时map中写入数据
    public void writerMethod(ReentrantReadWriteLock lock,ReentrantReadWriteLock.WriteLock writeLock){
        //ReentrantReadWriteLock.WriteLock writeLock=lock.writeLock();
        System.out.println(Thread.currentThread().getName()+"----获取写操作时，持有锁个数："+lock.getWriteHoldCount()+"--- 获取读锁的个数：");
        writeLock.lock();


        for(int i=0;i<10;i++){
            tempMap.put(i+"s","v"+i);
        }

        writeLock.unlock();
       // System.out.println(lock.getWriteHoldCount());


    }
    // 向临时map中读取数据
    public void readMethod(ReentrantReadWriteLock lock, ReentrantReadWriteLock.ReadLock readLock){
      //  ReentrantReadWriteLock.ReadLock readLock=lock.readLock();
        readLock.lock();
        //Thread[] k=new Thread[10000];
        System.out.println(Thread.currentThread().getName()+"----获取读操作时，持有的锁个数："+lock.getReadHoldCount()+"--- 获取写锁等待的个数：");
        System.out.println(tempMap);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // System.out.println(lock.writeLock());
        readLock.unlock();



    }

/// 向 临时map中写入数据 线程对象类
    public static class WriterTempMapInfo implements Runnable{
      private final ReentrantReadWriteLockStudy reentrantReadWriteLockStudy;
    private final  ReentrantReadWriteLock lock;
    private final  ReentrantReadWriteLock.WriteLock writeLock;
      public WriterTempMapInfo(ReentrantReadWriteLockStudy reentrantReadWriteLockStudy,ReentrantReadWriteLock lock,ReentrantReadWriteLock.WriteLock writeLock){
          this.reentrantReadWriteLockStudy=reentrantReadWriteLockStudy;
          this.lock=lock;
          this.writeLock=writeLock;

      }

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
        reentrantReadWriteLockStudy.writerMethod(lock,writeLock);
        reentrantReadWriteLockStudy.writerMethod(lock,writeLock);
        reentrantReadWriteLockStudy.writerMethod(lock,writeLock);
        reentrantReadWriteLockStudy.writerMethod(lock,writeLock);
    }
}
    /// 向 临时map中写入数据 线程对象类
    public static class ReadTempMapInfo implements Runnable{
        private final ReentrantReadWriteLockStudy reentrantReadWriteLockStudy;
        private final  ReentrantReadWriteLock lock;
        private final  ReentrantReadWriteLock.ReadLock readLock;
        public ReadTempMapInfo(ReentrantReadWriteLockStudy reentrantReadWriteLockStudy,ReentrantReadWriteLock lock,ReentrantReadWriteLock.ReadLock readLock){
            this.reentrantReadWriteLockStudy=reentrantReadWriteLockStudy;
            this.lock=lock;
            this.readLock=readLock;

        }


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
            reentrantReadWriteLockStudy.readMethod(lock,readLock);
            reentrantReadWriteLockStudy.readMethod(lock,readLock);
            reentrantReadWriteLockStudy.readMethod(lock,readLock);
        }
    }
}
