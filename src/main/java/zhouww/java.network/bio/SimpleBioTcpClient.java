package zhouww.java.network.bio;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * 简单的客户端
 */
public class SimpleBioTcpClient {
    public static void main(String[] args) throws IOException {
        // 创建一个 socket 实例
        Socket socket=new Socket();
        // 绑定远程访问地址和端口
        socket.connect(new InetSocketAddress("localhost",8090));
        OutputStream outputStream = socket.getOutputStream();
        while (true){
            if(socket.isClosed()){// 断开重连机制
                socket=null;
                socket=new Socket();
                socket.connect(new InetSocketAddress("localhost",8090));
                 outputStream = socket.getOutputStream();

            }
            Scanner scanner=new Scanner(System.in);

            // 向服务端写入信息
            outputStream.write((scanner.next()+"\n").getBytes());
            // 将数据从缓存刷如字节流中
            outputStream.flush();
            // 接收服务端响应
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));



            System.out.println("接收到信息 "+inputStream.readLine());


        }




    }
}
