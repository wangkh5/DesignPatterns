package com.wkh.designpatterns.simplefactory.example3;

/**
 * @author wangkaihua
 * @create 2018-04-04 17:43
 **/

public class Client3 {

    public static void main(String [] args){

//        存在工厂，利用第三种工厂方法创建
        try {
            Fruit banana = FruitFactory3.getFruit("com.wkh.designpatterns.Banana");
            Fruit apple = FruitFactory3.getFruit("com.wkh.designpatterns.Apple");
            apple.get();
            banana.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

