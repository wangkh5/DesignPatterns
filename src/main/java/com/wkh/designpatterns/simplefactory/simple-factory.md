# 简单工厂模式

标签（空格分隔）：设计模式

---

## 什么是简单工厂模式
简单工厂模式属于类的创建性模式，又叫作静态工厂方法模式。通过专门定义一个类来负责创建其他类的实例，被创建的实例通常都具有共同的父类。

## 简单工厂模式中包含的角色及其职责
 1. 工厂角色：简单工厂模式的核心，它负责实现创建所有实例的内部逻辑，工厂类可以被外界直接调用，创建所需的产品对象。
 2. 抽象角色：简单工厂模式所创建的所有对象的父类，它负责描述全部实例所共有的公共接口。
 3. 具体产品角色：简单工厂模式所创建的具体实例的对象。
 
## 代码
```
interface Fruit{
    /**
     * 采摘水果方法
      */
    void get();
}


----------


class Apple implements Fruit{

    public void get() {
        System.out.println("采摘苹果");
    }
}


class Banana implements Fruit{

    public void get() {
        System.out.println("采摘香蕉");
    }
}


----------
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
    public static Fruit getFruit(String type) throws Exception {
        Class clazz = Class.forName(type);
        return (Fruit)clazz.newInstance();
    }
}


----------
public class MainClass {

    public static void main(String [] args){
//        没有工厂时
//        Apple apple = new Apple();
//        Banana banana = new Banana();
//        apple.get();
//        banana.get();
        
//        没有工厂时
//        Fruit apple = new Apple();
//        Fruit banana = new Apple();
//        apple.get();
//        banana.get();

//        存在工厂，利用第一种工厂方法创建
//        Fruit apple = FruitFactory.getApple();
//        Fruit banana = FruitFactory.getBanana();
//        apple.get();
//        banana.get();

//        存在工厂，利用第二种工厂方法创建
//        Fruit apple = FruitFactory.getFruit("apple");
//        Fruit banana = FruitFactory.getFruit("banana");
//        apple.get();
//        banana.get();

//        存在工厂，利用第三种工厂方法创建
        try {
            Fruit banana = FruitFactory.getFruit("com.wkh.Banana");
            Fruit apple = FruitFactory.getFruit("com.wkh.Apple");
            apple.get();
            banana.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

```

## 简单工厂模式的优缺点

 1. 工厂类是整个模式的关键，它包含必要的判断逻辑，能够根据外界给定的信息，决定究竟应该创建哪个具体类的对象。用户在使用时可以直接根据工厂类去创建所需的实例，而无需了解这些对象是如何创建和组织的，有利于整个软件体系结构的优化。
 2. 简单工厂模式的缺点也体现在工厂类上，由于工厂类集中了所有实例的创建逻辑，所以高内聚方面做得并不好。另外当系统中的具体产品类不断增多时，可能会出现要求工厂类也要做相应的修改，扩展性能不是很好（工厂类代码中的第三种创建方式可以很好解决这个问题）

