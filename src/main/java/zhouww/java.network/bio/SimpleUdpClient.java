package zhouww.java.network.bio;



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class SimpleUdpClient {
    public static void main(String[] args) {
        UdpClient udpClient=new UdpClient();
        udpClient.send();
    }
  static   class  UdpClient{


public void send(){
    try {
        DatagramSocket datagramSocket=new DatagramSocket(null);
        datagramSocket.connect(new InetSocketAddress("localhost",8090));
       byte[] bytes="你好 udp服务".getBytes();
        DatagramPacket datagramPacket=new DatagramPacket(bytes,bytes.length);
        datagramSocket.send(datagramPacket);
    } catch (SocketException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
  }
}
