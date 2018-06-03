package com.wkh.designpatterns.simplefactory.example3;

/**
 * @author wangkaihua
 * @create 2018-04-04 17:43
 **/

public class Client2 {

    public static void main(String [] args){


//        存在工厂，利用第二种工厂方法创建
        Fruit apple = FruitFactory2.getFruit("apple");
        Fruit banana = FruitFactory2.getFruit("banana");
        apple.get();
        banana.get();

    }

}

