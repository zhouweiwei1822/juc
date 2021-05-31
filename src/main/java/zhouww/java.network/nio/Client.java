package zhouww.java.network.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel=SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",8090));
        ByteBuffer n=ByteBuffer.allocate(1024);
        n.put("hskhfshfjshjfhjsdfhsdf".getBytes());
        n.flip();   
        socketChannel.write(n);
        while (true){

        }
    }
}
