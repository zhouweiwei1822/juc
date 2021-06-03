package zhouww.java.network.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel=SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",8090));
        ByteBuffer n=ByteBuffer.allocate(1024);
while (true){
     if(socketChannel!=null){
         Scanner scanner=new Scanner(System.in);
         n.put(scanner.next().getBytes());
         n.flip();
         socketChannel.write(n);
         n.clear();
         socketChannel.read(n);
         n.flip();
         System.out.println("数据来了"+StandardCharsets.UTF_8.decode(n));
         n.clear();
     }

}
    }
}
