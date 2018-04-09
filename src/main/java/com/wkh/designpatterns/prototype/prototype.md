# 原型模式

标签（空格分隔）： 设计模式

---

## 什么是原型模式
Prototype模式是一种对象创建型模式，它采取复制原型对象的方法来创建对象的实例。使用Prototype模式创建的实例，具有与原型一样的数据。

## 原型模式的特点

 1. 由原型对象自身创建目标对象。也就是说对象创建这一个动作发自原型对象本身。
 2. 目标对象是原型对象的一个克隆。通过原型模式创建的对象不仅与原型对象具有相同的结构，还与原型对象具有相同的值。
 3. 根据对象克隆深度层次的不同，分为浅度克隆和深度克隆。

## 代码
```
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

```

 ## 原型模式应用场景
 1. 在创建对象的时候，我们不只是希望被创建的对象继承其基类的基本结构，还希望继承原型对象的数据。
 2. 希望对目标对象的修改不影响既有的原型对象（深度克隆可以互不影响）
 3. 隐藏克隆操作的细节。很多时候，对对象本身的克隆需要涉及到类本身的数据细节。
 
