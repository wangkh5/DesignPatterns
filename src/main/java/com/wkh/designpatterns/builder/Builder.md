# 建造者模式

标签（空格分隔）： 设计模式

---
## 什么是建造者模式
Builder模式，建造者模式也叫生成器模式，是GoF提出的23种设计模式中的一种，是一种对象的创建型模式，用来隐藏复合对象的创建过程，它把复合对象的创建过程加以抽象，通过子类继承和重载的方式，动态地创建具有复合属性的对象。

## 代码
```
public class House {
    private String houseTop;
    private String wall;
    private String floor;
}

//public class HouseBuilder {
//
//    private House house = new House();
//
//    public void setFloor(){
//        house.setFloor("修建平房地板");
//    }
//    public void setWall(){
//        house.setWall("修建平房墙");
//    }
//    public void setHouseStop(){
//        house.setHouseTop("修建平房屋顶");
//    }
//
//    public House getHouse() {
//        return house;
//    }
//}

public interface HouseBuilder{
    public void makeHouse();
    public House getHouse();
}

public class LouFangBuilder implements HouseBuilder{
    private House house = new House();

    public void makeHouse() {
        house.setFloor("修建楼房地板");
        house.setWall("修建楼房墙");
        house.setHouseTop("修建楼房屋顶");
    }

    public House getHouse() {
        return house;
    }
}

public class PingFangBuilder implements HouseBuilder{

    private House house = new House();

    public void makeHouse() {
        house.setFloor("修建平房地板");
        house.setWall("修建平房墙");
        house.setHouseTop("修建平房屋顶");
    }

    public House getHouse() {
        return house;
    }
}

public class MainClass {

    public static void main(String [] args){
        //建造房子方式一，用户直接操作房子对象在建造
        House house = new House();
        house.setFloor("建造平房地板");
        house.setWall("建造平房墙");
        house.setHouseTop("建造平房屋顶");

        //我们引入工程队来建造
//        HouseBuilder houseBuilder = new HouseBuilder();
//        houseBuilder.setFloor();
//        houseBuilder.setHouseStop();
//        houseBuilder.setWall();
//        House house1 = houseBuilder.getHouse();

//        System.out.println(house);
//        System.out.println(house1);

        /**
         * 此时我们指挥工程队来建造房子，此时如果我们又想修建楼房那么我们又需要找楼房工程队
         * 我们引入设计师来建造房子，并封装了工程队修建房子的细节
         */
        //建造楼房工程队
        HouseBuilder louFangBuilder = new LouFangBuilder();
        //建造平房工程队
        HouseBuilder pingFangBuilder = new PingFangBuilder();

        HouseDirector houseDirector = new HouseDirector(louFangBuilder);
        houseDirector.makeHouse();
        House house2 = louFangBuilder.getHouse();

        System.out.println(house);
        System.out.println(house2);
    }

}
```

## 建造者模式应用场景

 1. 对象的创建：Builder模式是为对象的创建而设计的模式。
 2. 创建的是一个复合对象：被创建的对象为一个具有复合属性的复合对象
 3. 关注对象创建的各部分的创建过程：不同的工厂（指builder生成器）对产品属性有不同的创建方法。

