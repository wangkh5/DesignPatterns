package com.wkh.designpatterns.abstractfactory;

/**
 * @author wangkaihua
 * @create 2018-04-08 19:53
 **/
public class NorthBanana extends Banana implements Fruit {
    public void  get() {
        System.out.println("采摘北方香蕉");
    }
}
