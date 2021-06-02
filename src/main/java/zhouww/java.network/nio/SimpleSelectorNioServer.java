package zhouww.java.network.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SimpleSelectorNioServer {
    public static void main(String[] args) {
        SelectorNioServer server=new SelectorNioServer();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class SelectorNioServer{
        void start() throws IOException {
            // 创建一个socket通道实例
            ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
            // 设置是否为阻塞
            serverSocketChannel.configureBlocking(false);
            // 绑定地址和端口协议
            serverSocketChannel.bind(new InetSocketAddress("localhost",8090));

            //

            // 创建一个选择器
            Selector selector=Selector.open();
            // 将选择器和 通道注册到选择器中 并且选择想要的功能进行
            SelectionKey key=serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
            ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
            ByteBuffer byteBufferw=ByteBuffer.allocate(1024);
            // 监听不同业务处理
            while (true){
                // 选择好I/o通通道进行数据操作
                int nRead=selector.select();
                // 获取 选择选择集合
                Set<SelectionKey> selectionKeySet=selector.selectedKeys();
                // 创建选择器的的迭代器
                Iterator<SelectionKey> iterator=selectionKeySet.iterator();

                while (iterator.hasNext()){
                    SelectionKey selectionKey=iterator.next();
                    iterator.remove();
                    try {
                        if(selectionKey.isAcceptable()){// 可接受事件处理
                            // 真实接收业务 创建新的连接
                            SocketChannel accept = serverSocketChannel.accept();
                            // 设置非阻塞
                            accept.configureBlocking(false);
                            // 在选择器上注册一个事件 读事件
                            accept.register(selector,SelectionKey.OP_READ);

                        }
                        if(selectionKey.isReadable()){
                            SocketChannel socketChannel=(SocketChannel)selectionKey.channel();
                            if(socketChannel!=null){
                                byteBuffer.clear();
                                socketChannel.read(byteBuffer);
                                byteBuffer.flip();
                                System.out.println(byteBuffer.mark());
                                System.out.println("独处数据："+new String(byteBuffer.array(),0,byteBuffer.remaining()));
                                selectionKey.interestOps(SelectionKey.OP_WRITE);
                            }

                        }
                        if(selectionKey.isWritable()){
                            SocketChannel socketChannel=(SocketChannel)selectionKey.channel();
                            if(socketChannel!=null){

                                byteBufferw.clear();
                                byteBufferw.put("张三你好：kiali".getBytes());
                                byteBufferw.flip();
                                System.out.println("写书数据："+new String(byteBufferw.array(),0,byteBufferw.remaining()));
                                socketChannel.write(byteBufferw);


                                selectionKey.interestOps(SelectionKey.OP_READ);
                            }
                        }
                    }catch (IOException e){
                        System.out.println("远程连接已断开");
                    }

                }
            }



        }
    }
}
