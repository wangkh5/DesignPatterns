package com.wkh.designpatterns.builder;

/**
 * @author wangkaihua
 * @create 2018-04-10 18:24
 **/
public class House {

    private String houseTop;
    private String wall;
    private String floor;

    public String getHouseTop() {
        return houseTop;
    }

    public void setHouseTop(String houseTop) {
        this.houseTop = houseTop;
    }

    public String getWall() {
        return wall;
    }

    public void setWall(String wall) {
        this.wall = wall;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    @Override
    public String toString() {
        return "House{" +
                "houseTop='" + houseTop + '\'' +
                ", wall='" + wall + '\'' +
                ", floor='" + floor + '\'' +
                '}';
    }
}
