package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Request {

	private InputStream in;
	private String method;
	private String url;
	private byte[] content;

	/**
	 * 初始化属性，方法、url、报文体
	 * 
	 * @param in
	 * @throws IOException
	 */
	public Request(InputStream in) throws IOException {
		this.in = in;
		praseRequest();
	}

	private void praseRequest() throws IOException{
		DataInputStream input = new DataInputStream(in);
		// 读去请求行内容
		@SuppressWarnings("deprecation")
		String requestLine = input.readLine();
		parseMethod(requestLine);
		parseUrl(requestLine);
		// 如果方法为post，则初始化报文体内容 content
		if (method.equals("POST")) {
			// 解析并初始化 content
			parserContent();
		}
	}
	
	/**
	 * 解析 content 的内容
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	private void parserContent() throws IOException {
		DataInputStream input = new DataInputStream(in);
		// 读取第一行
		String line = input.readLine();
		// 报文长度
		int len = 0;

		while (!line.equals("")) {
			if (line.contains("Content-Length")) {
				int index = line.indexOf(" ");
				line = line.substring(index + 1);
				len = Integer.parseInt(line);
			}
			line = input.readLine();
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
	 * @param requestLine
	 */
	private void parseUrl(String requestLine) {
		// 一般的HTTP请求报文的第一行是“GET /index.html HTTP/1.1”
		// 我们要获取的就是中间的"/indext.apsx"
		url = requestLine.substring(requestLine.indexOf(' ') + 1);
		url = url.substring(1, url.lastIndexOf(' '));
		// 默认资源为index.html
		if (url.equals(""))
			url = "index.html";
	}

	/**
	 * 解析方法名称
	 * 
	 * @param reuqestLine
	 */
	private void parseMethod(String reuqestLine) {
		// 一般的HTTP请求报文的第一行是“GET /index.html HTTP/1.1”
		// 我们要获取的就是中间的"GET"
		method = reuqestLine.substring(0, reuqestLine.indexOf(' '));
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public byte[] getContent() {
		return content;
	}

}
