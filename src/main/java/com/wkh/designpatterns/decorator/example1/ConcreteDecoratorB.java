package com.wkh.designpatterns.decorator.example1;

/**
 * 装饰器的具体实现对象，向组件对象添加职责
 *
 * @author wangkaihua
 * @create 2018-04-11 18:26
 **/
public class ConcreteDecoratorB extends Decorator{

    /**
     * 添加的状态
     * */
    private String addedState;

    /**
     * 构造方法，传入组件对象
     *
     * @param component 组件对象
     */
    public ConcreteDecoratorB(Component component) {
        super(component);
    }

    private String getAddedState() {
        return addedState;
    }

    private void setAddedState(String addedState) {
        this.addedState = addedState;
    }

    @Override
    public void operation() {
        //调用父类的方法，可以在调用前后执行一些附加动作
        //在这里处理的时候，可以使用附加的状态
        System.out.println("执行前增加的操作！");
        super.operation();
        System.out.println("执行后增加的操作！");
    }
}
