package zhouww.nio.study;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class SimpleNioServer {
    public static void main(String[] args) throws IOException {
        NioServer nioServer = new NioServer();
        nioServer.server();
    }

    static class NioServer {


        NioServer() {
        }

        public void server() throws IOException {
            // 创建个通道实例
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            // 绑定绑定服务监听端口
            serverSocketChannel.bind(new InetSocketAddress("localhost", 8090));
            while (true) {

                // 进行数据获取
                SocketChannel socketChannel = serverSocketChannel.accept();
                ByteBuffer byteBuffers = ByteBuffer.allocate(1024);
                if(socketChannel!=null){
                    // 读取数据
                    socketChannel.read(byteBuffers);
                    byteBuffers.flip();
                    while (byteBuffers.hasRemaining()) {
                        System.out.print((char)byteBuffers.get());
                    }
                    // System.out.println(new String(b,"UTF-8"));
                }

                }

            }
        }
    }
