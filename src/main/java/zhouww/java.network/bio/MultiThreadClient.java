package zhouww.java.network.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;

public class MultiThreadClient {
    static CyclicBarrier cyclicBarrier=new CyclicBarrier(50);

    public static void main(String[] args) {
        ThreadPoolExecutor acceptWor=new ThreadPoolExecutor(100,200,60, TimeUnit.MICROSECONDS,new LinkedBlockingQueue<Runnable>(1000));
        for (int i = 0; i < 1000; i++) {
            acceptWor.execute(new Taskwor());

        }

    }
    static class Taskwor implements Runnable{

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
                task();
            } catch (IOException | BrokenBarrierException | InterruptedException e) {
                e.printStackTrace();
            }

        }
        private void task() throws IOException, BrokenBarrierException, InterruptedException {

            // 创建一个 socket 实例
            Socket socket=new Socket();
            cyclicBarrier.await();
            System.out.println("50 客户端同时发起请求");
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
             //   Scanner scanner=new Scanner(System.in);

                // 向服务端写入信息
                outputStream.write((Thread.currentThread()+" 客户端\n").getBytes());
                // 将数据从缓存刷如字节流中
                outputStream.flush();
                // 接收服务端响应
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));



                System.out.println("接收到信息 "+inputStream.readLine());


            }

        }

    }
}
