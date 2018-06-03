package com.wkh.designpatterns.simplefactory.example3;

/**
 * @author wangkaihua
 * @create 2018-04-04 17:43
 **/

public class Client {

    public static void main(String [] args){
//        没有工厂时
//        Apple apple = new Apple();
//        Banana banana = new Banana();
//        apple.get();
//        banana.get();

//        存在工厂，利用第一种工厂方法创建
        Fruit apple = FruitFactory.getApple();
        Fruit banana = FruitFactory.getBanana();
        apple.get();
        banana.get();

    }

}

