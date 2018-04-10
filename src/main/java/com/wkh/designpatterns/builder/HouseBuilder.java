package com.wkh.designpatterns.builder;

/**
 * @author wangkaihua
 * @create 2018-04-10 18:31
 **/
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