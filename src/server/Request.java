package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Request {

	private InputStream in;
	private DataInputStream input;
	private String method;
	private String uri;
	private byte[] content;

	/**
	 * 初始化属性，方法、url、报文体
	 * 
	 * @param in
	 * @throws IOException
	 */
	public Request(InputStream in) throws IOException {
		this.in = in;
		this.input = new DataInputStream(in);
		// 读出第一行内容
		String msg = input.readLine();
		// 初始化 method
		parseMethod(msg);
		// 初始化 url
		parseUri(msg);
		
		
		// 如果方法为post，则初始化报文体内容 content
		if (method.equals("POST")) {
			// 解析并初始化 content
			ParserContent();
		}
	}

	/**
	 * 解析 content 的内容
	 * @throws IOException
	 */
	private void ParserContent() throws IOException {

		// 读取第一行
		String string = input.readLine();
		// 报文长度
		int len = 0;

		while (!string.equals("")) {
			if (string.contains("Content-Length")) {
				int index = string.indexOf(" ");
				string = string.substring(index + 1);
				len = Integer.parseInt(string);
			}
			string = input.readLine();
		}
		// 创建字节数组接收 content 的内容
		content = new byte[len];
		int count = 0;
		while (len != count) {
			count += in.read(content, count, len);
		}
	}

	/**
	 * 解析资源路径
	 * 
	 * @param msg
	 */
	private void parseUri(String msg) {
		// 一般的HTTP请求报文的第一行是“GET /index.html HTTP/1.1”
		// 我们要获取的就是中间的"/indext.apsx"

		// 获取资源的位置
		uri = msg.substring(msg.indexOf(' ') + 1);
		uri = uri.substring(1, uri.lastIndexOf(' '));

		// 默认资源为index.html
		if (uri.equals(""))
			uri = "index.html";
	}

	/**
	 * 解析方法名称
	 * 
	 * @param msg
	 */
	private void parseMethod(String msg) {
		// *****
		// 一般的HTTP请求报文的第一行是“GET /index.html HTTP/1.1”
		// 我们要获取的就是中间的"GET"

		// 获取方法
		method = msg.substring(0, msg.indexOf(' ') + 1).trim();
	}

	public String getMethod() {
		// 返回方法
		return method;
	}

	public String getUri() {
		return uri;
	}

	public byte[] getContent() {
		return content;
	}

}
