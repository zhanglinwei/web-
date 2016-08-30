package server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class Response {
	
	private PrintStream output;
	private Request request;
	
	/**
	 * 构造 response 
	 * @param out
	 * @param request
	 */
	public Response(OutputStream out, Request request) {
		output = new PrintStream(out);
		this.request = request;
	}
	
	
	/**
	 * 根据方法与请求的文件，做出对应的操作
	 * @param method
	 * @param fileName
	 */
	public void doSomething(String method, String fileName) {
		if(method.equals("POST")){
			setPost();
			return;
		}
		File file = new File(fileName);
		if (file.exists()) {
			setSuccessPage(file);
		} else {
			setErrorPage();
		}
	}

	
	/**
	 * 对 post 做处理
	 */
	private void setPost() {
		
		// 响应成功， 添加 http 报文头部
		output.println("HTTP/1.0 200 OK");
		output.println("MIME_version:1.0");
		output.println("Content_Type:text/html");
		byte[] content = request.getContent();
		int len = content.length;
		output.println("Content_Length:" + len);
		output.println("");// 报文头和信息之间要空一行
		
		
		// http 报文信息，将字节数组转化为字符数组
		char[] data = new char[len];
		for (int i = 0; i < content.length; i++) {
			data[i] = (char) content[i];
		}
		
		// 发送文件
		output.print(data);
		
		output.flush();
	}

	/**
	 * 返回错误页面， error.html
	 */
	private void setErrorPage() {
		// 响应失败， 添加 http 报文头部
		File file = new File("error.html");
		String errorMessage = "HTTP/1.1 404 File Not Found \r\n" + "Content-Type: text/html\r\n"
				+ "Content-Length: " + file.length();
		System.out.println(errorMessage);
		output.println(errorMessage);
		output.println("");
		
		// http 报文信息
		sendFile(output, file);
		output.flush();
	}

	
	/**
	 * 返回请求的页面
	 * @param file
	 */
	public void setSuccessPage(File file) {
		output.println("HTTP/1.0 200 OK");
		output.println("MIME_version:1.0");
		output.println("Content_Type:text/html");
		int len = (int) file.length();
		output.println("Content_Length:" + len);
		output.println("");// 报文头和信息之间要空一行

		// 发送文件
		sendFile(output, file);

		output.flush();
		
	}
	
	
	/**
	 * 发送请求的文件
	 * @param out
	 * @param file
	 */
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
	
	
}
