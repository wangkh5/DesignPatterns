package com.wkh.designpatterns.decorator.example5;

import java.io.*;

public class Client {
	public static void main(String[] args) throws Exception {
		//流式输出文件
		//正确示范
		DataOutputStream dout = new DataOutputStream(
				new BufferedOutputStream(new EncryptOutputStream(
						new FileOutputStream("MyEncrypt.txt"))));
		//错误示范
//		DataOutputStream dout = new DataOutputStream(
//			new EncryptOutputStream(
//				new BufferedOutputStream(
//					new FileOutputStream("MyEncrypt.txt"))));
		//然后就可以输出内容了
		dout.write("abcdxyz".getBytes());
		dout.close();
	}

}
