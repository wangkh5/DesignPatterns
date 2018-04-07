package com.wkh.designpatterns.factorymethod;

/**
 * @author wangkaihua
 * @create 2018-04-07 19:00
 **/
public class AppleFactory implements FruitFactory {

    public Fruit getFruit() {
        return new Apple();
    }
}
