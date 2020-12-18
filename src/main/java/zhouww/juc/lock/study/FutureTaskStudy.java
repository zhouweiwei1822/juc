package zhouww.juc.lock.study;

import java.util.concurrent.atomic.AtomicInteger;

public class FutureTaskStudy<T> {
    private static final int HASH_INCREMENT = 0x61c88647;
    private static final int INITIAL_CAPACITY = 16;
    private static test[] table;
    private static AtomicInteger nextHashCode =
            new AtomicInteger();
    private  int keyM=nextHashCode();
    private static FutureTaskStudy<String> kk=new FutureTaskStudy();
    private static int nextHashCode() {
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }

    public static void main(String[] args) {
        FutureTaskStudy p=new FutureTaskStudy();

        p.set("ooooo","op");
        p.set("ooooo1","op");
        p.set("ooooo1","op");
       // p.get(i);
    }
    FutureTaskStudy(){
        table=new test[16];
    }
    private  void set(T t,Object vale){
        //for(int j=0;j<=16;j++){
          //  int i=t.threadLocalHashCode & (INITIAL_CAPACITY-1);
            System.out.println("ru=====ttt--"+((this.keyM)));
            System.out.println("ru====="+((this.keyM) & (16-1)));
       // }

    }
    private  test get(test t){
        int i=t.threadLocalHashCode & (table.length-1);
        test h=table[i];
        for(int j=0;j<=32;j++){
            //  int i=t.threadLocalHashCode & (INITIAL_CAPACITY-1);

            System.out.println("su====="+((nextHashCode.getAndAdd(HASH_INCREMENT)) & 32-1));
        }

        return h;

    }
  public static class test{
        private String hh;
      private static AtomicInteger nextHashCode =
              new AtomicInteger();

      private final int threadLocalHashCode = nextHashCode();
      private static int nextHashCode() {
          return nextHashCode.getAndAdd(HASH_INCREMENT);
      }


  }


}
