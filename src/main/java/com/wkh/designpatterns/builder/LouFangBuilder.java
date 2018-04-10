package com.wkh.designpatterns.builder;

/**
 * @author wangkaihua
 * @create 2018-04-10 18:44
 **/
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
