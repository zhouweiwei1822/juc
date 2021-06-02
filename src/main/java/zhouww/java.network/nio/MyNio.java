package zhouww.java.network.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyNio {

    public static void main(String[] args) throws IOException {
      new Reactor().run();

    }
    // 首先定义一个 处理响应模型io reactor模型先定义
    static class Reactor implements Runnable{
        final ServerSocketChannel serverSocketChannel;
        final  Selector selector;
        Reactor() throws IOException {


                // 创建一个 通道实例
                serverSocketChannel=ServerSocketChannel.open();
                // 设置 异步结构
                serverSocketChannel.configureBlocking(false);
                // 绑定 ip和端口
                serverSocketChannel.bind(new InetSocketAddress("localhost",8091));
                // 创建一个事件选择器
                selector=Selector.open();
                // 注册 感兴趣的事件
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT,new Acceptor(selector,serverSocketChannel));


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
                // 准备通道监控 事件触发
                try {
                    selector.select();
                    // 获取处理集合内容
                    Set<SelectionKey> selectionKeys=selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator=selectionKeys.iterator();
                    while (keyIterator.hasNext()){
                        SelectionKey key=keyIterator.next();
                        dispatch( key);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
        // 业务处理分发操作
        void dispatch(SelectionKey key){
           Runnable runnable=(Runnable)key.attachment();
           // 调用任务执行
            runnable.run();

        }
    }

    // 定义一个接收连接事件处理
    static class Acceptor implements Runnable{
        final Selector selector;
        final ServerSocketChannel serverSocketChannel;
        Acceptor(Selector selector,ServerSocketChannel serverSocketChannel){
            this.selector=selector;
            this.serverSocketChannel=serverSocketChannel;

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
                System.out.println(Thread.currentThread().getName()+"即将接收客户端的请求");
                // 接收客户端请求
                SocketChannel socketChannel=serverSocketChannel.accept();
                if(socketChannel!=null){
                    System.out.println(Thread.currentThread().getName()+"即将接收客户端的请求"+socketChannel);
                    new Handler(socketChannel,selector);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    // 三业务处理
    static class Handler implements Runnable{
        final SocketChannel socketChannel;
        final SelectionKey selectionKey;
        ByteBuffer input = ByteBuffer.allocate(20);
        ByteBuffer output = ByteBuffer.allocate(20);
        static int read=0,send=1;
        int state=read;

        Handler(SocketChannel socketChannel,Selector selector) throws IOException {
            this.socketChannel=socketChannel;
            socketChannel.configureBlocking(false);
            this.selectionKey=socketChannel.register(selector,SelectionKey.OP_READ,this);


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
            if(state==read){
                read();
                state=send;
            }
            if(state==send){
                send();
                state=read;
            }

        }
        void read(){
           SocketChannel socketChannel= (SocketChannel)selectionKey.channel();
            try {
                socketChannel.read(input);
                input.flip();// 转换为读模式
                while(input.hasRemaining()){// 判断是否还存在 未读完的信息
                    System.out.println("接收的数据"+StandardCharsets.UTF_8.decode(input));
                }
                selectionKey.interestOps(SelectionKey.OP_WRITE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        void send() {
            SocketChannel socketChannel= (SocketChannel)selectionKey.channel();
            output.clear();
            try {
                //
                output.put("收到数据".getBytes());
                socketChannel.write(output);
                selectionKey.interestOps(SelectionKey.OP_READ);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
