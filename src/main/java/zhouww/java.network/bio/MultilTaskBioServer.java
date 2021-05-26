package zhouww.java.network.bio;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 支持多个客户端进行 并发访问
 * 分三部分 1、服务启动
 *    2、服务接收客客户端 多个服务
 *    3、处理客户端业务 线程
 */
public class MultilTaskBioServer {
    // 根据电脑系统的CPU分配接收客户端服务的 服务
    static final int CPUNm =Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException {
        TaskBioService taskBioService=new TaskBioService();
        // 启动服务
        taskBioService.start();
    }
    //
   static  class TaskBioService{


       public void start() throws IOException {
           // 创建服务实体 socket服务
           ServerSocket serverSocket=new ServerSocket();
           // 绑定监听端口 和 ip
           serverSocket.bind(new InetSocketAddress("localhost",8090),10);
           // 创建接待者的线程池
           ThreadPoolExecutor acceptWor=new ThreadPoolExecutor(CPUNm,CPUNm*2,60, TimeUnit.MICROSECONDS,new LinkedBlockingQueue<>(100));
           // 处理客户端的 业务的线程池
           ThreadPoolExecutor task=new ThreadPoolExecutor(CPUNm*2,CPUNm*4,60, TimeUnit.MICROSECONDS,new LinkedBlockingQueue<>(100));
           //创建 4个接待者
           for (int i = 0; i <4; i++) {
               acceptWor.execute(new AcceptWorker(serverSocket,task));
           }
           System.out.println("服务已启动 创建了4个接待者 。。。");
       }
    }
     // 接待客户端请求的 类
    static class AcceptWorker implements Runnable{
       private ServerSocket serverSocket;
       private ThreadPoolExecutor threadPoolExecutor;
         AcceptWorker(ServerSocket serverSocket,ThreadPoolExecutor threadPoolExecutor){
             this.serverSocket=serverSocket;
             this.threadPoolExecutor=threadPoolExecutor;

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
            task();
        }
        private void task()  {
            try {
                while (true){
                    System.out.println(Thread.currentThread().getName()+"开始等待接收了客户端的请求");
                    Socket accept = serverSocket.accept();
                    System.out.println(Thread.currentThread().getName()+"接收了客户端的请求");
                    threadPoolExecutor.execute(new TaskWorker(accept));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    // 处理客户端业务的 任务类
    static class TaskWorker implements Runnable{
       private  Socket socket;
        TaskWorker(Socket socket){
            this.socket=socket;
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
            task();
        }
        private void task()   {
            try {
                while (true){
                    if(socket!=null && !socket.isClosed()){
                        // 读取 客户端发来的请求
                        BufferedReader inputStream =new BufferedReader(new InputStreamReader(socket.getInputStream())) ;
                        // 打印出信息
                        System.out.println("线程"+Thread.currentThread().getName()+"处理了 客户 端 发来的 信息："+inputStream.readLine());
                        // 回复客户端信息
                        OutputStream outputStream = socket.getOutputStream();
                        outputStream.write(("线程"+Thread.currentThread().getName()+"你好我已经收到你的消息了\n").getBytes());
                        // 将缓存输入字节流
                        outputStream.flush();

                    }else {
                        socket=null;
                        break;
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            finally {
                if(socket!=null){
                    try {
                        socket.shutdownInput();
                        socket.shutdownOutput();
                        socket.isClosed();
                        socket=null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }


        }
    }

}
