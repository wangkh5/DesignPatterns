package com.wkh.designpatterns.simplefactory;

/**
 * @author wangkaihua
 * @create 2018-04-05 19:03
 **/
class FruitFactory{

//    第一种
//    public static Fruit getApple() {
//        return new Apple();
//    }
//
//     public static Fruit getBanana(){
//        return new Banana();
//    }

//    第二种
//    public static Fruit getFruit(String type){
//        if(type.equalsIgnoreCase("apple")){
//            return new Apple();
//        }else if(type.equalsIgnoreCase("banana")){
//            return new Banana();
//        }else {
//            System.out.println("类型错误");
//            return null;
//        }
//    }

//    第三种
    public static Fruit getFruit(String type) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class clazz = Class.forName(type);
        return (Fruit)clazz.newInstance();
    }
}
