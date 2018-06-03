package com.wkh.designpatterns.simplefactory.example3;

/**
 * @author wangkaihua
 * @create 2018-04-05 19:03
 **/
class FruitFactory3 {
    public static Fruit getFruit(String type)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class clazz = Class.forName(type);
        return (Fruit)clazz.newInstance();
    }
}
