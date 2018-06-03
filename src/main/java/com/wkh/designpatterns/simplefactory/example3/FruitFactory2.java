package com.wkh.designpatterns.simplefactory.example3;

/**
 * @author wangkaihua
 * @create 2018-04-05 19:03
 **/
class FruitFactory2 {


//    第二种
    public static Fruit getFruit(String type){
        if(type.equalsIgnoreCase("apple")){
            return new Apple();
        }else if(type.equalsIgnoreCase("banana")){
            return new Banana();
        }else {
            System.out.println("类型错误");
            return null;
        }
    }

}
