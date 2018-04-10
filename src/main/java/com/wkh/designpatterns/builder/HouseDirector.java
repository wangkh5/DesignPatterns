package com.wkh.designpatterns.builder;

/**
 * @author wangkaihua
 * @create 2018-04-10 18:46
 **/
public class HouseDirector {
    private HouseBuilder houseBuilder;

    public HouseDirector(HouseBuilder houseBuilder) {
        this.houseBuilder = houseBuilder;
    }

    public void makeHouse(){
        houseBuilder.makeHouse();
    }
}
