# 策略模式

标签 ： 设计模式

---

[配套源码github地址][1]

## 初识策略模式
### 定义

> 定义一系列的算法，把它们一个个封装起来，并且使它们可相互替换。本模式使得算法可独立于使用它的客户而变化。


### 结构和说明

![image_1cevrso221hjvm881a0c15d51nse9.png-67.5kB][2]

- Strategy：策略接口，用来约束一系列具体的策略算法。Context使用这个接口来调用具体的策略实现定义的算法。
- ConcreteStrategy：具体的策略实现，也就是具体的算法实现。
- Context：上下文，负责和具体的策略类交互，通常上下文会持有一个真正的策略实现，上下文还可以让具体的策略类来获取上下文的数据，甚至让具体的策略类来回调上下文的方法。

```
/**
 * 策略，定义算法的接口
 */
public interface Strategy {
	/**
	 * 某个算法的接口，可以有传入参数，也可以有返回值
	 */
	public void algorithmInterface();
}

/**
 * 实现具体的算法
 */
public class ConcreteStrategyA implements Strategy {
	public void algorithmInterface() {
		//具体的算法实现		
	}
}

/**
 * 实现具体的算法
 */
public class ConcreteStrategyB implements Strategy {

	public void algorithmInterface() {
		//具体的算法实现		
	}
}

/**
 * 实现具体的算法
 */
public class ConcreteStrategyC implements Strategy {

	public void algorithmInterface() {
		//具体的算法实现		
	}
}


/**
 * 上下文对象，通常会持有一个具体的策略对象
 */
public class Context {
	/**
	 * 持有一个具体的策略对象
	 */
	private Strategy strategy;
	/**
	 * 构造方法，传入一个具体的策略对象
	 * @param aStrategy 具体的策略对象
	 */
	public Context(Strategy aStrategy) {
		this.strategy = aStrategy;
	}
	/**
	 * 上下文对客户端提供的操作接口，可以有参数和返回值
	 */
	public void contextInterface() {
		//通常会转调具体的策略对象进行算法运算
		strategy.algorithmInterface();
	}
}

```

## 体会策略模式
### 报价管理

向客户报价，对于销售部门的人来讲，这是一个非常重大、非常复杂的问题，对不同的客户要报不同的价格。比如：
    1：对普通客户或者是新客户报的是全价
    2：对老客户报的价格，根据客户年限，给予一定的折扣
    3：对大客户报的价格，根据大客户的累计消费金额，给予一定的折扣
    4：还要考虑客户购买的数量和金额，比如：虽然是新用户，但是一次购买的数量非常大，或者是总金额非常高，也会有一定的折扣
    5：还有，报价人员的职务高低，也决定了他是否有权限对价格进行一定的浮动折扣
    6：甚至在不同的阶段，对客户的报价也不同，一般情况是刚开始比较高，越接近成交阶段，报价越趋于合理。

总之，向客户报价是非常复杂的，因此在一些CRM（客户关系管理）的系统中，会有一个单独的报价管理模块，来处理复杂的报价功能。

为了演示的简洁性，假定现在需要实现一个简化的报价管理，实现如下的功能：
（1）对普通客户或者是新客户报全价
（2）对老客户报的价格，统一折扣5%
（3）对大客户报的价格，统一折扣10%
 
### 不用模式的解决方案
```
/**
 * 价格管理，主要完成计算向客户所报价格的功能
 */
public class Price {
	/**
	 * 报价，对不同类型的，计算不同的价格
	 * @param goodsPrice 商品销售原价
	 * @param customerType 客户类型
	 * @return 计算出来的，应该给客户报的价格
	 */
	public double quote(double goodsPrice,String customerType){
		if("普通客户".equals(customerType)){
			System.out.println("对于新客户或者是普通客户，没有折扣");
			return goodsPrice;
		}else if("老客户".equals(customerType)){
			System.out.println("对于老客户，统一折扣5%");
			return goodsPrice*(1-0.05);
		}else if("大客户".equals(customerType)){
			System.out.println("对于大客户，统一折扣10%");
			return goodsPrice*(1-0.1);			
		}
		//其余人员都是报原价
		return goodsPrice;
	}
}

```
**不用模式第二种写法**
```
/**
 * 价格管理，主要完成计算向客户所报价格的功能
 */
public class Price {
	/**
	 * 报价，对不同类型的，计算不同的价格
	 * @param goodsPrice 商品销售原价
	 * @param customerType 客户类型
	 * @return 计算出来的，应该给客户报的价格
	 */
	public double quote(double goodsPrice,String customerType){
		if("普通客户".equals(customerType)){
			return this.calcPriceForNormal(goodsPrice);
		}else if("老客户".equals(customerType)){
			return this.calcPriceForOld(goodsPrice);
		}else if("大客户".equals(customerType)){
			return this.calcPriceForLarge(goodsPrice);		
		}
		//其余人员都是报原价
		return goodsPrice;
	}
	/**
	 * 为新客户或者是普通客户计算应报的价格
	 * @param goodsPrice 商品销售原价
	 * @return 计算出来的，应该给客户报的价格
	 */
	private double calcPriceForNormal(double goodsPrice){
		System.out.println("对于新客户或者是普通客户，没有折扣");
		return goodsPrice;
	}
	/**
	 * 为老客户计算应报的价格
	 * @param goodsPrice 商品销售原价
	 * @return 计算出来的，应该给客户报的价格
	 */
	private double calcPriceForOld(double goodsPrice){
		System.out.println("对于老客户，统一折扣5%");
		return goodsPrice*(1-0.05);
	}
	/**
	 * 为大客户计算应报的价格
	 * @param goodsPrice 商品销售原价
	 * @return 计算出来的，应该给客户报的价格
	 */
	private double calcPriceForLarge(double goodsPrice){
		System.out.println("对于大客户，统一折扣10%");
		return goodsPrice*(1-0.1);	
	}
}
```


### 存在的问题

上面的写法是很简单的，也很容易想，但是有缺点：

 - 价格类包含了所有计算报价的算法，使得价格类，尤其是报价这个方法比较庞杂，难以维护。
 - 经常会有这样的需要，在不同的时候，要使用不同的计算方式。

 
### 使用模式来解决的思路

分析：各种计算报价的计算方式就好比是具体的算法，而使用这些计算方式来计算报价的程序，就相当于是使用算法的客户。

实现方式存在的问题：算法和使用算法的客户是耦合的，在上面实现中，具体的算法和使用算法的客户是同一个类里面的不同方法。

解决思路：

 1. 按照策略模式的方式，应该先把所有的计算方式独立出来做成单独的算法类。
 2. 引入上下文对象来实现具体的算法和直接使用算法的客户是分离的。
具体的算法和使用它的客户分离过后，使得算法可独立于使用它的客户而变化，并且能够动态的切换需要使用的算法，只要客户端动态的选择不同的算法，然后设置到上下文对象中去，实际调用的时候，就可以调用到不同的算法。
        
```
/**
 * 策略，定义计算报价算法的接口
 */
public interface Strategy {
	/**
	 * 计算应报的价格
	 * @param goodsPrice 商品销售原价
	 * @return 计算出来的，应该给客户报的价格
	 */
	public double calcPrice(double goodsPrice);
}

/**
 * 具体算法实现，为新客户或者是普通客户计算应报的价格
 */
public class NormalCustomerStrategy implements Strategy{
	public double calcPrice(double goodsPrice) {
		System.out.println("对于新客户或者是普通客户，没有折扣");
		return goodsPrice;
	}
}
/**
 * 具体算法实现，为老客户计算应报的价格
 */
public class OldCustomerStrategy implements Strategy{
	public double calcPrice(double goodsPrice) {
		System.out.println("对于老客户，统一折扣5%");
		return goodsPrice*(1-0.05);
	}
}
/**
 * 具体算法实现，为大客户计算应报的价格
 */
public class LargeCustomerStrategy implements Strategy{
	public double calcPrice(double goodsPrice) {
		System.out.println("对于大客户，统一折扣10%");
		return goodsPrice*(1-0.1);
	}
}
/**
 * 具体算法实现，为战略合作客户客户计算应报的价格
 */
public class CooperateCustomerStrategy implements Strategy{
	public double calcPrice(double goodsPrice) {
		System.out.println("对于战略合作客户，统一8折");
		return goodsPrice*0.8;
	}
}

/**
 * 价格管理，主要完成计算向客户所报价格的功能
 */
public class Price {
	/**
	 * 持有一个具体的策略对象
	 */
	private Strategy strategy = null;
	/**
	 * 构造方法，传入一个具体的策略对象
	 * @param aStrategy 具体的策略对象
	 */
	public Price(Strategy aStrategy){
		this.strategy = aStrategy;
	}	
	/**
	 * 报价，计算对客户的报价
	 * @param goodsPrice 商品销售原价
	 * @return 计算出来的，应该给客户报的价格
	 */
	public double quote(double goodsPrice){
		return this.strategy.calcPrice(goodsPrice);
	}
}

public class Client {
	public static void main(String[] args) {
		//1：选择并创建需要使用的策略对象
		Strategy strategy = new LargeCustomerStrategy();
		//2：创建上下文
		Price ctx = new Price(strategy);
		
		//3：计算报价
		double quote = ctx.quote(1000);
		System.out.println("向客户报价："+quote);
	}
}

public class Client2 {
	public static void main(String[] args) {
		//1：选择并创建需要使用的策略对象
		Strategy strategy = new CooperateCustomerStrategy();
		//2：创建上下文
		Price ctx = new Price(strategy);
		
		//3：计算报价
		double quote = ctx.quote(1000);
		System.out.println("向客户报价："+quote);
	}
}
```
 
### 使用模式的解决方案的类图

![image_1cevug0hv1l8gk1u1aa21n3eg229.png-68.7kB][3]   
 
 
## 理解策略模式
### 认识策略模式

 - 策略模式的功能

> 把具体的算法实现，从具体的业务处理里面独立出来，实现成为单独的算法类，从而形成一系列的算法，并让这些算法可以相互替换。策略模式的重心不是如何来实现算法，而是如何组织、调用这些算法，从而让程序结构更灵活、具有更好的维护性和扩展性

- 策略模式和if-else语句

> 每个策略算法具体实现的功能，就是原来在if-else结构中的具体实现其实多个if-elseif语句表达的就是一个平等的功能结构，你要么执行if，要不你就执行else，或者是elseif。而策略模式就是把各个平等的具体实现封装到单独的策略实现类了，然后通过上下文来与具体的策略类进行交互。因此多个if-else语句可以考虑使用策略模式。

- 算法的平等性

>策略模式一个很大的特点就是各个策略算法的平等性。对于一系列具体的策略算法，大家的地位是完全一样的，正是因为这个平等性，才能实现算法之间可以相互替换。所有的策略算法在实现上也是相互独立的，相互之间是没有依赖的。所以可以这样描述这一系列策略算法：策略算法是相同行为的不同实现。

- 谁来选择具体的策略算法
>在策略模式中，可以在两个地方来进行具体策略的选择。一个是在客户端，在使用上下文的时候，由客户端来选择具体的策略算法，然后把这个策略算法设置给上下文。前面的示例就是这种情况。还有一个是客户端不管，由上下文来选择具体的策略算法，这个在后面讲容错恢复的时候来演示。

- Strategy的实现方式
>在前面的示例中，Strategy都是使用的接口来定义的，这也是常见的实现方式。但是如果多个算法具有公共功能的话，可以把Strategy实现成为抽象类，然后把多个算法的公共功能实现到Strategy里面。

- 增加新的策略
>增加一个新的算法，比如现在要实现如下的功能：对于公司的“战略合作客户”，统一8折。其实很简单，策略模式可以让你很灵活的扩展新的算法。具体的做法是：先写一个策略算法类来实现新的要求，然后在客户端使用的时候指定使用新的策略算法类就可以了。
 
 
### 策略模式调用顺序示意图 

![image_1cevvceaj9o21l3hvtn98j12dq1g.png-33.9kB][4]

Context和Strategy的关系

> 在策略模式中，通常是上下文使用具体的策略实现对象，策略实现对象也可以从上下文获取所需要的数据，因此可以将上下文当参数传递给策略实现对象,这种情况下上下文和策略实现对象是紧密耦合的。在这种情况下，上下文封装着具体策略对象进行算法运算所需要的数据，具体策略对象通过回调上下文的方法来获取这些数据。甚至在某些情况下，策略实现对象还可以回调上下文的方法来实现一定的功能，这种使用场景下，上下文变相充当了多个策略算法实现的公共接口，在上下文定义的方法可以当做是所有或者是部分策略算法使用的公共功能。但是请注意，由于所有的策略实现对象都实现同一个策略接口，传入同一个上下文，可能会造成传入的上下文数据的浪费，因为有的算法会使用这些数据，而有的算法不会使用，但是上下文和策略对象之间交互的开销是存在的了。
 
### 工资支付的实现思路
随着公司的发展，会不断有新的工资支付方式出现，这就要求能方便的扩展；另外工资支付方式不是固定的，要求能很方便的切换具体的支付方式。要实现这样的功能，策略模式是一个很好的选择。在实现这个功能的时候，不同的策略算法需要的数据是不一样，比如：现金支付就不需要银行帐号，而银行转账就需要帐号。这就导致在设计策略接口中的方法时，不太好确定参数的个数，而且，就算现在把所有的参数都列上了，今后扩展呢？难道再来修改策略接口吗？如果这样做，那无异于一场灾难，加入一个新策略，就需要修改接口，然后修改所有已有的实现，不疯掉才怪！那么到底如何实现，在今后扩展的时候才最方便呢？
解决方案之一，就是把上下文当做参数传递给策略对象，这样一来，如果要扩展新的策略实现，只需要扩展上下文就可以了，已有的实现不需要做任何的修改。另一种策略模式调用顺序示意图
![image_1cf009iv5113lk6bndb5tk108s1t.png-40.6kB][5]
```
/**
 * 支付工资的上下文，每个人的工资不同，支付方式也不同
 */
public class PaymentContext {
	/**
	 * 应被支付工资的人员，简单点，用姓名来代替
	 */
	private String userName = null;
	/**
	 * 应被支付的工资的金额
	 */
	private double money = 0.0;
	/**
	 * 支付工资的方式策略的接口
	 */
	private PaymentStrategy strategy = null;
	/**
	 * 构造方法，传入被支付工资的人员，应支付的金额和具体的支付策略
	 * @param userName 被支付工资的人员
	 * @param money 应支付的金额
	 * @param strategy 具体的支付策略
	 */
	public PaymentContext(String userName,double money,PaymentStrategy strategy){
		this.userName = userName;
		this.money = money;
		this.strategy = strategy;
	}
	/**
	 * 立即支付工资
	 */
	public void payNow(){
		//使用客户希望的支付策略来支付工资
		this.strategy.pay(this);
	}
	public String getUserName() {
		return userName;
	}
	public double getMoney() {
		return money;
	}
}

/**
 * 支付工资的策略的接口，公司有多种支付工资的算法
 * 比如：现金、银行卡、现金加股票、现金加期权、美元支付等等
 */
public interface PaymentStrategy {
	/**
	 * 公司给某人真正支付工资
	 * @param ctx 支付工资的上下文，里面包含算法需要的数据
	 */
	public void pay(PaymentContext ctx);
}

/**
 * 人民币现金支付
 */
public class RMBCash implements PaymentStrategy{
	
	public void pay(PaymentContext ctx) {
		System.out.println("现在给"+ctx.getUserName()+"人民币现金支付"+ctx.getMoney()+"元");
	}
}
/**
 * 美元现金支付
 */
public class DollarCash implements PaymentStrategy{
	public void pay(PaymentContext ctx) {
		System.out.println("现在给"+ctx.getUserName()+"美元现金支付"+ctx.getMoney()+"元");
	}
}

/**
 * 扩展的支付上下文对象
 */
public class PaymentContext2 extends PaymentContext {
	/**
	 * 银行帐号
	 */
	private String account = null;
	/**
	 * 构造方法，传入被支付工资的人员，应支付的金额和具体的支付策略
	 * @param userName 被支付工资的人员
	 * @param money 应支付的金额
	 * @param account 支付到的银行帐号
	 * @param strategy 具体的支付策略
	 */
	public PaymentContext2(String userName,double money,String account,PaymentStrategy strategy){
		super(userName,money,strategy);
		this.account = account;
	}
	public String getAccount() {
		return account;
	}
}

/**
 * 支付到银行卡
 */
public class Card implements PaymentStrategy{
	
	public void pay(PaymentContext ctx) {
		//这个新的算法自己知道要使用扩展的支付上下文，所以强制造型一下
		PaymentContext2 ctx2 = (PaymentContext2)ctx;
		System.out.println("现在给"+ctx2.getUserName()+"的"+ctx2.getAccount()+"帐号支付了"+ctx2.getMoney()+"元");
		//连接银行，进行转帐，就不去管了
	}
}
/**
 * 支付到银行卡
 */
public class Card2 implements PaymentStrategy{
	/**
	 * 帐号信息
	 */
	private String account = "";
	/**
	 * 构造方法，传入帐号信息
	 * @param account 帐号信息
	 */
	public Card2(String account){
		this.account = account;
	}
	public void pay(PaymentContext ctx) {
		System.out.println("现在给"+ctx.getUserName()+"的"+this.account+"帐号支付了"+ctx.getMoney()+"元");
		//连接银行，进行转帐，就不去管了
	}
}
```


### 容错恢复机制
什么是容错恢复呢？

> 程序运行的时候，正常情况下应该按照某种方式来做，如果按照某种方式来做发生错误的话，系统并不会崩溃，也不会就此不能继续向下运行了，而是有容忍出错的能力，不但能容忍程序运行出现错误，还提供出现错误后的备用方案，也就是恢复机制，来代替正常执行的功能，使程序继续向下运行。

举例:
> 在一个系统中，所有对系统的操作都要有日志记录，而且这个日志还需要有管理界面，这种情况下通常会把日志记录在数据库里面，方便后续的管理，但是在记录日志到数据库的时候，可能会发生错误，比如暂时连不上数据库了，那就先记录在文件里面，然后在合适的时候把文件中的记录再转录到数据库中。对于这样的功能，就可以采用策略模式，把日志记录到数据库和日志记录到文件当作两种记录日志的策略，然后在运行期间根据需要进行动态的切换。

### 策略模式结合模板方法模式

在实际应用策略模式的过程中，经常会出现一系列算法的实现上存在公共功能，甚至这一系列算法的实现步骤都是一样的，只是在某些局部步骤上有所不同。

对于一系列算法的实现上存在公共功能的情况，策略模式可以有如下三种实现方式：
 1. 在上下文当中实现公共功能，让所有具体的策略算法回调这些方法。
 2. 把策略的接口改成抽象类，然后在里面实现具体算法的公共功能。
 3. 给所有的策略算法定义一个抽象的父类，让这个父类去实现策略的接口，然后在这个父类里面去实现公共的功能。更进一步，如果这个时候发现“一系列算法的实现步骤都是一样的，只是在某些局部步骤上有所不同”的情况，那就可以在这个抽象类里面定义算法实现的骨架，然后让具体的策略算法去实现变化的部分。这样的一个结构自然就变成了策略模式来结合模板方法模式了，那个抽象类就成了模板方法模式的模板类。

在前面我们讨论过模板方法模式来结合策略模式的方式，也就是主要的结构是模板方法模式，局部采用策略模式。而这里讨论的是策略模式来结合模板方法模式，也就是主要的结构是策略模式，局部实现上采用模板方法模式。通过这个示例也可以看出来，模式之间的结合是没有定势的，要具体问题具体分析。

此时策略模式结合模板方法模式的系统结构如下图
![image_1cf01av1115nn1i79ugj8uufnp2a.png-76.4kB][6]

### 策略模式的优缺点

1. 定义一系列算法 
2. 避免多重条件语句 
3. 更好的扩展性
4. 客户必须了解每种策略的不同 
5. 增加了对象数目
6. 只适合扁平的算法结构

 
 
## 思考策略模式

### 策略模式的本质  
    

> 分离算法，选择实现

### 对设计原则的体现

> 从设计原则上来看，策略模式很好的体现了开-闭原则。策略模式通过把一系列可变的算法进行封装，并定义出合理的使用结构，使得在系统出现新算法的时候，能很容易的把新的算法加入到已有的系统中，而已有的实现不需要做任何修改。这在前面的示例中已经体现出来了，好好体会一下。
从设计原则上来看，策略模式还很好的体现了里氏替换原则。策略模式是一个扁平结构，一系列的实现算法其实是兄弟关系，都是实现同一个接口或者继承的同一个父类。这样只要使用策略的客户保持面向抽象类型编程，就能够使用不同的策略的具体实现对象来配置它，从而实现一系列算法可以相互替换。
 
### 何时选用策略模式

 - 出现有许多相关的类，仅仅是行为有差别的情况，可以使用策略模式来使用多个行为中的一个来配置一个类的方法，实现算法动态切换
 - 出现同一个算法，有很多不同的实现的情况，可以使用策略模式来把这些“不同的实现”实现成为一个算法的类层次
 - 需要封装算法中与算法相关的数据的情况，可以使用策略模式来避免暴露这些跟算法相关的数据结构
 - 出现抽象一个定义了很多行为的类，并且是通过多个if-else语句来选择这些行为的情况，可以使用策略模式来代替这些条件语句


  [1]: https://github.com/wangkh5/DesignPatterns
  [2]: http://static.zybuluo.com/wangkaihua5/aczb2y70w20mgszsxkcapx6r/image_1cevrso221hjvm881a0c15d51nse9.png
  [3]: http://static.zybuluo.com/wangkaihua5/c0167oahc0957movhfviin99/image_1cevug0hv1l8gk1u1aa21n3eg229.png
  [4]: http://static.zybuluo.com/wangkaihua5/1un0bzzncgmr7fr12anqp3gy/image_1cevvceaj9o21l3hvtn98j12dq1g.png
  [5]: http://static.zybuluo.com/wangkaihua5/2xxtqeig0933l8zrxvuka7tz/image_1cf009iv5113lk6bndb5tk108s1t.png
  [6]: http://static.zybuluo.com/wangkaihua5/i9rgrjwfda1ht4de8vidcr2s/image_1cf01av1115nn1i79ugj8uufnp2a.png