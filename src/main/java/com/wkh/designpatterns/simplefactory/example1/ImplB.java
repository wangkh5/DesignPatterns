package com.wkh.designpatterns.simplefactory.example1;
/**
 * 接口的具体实现对象B 
 */
public class ImplB implements Api{
	public void operation(String s) {
		System.out.println("ImplB s=="+s);
	}
}
