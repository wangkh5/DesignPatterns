# 迭代器模式

标签： 设计模式

---

## 初识迭代器模式
### 定义

> 提供一种方法顺序访问一个聚合对象中各个元素，而又不需暴露该对象的内部表示 。

### 结构和说明
 ![image_1cihcf6941lqc1li04991k4fko9.png-64.9kB][1]  
Iterator：
迭代器接口。定义访问和遍历元素的接口。
ConcreteIterator：
具体的迭代器实现对象。实现对聚合对象的遍历，并跟踪遍历时的当前位置。
Aggregate：
聚合对象。定义创建相应迭代器对象的接口。
ConcreteAggregate:
具体聚合对象。实现创建相应的迭代器对象。

```
/**
 * 迭代器接口，定义访问和遍历元素的操作
 */
public interface Iterator {
	/**
	 * 移动到聚合对象的第一个位置
	 */
	public void first();
	/**
	 * 移动到聚合对象的下一个位置
	 */
	public void next();
	/**
	 * 判断是否已经移动聚合对象的最后一个位置
	 * @return true表示已经移动到聚合对象的最后一个位置，
	 *         false表示还没有移动到聚合对象的最后一个位置
	 */
	public boolean isDone();
	/**
	 * 获取迭代的当前元素
	 * @return 迭代的当前元素
	 */
	public Object currentItem();
}


----------
/**
 * 具体迭代器实现对象，示意的是聚合对象为数组的迭代器
 * 不同的聚合对象相应的迭代器实现是不一样的
 */
public class ConcreteIterator implements Iterator {
	/**
	 * 持有被迭代的具体的聚合对象
	 */
	private ConcreteAggregate aggregate;
	/**
	 * 内部索引，记录当前迭代到的索引位置。
	 * -1表示刚开始的时候，迭代器指向聚合对象第一个对象之前
	 */
	private int index = -1;
	/**
	 * 构造方法，传入被迭代的具体的聚合对象
	 * @param aggregate 被迭代的具体的聚合对象
	 */
	public ConcreteIterator(ConcreteAggregate aggregate) {
		this.aggregate = aggregate;
	}

	public void first(){
		index = 0;
	}
	public void next(){
		if(index < this.aggregate.size()){
			index = index + 1;
		}
	}
	public boolean isDone(){
		if(index == this.aggregate.size()){
			return true;
		}
		return false;
	}
	public Object currentItem(){
		return this.aggregate.get(index);
	}
}


----------
/**
 * 聚合对象的接口，定义创建相应迭代器对象的接口
 */
public abstract class Aggregate {
	/**
	 * 工厂方法，创建相应迭代器对象的接口
	 * @return 相应迭代器对象的接口
	 */
	public abstract Iterator createIterator();
}


----------
/**
 * 具体的聚合对象，实现创建相应迭代器对象的功能
 */
public class ConcreteAggregate extends Aggregate {
	/**
	 * 示意，表示聚合对象具体的内容
	 */
	private String[] ss = null;
	
	/**
	 * 构造方法，传入聚合对象具体的内容
	 * @param ss 聚合对象具体的内容
	 */
	public ConcreteAggregate(String[] ss){
		this.ss = ss;
	}
	
	public Iterator createIterator() {
		//实现创建Iterator的工厂方法
		return new ConcreteIterator(this);
	}
	/**
	 * 获取索引所对应的元素
	 * @param index 索引
	 * @return 索引所对应的元素
	 */
	public Object get(int index){
		Object retObj = null;
		if(index < ss.length){
			retObj = ss[index];
		}
		return retObj;
	}
	/**
	 * 获取聚合对象的大小
	 * @return 聚合对象的大小
	 */
	public int size(){
		return this.ss.length;
	}
}


----------
public class Client {
	/**
	 * 示意方法，使用迭代器的功能。
	 * 这里示意使用迭代器来迭代聚合对象
	 */
	public void someOperation(){
		String[] names = {"张三","李四","王五"};
		//创建聚合对象
		Aggregate aggregate = new ConcreteAggregate(names);
		//循环输出聚合对象中的值
		Iterator it = aggregate.createIterator();
		//首先设置迭代器到第一个元素
		it.first();
		while(!it.isDone()){
			//取出当前的元素来
			Object obj = it.currentItem();
			System.out.println("the obj=="+obj);
			//如果还没有迭代到最后，那么就向下迭代一个
			it.next();
		}
	}	
	public static void main(String[] args) {
		//可以简单的测试一下
		Client client = new Client();
		client.someOperation();
	}
}

```

## 体会迭代器模式

### 工资表数据的整合 

这个项目的背景是这样的，项目的客户方收购了一家小公司，这家小公司有自己的工资系统，现在需要整合到客户方已有的工资系统上。
现在除了要把两个工资系统整合起来外，老板还希望能够通过决策辅助系统来统一查看工资数据，他不想看到两份不同的工资表。那么应该如何实现呢？

```
/**
 * 工资描述模型对象
 */
public class PayModel {
	/**
	 * 支付工资的人员
	 */
	private String userName;
	/**
	 * 支付的工资数额
	 */
	private double pay;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public double getPay() {
		return pay;
	}
	public void setPay(double pay) {
		this.pay = pay;
	}
	public String toString(){
		return "userName="+userName+",pay="+pay;
	}
}


----------
/**
 * 客户方已有的工资管理对象
 */
public class PayManager{
	/**
	 * 聚合对象，这里是Java的集合对象
	 */
	private List list = new ArrayList();
	/**
	 * 获取工资列表
	 * @return 工资列表
	 */
	public List getPayList(){
		return list;
	}
	/**
	 * 计算工资，其实应该有很多参数，为了演示从简
	 */
	public void calcPay(){
		//计算工资，并把工资信息填充到工资列表里面
		//为了测试，做点假数据进去
		PayModel pm1 = new PayModel();
		pm1.setPay(3800);
		pm1.setUserName("张三");
		
		PayModel pm2 = new PayModel();
		pm2.setPay(5800);
		pm2.setUserName("李四");
		
		list.add(pm1);
		list.add(pm2);
	}
}


----------
/**
 * 被客户方收购的那个公司的工资管理类
 */
public class SalaryManager{
	/**
	 * 用数组管理
	 */
	private PayModel[] pms = null;
	/**
	 * 获取工资列表
	 * @return 工资列表
	 */
	public PayModel[] getPays(){
		return pms;
	}
	/**
	 * 计算工资，其实应该有很多参数，为了演示从简
	 */
	public void calcSalary(){
		//计算工资，并把工资信息填充到工资列表里面
		//为了测试，做点假数据进去
		PayModel pm1 = new PayModel();
		pm1.setPay(2200);
		pm1.setUserName("王五");
		
		PayModel pm2 = new PayModel();
		pm2.setPay(3600);
		pm2.setUserName("赵六");
		
		pms = new PayModel[2];
		pms[0] = pm1;
		pms[1] = pm2;
	}
}


----------
public class Client {
	public static void main(String[] args) {
		//访问集团的工资列表
		PayManager payManager= new PayManager();
		//先计算再获取
		payManager.calcPay();
		Collection payList = payManager.getPayList();
		Iterator it = payList.iterator();
		System.out.println("集团工资列表：");
		while(it.hasNext()){
			PayModel pm = (PayModel)it.next();
			System.out.println(pm);
		}
		
		//访问新收购公司的工资列表
		SalaryManager salaryManager = new SalaryManager();
		//先计算再获取
		salaryManager.calcSalary();
		PayModel[] pms = salaryManager.getPays();
		System.out.println("新收购的公司工资列表：");
		for(int i=0;i<pms.length;i++){
			System.out.println(pms[i]);
		}
	}
}

```


### 有何问题

本来就算内部描述形式不一样，只要不需要整合在一起，两个系统单独输出自己的工资表，是没有什么问题的。但是，老板还希望能够以一个统一的方式来查看所有的工资数据，也就是说从外部看起来，两个系统输出的工资表应该是一样的。
经过分析，要满足老板的要求，而且要让两边的系统改动都尽可能的小的话，问题的核心就在于如何能够以一种统一的方式来提供工资数据给决策辅助系统，换句说来说就是： 如何能够以一个统一的方式来访问内部实现不同的聚合对象。
 
### 使用模式的解决方案的类图

![image_1cihcggpek4a1iikh5ejje1mh8m.png-166.2kB][2]   

```
/**
 * 工资描述模型对象
 */
public class PayModel {
	/**
	 * 支付工资的人员
	 */
	private String userName;
	/**
	 * 支付的工资数额
	 */
	private double pay;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public double getPay() {
		return pay;
	}
	public void setPay(double pay) {
		this.pay = pay;
	}
	public String toString(){
		return "userName="+userName+",pay="+pay;
	}
}


----------


/**
 * 迭代器接口，定义访问和遍历元素的操作
 */
public interface Iterator {
	/**
	 * 移动到聚合对象的第一个位置
	 */
	public void first();
	/**
	 * 移动到聚合对象的下一个位置
	 */
	public void next();
	/**
	 * 判断是否已经移动聚合对象的最后一个位置
	 * @return true表示已经移动聚合对象的最后一个位置，
	 *         false表示还没有移动到聚合对象的最后一个位置
	 */
	public boolean isDone();
	/**
	 * 获取迭代的当前元素
	 * @return 迭代的当前元素
	 */
	public Object currentItem();
}


----------
/**
 * 用来实现访问数组的迭代接口
 */
public class ArrayIteratorImpl implements Iterator{
	/**
	 * 用来存放被迭代的聚合对象
	 */
	private SalaryManager aggregate = null;
	/**
	 * 用来记录当前迭代到的位置索引
	 * -1表示刚开始的时候，迭代器指向聚合对象第一个对象之前
	 */
	private int index = -1;
	
	public ArrayIteratorImpl(SalaryManager aggregate){
		this.aggregate = aggregate;
	}
	
	
	public void first(){
		index = 0;
	}
	public void next(){
		if(index < this.aggregate.size()){
			index = index + 1;
		}
	}
	public boolean isDone(){
		if(index == this.aggregate.size()){
			return true;
		}
		return false;
	}
	public Object currentItem(){
		return this.aggregate.get(index);
	}
}


----------
/**
 * 用来实现访问Collection集合的迭代接口，为了外部统一访问方式
 */
public class CollectionIteratorImpl implements Iterator {
	/**
	 * 用来存放被迭代的聚合对象
	 */
	private PayManager aggregate = null;
	/**
	 * 用来记录当前迭代到的位置索引
	 * -1表示刚开始的时候，迭代器指向聚合对象第一个对象之前
	 */
	private int index = -1;
	
	public CollectionIteratorImpl(PayManager aggregate){
		this.aggregate = aggregate;
	}
	
	public void first(){
		index = 0;
	}
	public void next(){
		if(index < this.aggregate.size()){
			index = index + 1;
		}
	}
	public boolean isDone(){
		if(index == this.aggregate.size()){
			return true;
		}
		return false;
	}
	public Object currentItem(){
		return this.aggregate.get(index);
	}
}


----------

/**
 * 聚合对象的接口，定义创建相应迭代器对象的接口
 */
public abstract class Aggregate {
	/**
	 * 工厂方法，创建相应迭代器对象的接口
	 * @return 相应迭代器对象的接口
	 */
	public abstract Iterator createIterator();
}


----------

/**
 * 客户方已有的工资管理对象
 */
public class PayManager extends Aggregate{
	/**
	 * 聚合对象，这里是Java的集合对象
	 */
	private List list = new ArrayList();
	/**
	 * 获取工资列表
	 * @return 工资列表
	 */
	public List getPayList(){
		return list;
	}
	/**
	 * 计算工资，其实应该有很多参数，为了演示从简
	 */
	public void calcPay(){
		//计算工资，并把工资信息填充到工资列表里面
		//为了测试，做点假数据进去
		PayModel pm1 = new PayModel();
		pm1.setPay(3800);
		pm1.setUserName("张三");
		
		PayModel pm2 = new PayModel();
		pm2.setPay(5800);
		pm2.setUserName("李四");
		
		list.add(pm1);
		list.add(pm2);
	}
	
	public Iterator createIterator(){
		return new CollectionIteratorImpl(this);
	}
	public Object get(int index){
		Object retObj = null;
		if(index < this.list.size()){
			retObj = this.list.get(index);
		}
		return retObj;
	}
	public int size(){
		return this.list.size();
	}
}


----------
/**
 * 被客户方收购的那个公司的工资管理类
 */
public class SalaryManager extends Aggregate{
	/**
	 * 用数组管理
	 */
	private PayModel[] pms = null;
	/**
	 * 获取工资列表
	 * @return 工资列表
	 */
	public PayModel[] getPays(){
		return pms;
	}
	/**
	 * 计算工资，其实应该有很多参数，为了演示从简
	 */
	public void calcSalary(){
		//计算工资，并把工资信息填充到工资列表里面
		//为了测试，做点假数据进去
		PayModel pm1 = new PayModel();
		pm1.setPay(2200);
		pm1.setUserName("王五");
		
		PayModel pm2 = new PayModel();
		pm2.setPay(3600);
		pm2.setUserName("赵六");
		
		pms = new PayModel[2];
		pms[0] = pm1;
		pms[1] = pm2;
	}
	
	public Iterator createIterator(){
		return new ArrayIteratorImpl(this);
	}
	public Object get(int index){
		Object retObj = null;
		if(index < pms.length){
			retObj = pms[index];
		}
		return retObj;
	}
	public int size(){
		return this.pms.length;
	}
}


----------
public class Client {
	public static void main(String[] args) {
		//访问集团的工资列表
		PayManager payManager= new PayManager();
		//先计算再获取
		payManager.calcPay();
		System.out.println("集团工资列表：");
//		test(payManager.createIterator());
		
		//访问新收购公司的工资列表
		SalaryManager salaryManager = new SalaryManager();
		//先计算再获取
		salaryManager.calcSalary();
		System.out.println("新收购的公司工资列表：");
		test(salaryManager.createIterator());
	}
	/**
	 * 测试通过访问聚合对象的迭代器，是否能正常访问聚合对象
	 * @param it 聚合对象的迭代器
	 */
	private static void test(Iterator it){
		//循环输出聚合对象中的值
		//首先设置迭代器到第一个元素
		it.first();
		while(!it.isDone()){
			//取出当前的元素来
			Object obj = it.currentItem();
			System.out.println("the obj=="+obj);
			//如果还没有迭代到最后，那么就向下迭代一个
			it.next();
		}
	}
}


```


## 理解迭代器模式

### 认识迭代器模式

1：迭代器模式的功能
迭代器模式的功能主要在于提供对聚合对象的迭代访问。迭代器就围绕着这个“访问”做文章，延伸出很多的功能来。比如：
（1）以不同的方式遍历聚合对象，比如向前、向后等
（2）对同一个聚合同时进行多个遍历
（3）以不同的遍历策略来遍历聚合，比如是否需要过滤等
（4）多态迭代，含义是：为不同的聚合结构，提供统一的迭代接口，也就是说通过一个迭代接口可以访问不同的聚合结构，这就叫做多态迭代。上面的示例就已经实现了多态迭代，事实上，标准的迭代模式实现基本上都是支持多态迭代的。
但是请注意：多态迭代可能会带来类型安全的问题，可以考虑使用泛型

2：迭代器模式的关键思想
聚合对象的类型很多，如果对聚合对象的迭代访问跟聚合对象本身融合在一起的话，会严重影响到聚合对象的可扩展性和可维护性。
因此 迭代器模式的关键思想就是把对聚合对象的遍历和访问从聚合对象中分离出 来，放入到单独的迭代器中，这样聚合对象会变得简单一些；而且迭代器和聚合对象可以独立的变化和发展，会大大加强系统的灵活性。

3：内部迭代器和外部迭代器 
所谓内部迭代器，指的是由迭代器自己来控制迭代下一个元素的步骤，客户端无法干预，因此，如果想要在迭代的过程中完成工作的话，客户端就需要把操作传给迭代器，迭代器在迭代的时候会在每个元素上执行这个操作，类似于Java的回调机制。
所谓外部迭代器，指的是由客户端来控制迭代下一个元素的步骤，像前面的示例一样，客户端必须显示的调用next来迭代下一个元素。
总体来说外部迭代器比内部迭代器要灵活一些，因此我们常见的实现多属于外部迭代器，前面的例子也是实现的外部迭代器。

4：Java中最简单的统一访问聚合的方式
如果只是想要使用一种统一的访问方式来访问聚合对象，在Java中有更简单的方式，简单到几乎什么都不用做，利用Java 5以上版本本身的特性即可。
但是请注意，这只是从访问形式上一致了，但是也暴露了聚合的内部实现，因此并不能算是标准迭代器模式的实现，但是从某种意义上说，可以算是隐含的实现了部分迭代器模式的功能。
 
 
### 使用Java的迭代器
大家都知道，在java.util包里面有一个Iterator的接口，在Java中实现迭代器模式是非常简单的，而且java的集合框架中的聚合对象，基本上都是提供了迭代器的。
下面就来把前面的例子改成用Java中的迭代器实现，一起来看看有些什么改变。
1：不再需要自己实现的Iterator接口，直接实现java.util.Iterator接口就可以了，所有使用自己实现的Iterator接口的地方都需要修改过来
2：Java中Iterator接口跟前面自己定义的接口相比，需要实现的方法是不一样的
3：集合已经提供了Iterator，那么CollectionIteratorImpl类就不需要了，直接去掉

```
/**
 * 聚合对象的接口，定义创建相应迭代器对象的接口
 */
public abstract class Aggregate {
	/**
	 * 工厂方法，创建相应迭代器对象的接口
	 * @return 相应迭代器对象的接口
	 */
	public abstract Iterator createIterator();
}


----------
/**
 * 客户方已有的工资管理对象
 */
public class PayManager extends Aggregate{
	private List<PayModel> list = new ArrayList<PayModel>();
	/**
	 * 获取工资列表
	 * @return 工资列表
	 */
	public List<PayModel> getPayList(){
		return list;
	}
	/**
	 * 计算工资，其实应该有很多参数，为了演示从简
	 */
	public void calcPay(){
		//计算工资，并把工资信息填充到工资列表里面
		//为了测试，做点假数据进去
		PayModel pm1 = new PayModel();
		pm1.setPay(3800);
		pm1.setUserName("张三");
		
		PayModel pm2 = new PayModel();
		pm2.setPay(5800);
		pm2.setUserName("李四");
		
		list.add(pm1);
		list.add(pm2);
	}
	
	public Iterator createIterator() {
		return list.iterator();
	}
}


----------
/**
 * 被客户方收购的那个公司的工资管理类
 */
public class SalaryManager extends Aggregate{
	/**
	 * 用数组管理
	 */
	private PayModel[] pms = null;
	/**
	 * 获取工资列表
	 * @return 工资列表
	 */
	public PayModel[] getPays(){
		return pms;
	}
	/**
	 * 计算工资，其实应该有很多参数，为了演示从简
	 */
	public void calcSalary(){
		//计算工资，并把工资信息填充到工资列表里面
		//为了测试，做点假数据进去
		PayModel pm1 = new PayModel();
		pm1.setPay(2200);
		pm1.setUserName("王五");
		
		PayModel pm2 = new PayModel();
		pm2.setPay(3600);
		pm2.setUserName("赵六");
		
		pms = new PayModel[2];
		pms[0] = pm1;
		pms[1] = pm2;
	}
	
	public Iterator createIterator(){
		return new ArrayIteratorImpl(this);
	}
	public Object get(int index){
		Object retObj = null;
		if(index < pms.length){
			retObj = pms[index];
		}
		return retObj;
	}
	public int size(){
		return this.pms.length;
	}
}


----------
import java.util.Iterator;
/**
 * 用来实现访问数组的迭代接口
 */
public class ArrayIteratorImpl implements Iterator{
	/**
	 * 用来存放被迭代的聚合对象
	 */
	private SalaryManager aggregate = null;
	/**
	 * 用来记录当前迭代到的位置索引
	 */
	private int index = 0;
	
	public ArrayIteratorImpl(SalaryManager aggregate){
		this.aggregate = aggregate;
	}
	
	
	public boolean hasNext() {
		//判断是否还有下一个元素
		if(aggregate!=null && index<aggregate.size()){
			return true;
		}
		return false;
	}

	
	public Object next() {
		Object retObj = null;
		if(hasNext()){
			retObj = aggregate.get(index);
			//每取走一个值，就把已访问索引加1
			index++;
		}
		return retObj;
	}

	
	public void remove() {
		//暂时可以不实现		
	}
}


----------
public class Client {
	public static void main(String[] args) {
		//访问集团的工资列表
		PayManager payManager= new PayManager();
		//先计算再获取
		payManager.calcPay();
		System.out.println("集团工资列表：");
		test(payManager.createIterator());
		//访问新收购公司的工资列表
		SalaryManager salaryManager = new SalaryManager();
		//先计算再获取
		salaryManager.calcSalary();
		System.out.println("新收购的公司工资列表：");
		test(salaryManager.createIterator());
	}
	/**
	 * 测试通过访问聚合对象的迭代器，是否能正常访问聚合对象
	 * @param it 聚合对象的迭代器
	 */
	private static void test(Iterator it){
		while(it.hasNext()){
			PayModel pm = (PayModel)it.next();
			System.out.println(pm);
		}
	}
}

```

 
### 带迭代策略的迭代器

由于迭代器模式把聚合对象和访问聚合的机制实现了分离，所以可以在迭代器上实现不同的迭代策略，最为典型的就是实现过滤功能的迭代器。
在实际开发中，对于经常被访问的一些数据可以使用缓存，把这些数据存放在内存中。但是不同的业务功能需要访问的数据是不同的，还有不同的业务访问权限能访问的数据也是不同的，对于这种情况，就可以使用实现过滤功能的迭代器，让不同功能使用不同的迭代器来访问。当然，这种情况也可以结合策略模式来实现。

在实现过滤功能的迭代器中，又有两种常见的需要过滤的情况，一种是对数据进行整条过滤，比如只能查看自己部门的数据；另外一种情况是对数据进行部分过滤，比如某些人不能查看工资数据。
```
/**
 * 聚合对象的接口，定义创建相应迭代器对象的接口
 */
public abstract class Aggregate {
	/**
	 * 工厂方法，创建相应迭代器对象的接口
	 * @return 相应迭代器对象的接口
	 */
	public abstract Iterator createIterator();
}


----------
/**
 * 客户方已有的工资管理对象
 */
public class PayManager extends Aggregate{
	private List<PayModel> list = new ArrayList<PayModel>();
	/**
	 * 获取工资列表
	 * @return 工资列表
	 */
	public List<PayModel> getPayList(){
		return list;
	}
	/**
	 * 计算工资，其实应该有很多参数，为了演示从简
	 */
	public void calcPay(){
		//计算工资，并把工资信息填充到工资列表里面
		//为了测试，做点假数据进去
		PayModel pm1 = new PayModel();
		pm1.setPay(3800);
		pm1.setUserName("张三");
		
		PayModel pm2 = new PayModel();
		pm2.setPay(5800);
		pm2.setUserName("李四");
		
		list.add(pm1);
		list.add(pm2);
	}
	
	public Iterator createIterator() {
		return list.iterator();
	}
}


----------
/**
 * 被客户方收购的那个公司的工资管理类
 */
public class SalaryManager extends Aggregate{
	/**
	 * 用数组管理
	 */
	private PayModel[] pms = null;
	/**
	 * 获取工资列表
	 * @return 工资列表
	 */
	public PayModel[] getPays(){
		return pms;
	}
	/**
	 * 计算工资，其实应该有很多参数，为了演示从简
	 */
	public void calcSalary(){
		//计算工资，并把工资信息填充到工资列表里面
		//为了测试，做点假数据进去
		PayModel pm1 = new PayModel();
		pm1.setPay(2200);
		pm1.setUserName("王五");
		
		PayModel pm2 = new PayModel();
		pm2.setPay(3600);
		pm2.setUserName("赵六");
		
		pms = new PayModel[2];
		pms[0] = pm1;
		pms[1] = pm2;
	}
	
	public Iterator createIterator(){
		return new ArrayIteratorImpl(this);
	}
	public Object get(int index){
		Object retObj = null;
		if(index < pms.length){
			retObj = pms[index];
		}
		return retObj;
	}
	public int size(){
		return this.pms.length;
	}
}


----------
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * 用来实现访问数组的迭代接口,加入了迭代策略
 */
public class ArrayIteratorImpl implements Iterator{
	/**
	 * 用来存放被迭代的数组
	 */
	private PayModel[] pms = null;
	/**
	 * 用来记录当前迭代到的位置索引
	 */
	private int index = 0;
	
	public ArrayIteratorImpl(SalaryManager aggregate){
		//在这里先对聚合对象的数据进行过滤，比如工资必须在3000以下
		Collection<PayModel> tempCol = new ArrayList<PayModel>();
		for(PayModel pm : aggregate.getPays()){
			if(pm.getPay() < 3000){
				tempCol.add(pm);
			}
		}
		//然后把符合要求的数据存放到用来迭代的数组
		this.pms = new PayModel[tempCol.size()];
		int i=0;
		for(PayModel pm : tempCol){
			this.pms[i] = pm;
			i++;
		}
	}
	
	
	public boolean hasNext() {
		//判断是否还有下一个元素
		if(pms!=null && index<=(pms.length-1)){
			return true;
		}
		return false;
	}

	
	public Object next() {
		Object retObj = null;
		if(hasNext()){
			retObj = pms[index];
			//每取走一个值，就把已访问索引加1
			index++;
		}
		
		//在这里对要返回的数据进行过滤，比如不让查看工资数据
		((PayModel)retObj).setPay(0.0);
		
		return retObj;
	}

	
	public void remove() {
		//暂时可以不实现		
	}
}


----------
public class Client {
	public static void main(String[] args) {
		//访问集团的工资列表
		PayManager payManager= new PayManager();
		//先计算再获取
		payManager.calcPay();
		System.out.println("集团工资列表：");
		test(payManager.createIterator());
		//访问新收购公司的工资列表
		SalaryManager salaryManager = new SalaryManager();
		//先计算再获取
		salaryManager.calcSalary();
		System.out.println("新收购的公司工资列表：");
		test(salaryManager.createIterator());
	}
	/**
	 * 测试通过访问聚合对象的迭代器，是否能正常访问聚合对象
	 * @param it 聚合对象的迭代器
	 */
	private static void test(Iterator it){
		while(it.hasNext()){
			PayModel pm = (PayModel)it.next();
			System.out.println(pm);
		}
	}
}

```


### 谁定义遍历算法的问题
在迭代器模式的实现中，常见有两个地方可以来定义遍历算法，一个就是聚合对象本身，另外一个就是迭代器负责遍历算法。
在聚合对象本身定义遍历的算法这种情况下，通常会在遍历过程中，用迭代器来存储当前迭代的状态，这种迭代器被称为游标，因为它仅用来指示当前的位置。
在迭代器里面定义遍历算法，会易于在相同的聚合上使用不同的迭代算法，同时也易于在不同的聚合上重用相同的算法。比如上面带策略的迭代器的示例，迭代器把需要迭代的数据从聚合对象中取出并存放到自己对象里面，然后再迭代自己的数据，这样一来，除了刚开始创建迭代器的时候需要访问聚合对象外，真正迭代过程已经跟聚合对象无关了。


### 双向迭代器
所谓双向迭代器的意思就是：可以同时向前和向后遍历数据的迭代器。
在Java util包中的ListIterator接口就是一个双向迭代器的示例，当然自己实现双向迭代器也非常容易，只要在自己的Iterator接口中添加向前的判断和向前获取值的方法，然后在实现中实现即可。

```
/**
 * 迭代器接口，定义访问和遍历元素的操作，实现双向迭代
 */
public interface Iterator {
	/**
	 * 移动到聚合对象的第一个位置
	 */
	public void first();
	/**
	 * 移动到聚合对象的下一个位置
	 */
	public void next();
	/**
	 * 判断是否已经移动聚合对象的最后一个位置
	 * @return true表示已经移动聚合对象的最后一个位置，
	 *         false表示还没有移动到聚合对象的最后一个位置
	 */
	public boolean isDone();
	/**
	 * 获取迭代的当前元素
	 * @return 迭代的当前元素
	 */
	public Object currentItem();
	
	
	/**
	 * 判断是否为第一个元素
	 * @return 如果为第一个元素，返回true，否则返回false
	 */
	public boolean isFirst();
	/**
	 * 移动到聚合对象的上一个位置
	 */
	public void previous();
}


----------
/**
 * 用来实现访问数组的双向迭代接口
 */
public class ArrayIteratorImpl implements Iterator{
	/**
	 * 用来存放被迭代的聚合对象
	 */
	private SalaryManager aggregate = null;
	/**
	 * 用来记录当前迭代到的位置索引
	 * -1表示刚开始的时候，迭代器指向聚合对象第一个对象之前
	 */
	private int index = -1;
	
	public ArrayIteratorImpl(SalaryManager aggregate){
		this.aggregate = aggregate;
	}	
	
	public void first(){
		index = 0;
	}
	public void next(){
		if(index < this.aggregate.size()){
			index = index + 1;
		}
	}
	public boolean isDone(){
		if(index == this.aggregate.size()){
			return true;
		}
		return false;
	}
	public Object currentItem(){
		return this.aggregate.get(index);
	}
	
	public boolean isFirst(){
		if(index==0){
			return true;
		}
		return false;
	}
	public void previous(){
		if(index > 0 ){
			index = index - 1;
		}
	}
}


----------
/**
 * 收购的那个公司的工资管理类
 */
public class SalaryManager{
	/**
	 * 用数组管理
	 */
	private PayModel[] pms = null;
	/**
	 * 获取工资列表
	 * @return 工资列表
	 */
	public PayModel[] getPays(){
		return pms;
	}
	/**
	 * 计算工资，其实应该有很多参数，为了演示从简
	 */
	public void calcSalary(){
		//计算工资，并把工资信息填充到工资列表里面
		//为了测试，做点假数据进去
		PayModel pm1 = new PayModel();
		pm1.setPay(2200);
		pm1.setUserName("王五");
		
		PayModel pm2 = new PayModel();
		pm2.setPay(3600);
		pm2.setUserName("赵六");
		
		pms = new PayModel[2];
		pms[0] = pm1;
		pms[1] = pm2;
	}

	public Iterator createIterator(){
		return new ArrayIteratorImpl(this);
	}
	public Object get(int index){
		Object retObj = null;
		if(index < pms.length){
			retObj = pms[index];
		}
		return retObj;
	}
	public int size(){
		return this.pms.length;
	}
}


----------
public class Client {
	public static void main(String[] args) {
		//访问新收购公司的工资列表
		SalaryManager salaryManager = new SalaryManager();
		//先计算再获取
		salaryManager.calcSalary();
		
		//得到双向迭代器
		Iterator it = salaryManager.createIterator();
		//首先设置迭代器到第一个元素
		it.first();
		
		//先next一个
		if(!it.isDone()){
			PayModel pm = (PayModel)it.currentItem();
			System.out.println("next1 == "+pm);
			//向下迭代一个
			it.next();
		}
		//然后previous一个
		if(!it.isFirst()){
			//向前迭代一个
			it.previous();
			PayModel pm = (PayModel)it.currentItem();
			System.out.println("previous1 == "+pm);			
		}
		//再next一个
		if(!it.isDone()){
			PayModel pm = (PayModel)it.currentItem();
			System.out.println("next2 == "+pm);
			//向下迭代一个
			it.next();
		}
		//继续next一个
		if(!it.isDone()){
			PayModel pm = (PayModel)it.currentItem();
			System.out.println("next3 == "+pm);
			//向下迭代一个
			it.next();
		}
		//然后previous一个
		if(!it.isFirst()){
			//向前迭代一个
			it.previous();
			PayModel pm = (PayModel)it.currentItem();
			System.out.println("previous2 == "+pm);			
		}
		
	}
}

```
 
### 迭代器模式的优缺点
1：更好的封装性
2：可以以不同的遍历方式来遍历一个聚合 
3：迭代器简化了聚合的接口
4：简化客户端调用
5：同一个聚合上可以有多个遍历
 
## 思考迭代器模式
### 迭代器模式的本质  

> 控制访问聚合对象中的元素

### 何时选用迭代器模式

1：如果你希望提供访问一个聚合对象的内容，但是又不想暴露它的内部表示的时候，可以使用迭代器模式来提供迭代器接口，从而让客户端只是通过迭代器的接口来访问聚合对象，而无需关心聚合对象内部实现。
2：如果你希望有多种遍历方式可以访问聚合对象，可以使用迭代器模式
3：如果你希望为遍历不同的聚合对象提供一个统一的接口，可以使用迭代器模式


  [1]: http://static.zybuluo.com/wangkaihua5/46v5ejd3trz7yfjxewunvxwx/image_1cihcf6941lqc1li04991k4fko9.png
  [2]: http://static.zybuluo.com/wangkaihua5/opgeeso1usv5gjw9n0ts7vhl/image_1cihcggpek4a1iikh5ejje1mh8m.png