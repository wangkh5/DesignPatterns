package com.wkh.designpatterns.builder;

/**
 * @author wangkaihua
 * @create 2018-04-10 18:42
 **/
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
