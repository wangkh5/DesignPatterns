package com.wkh.designpatterns.factorymethod;

/**
 * @author wangkaihua
 * @create 2018-04-07 19:01
 **/
public class BananaFactory implements FruitFactory {

    public Fruit getFruit() {
        return new Banana();
    }
}
