package com.wkh.designpatterns.simplefactory;

/**
 * @author wangkaihua
 * @create 2018-04-04 17:43
 **/

public class MainClass {

    public static void main(String [] args){
//        没有工厂时
//        Apple apple = new Apple();
//        Banana banana = new Banana();
//        apple.get();
//        banana.get();

//        没有工厂时
//        Fruit apple = new Apple();
//        Fruit banana = new Apple();
//        apple.get();
//        banana.get();

//        存在工厂，利用第一种工厂方法创建
//        Fruit apple = FruitFactory.getApple();
//        Fruit banana = FruitFactory.getBanana();
//        apple.get();
//        banana.get();

//        存在工厂，利用第二种工厂方法创建
//        Fruit apple = FruitFactory.getFruit("apple");
//        Fruit banana = FruitFactory.getFruit("banana");
//        apple.get();
//        banana.get();

//        存在工厂，利用第三种工厂方法创建
        try {
            Fruit banana = FruitFactory.getFruit("com.wkh.Banana");
            Fruit apple = FruitFactory.getFruit("com.wkh.Apple");
            apple.get();
            banana.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

