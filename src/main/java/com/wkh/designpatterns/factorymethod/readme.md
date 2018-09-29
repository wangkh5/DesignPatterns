# 工厂方法模式

标签： 设计模式

---

## 什么是工厂方法模式
工厂方法模式属于类的创建型模式，又被称为多态工厂模式。工厂方法模式定义了一个创建产品对象的工厂接口，将实际创建工作推迟到子类中。核心工厂类不再负责产品的创建，这样核心类成为一个抽象工厂角色，仅负责具体工厂子类必须实现的接口，这样进一步抽象化的好处是使得工厂方法模式可以使系统在不修改具体工厂角色的情况下引进新的产品。

## 模式中包含的角色及其职责

 1. 抽象工厂角色：工厂方法模式的核心，任何工厂类都必须实现这个接口。
 2. 具体工厂角色：具体工厂类是抽象工厂的一个实现，负责实例化产品对象。
 3. 抽象角色：工厂方法模式所创建的所有对象的父类，它负责描述所有实例所共有的公共接口。
 4. 具体产品角色：工厂方法模式所创建的具体实例对象。

## 代码
```
interface Fruit{
    /**
     * 采摘水果方法
      */
    void get();
}

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

interface FruitFactory{

    Fruit getFruit();
}

public class AppleFactory implements FruitFactory {

    public Fruit getFruit() {
        return new Apple();
    }
}

public class BananaFactory implements FruitFactory {

    public Fruit getFruit() {
        return new Banana();
    }
}

<!--以下为扩展产品时增加的类-->
public class Pear implements Fruit {
    public void get() {
        System.out.println("采摘梨");
    }
}

public class PearFactory implements FruitFactory {
    public Fruit getFruit() {
        return new Pear();
    }
}


----------


public class MainClass {

    public static void main(String [] args){
        FruitFactory appleFactory = new AppleFactory();
        FruitFactory bananaFactory = new BananaFactory();
        Fruit apple = appleFactory.getFruit();
        Fruit fruit = bananaFactory.getFruit();
        apple.get();
        fruit.get();

        /**
         * 如果此时新增来了梨，如果使用抽象工厂模式我们需要修改具体的工厂类增加梨的创建逻辑（
         * 不使用简单工厂模式的第三种反射创建），这违反了开闭原则。
         * 如果我们使用工厂方法模式：
         *      1.新建Pear类
         *      2.新建PearFactory工厂
         *      3.利用PearFacory获取新产品梨对象
         */
        FruitFactory pearFactory = new PearFactory();
        Fruit pear = pearFactory.getFruit();
        pear.get();
    }
}
```

## 工厂方法模式和简单工厂模式比较

 1. 工厂方法模式的核心是一个抽象工厂类，而简单工厂模式把核心放在一个具体类上。
 2. 工厂方法模式之所以叫多态性工厂模式是因为具体工厂类都有共同的接口，或者有共同的抽象父类。
 3. 当系统扩展需要添加新的产品对象时，仅仅需要添加一个具体对象以及一个具体工厂对象，原有工厂对象不需要进行任何修改，也不需要修改客户端，很好的符合了“开放-封闭”原则。而简单工厂模式在添加新产品对象后不得不修改工厂方法，扩展性不好。

 
 
