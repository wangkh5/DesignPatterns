package com.wkh.designpatterns.simplefactory.example3;

/**
 * @author wangkaihua
 * @create 2018-04-05 19:03
 **/
class FruitFactory {

//    第一种
    public static Fruit getApple() {
        return new Apple();
    }

     public static Fruit getBanana(){
        return new Banana();
    }

}
