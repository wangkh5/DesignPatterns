# 观察者模式

标签（空格分隔）： 设计模式

---

## 初识观察者模式
### 定义

> 定义对象间的一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新。

### 结构和说明
 
 ![image_1cih7tcade8smpch4u1i7p10ut9.png-68.3kB][1]
   
Subject：
目标对象，通常具有如下功能：
（1）一个目标可以被多个观察者观察
（2）目标提供对观察者注册和退订的维护
（3）当目标的状态发生变化时，目标负责通知所有注册的、有效的观察者
Observer：
定义观察者的接口，提供目标通知时对应的更新方法，这个更新方法进行相应的业务处理，可以在这个方法里面回调目标对象，以获取目标对象的数据。
ConcreteSubject：
具体的目标实现对象，用来维护目标状态，当目标对象的状态发生改变时，通知所有注册有效的观察者，让观察者执行相应的处理。
ConcreteObserver：
观察者的具体实现对象，用来接收目标的通知，并进行相应的后续处理，比如更新自身的状态以保持和目标的相应状态一致。

```
/**
 * 目标对象，它知道观察它的观察者，并提供注册和删除观察者的接口
 */
public class Subject {
	/**
	 * 用来保存注册的观察者对象
	 */
	private List<Observer> observers = new ArrayList<Observer>();
	/**
	 * 注册观察者对象
	 * @param observer 观察者对象
	 */
	public void attach(Observer observer) {
		observers.add(observer);
	}
	/**
	 * 删除观察者对象
	 * @param observer 观察者对象
	 */
	public void detach(Observer observer) {
		observers.remove(observer);
	}
	/**
	 * 通知所有注册的观察者对象
	 */
	protected void notifyObservers() {
		for(Observer observer : observers){
			observer.update(this);
		}
	}
}


----------
/**
 * 具体的目标对象，负责把有关状态存入到相应的观察者对象，
 * 并在自己状态发生改变时，通知各个观察者
 */
public class ConcreteSubject extends Subject {
	/**
	 * 示意，目标对象的状态
	 */
	private String subjectState;
	public String getSubjectState() {
		return subjectState;
	}
	public void setSubjectState(String subjectState) {
		this.subjectState = subjectState;
		//状态发生了改变，通知各个观察者
		this.notifyObservers();
	}
}


----------
/**
 * 观察者接口，定义一个更新的接口给那些在目标发生改变的时候被通知的对象
 */
public interface Observer {
	/**
	 * 更新的接口
	 * @param subject 传入目标对象，好获取相应的目标对象的状态
	 */
	public void update(Subject subject);

}


----------
/**
 * 具体观察者对象，实现更新的方法，使自身的状态和目标的状态保持一致
 */
public class ConcreteObserver implements Observer {
	/**
	 * 示意，观者者的状态
	 */
	private String observerState;
	
	public void update(Subject subject) {
		// 具体的更新实现
		//这里可能需要更新观察者的状态，使其与目标的状态保持一致
		observerState = ((ConcreteSubject)subject).getSubjectState();
	}
}

```
 
## 体会观察者模式

订阅报纸的过程
![image_1cih7ua0q7ek1r874d41bdeecum.png-31.3kB][2]

在整个过程中，邮局只不过起到一个中转的作用，为了简单，我们去掉邮局，让订阅者直接和报社交互
![image_1cih7ur0ts9ll396i31cg2lk313.png-21.6kB][3]  

订阅报纸的问题
在上述过程中，订阅者在完成订阅后，最关心的问题就是何时能收到新出的报纸。幸好在现实生活中，报纸都是定期出版，这样发放到订阅者手中也基本上有一个大致的时间范围，差不多到时间了，订阅者就会看看邮箱，查收新的报纸。
要是报纸出版的时间不固定呢？
那订阅者就麻烦了，如果订阅者想要第一时间阅读到新报纸，恐怕只能天天守着邮箱了，这未免也太痛苦了吧。
继续引申一下，用类来描述上述的过程，描述如下：
订阅者类向出版者类订阅报纸，很明显不会只有一个订阅者订阅报纸，订阅者类可以有很多；当出版者类出版新报纸的时候，多个订阅者类如何知道呢？还有订阅者类如何得到新报纸的内容呢？
 
把上面的问题对比描述一下：
![image_1cih7vqms1t6v1dj31uilhp01moc1g.png-48.2kB][4]

进一步抽象描述这个 问题：当一个对象的状态发生改变的时候，如何让依赖于 它的所有对象得到通知，并进行相应的处理呢？
 
使用模式的解决方案
![image_1cih8mcru1bjogq93quo761kbr1t.png-59.5kB][5]   

拉模式：
```
/**
 * 目标对象，作为被观察者
 */
public class Subject {
	/**
	 * 用来保存注册的观察者对象，也就是报纸的订阅者
	 */
	private List<Observer> readers = new ArrayList<Observer>();
	/**
	 * 报纸的读者需要先向报社订阅，先要注册
	 * @param reader 报纸的读者 
	 * @return 是否注册成功
	 */
	public void attach(Observer reader) {
		readers.add(reader);
	}
	/**
	 * 报纸的读者可以取消订阅
	 * @param reader 报纸的读者
	 * @return 是否取消成功
	 */
	public void detach(Observer reader) {
		readers.remove(reader);
	}
	/**
	 * 当每期报纸印刷出来后，就要迅速的主动的被送到读者的手中，
	 * 相当于通知读者，让他们知道
	 */
	protected void notifyObservers() {
		for(Observer reader : readers){
			reader.update(this);
		}
	}
}


----------
/**
 * 报纸对象，具体的目标实现
 */
public class NewsPaper extends Subject{
	/**
	 * 报纸的具体内容
	 */
	private String content;
	/**
	 * 获取报纸的具体内容
	 * @return 报纸的具体内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 示意，设置报纸的具体内容，相当于要出版报纸了
	 * @param content 报纸的具体内容
	 */
	public void setContent(String content) {
		this.content = content;
		//内容有了，说明又出报纸了，那就通知所有的读者
		notifyObservers();
	}
}


----------
/**
 * 观察者，比如报纸的读者
 */
public interface Observer {
	/**
	 * 被通知的方法
	 * @param subject 具体的目标对象，可以获取报纸的内容
	 */
	public void update(Subject subject);
}


----------
/**
 * 真正的读者，为了简单就描述一下姓名
 */
public class Reader implements Observer{
	/**
	 * 读者的姓名
	 */
	private String name;

	public void update(Subject subject) {
		//这是采用拉的方式
		System.out.println(name+"收到报纸了，阅读先。内容是==="+((NewsPaper)subject).getContent());
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}


----------
public class Client {
	public static void main(String[] args) {
		//创建一个报纸，作为被观察者
		NewsPaper subject = new NewsPaper();
		
		//创建阅读者，也就是观察者
		Reader reader1 = new Reader();
		reader1.setName("张三");
		
		Reader reader2 = new Reader();
		reader2.setName("李四");
		
		Reader reader3 = new Reader();
		reader3.setName("王五");
		
		//注册阅读者
		subject.attach(reader1);
		subject.attach(reader2);
		subject.attach(reader3);
		
		//要出报纸啦
		subject.setContent("本期内容是观察者模式");
	}
}

```
推模式：
```

/**
 * 目标对象，作为被观察者，使用推模型
 */
public class Subject {
	/**
	 * 用来保存注册的观察者对象，也就是报纸的订阅者
	 */
	private List<Observer> readers = new ArrayList<Observer>();
	/**
	 * 报纸的读者需要先向报社订阅，先要注册
	 * @param reader 报纸的读者 
	 * @return 是否注册成功
	 */
	public void attach(Observer reader) {
		readers.add(reader);
	}
	/**
	 * 报纸的读者可以取消订阅
	 * @param reader 报纸的读者
	 * @return 是否取消成功
	 */
	public void detach(Observer reader) {
		readers.remove(reader);
	}
	/**
	 * 当每期报纸印刷出来后，就要迅速的主动的被送到读者的手中，
	 * 相当于通知读者，让他们知道
	 * @param content 要主动推送的内容
	 */
	protected void notifyObservers(String content) {
		for(Observer reader : readers){
			reader.update(content);
		}
	}
}


----------
/**
 * 报纸对象，具体的目标实现
 */
public class NewsPaper extends Subject{
	/**
	 * 报纸的具体内容
	 */
	private String content;
	/**
	 * 获取报纸的具体内容
	 * @return 报纸的具体内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 示意，设置报纸的具体内容，相当于要出版报纸了
	 * @param content 报纸的具体内容
	 */
	public void setContent(String content) {
		this.content = content;
		//内容有了，说明又出报纸了，那就通知所有的读者
		notifyObservers(content);
	}
}


----------
/**
 * 观察者，比如报纸的读者
 */
public interface Observer {
	/**
	 * 被通知的方法，直接把报纸的内容推送过来
	 * @param content 报纸的内容
	 */
	public void update(String content);
}


----------
/**
 * 真正的读者，为了简单就描述一下姓名
 */
public class Reader implements Observer{
	/**
	 * 读者的姓名
	 */
	private String name;

	public void update(String content) {
		//这是采用推的方式
		System.out.println(name+"收到报纸了，阅读先。内容是==="+content);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}


----------

public class Client {
	public static void main(String[] args) {
		//创建一个报纸，作为被观察者
		NewsPaper subject = new NewsPaper();
		
		//创建阅读者，也就是观察者
		Reader reader1 = new Reader();
		reader1.setName("张三");
		
		Reader reader2 = new Reader();
		reader2.setName("李四");
		
		Reader reader3 = new Reader();
		reader3.setName("王五");
		
		//注册阅读者
		subject.attach(reader1);
		subject.attach(reader2);
		subject.attach(reader3);
		
		//要出报纸啦
		subject.setContent("本期内容是观察者模式");
	}
}

```


## 理解观察者模式
### 认识观察者模式
1：目标和观察者之间的关系
按照模式的定义，目标和观察者之间是典型的一对多的关系。
但是要注意，如果观察者只有一个，也是可以的，这样就变相实现了目标和观察者之间一对一的关系，这也使得在处理一个对象的状态变化会影响到另一个对象的时候，也可以考虑使用观察者模式。
同样的，一个观察者也可以观察多个目标，如果观察者为多个目标定义的通知更新方法都是update方法的话，这会带来麻烦，因为需要接收多个目标的通知，如果是一个update的方法，那就需要在方法内部区分，到底这个更新的通知来自于哪一个目标，不同的目标有不同的后续操作。
一般情况下，观察者应该为不同的观察者目标，定义不同的回调方法，这样实现最简单，不需要在update方法内部进行区分。

2：单向依赖 
在观察者模式中，观察者和目标是单向依赖的，只有观察者依赖于目标，而目标是不会依赖于观察者的。
它们之间联系的主动权掌握在目标手中，只有目标知道什么时候需要通知观察者，在整个过程中，观察者始终是被动的，被动的等待目标的通知，等待目标传值给它。
对目标而言，所有的观察者都是一样的，目标会一视同仁的对待。当然也可以通过在目标里面进行控制，实现有区别对待观察者，比如某些状态变化，只需要通知部分观察者，但那是属于稍微变形的用法了，不属于标准的、原始的观察者模式了。

3：基本的实现说明
具体的目标实现对象要能维护观察者的注册信息，最简单的实现方案就如同前面的例子那样，采用一个集合来保存观察者的注册信息。
具体的目标实现对象需要维护引起通知的状态，一般情况下是目标自身的状态，变形使用的情况下，也可以是别的对象的状态。
具体的观察者实现对象需要能接收目标的通知，能够接收目标传递的数据，或者是能够主动去获取目标的数据，并进行后续处理。
如果是一个观察者观察多个目标，那么在观察者的更新方法里面，需要去判断是来自哪一个目标的通知。一种简单的解决方案就是扩展update方法，比如在方法里面多传递一个参数进行区分等；还有一种更简单的方法，那就是干脆定义不同的回调方法。

4：命名建议
（1）观察者模式又被称为发布-订阅模式
（2）目标接口的定义，建议在名称后面跟Subject
（3）观察者接口的定义，建议在名称后面跟Observer
（4）观察者接口的更新方法，建议名称为update，当然方法的参数可以根据需要定义，参数个数不限、参数类型不限

5：触发通知的时机
一般情况下，是在完成了状态维护后触发，因为通知会传递数据，不能够先通知后改数据，这很容易出问题，会导致观察者和目标对象的状态不一致。

6：相互观察
A对象的状态变化会引起C对象的联动操作，反过来，C 对象的状态变化也会引起A对象的联动操作。对于出现这种状况，要特别小心处理，因为可能会出现死循环的情况。

7：观察者模式的调用顺序示意图
在使用观察者模式时，会很明显的分成两个阶段，第一个阶段是准备阶段，也就是维护目标和观察者关系的阶段，这个阶段的调用顺序如图
   
 
 
接下来就是实际的运行阶段了，这个阶段的调用顺序如图

8：通知的顺序
从理论上说，当目标对象的状态变化后通知所有观察者的时候，顺序是不确定的，因此观察者实现的功能，绝对不要依赖于通知的顺序，也就是说，多个观察者之间的功能是平行的，相互不应该有先后的依赖关系。

推模型和拉模型
推模型：目标对象主动向观察者推送目标的详细信息，不管观察者是否需要，推送的信息通常是目标对象的全部或部分数据，相当于是在广播通信。
拉模型：目标对象在通知观察者的时候，只传递少量信息，如果观察者需要更具体的信息，由观察者主动到目标对象中获取，相当于是观察者从目标对象中拉数据。
一般这种模型的实现中，会把目标对象自身通过update方法传递给观察者，这样在观察者需要获取数据的时候，就可以通过这个引用来获取了
 
关于两种模型的比较
两种实现模型，在开发的时候，究竟应该使用哪一种，还是应该具体问题具体分析。这里，只是把两种模型进行一个简单的比较。
1：推模型是假定目标对象知道观察者需要的数据；而拉模型是目标对象不知道观察者具体需要什么数据，没有办法的情况下，干脆把自身传给观察者，让观察者自己去按需取值。
2：推模型可能会使得观察者对象难以复用，因为观察者定义的update方法是按需而定义的，可能无法兼顾没有考虑到的使用情况。这就意味着出现新情况的时候，就可能需要提供新的update方法，或者是干脆重新实现观察者。
而拉模型就不会造成这样的情况，因为拉模型下，update方法的参数是目标对象本身，这基本上是目标对象能传递的最大数据集合了，基本上可以适应各种情况的需要。 
 
 
 
### Java中的观察者模式
在java.util包里面有一个类Observable，它实现了大部分我们需要的目标的功能；还有一个接口Observer，它里面定义了update的方法，就是观察者的接口。
```
/**
 * 报纸对象，具体的目标实现
 */
public class NewsPaper extends Observable{
	/**
	 * 报纸的具体内容
	 */
	private String content;
	/**
	 * 获取报纸的具体内容
	 * @return 报纸的具体内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 示意，设置报纸的具体内容，相当于要出版报纸了
	 * @param content 报纸的具体内容
	 */
	public void setContent(String content) {
		this.content = content;
		//内容有了，说明又出报纸了，那就通知所有的读者
		//注意在用Java中的Observer模式的时候，这句话不可少
		this.setChanged();
		//然后主动通知，这里用的是推的方式
		this.notifyObservers(this.content);
		//如果用拉的方式，这么调用
		//this.notifyObservers();
	}
}


----------
/**
 * 真正的读者，为了简单就描述一下姓名
 */
public class Reader implements java.util.Observer{
	/**
	 * 读者的姓名
	 */
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void update(Observable o, Object obj) {
		//这是采用推的方式
		System.out.println(name+"收到报纸了，阅读先。目标推过来的内容是==="+obj);
		//这是获取拉的数据
		System.out.println(name+"收到报纸了，阅读先。主动到目标对象去拉的内容是==="
		+((NewsPaper)o).getContent());
	}
	
}


----------
public class Client {
	public static void main(String[] args) {
		//创建一个报纸，作为被观察者
		NewsPaper subject = new NewsPaper();
		
		//创建阅读者，也就是观察者
		Reader reader1 = new Reader();
		reader1.setName("张三");
		
		Reader reader2 = new Reader();
		reader2.setName("李四");
		
		Reader reader3 = new Reader();
		reader3.setName("王五");
		
		//注册阅读者
		subject.addObserver(reader1);
		subject.addObserver(reader2);
		subject.addObserver(reader3);
		
		//要出报纸啦
		subject.setContent("本期内容是观察者模式");
	}
}

```

### Swing中的观察者模式
Swing中到处都是观察者模式的身影，比如大家熟悉的事件处理，就是典型的观察者模式的应用。（说明一下：早期的Swing事件处理用的是职责链）
Swing组件是被观察的目标，而每个实现监听器的类就是观察者，监听器的接口就是观察者的接口，在调用addXXXListener方法的时候就相当于注册观察者。
当组件被点击，状态发生改变的时候，就会产生相应的通知，会调用注册的观察者的方法，就是我们所实现的监听器的方法。
从这里还可以学一招：如何处理一个观察者观察多个目标对象
 
### 观察者模式的优缺点
1：观察者模式实现了观察者和目标之间的抽象耦合
2：观察者模式实现了动态联动
3：观察者模式支持广播通信
4：观察者模式可能会引起无谓的操作
 
## 思考观察者模式
### 观察者模式的本质  

> 触发联动

### 何时选用观察者模式

1：当一个抽象模型有两个方面，其中一个方面的操作依赖于另一个方面的状态变化，那么就可以选用观察者模式。

2：如果在更改一个对象的时候，需要同时连带改变其它的对象，而且不知道究竟应该有多少对象需要被连带改变，这种情况可以选用观察者模式，被更改的那一个对象很明显就相当于是目标对象，而需要连带修改的多个其它对象，就作为多个观察者对象了。

3：当一个对象必须通知其它的对象，但是你又希望这个对象和其它被它通知的对象是松散耦合的，也就是说这个对象其实不想知道具体被通知的对象，这种情况可以选用观察者模式，这个对象就相当于是目标对象，而被它通知的对象就是观察者对象了。
 
简单变形示例——区别对待观察者

1：范例需求
这是一个实际系统的简化需求：在一个水质监测系统中有这样一个功能，当水中的杂质为正常的时候，只是通知监测人员做记录；当为轻度污染的时候，除了通知监测人员做记录外，还要通知预警人员，判断是否需要预警；当为中度或者高度污染的时候，除了通知监测人员做记录外，还要通知预警人员，判断是否需要预警，同时还要通知监测部门领导做相应的处理。

2：解决思路和范例代码
分析上述需求就会发现，对于水质污染这件事情，有可能会涉及到监测员、预警人员、监测部门领导，根据不同的水质污染情况涉及到不同的人员，也就是说，监测员、预警人员、监测部门领导他们三者是平行的，职责都是处理水质污染，但是处理的范围不一样。
因此很容易套用上观察者模式，如果把水质污染的记录当作被观察的目标的话，那么监测员、预警人员和监测部门领导就都是观察者了。
 
前面学过的观察者模式，当目标通知观察者的时候是全部都通知，但是现在这个需求是不同的情况来让不同的人处理，怎么办呢？

解决的方式通常有两种，一种是目标可以通知，但是观察者不做任何操作； 另 外一种是在目标里面进行判断，干脆就不通知了。两种实现方式各有千秋，这里选择后面一种方式来示例，这种方式能够统一逻辑控制，并进行观察者的统一分派，有利于业务控制和今后的扩展。

```
/**
 * 定义水质监测的目标对象
 */
public abstract class WaterQualitySubject {
	/**
	 * 用来保存注册的观察者对象
	 */
	protected List<WatcherObserver> observers = new ArrayList<WatcherObserver>();
	/**
	 * 注册观察者对象
	 * @param observer 观察者对象
	 */
	public void attach(WatcherObserver observer) {
		observers.add(observer);
	}
	/**
	 * 删除观察者对象
	 * @param observer 观察者对象
	 */
	public void detach(WatcherObserver observer) {
		observers.remove(observer);
	}
	/**
	 * 通知相应的观察者对象
	 */
	public abstract void notifyWatchers();
	/**
	 * 获取水质污染的级别
	 * @return 水质污染的级别
	 */
	public abstract int getPolluteLevel();
}


----------
/**
 * 具体的水质监测对象
 */
public class WaterQuality extends WaterQualitySubject{
	/**
	 * 污染的级别，0表示正常，1表示轻度污染，2表示中度污染，3表示高度污染
	 */
	private int polluteLevel = 0;
	/**
	 * 获取水质污染的级别
	 * @return 水质污染的级别
	 */
	public int getPolluteLevel() {
		return polluteLevel;
	}
	/**
	 * 当监测水质情况后，设置水质污染的级别
	 * @param polluteLevel 水质污染的级别
	 */
	public void setPolluteLevel(int polluteLevel) {
		this.polluteLevel = polluteLevel;
		//通知相应的观察者
		this.notifyWatchers();
	}
	/**
	 * 通知相应的观察者对象
	 */
	public void notifyWatchers() {
		//循环所有注册的观察者
		for(WatcherObserver watcher : observers){
						//开始根据污染级别判断是否需要通知，由这里总控
						if(this.polluteLevel >= 0){
							//通知监测员做记录
							if("监测人员".equals(watcher.getJob())){
								watcher.update(this);
							}
						}
						if(this.polluteLevel >= 1){
							//通知预警人员
							if("预警人员".equals(watcher.getJob())){
								watcher.update(this);
							}
						}
						if(this.polluteLevel >= 2){
							//通知监测部门领导
							if("监测部门领导".equals(watcher.getJob())){
								watcher.update(this);
							}
						}
		}
	}
}


----------
/**
 * 水质观察者接口定义
 */
public interface WatcherObserver {
	/**
	 * 被通知的方法
	 * @param subject 传入被观察的目标对象
	 */
	public void update(WaterQualitySubject subject);
	/**
	 * 设置观察人员的职务
	 * @param job 观察人员的职务
	 */
	public void setJob(String job);
	/**
	 * 获取观察人员的职务
	 * @return 观察人员的职务
	 */
	public String getJob();
}


----------
/**
 * 具体的观察者实现
 */
public class Watcher implements WatcherObserver{
	/**
	 * 职务
	 */
	private String job;
	
	public void update(WaterQualitySubject subject) {
		//这里采用的是拉的方式
		System.out.println(job+"获取到通知，当前污染级别为："+subject.getPolluteLevel());
	}
	
	public String getJob() {
		return this.job;
	}
	
	public void setJob(String job) {
		this.job = job;
	}
}


----------
public class Client {
	public static void main(String[] args) {
		//创建水质主题对象
		WaterQuality subject = new WaterQuality();
		//创建几个观察者
		WatcherObserver watcher1 = new Watcher();
		watcher1.setJob("监测人员");
		WatcherObserver watcher2 = new Watcher();
		watcher2.setJob("预警人员");
		WatcherObserver watcher3 = new Watcher();
		watcher3.setJob("监测部门领导");
		//注册观察者
		subject.attach(watcher1);
		subject.attach(watcher2);
		subject.attach(watcher3);
		
		//填写水质报告
		System.out.println("当水质为正常的时候------------------〉");
		subject.setPolluteLevel(0);
		System.out.println("当水质为轻度污染的时候---------------〉");
		subject.setPolluteLevel(1);
		System.out.println("当水质为中度污染的时候---------------〉");
		subject.setPolluteLevel(2);
	}
}

```

  [1]: http://static.zybuluo.com/wangkaihua5/jkjw3c0tqwme1ivy9pwhso6i/image_1cih7tcade8smpch4u1i7p10ut9.png
  [2]: http://static.zybuluo.com/wangkaihua5/4gqwfjokte0n9m0d41lnfeps/image_1cih7ua0q7ek1r874d41bdeecum.png
  [3]: http://static.zybuluo.com/wangkaihua5/h7fkj3zgw3i5bii77l09zpcu/image_1cih7ur0ts9ll396i31cg2lk313.png
  [4]: http://static.zybuluo.com/wangkaihua5/n6i15cevw6s6fw6qt4leko7r/image_1cih7vqms1t6v1dj31uilhp01moc1g.png
  [5]: http://static.zybuluo.com/wangkaihua5/78ulwl3tuqpqi84tbgwonyg3/image_1cih8mcru1bjogq93quo761kbr1t.png