package com.wkh.designpatterns.factorymethod;

/**
 * @author wangkaihua
 * @create 2018-04-07 19:11
 **/
public class PearFactory implements FruitFactory {
    public Fruit getFruit() {
        return new Pear();
    }
}
