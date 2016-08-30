package server;

import java.net.Socket;

//每有一个连接建立时，服务器分出一个通信的线程
public class Service implements Runnable {
	// 与客户端通信的套接字
	Socket client;

	public Service(Socket s) {
		client = s;
	}

	public void run() {
		try {
			// 创建请求对象
			Request request = new Request(client.getInputStream());
			// 创建响应对象
			Response response = new Response(client.getOutputStream(), request);

			// 获取方法
			String method = request.getMethod();
			System.out.println("The request's method is: " + method);
			// 获取文件路径
			String fileName = request.getUrl();
			System.out.println("The user asked for resource: " + fileName);
			
			// response 根据 method 和 filename 作响应
			response.doSomething(method, fileName);
			client.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}