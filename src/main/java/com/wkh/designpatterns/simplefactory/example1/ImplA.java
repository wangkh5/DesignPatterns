package com.wkh.designpatterns.simplefactory.example1;
/**
 * 接口的具体实现对象A 
 */
public class ImplA implements Api{
	public void operation(String s) {
		System.out.println("ImplA s=="+s);
	}
}
