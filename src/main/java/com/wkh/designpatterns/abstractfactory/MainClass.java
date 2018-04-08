package com.wkh.designpatterns.abstractfactory;

/**
 * @author wangkaihua
 * @create 2018-04-04 17:43
 **/

public class MainClass {

    public static void main(String [] args){
        FruitFactory northFruitFactory = new NorthFruitFactory();
        Fruit northApple = northFruitFactory.getApple();
        Fruit northBanana = northFruitFactory.getBanana();
        northApple.get();
        northBanana.get();

        FruitFactory mouthFruitFactory = new SouthFruitFactory();
        Fruit mouthApple = mouthFruitFactory.getApple();
        Fruit mouthBanana = mouthFruitFactory.getBanana();
        mouthApple.get();
        mouthBanana.get();
    }

}

