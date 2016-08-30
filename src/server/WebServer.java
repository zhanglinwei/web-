package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer implements Runnable {
	
	private final ServerSocket serverSocket;
	private final ExecutorService pool;

	/**
	 * 传入端口号、缓冲池大小，构造WebServer
	 * 
	 * @param port
	 * @param poolSize
	 * @throws IOException
	 */
	public WebServer(int port, int poolSize) throws IOException {
		serverSocket = new ServerSocket(port, poolSize);
		pool = Executors.newFixedThreadPool(poolSize);
	}

	/**
	 * 开启服务器 处理缓冲池中的连接请求
	 */
	public void run() {
		try {
			for (;;) {
				pool.execute(new Service(serverSocket.accept()));
			}
		} catch (IOException ex) {
			pool.shutdown();
		}
	}
	
	public static void main(String[] args) {
		try {
			new Thread(new WebServer(12345, 50)).start();;
			System.out.println("服务器已开启！！");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}