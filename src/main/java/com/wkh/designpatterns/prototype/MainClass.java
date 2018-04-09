package com.wkh.designpatterns.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangkaihua
 * @create 2018-04-04 17:43
 **/

public class MainClass {

    public static void main(String [] args){
        try {
            Person person1 = new Person();
            person1.setName("张三");
            person1.setAge(20);
            person1.setSex("男");
            List<String> friends1 = new ArrayList<String>();
            friends1.add("lihua");
            friends1.add("daniu");
            friends1.add("wangmazi");
            person1.setFriends(friends1);

            Person person2 = new Person();
            person2.setName("李四");
            person2.setAge(20);
            person2.setSex("男");
            List<String> friends2 = new ArrayList<String>();
            friends2.add("lihua");
            friends2.add("daniu");
            friends2.add("wangmazi");
            person2.setFriends(friends2);
            /**
             *  我们可以看到person1 和 person2 只有名称不相同
             *  下面我们使用原型对象创建
             */

            Person person3 = person1.clone();
            person3.setName("李四");

//            System.out.println(person1);
//            System.out.println(person2);
//            System.out.println(person3);

            /**
             * 上面示范只是浅度克隆只是把friends的引用赋值给了persons
             * 下面示范深度克隆，更改person类中的clone方法
             * 此时在克隆出来的person4对象的friends属性中新增一个好友变为四个，原来的对象不变还是三个。
             */
            Person person4 = person1.clone();
            person4.getFriends().add("wangwu");
            System.out.println(person1);
            System.out.println(person4);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}

