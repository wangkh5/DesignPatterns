package com.wkh.designpatterns.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangkaihua
 * @create 2018-04-04 17:43
 **/

public class Person implements Cloneable{

    private String  name;
    private int age;
    private String sex;
    private List<String> friends;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    @Override
    protected Person clone() throws CloneNotSupportedException {
//        浅度克隆
//        return (Person) super.clone();
//        深度克隆
        Person person = (Person)super.clone();
        List<String> friendList = new ArrayList<String>();
        for(String str:this.friends){
            friendList.add(str);
        }
        person.setFriends(friendList);
        return person;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", friends=" + friends +
                '}';
    }
}

