package zhouww.java.network.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class MultiThreadSelectorNio {
    public static void main(String[] args) {
        try {
            Reactor reactor=new Reactor(8090);
            reactor.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 首先创建一个reactor
    static class  Reactor implements Runnable{
        // 定义一个 选择器常量 通过构造函数初始化
        final Selector selector;
        // 定义一个 服务socket服务管道
        final ServerSocketChannel serverSocketChannel;
        Reactor(int port) throws IOException {
            selector=Selector.open();
            serverSocketChannel=ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress("localhost",8090));
            serverSocketChannel.configureBlocking(false);
            SelectionKey selectionKey=serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
            selectionKey.attach(new Acceptor(serverSocketChannel,selector));
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
            //while (true){
                // 判断 当前线程是否已中断
                while(!Thread.currentThread().isInterrupted()){
                    try {

                        // 选择一个Io通道准备进行i/o操作
                        selector.select();
                        // 选择器集合
                        Set<SelectionKey> selectionKeys=selector.keys();
                        Iterator<SelectionKey> iterator=selectionKeys.iterator();
                        while (iterator.hasNext()){
                            SelectionKey key=iterator.next();
                            dispatch(key);
                          //  iterator.remove();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
              //  }

            }

        }
        void dispatch(SelectionKey key){
            Runnable attachment = (Runnable)key.attachment();
            if(attachment!=null){
                attachment.run();
            }


        }
    }

    static class Acceptor implements Runnable{
        private ServerSocketChannel serverSocketChannel;
        private Selector selector;
        Acceptor(ServerSocketChannel serverSocketChannel,Selector selector){
            this.serverSocketChannel=serverSocketChannel;
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
            try {
                SocketChannel accept = serverSocketChannel.accept();
                if(accept!=null){
                    new Heandler(selector,accept);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    // headler 部分
    static class Heandler implements Runnable{
        final SocketChannel socketChannel;
        final SelectionKey selectionKey;
        ByteBuffer input = ByteBuffer.allocate(1024);
        ByteBuffer output = ByteBuffer.allocate(1024);
        static int read=0,send=1;
        int state=read;
        Heandler(Selector sel, SocketChannel c) throws IOException {
            socketChannel=c;
            socketChannel.configureBlocking(false);
            selectionKey=socketChannel.register(sel,0);
            selectionKey.attach(this);
            selectionKey.interestOps(SelectionKey.OP_READ);
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

            if(socketChannel.isConnected()){
            if(state==read){
                try {
                    read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                state=send;
                selectionKey.interestOps(SelectionKey.OP_WRITE);
            }
            if(state==send) {
                try {
                    send();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                state = read;
                selectionKey.interestOps(SelectionKey.OP_READ);
            }
            }


        }
        void read() throws IOException {
            input.clear();
            socketChannel.read(input);
            input.flip();
            if(input.hasRemaining()){
                System.out.println("数据有"+new String(input.array(),0,input.remaining()));
            }

        }
        void send() throws IOException {
            if(socketChannel.isConnected()){
                output.clear();
                output.put("测试数据发送".getBytes("UTF-8"));
                output.flip();
                socketChannel.write(output);
                System.out.println("发送");
            }


        }
    }
}
