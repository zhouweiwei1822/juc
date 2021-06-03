package zhouww.java.network.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MyNio {
    static  Wakup wakup=new Wakup();
    public static void main(String[] args) {
        startserver();
    }
    //1、创建一个监听服务
   static void startserver(){

        //ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(2,2*2,10l,TimeUnit.MICROSECONDS,new LinkedBlockingQueue<Runnable>(1000));
       try {
           // 创建一个 服务通道实体
           ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
           // 创建一个 选择器
           Selector selector=Selector.open();

           // 设置非阻塞模式
           serverSocketChannel.configureBlocking(false);
           // 创建绑定 地址
           serverSocketChannel.bind(new InetSocketAddress("localhost",8090));
           // 选择感兴趣 事件注册到选择器中
           serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
           new Thread(new Accept(selector,serverSocketChannel)).start();


       } catch (IOException e) {
           e.printStackTrace();
       }

    }


    // 创建一个调度器   // 2、创建一个接收者服务
    static class Accept implements Runnable{

        static List<Selector> list=new ArrayList<>();
        static AtomicInteger atomicInteger=new AtomicInteger();
        private static ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(2,Integer.MAX_VALUE,0l,TimeUnit.MICROSECONDS,new SynchronousQueue<Runnable>());
        private Selector selector;
       // private Selector selectorChile;
        private ServerSocketChannel serverSocketChannel;
        Accept (Selector selector,ServerSocketChannel serverSocketChannel){
            this.selector=selector;
            this.serverSocketChannel=serverSocketChannel;
           // this.selectorChile=selectorChile;

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
            while (true){
                try {
                    // 准备io选择事件
                    selector.select();
                    // 获取适配事件
                    Set<SelectionKey> selectionKeys= selector.selectedKeys();
                    Iterator<SelectionKey> iterator=selectionKeys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey key=iterator.next();
                        iterator.remove();
                        if(key.isAcceptable()){

                            // 接收可客户端连接新的channl
                            SocketChannel socketChannel=serverSocketChannel.accept();
                            if(socketChannel!=null){
                                System.out.println("连接："+socketChannel);
                                Selector selectorChile=Selector.open();
                                System.out.println("创建："+selectorChile);
                                if(list.size()<=5){
                                    atomicInteger.addAndGet(1);
                                    list.add(selectorChile);

                                }else {
                                    selectorChile=list.get(atomicInteger.addAndGet(1)%list.size());
                                }

                                // 设置阻塞类型 非阻塞
                                socketChannel.configureBlocking(false);

                                new Thread(new wakup1(selectorChile,socketChannel)).start();



                            }
                        }


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }



    }

   static class wakup1 implements  Runnable{
       private Selector selector;
       private SocketChannel socketChannel;
       wakup1(Selector selector,SocketChannel socketChannel){
           this.selector=selector;
           this.socketChannel=socketChannel;
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



           //注册感兴趣事件

           try {
               selector.wakeup();
               socketChannel.register(selector,SelectionKey.OP_READ);
               System.out.println("注册成功");
           } catch (ClosedChannelException e) {
               e.printStackTrace();
           }

           if(selector.selectedKeys().size()<=0) {
               new Thread(new Dispatch(selector)).start();
           }




       }
   }
    // 创建多个工作工人处理业务
    static class Dispatch implements Runnable{
       final ReentrantLock reentrantLock=new ReentrantLock();
        private Selector selector;
        Dispatch(Selector selector){
            this.selector=selector;
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
            while (true){
                // 准备监听io准备
                try {
                    selector.select();
                    Set<SelectionKey> selectionKeys=selector.selectedKeys();
                    Iterator<SelectionKey> selectionKeyIterator=selectionKeys.iterator();
                    while (selectionKeyIterator.hasNext()){
                        SelectionKey key=selectionKeyIterator.next();
                        selectionKeyIterator.remove();
                        dispatch( key);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
        void dispatch(SelectionKey key){
             ByteBuffer in=ByteBuffer.allocate(1024);
             ByteBuffer out=ByteBuffer.allocate(1024);
            // 获取通道信息
            SocketChannel socketChannel=(SocketChannel) key.channel();
            if(socketChannel!=null){
                try {
                    if(key.isReadable()){

                        socketChannel.read(in);
                        //切换到读模式
                        in.flip();
                        System.out.println(Thread.currentThread().getName()+"接收到了你的请求："+(StandardCharsets.UTF_8.decode(in).toString()));
                        // 切换到关注事件
                        key.interestOps(SelectionKey.OP_WRITE);
                    }else if(key.isWritable()){
                        socketChannel.write(StandardCharsets.UTF_8.encode(Thread.currentThread().getName()+" hello 你好"));
                        // 切换到关注事件
                        key.interestOps(SelectionKey.OP_READ);

                    }
                } catch (IOException e) {
                    key.cancel();
                    e.printStackTrace();
                }
            }
        }
    }
    static class Wakup{
        private volatile Boolean flag=true;
        private volatile SocketChannel socketChannel;

        public Boolean getFlag() {
            return flag;
        }

        public void setFlag(Boolean flag) {
            this.flag = flag;
        }

        public SocketChannel getSocketChannel() {
            return socketChannel;
        }

        public void setSocketChannel(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }
    }

}
