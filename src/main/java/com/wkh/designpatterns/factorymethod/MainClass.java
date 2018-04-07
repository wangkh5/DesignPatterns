package com.wkh.designpatterns.factorymethod;

/**
 * @author wangkaihua
 * @create 2018-04-04 17:43
 **/

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

