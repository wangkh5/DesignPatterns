package com.wkh.designpatterns.builder;

/**
 * @author wangkaihua
 * @create 2018-04-04 17:43
 **/

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

