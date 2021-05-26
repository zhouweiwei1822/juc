package zhouww.java.network.bio;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端 此处是一个简单的阻塞的socket服务 单一的读写阻塞
 */
public class SimpleBioTcpServer {
    public static void main(String[] args) throws IOException {
        BioService service=new BioService();
        service.serverStart();
    }
    static class BioService{
        public void serverStart() throws IOException {// 启动方法
           // 创建一个 服务端的serverSocket对象
            ServerSocket serverSocket=new ServerSocket();
            // 绑定一个 服务监听端口
            serverSocket.bind(new InetSocketAddress("localhost",8090));
            System.out.println("simple socket tcp model server start success ");
            // 进行接收数据通过 阻塞 accept进行数据接收
            while (true){
            Socket socket=serverSocket.accept();
            System.out.println(" 有客户端连接进来了");

            // 判断是否获取到了连接
            if(socket!=null && !socket.isClosed()){

                    // 获取输入流
                    InputStream inputStream=socket.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                    String ls="";

                    System.out.println("接收到信息 "+bufferedReader.readLine());
                    // 给客户端 以回应
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write("simple socket tcp model server receive message\n".getBytes());
                    outputStream.flush();

                }




            }

        }

    }


}
