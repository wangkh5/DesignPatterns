package com.wkh.designpatterns.abstractfactory;

/**
 * @author wangkaihua
 * @create 2018-04-08 19:52
 **/
public class SouthFruitFactory implements FruitFactory{
    public Fruit getApple() {
        return new SouthApple();
    }

    public Fruit getBanana() {
        return new SouthBanana();
    }
}
