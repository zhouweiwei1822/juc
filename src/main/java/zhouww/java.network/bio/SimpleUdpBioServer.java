package zhouww.java.network.bio;



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * 基于udp 的网络服务
 */
public class SimpleUdpBioServer {
    public static void main(String[] args) throws IOException {
        UpdServer server=new UpdServer();
        server.start();
    }

    // 使用java的DatagramSocket 实现的功能
    static class UpdServer{
        public void start () throws IOException {
            DatagramSocket datagramSocket=new DatagramSocket(null);
            datagramSocket.bind(new InetSocketAddress("localhost",8090));
            byte[] bytes=new byte[ 1024*1024];
            DatagramPacket datagramPacket=new DatagramPacket(bytes,bytes.length);
            datagramSocket.receive(datagramPacket);
            datagramSocket.close();
            System.out.println(new String(bytes,0,datagramPacket.getLength()));

        }
    }
}

