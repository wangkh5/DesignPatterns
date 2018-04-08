# 抽象工厂模式

标签（空格分隔）： 设计模式

---

## 什么是抽象工厂模式
抽象工厂模式是所有形态的工厂模式中最为抽象和最具一般性的。抽象工厂模式可以向客户端提供一个接口，使得客户端在不必指定产品的具体类型的情况下，能够创建多个产品族的产品对象。

## 模式中包含的角色及其职责

 1. 抽象工厂角色：抽象工厂模式的核心，包含了对多个产品结构的声明，任何工厂类都必须实现这个接口。
 2. 具体工厂角色：具体工厂类是抽象工厂的一个实现，负责实例化某个产品族中的产品对象。
 3. 抽象角色：抽象模式所创建的所有对象的父类，它负责描述所有实例所共有的公共接口。
 4. 具体产品角色：抽象模式所创建的具体实例对象。
## 代码
```
interface Fruit{
    /**
     * 采摘水果方法
      */
    void get();
}

abstract class Apple implements Fruit{

}

abstract class Banana implements Fruit {

}

public class NorthApple extends Apple{
    public void get() {
        System.out.println("采摘北方苹果");
    }
}

public class NorthBanana extends Banana implements Fruit {
    public void  get() {
        System.out.println("采摘北方香蕉");
    }
}

public class SouthApple extends Apple{
    public void get() {
        System.out.println("采摘南方苹果");
    }
}

public class SouthBanana extends Apple{
    public void get() {
        System.out.println("采摘南方香蕉");
    }
}

interface FruitFactory{

    Fruit getApple();

    Fruit getBanana();

}

public class NorthFruitFactory implements FruitFactory{
    public Fruit getApple() {
        return new NorthApple();
    }

    public Fruit getBanana() {
        return new NorthBanana();
    }
}

public class SouthFruitFactory implements FruitFactory{
    public Fruit getApple() {
        return new SouthApple();
    }

    public Fruit getBanana() {
        return new SouthBanana();
    }
}
```

## 总结
抽象工厂中的方法对应产品结构。具体工厂对应产品族。

