 停用线程的方法：
 - 系统方式退出 直接调用System.exit(0) 直接退出系统 也就是调用虚拟机的停用让所用的线程全部停止
 - 程序正常执行退出
 - 程序执行异常退出
 - 使用退出标志进行退出 interrupt（）、interrupted（）和isInterrupted（）  interrupt（）是给线程设置中断标志；interrupted（）是检测中断并清除中断状态；isInterrupted（）只检测中断。还有重要的一点就是interrupted（）作用于当前线程，interrupt（）和isInterrupted（）作用于此线程
 
