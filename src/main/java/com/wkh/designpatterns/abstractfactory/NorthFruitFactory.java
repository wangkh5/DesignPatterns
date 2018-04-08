package com.wkh.designpatterns.abstractfactory;

/**
 * @author wangkaihua
 * @create 2018-04-08 19:52
 **/
public class NorthFruitFactory implements FruitFactory{
    public Fruit getApple() {
        return new NorthApple();
    }

    public Fruit getBanana() {
        return new NorthBanana();
    }
}
