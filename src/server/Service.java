package server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.net.Socket;

//每有一个连接建立时，服务器分出一个通信的线程
public class Service implements Runnable {
	// 与客户端通信的套接字
	Socket client;

	public Service(Socket s) {
		client = s;
	}

	// 获取浏览器请求资源的路径
	public String getResourcePath(String s) {
		// 一般的HTTP请求报文的第一行是“GET /index.html HTTP/1.1”
		// 我们要获取的就是中间的"/indext.apsx"

		// 获取资源的位置
		String s1 = s.substring(s.indexOf(' ') + 1);
		s1 = s1.substring(1, s1.indexOf(' '));

		// 默认资源为index.html
		if (s1.equals("")){
			s1 = "index.html";			
		}
		return s1;
	}

	// 获取请求方法
	public String getMethod(String s) {
		// 一般的HTTP请求报文的第一行是“GET /index.html HTTP/1.1”
		// 我们要获取的就是中间的"/indext.apsx"

		// 获取资源的位置
		String s1 = s.substring(0, s.indexOf(' ') + 1);
		return s1;
	}

	public void sendFile(PrintStream out, File file) {
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int len = (int) file.length();
			byte buf[] = new byte[len];
			in.readFully(buf);// 读取文件内容到buf数组中
			out.write(buf, 0, len);
			out.flush();
			in.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
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
			String fileName = request.getUri();
			System.out.println("The user asked for resource: " + fileName);
			
			// response 根据 method 和 filename 作响应
			response.doSomething(method, fileName);
			client.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}