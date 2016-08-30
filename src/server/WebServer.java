package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {

	public static void main(String[] args) {

		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);

		int Port = 12345;// 端口号，由于这里是测试，所以不使用常用端口
		// 创建两个套接字
		ServerSocket server = null;
		Socket client = null;
		try {
			server = new ServerSocket(Port);
			// 服务器开始监听
			System.out.println("The WebServer is listening on port " + server.getLocalPort());
			while (true) {
				// 接收连接
				client = server.accept();
				// 多线程运行
				fixedThreadPool.execute(new HttpSer(client));
				// new CommunicateThread(client).start();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}