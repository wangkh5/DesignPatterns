# 装饰者模式

标签 ： 设计模式

---

## 什么是装饰者模式

> 动态地给一个对象添加一些额外的职责。就增加功能来说，装饰者模式比生成子类更为灵活。

## 体会装饰者模式

![image_1chieqtsf1gg71c1kqh7tpr1g7r9.png-78.4kB][1]

 1. Component:组件对象的接口，可以被动态地添加职责。
 2. ConcreteComponent:具体的组件对象，被装饰器装饰的原始对象。
 3. Decorator：所有装饰器的抽象父类，是一个与组件接口一致的接口（实现了组件接口），并持有一个Component对象，其实就是持有一个被装饰的对象。注意这个被装饰的对象不一定是最原始的那个对象了，也可能是被其他装饰器装饰过后的对象，反正都是实现的同一个接口，也就是同一类型。
 4. ConcreteDecorator：实际的装饰器对象，实现具体要向被装饰对象添加的功能。


```
/**
 * 组件对象的接口，可以给这些对象动态的添加职责
 */
public abstract class Component {
	/**
	 * 示例方法
	 */
	public abstract void operation();
}


----------


/**
 * 具体实现组件对象接口的对象
 */
public class ConcreteComponent extends Component {

	public void operation() {
		//相应的功能处理
	}

}


----------


/**
 * 装饰器接口，维持一个指向组件对象的接口对象，
 * 并定义一个与组件接口一致的接口
 */
public abstract class Decorator extends Component {
	/**
	 * 持有组件对象
	 */
	protected Component component;

	/**
	 * 构造方法，传入组件对象
	 * @param component 组件对象
	 */
	public Decorator(Component component) {
		this.component = component;
	}

	public void operation() {
		//转发请求给组件对象，可以在转发前后执行一些附加动作
		component.operation();
	}

}


----------


/**
 * 装饰器的具体实现对象，向组件对象添加职责
 */
public class ConcreteDecoratorA extends Decorator {
	public ConcreteDecoratorA(Component component) {
		super(component);
	}
	/**
	 * 添加的状态
	 */
	private String addedState;
	
	public String getAddedState() {
		return addedState;
	}

	public void setAddedState(String addedState) {
		this.addedState = addedState;
	}

	public void operation() {
		//调用父类的方法，可以在调用前后执行一些附加动作
		//在这里进行处理的时候，可以使用添加的状态
		super.operation();
	}
}


----------


/**
 * 装饰器的具体实现对象，向组件对象添加职责
 */
public class ConcreteDecoratorB extends Decorator {
	public ConcreteDecoratorB(Component component) {
		super(component);
	}
	/**
	 * 需要添加的职责
	 */
	private void addedBehavior() {
		//需要添加的职责实现
	}
	public void operation() {
		//调用父类的方法，可以在调用前后执行一些附加动作
		super.operation();
		addedBehavior();
	}
}
```

### 例子：复杂的奖金计算

奖金计算是非常复杂的，除了业务功能复杂外，另外一个麻烦之处是计算方式还经常需要变动，因为业务部门经常通过调整奖金的计算方式来激励士气。

奖金计算方式的复杂性：
- 首先是奖金分类：对于个人，大致有个人当月业务奖金、个人累计奖金、个人业务增长奖金、及时回款奖金、限时成交加码奖金等等；
- 对于业务主管或者是业务经理，除了个人奖金外，还有：团队累计奖金、团队业务增长奖金、团队盈利奖金等等。
- 其次是计算奖金的金额，又有这么几个基数：销售额、销售毛利、实际回款、业务成本、奖金基数等等；
- 另外一个就是计算的公式，针对不同的人、不同的奖金类别、不同的计算奖金的金额，计算的公式是不同的，就算是同一个公式，里面计算的比例参数也有可能是不同的。

为了方便演示学习设计模式我们简化计算体系：

 1. 每个人当月业务奖金 = 当月销售额 * 3%
 2. 每个人累计奖金 = 总的汇款额 * 0.1%
 3. 团队奖金 = 团队总销售额 * 1% 

###  不使用模式的解决方案

```
/**
 * 在内存中模拟数据库，准备点测试数据，好计算奖金
 */
public class TempDB {
	private TempDB(){}
	/**
	 * 记录每个人的月度销售额，只用了人员，月份没有用
	 */
	public static Map<String,Double> mapMonthSaleMoney = new HashMap<String,Double>();
	
	static{
		//填充测试数据
		mapMonthSaleMoney.put("张三",10000.0);
		mapMonthSaleMoney.put("李四",20000.0);
		mapMonthSaleMoney.put("王五",30000.0);
	}
}


----------
/**
 * 计算奖金的对象
 */
public class Prize {
	/**
	 * 计算某人在某段时间内的奖金，有些参数在演示中并不会使用，
	 * 但是在实际业务实现上是会用的，为了表示这是个具体的业务方法，
	 * 因此这些参数被保留了
	 * @param user 被计算奖金的人员
	 * @param begin 计算奖金的开始时间
	 * @param end 计算奖金的结束时间
	 * @return 某人在某段时间内的奖金
	 */
	public  double calcPrize(String user,Date begin,Date end){
		double prize = 0.0;
		
		//计算当月业务奖金，所有人都会计算
		prize = this.monthPrize(user, begin, end);
		//计算累计奖金
		prize += this.sumPrize(user, begin, end);
		
		//需要判断该人员是普通人员还是业务经理，团队奖金只有业务经理才有
		if(this.isManager(user)){
			prize += this.groupPrize(user, begin, end);
		}
		
		return prize;
	}
	/**
	 * 计算某人的当月业务奖金，参数重复，就不再注释了
	 */
	private double monthPrize(String user, Date begin, Date end) {
		//计算当月业务奖金,按照人员去获取当月的业务额，然后再乘以3%
		double prize = TempDB.mapMonthSaleMoney.get(user) * 0.03;
		System.out.println(user+"当月业务奖金"+prize);
		return prize;
	}
	/**
	 * 计算某人的累计奖金，参数重复，就不再注释了
	 */
	public double sumPrize(String user, Date begin, Date end) {
		//计算累计奖金,其实这里应该按照人员去获取累计的业务额，然后再乘以0.1%
		//简单演示一下，假定大家的累计业务额都是1000000元
		double prize = 1000000 * 0.001;
		System.out.println(user+"累计奖金"+prize);
		return prize;
	}	
	/**
	 * 判断人员是普通人员还是业务经理
	 * @param user 被判断的人员
	 * @return true表示是业务经理,false表示是普通人员
	 */
	private boolean isManager(String user){
		//应该从数据库中获取人员对应的职务
		//为了演示，简单点判断，只有王五是经理
		if("王五".equals(user)){
			return true;			
		}
		return false;
	}
	/**
	 * 计算当月团队业务奖金
	 */
	public double groupPrize(String user, Date begin, Date end) {
		//计算当月团队业务奖金，先计算出团队总的业务额，然后再乘以1%，假设都是一个团队的
		double group = 0.0;
		for(double d : TempDB.mapMonthSaleMoney.values()){
			group += d;
		}
		double prize = group * 0.01;
		System.out.println(user+"当月团队业务奖金"+prize);
		return prize;
	}
}


----------
public class Client {
	public static void main(String[] args) {
		//先创建计算奖金的对象
		Prize p = new Prize();
		
		//日期对象都没有用上，所以传null就可以了
		double zs = p.calcPrize("张三",null,null);		
		System.out.println("==========张三应得奖金："+zs);
		double ls = p.calcPrize("李四",null,null);
		System.out.println("==========李四应得奖金："+ls);		
		double ww = p.calcPrize("王五",null,null);
		System.out.println("==========王经理应得奖金："+ww);
	}
}

```
### 不使用模式解决方案存在的问题

这些奖金的计算方式经常发生变动，每季度，每年都会进行调整修改，要求能很快进行相应的调整和修改，否则不能满足实际的业务的需求。

比如：现根据业务需要，要增加一个环比增长奖金，就是本月销售额比上个月有增加的，而且达到一定的比例，奖金越高。那么软件必须要重新实现这个功能并正确添加到系统中区。鼓了两个月不需要这个功能了或者另外换了一种新的计算方式，还要把这个功能从软件中去掉，然后功能。

还有一个问题，就是在运行期间，不同的人员参与的奖金计算方式也是不同的，业务经理不仅参与个人计算部分外，还要参与团队奖金的计算，这就意味着需要在运行期间动态来组合需要计算的部分，会有一堆的if-else

### 使用模式解决的方案
![image_1chii4m2e1mmqccp1a32cu1ca19.png-25.3kB][2]

```
/**
 * 计算奖金的组件接口
 */
public abstract class Component {
	/**
	 * 计算某人在某段时间内的奖金，有些参数在演示中并不会使用，
	 * 但是在实际业务实现上是会用的，为了表示这是个具体的业务方法，
	 * 因此这些参数被保留了
	 * @param user 被计算奖金的人员
	 * @param begin 计算奖金的开始时间
	 * @param end 计算奖金的结束时间
	 * @return 某人在某段时间内的奖金
	 */
	public abstract double calcPrize(String user,Date begin,Date end);
}


----------
/**
 * 基本的实现计算奖金的类，也是被装饰器装饰的对象
 */
public class ConcreteComponent extends Component{
	
	public double calcPrize(String user, Date begin, Date end) {
		//只是一个默认的实现，默认没有奖金
		return 0;
	}
}


----------
/**
 * 装饰器的接口，需要跟被装饰的对象实现同样的接口
 */
public abstract class Decorator extends Component{
	/**
	 * 持有被装饰的组件对象
	 */
	protected Component c;
	/**
	 * 通过构造方法传入被装饰的对象
	 * @param c被装饰的对象
	 */
	public Decorator(Component c){
		this.c = c;
	}

	public double calcPrize(String user, Date begin, Date end) {
		//转调组件对象的方法
		return c.calcPrize(user, begin, end);
	}
}


----------
/**
 * 装饰器对象，计算累计奖金
 */
public class SumPrizeDecorator extends Decorator{
	public SumPrizeDecorator(Component c){
		super(c);
	}
	
	public double calcPrize(String user, Date begin, Date end) {
		//1：先获取前面运算出来的奖金
		double money = super.calcPrize(user, begin, end);
		//2：然后计算累计奖金,其实这里应该按照人员去获取累计的业务额，然后再乘以0.1%
		//简单演示一下，假定大家的累计业务额都是1000000元
		double prize = 1000000 * 0.001;
		System.out.println(user+"累计奖金"+prize);
		return money + prize;
	}

}


----------
/**
 * 装饰器对象，计算当月业务奖金
 */
public class MonthPrizeDecorator extends Decorator{
	public MonthPrizeDecorator(Component c){
		super(c);
	}
	
	public double calcPrize(String user, Date begin, Date end) {
		//1：先获取前面运算出来的奖金
		double money = super.calcPrize(user, begin, end);
		//2：然后计算当月业务奖金,按照人员和时间去获取当月的业务额，然后再乘以3%
		double prize = TempDB.mapMonthSaleMoney.get(user) * 0.03;
		System.out.println(user+"当月业务奖金"+prize);
		return money + prize;
	}

}


----------
/**
 * 装饰器对象，计算当月团队业务奖金
 */
public class GroupPrizeDecorator extends Decorator{
	public GroupPrizeDecorator(Component c){
		super(c);
	}
	
	public double calcPrize(String user, Date begin, Date end) {
		//1：先获取前面运算出来的奖金
		double money = super.calcPrize(user, begin, end);
		//2：然后计算当月团队业务奖金，先计算出团队总的业务额，然后再乘以1%
		//假设都是一个团队的
		double group = 0.0;
		for(double d : TempDB.mapMonthSaleMoney.values()){
			group += d;
		}
		double prize = group * 0.01;
		System.out.println(user+"当月团队业务奖金"+prize);
		return money + prize;
	}

}


----------
/**
 * 使用装饰模式的客户端
 */
public class Client {
	public static void main(String[] args) {
		//先创建计算基本奖金的类，这也是被装饰的对象
		Component c1 = new ConcreteComponent();
		
		//然后对计算的基本奖金进行装饰，这里要组合各个装饰
		//说明，各个装饰者之间最好是不要有先后顺序的限制，也就是先装饰谁和后装饰谁都应该是一样的
		
		//先组合普通业务人员的奖金计算
		Decorator d1 = new MonthPrizeDecorator(c1);
		Decorator d2 = new SumPrizeDecorator(d1);	
		
		//注意：这里只需要使用最后组合好的对象调用业务方法即可，会依次调用回去
		//日期对象都没有用上，所以传null就可以了
		double zs = d2.calcPrize("张三",null,null);		
		System.out.println("==========张三应得奖金："+zs);
		double ls = d2.calcPrize("李四",null,null);
		System.out.println("==========李四应得奖金："+ls);
		
		//如果是业务经理，还需要一个计算团队的奖金计算
		Decorator d3 = new GroupPrizeDecorator(d2);
		double ww = d3.calcPrize("王五",null,null);
		System.out.println("==========王经理应得奖金："+ww);
		
	}
}

```

![image_1chii85j21b8e19235j61c84t2em.png-25.1kB][3]


## 理解装饰者模式
（1）模式功能
装饰模式能够实现动态的为对象添加功能，是从一个对象外部来给对象增加功能，相当于是改变了对象的外观。当装饰过后，从外部使用系统的角度看，就不再是使用原始的那个对象了，而是使用被一系列的装饰器装饰过后的对象。

这样就能够灵活的改变一个对象的功能，只要动态组合的装饰器发生了改变，那么最终所得到的对象的功能也就发生了改变。

变相的还得到了另外一个好处，那就是装饰器功能的复用，可以给一个对象多次增加同一个装饰器，也可以用同一个装饰器装饰不同的对象。

（2）对象组合
前面已经讲到了，一个类的功能的扩展方式，可以是继承，也可以是功能更强大、更灵活的对象组合的方式。

（3）装饰器
装饰器实现了对被装饰对象的某些装饰功能，可以在装饰器里面调用被装饰对象的功能，获取相应的值，这其实是一种递归调用。

在装饰器里不仅仅是可以给被装饰对象增加功能，还可以根据需要选择是否调用被装饰对象的功能，如果不调用被装饰对象的功能，那就变成完全重新实现了，相当于动态修改了被装饰对象的功能。

**另外一点，各个装饰器之间最好是完全独立的功能，不要有依赖，这样在进行装饰组合的时候，才没有先后顺序的限制，也就是先装饰谁和后装饰谁都应该是一样的，否则会大大降低装饰器组合的灵活性。**

（4）装饰器和组件类的关系
装饰器是用来装饰组件的，装饰器一定要实现和组件类一致的接口，保证它们是同一个类型，并具有同一个外观，这样组合完成的装饰才能够递归的调用下去。

组件类是不知道装饰器的存在的，装饰器给组件添加功能是一种透明的包装，组件类毫不知情。需要改变的是外部使用组件类的地方，现在需要使用包装后的类，接口是一样的，但是具体的实现类发生了改变。

（5）退化形式
如果仅仅只是想要添加一个功能，就没有必要再设计装饰器的抽象类了，直接在装饰器里面实现跟组件一样的接口，然后实现相应的装饰功能就可以了。但是建议最好还是设计上装饰器的抽象类，这样有利于程序的扩展。

### Java中装饰模式的应用——I/O流

装饰模式在Java中最典型的应用，就是I/O流，简单回忆一下，如果使用流式操作读取文件内容，会怎么实现呢，简单的代码示例如下：

```
public class IOTest {
    public static void main(String[] args)throws Exception  {
       //流式读取文件
       DataInputStream din = null;
       try{
           din = new DataInputStream(
              new BufferedInputStream(
                     new FileInputStream("IOTest.txt")
              )
           );
           //然后就可以获取文件内容了
           byte bs []= new byte[din.available()];
           din.read(bs);
           String content = new String(bs);
           System.out.println("文件内容===="+content);
       }finally{
           din.close();
       }     
    }
}
```
> 仔细观察上面的代码，会发现最里层是一个FileInputStream对象，然后把它传递给一个BufferedInputStream对象，经过BufferedInputStream处理过后，再把处理过后的对象传递给了DataInputStream对象进行处理，这个过程其实就是装饰器的组装过程，FileInputStream对象相当于原始的被装饰的对象，而BufferedInputStream对象和DataInputStream对象则相当于装饰器。

 
Java的I/O对象层次图
![image_1chij4g4e13uq3nriuf91q4b1g.png-31.2kB][4]

查看上图会发现，它的结构和装饰模式的结构几乎是一样的：

 - InputStream就相当于装饰模式中的Component。
 - 其实FileInputStream、ObjectInputStream、StringBufferInputStream这几个对象是直接继承了InputSream，还有几个直接继承InputStream的对象，比如：ByteArrayInputStream、PipedInputStream等。这些对象相当于装饰模式中的ConcreteComponent，是可以被装饰器装饰的对象。
 - FilterInputStream就相当于装饰模式中的Decorator，而它的子类DataInputStream、BufferedInputStream、LineNumberInputStream和PushbackInputStream就相当于装饰模式中的ConcreteDecorator了。另外FilterInputStream和它的子类对象的构造器，都是传入组件InputStream类型，这样就完全符合前面讲述的装饰器的结构了。
同样的，输出流部分也类似。

既然I/O流部分是采用装饰模式实现的，也就是说，如果我们想要添加新的功能的话，只需要实现新的装饰器，然后在使用的时候，组合进去就可以了，也就是说，我们可以自定义一个装饰器，然后和JDK中已有的流的装饰器一起使用。能行吗？试试看吧，前面是按照输入流来讲述的，下面的示例按照输出流来做，顺便体会一下Java的输入流和输出流在结构上的相似性。

### 自己实现的I/O流的装饰器——第一版

> 来个功能简单点的，实现把英文加密存放吧，也谈不上什么加密算法，就是把英文字母向后移动两个位置，比如：a变成c，b变成d，以此类推，最后的y变成a，z就变成b，而且为了简单，只处理小写的，够简单的吧。
```
/**
 * 实现简单的加密
 */
public class EncryptOutputStream  extends OutputStream{
	//持有被装饰的对象
	private OutputStream os = null;
	public EncryptOutputStream(OutputStream os){
		this.os = os;
	}
	public void write(int a) throws IOException {
		//先统一向后移动两位
		a = a+2;
		//97是小写的a的码值
		if(a >= (97+26)){
			//如果大于，表示已经是y或者z了，减去26就回到a或者b了
			a = a-26;
		}
		this.os.write(a);
	}
}


----------
public class Client {
	public static void main(String[] args) throws Exception {
		//流式输出文件
		DataOutputStream dout = new DataOutputStream(
				new BufferedOutputStream(
				new EncryptOutputStream(
						new FileOutputStream("MyEncrypt.txt"))));
		//然后就可以输出内容了
		dout.write("abcdxyz".getBytes());
		dout.close();
	}
}

```
很好，是不是被加密了，虽然是明文的，但已经不是最初存放的内容了，一切显得非常的完美。

再试试看，不是说装饰器可以随意组合吗，换一个组合方式看看，比如把BufferedOutputStream和我们自己的装饰器在组合的时候换个位，示例如下：
```
public class Client {
    public static void main(String[] args) throws Exception {
       //流式输出文件
       DataOutputStream dout = new DataOutputStream(
           new EncryptOutputStream (
              new BufferedOutputStream(
                  new FileOutputStream("MyEncrypt.txt"))));
       dout.write("abcdxyz".getBytes());
       dout.close();
    }
}
```

再次运行，看看结果。坏了，出大问题了，这个时候输出的文件一片空白，什么都没有。这是哪里出了问题呢？

要把这个问题搞清楚，就需要把上面I/O流的内部运行和基本实现搞明白，分开来看看具体的运行过程吧。

（1）先看看成功输出流中的内容的写法的运行过程：
当执行到“dout.write("abcdxyz".getBytes());”这句话的时候，会调用DataOutputStream的write方法，把数据输出到BufferedOutputStream中；由于BufferedOutputStream流是一个带缓存的流，它默认缓存8192byte，也就是默认流中的缓存数据到了8192byte，它才会自动输出缓存中的数据；而目前要输出的字节肯定不到8192byte，因此数据就被缓存在BufferedOutputStream流中了，而不会被自动输出。当执行到“dout.close();”这句话的时候：会调用关闭DataOutputStream流，这会转调到传入DataOutputStream中的流的close方法，也就是BufferedOutputStream的close方法，而BufferedOutputStream的close方法继承自FilterOutputStream，在FilterOutputStream的close方法实现里面，会先调用输出流的方法flush，然后关闭流。也就是此时BufferedOutputStream流中缓存的数据会被强制输出；BufferedOutputStream流中缓存的数据被强制输出到EncryptOutputStream流，也就是我们自己实现的流，没有缓存，经过处理后继续输出；EncryptOutputStream流会把数据输出到FileOutputStream中，FileOutputStream会直接把数据输出到文件中，因此，这种实现方式会输出文件的内容。

（2）再来看看不能输出流中的内容的写法的运行过程：
当执行到“dout.write("abcdxyz".getBytes());”这句话的时候，会调用DataOutputStream的write方法，把数据输出到EncryptOutputStream中；EncryptOutputStream流，也就是我们自己实现的流，没有缓存，经过处理后继续输出，把数据输出到BufferedOutputStream中；由于BufferedOutputStream流是一个带缓存的流，它默认缓存8192byte，也就是默认流中的缓存数据到了8192byte，它才会自动输出缓存中的数据；而目前要输出的字节肯定不到8192byte，因此数据就被缓存在BufferedOutputStream流中了，而不会被自动输出当执行到“dout.close();”这句话的时候：会调用关闭DataOutputStream流，这会转调到传入DataOutputStream流中的流的close方法，也就是EncryptOutputStream的close方法，而EncryptOutputStream的close方法继承自OutputStream，在OutputStream的close方法实现里面，是个空方法，什么都没有做。因此，这种实现方式没有flush流的数据，也就不会输出文件的内容，自然是一片空白了。

3：自己实现的I/O流的装饰器——第二版
 要让我们写的装饰器跟其它Java中的装饰器一样用，最合理的方案就应该是：让我们的装饰器继承装饰器的父类，也就是FilterOutputStream类，然后使用父类提供的功能来协助完成想要装饰的功能。示例代码如下：
```
public class EncryptOutputStream2  extends FilterOutputStream{
    private OutputStream os = null;
    public EncryptOutputStream2(OutputStream os){
       //调用父类的构造方法
       super(os);
    }
    public void write(int a) throws IOException {
       //先统一向后移动两位
       a = a+2;
       //97是小写的a的码值
       if(a >= (97+26)){
           //如果大于，表示已经是y或者z了，减去26就回到a或者b了
           a = a-26;
       }
       //调用父类的方法
       super.write(a);
    }
}
```
再测试看看，是不是跟其它的装饰器一样，可以随便换位了呢？


#### 装饰模式和AOP

> 装饰模式和AOP在思想上有共同之处。可能有些朋友还不太了解AOP，下面先简单介绍一下AOP的基础知识。

1：什么是AOP——面向切面编程
AOP是一种编程范式，提供从另一个角度来考虑程序结构以完善面向对象编程（OOP）。

在面向对象开发中，考虑系统的角度通常是纵向的，比如我们经常画出的如下的系统架构图，默认都是从上到下，上层依赖于下层，如图所示：
![image_1chilhju41lpj1ntmpem10s017831t.png-41.6kB][5]

而在每个模块内部呢？就拿大家都熟悉的三层架构来说，也是从上到下来考虑的，通常是表现层调用逻辑层，逻辑层调用数据层
![image_1chiljl4icmo4sk1rsk1db7194o2a.png-6.9kB][6]
 
慢慢的，越来越多的人发现，在各个模块之中，存在一些共性的功能，比如日志管理、事务管理等等.
![image_1chill0oamql1703emn10ts4no2n.png-28.6kB][7]

这个时候，在思考这些共性功能的时候，是从横向在思考问题，与通常面向对象的纵向思考角度不同，很明显，需要有新的解决方案，这个时候AOP站出来了。

AOP为开发者提供了一种描述横切关注点的机制，并能够自动将横切关注点织入到面向对象的软件系统中，从而实现了横切关注点的模块化。

AOP能够将那些与业务无关，却为业务模块所共同调用的逻辑或责任，例如事务处理、日志管理、权限控制等，封装起来，便于减少系统的重复代码，降低模块间的耦合度，并有利于未来的可操作性和可维护性。

AOP之所以强大，就是因为它能够自动把横切关注点的功能模块，自动织入回到软件系统中，这是什么意思呢？

先看看没有AOP，在常规的面向对象系统中，对这种共性的功能如何处理，大都是把这些功能提炼出来，然后在需要用到的地方进行调用，只画调用通用日志的公共模块，其它的类似，就不去画了.
![image_1chilm6oc3qqipruci10us1sfp34.png-28.4kB][8]

看清楚，是从应用模块中主动去调用公共模块，也就是应用模块要很清楚公共模块的功能，还有具体的调用方法才行，应用模块是依赖于公共模块的，是耦合的，这样一来，要想修改公共模块就会很困难了，牵一而发百。
看看有了AOP会怎样，还是画个图来说明.
![image_1chilnc6h11tcl967t7mpe4773h.png-25.5kB][9]
 
乍一看，跟上面不用AOP没有什么区别嘛，真的吗？看得仔细点，有一个非常非常大的改变，就是所有的箭头方向反过来了，原来是应用系统主动去调用各个公共模块的，现在变成了各个公共模块主动织入回到应用系统。

不要小看这一点变化，这样一来应用系统就不需要知道公共功能模块，也就是应用系统和公共功能解耦了。公共功能会在合适的时候，由外部织入回到应用系统中，至于谁来实现这样的功能，以及如何实现不再我们的讨论之列，我们更关注这个思想。

如果按照装饰模式来对比上述过程，业务功能对象就可以被看作是被装饰的对象，而各个公共的模块就好比是装饰器，可以透明的来给业务功能对象增加功能。

所以从某个侧面来说，装饰模式和AOP要实现的功能是类似的，只不过AOP的实现方法不同，会更加灵活，更加可配置；另外AOP一个更重要的变化是思想上的变化——“主从换位”，让原本主动调用的功能模块变成了被动等待，甚至毫不知情的情况下被织入了很多新的功能。

2：使用装饰模式做出类似AOP的效果

下面来演示一下使用装饰模式，把一些公共的功能，比如权限控制，日志记录，透明的添加回到业务功能模块中去，做出类似AOP的效果。

（1）首先定义业务接口

这个接口相当于装饰模式的Component。注意这里使用的是接口，而不像前面一样使用的是抽象类，虽然使用抽象类的方式来定义组件是装饰模式的标准实现方式，但是如果不需要为子类提供公共的功能的话，也是可以实现成接口的，这点要先说明一下，免得有些朋友会认为这就不是装饰模式了，示例代码如下：
```
/**
* 商品销售管理的业务接口
*/
public interface GoodsSaleEbi {
    /**
     * 保存销售信息，本来销售数据应该是多条，太麻烦了，为了演示，简单点
     * @param user 操作人员
     * @param customer 客户
     * @param saleModel 销售数据
     * @return 是否保存成功
     */
    public boolean sale(String user,String customer,
SaleModel saleModel);
}

顺便把封装业务数据的对象也定义出来，很简单，示例代码如下：
/**
* 封装销售单的数据，简单的示意一些
*/
public class SaleModel {
    /**
* 销售的商品
*/
    private String goods;
    /**
* 销售的数量
*/
    private int saleNum;
    public String getGoods() { 
return goods;
}
    public void setGoods(String goods) {
this.goods = goods; 
}
    public int getSaleNum() {
       return saleNum;
    }
    public void setSaleNum(int saleNum) {
       this.saleNum = saleNum;
    }
    public String toString(){
       return "商品名称="+goods+",购买数量="+saleNum;
    }
}
（2）定义基本的业务实现对象，示例代码如下：
public class GoodsSaleEbo implements GoodsSaleEbi{
    public boolean sale(String user,String customer,
SaleModel saleModel) {
       System.out.println(user+"保存了"
+customer+"购买 "+saleModel+" 的销售数据");
       return true;
    }
}
（3）接下来该来实现公共功能了，把这些公共功能实现成为装饰器，那么需要给它们定义一个抽象的父类，示例如下：
/**
* 装饰器的接口，需要跟被装饰的对象实现同样的接口
*/
public abstract class Decorator implements GoodsSaleEbi{
    /**
* 持有被装饰的组件对象
*/
    protected GoodsSaleEbi ebi;
    /**
     * 通过构造方法传入被装饰的对象
     * @param ebi被装饰的对象
     */
    public Decorator(GoodsSaleEbi ebi){
       this.ebi = ebi;
    }
}
（4）实现权限控制的装饰器
先检查是否有运行的权限，如果有就继续调用，如果没有，就不递归调用了，而是输出没有权限的提示，示例代码如下：
/**
 * 实现权限控制
 */
public class CheckDecorator extends Decorator{
    public CheckDecorator(GoodsSaleEbi ebi){
       super(ebi);
    }
    public boolean sale(String user,String customer
, SaleModel saleModel) {
       //简单点，只让张三执行这个功能
       if(!"张三".equals(user)){
           System.out.println("对不起"+user
+"，你没有保存销售单的权限");
           //就不再调用被装饰对象的功能了
           return false;
       }else{
           return this.ebi.sale(user, customer, saleModel);
        }      
    }
}
（5）实现日志记录的装饰器，就是在功能执行完成后记录日志即可，示例代码如下：
/**
* 实现日志记录
*/
public class LogDecorator extends Decorator{
    public LogDecorator(GoodsSaleEbi ebi){
       super(ebi);
    }
    public boolean sale(String user,String customer,
SaleModel saleModel) {
       //执行业务功能
       boolean f = this.ebi.sale(user, customer, saleModel);
       //在执行业务功能过后，记录日志
       DateFormat df =
new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
       System.out.println("日志记录："+user+"于"+
df.format(new Date())+"时保存了一条销售记录，客户是"
+customer+",购买记录是"+saleModel);
       return f;
    }
}
```
（6）组合使用这些装饰器

在组合的时候，权限控制应该是最先被执行的，所以把它组合在最外面，日志记录的装饰器会先调用原始的业务对象，所以把日志记录的装饰器组合在中间。

前面讲过，装饰器之间最好不要有顺序限制，但是在实际应用中，要根据具体的功能要求来，有需要的时候，也可以有顺序的限制，但应该尽量避免这种情况。

此时客户端测试代码示例如下：
```
public class Client {
    public static void main(String[] args) {
       //得到业务接口，组合装饰器
       GoodsSaleEbi ebi = new CheckDecorator(
new LogDecorator(
new GoodsSaleEbo()));
       //准备测试数据
       SaleModel saleModel = new SaleModel();
       saleModel.setGoods("Moto手机");
       saleModel.setSaleNum(2);
       //调用业务功能
       ebi.sale("张三","张三丰", saleModel);
       ebi.sale("李四","张三丰", saleModel);
    }
}
```
运行结果如下：
```
张三保存了张三丰购买 商品名称=Moto手机,购买数量=2 的销售数据
日志记录：张三于2010-02-12 16:38:56 730时保存了一条销售记录，客户是张三丰,购买记录是商品名称=Moto手机,购买数量=2
对不起李四，你没有保存销售单的权限
```
好好体会一下，是不是也在没有惊动原始业务对象的情况下，给它织入了新的功能呢？也就是说是在原始业务不知情的情况下，给原始业务对象透明的增加了新功能，从而模拟实现了AOP的功能。

事实上，这种做法，完全可以应用在项目开发上，在后期为项目的业务对象添加数据检查、权限控制、日志记录等功能，就不需要在业务对象上去处理这些功能了，业务对象可以更专注于具体业务的处理。

### 装饰模式的优缺点
-  比继承更灵活
从为对象添加功能的角度来看，装饰模式比继承来得更灵活。继承是静态的，而且一旦继承是所有子类都有一样的功能。而装饰模式采用把功能分离到每个装饰器当中，然后通过对象组合的方式，在运行时动态的组合功能，每个被装饰的对象，最终有哪些功能，是由运行期动态组合的功能来决定的。

- 更容易复用功能
装饰模式把一系列复杂的功能，分散到每个装饰器当中，一般一个装饰器只实现一个功能，这样实现装饰器变得简单，更重要的是这样有利于装饰器功能的复用，可以给一个对象增加多个同样的装饰器，也可以把一个装饰器用来装饰不同的对象，从而复用装饰器的功能。

- 简化高层定义
装饰模式可以通过组合装饰器的方式，给对象增添任意多的功能，因此在进行高层定义的时候，不用把所有的功能都定义出来，而是定义最基本的就可以了，可以在使用需要的时候，组合相应的装饰器来完成需要的功能。

- 会产生很多细粒度对象
前面说了，装饰模式是把一系列复杂的功能，分散到每个装饰器当中，一般一个装饰器只实现一个功能，这样会产生很多细粒度的对象，而且功能越复杂，需要的细粒度对象越多。


### 装饰模式的本质  

> 动态组合

动态是手段，组合才是目的。这里的组合有两个意思，一个是动态功能的组合，也就是动态进行装饰器的组合；另外一个是指对象组合，通过对象组合来实现为被装饰对象透明的增加功能。

但是要注意，装饰模式不仅仅可以增加功能，也可以控制功能的访问，可以完全实现新的功能，还可以控制装饰的功能是在被装饰功能之前还是之后来运行等。

总之，装饰模式是通过把复杂功能简单化，分散化，然后在运行期间，根据需要来动态组合的这么一个模式。

### 何时选用装饰模式

> - 如果需要在不影响其它对象的情况下，以动态、透明的方式给对象添加职责，这几乎就是装饰模式的主要功能
> - 如果不合适使用子类来进行扩展的时候，可以考虑使用装饰模式，因为装饰模式是使用的“对象组合”的方式。所谓不适合用子类扩展的方式，比如：扩展功能需要的子类太多，造成子类数目呈爆炸性增长。


### 相关模式

#### 装饰模式与组合模式

这两个模式有相似之处，都涉及到对象的递归调用，从某个角度来说，可以把装饰看成是只有一个组件的组合。

但是它们的目的完全不一样，装饰模式是要动态的给对象增加功能；而组合模式是想要管理组合对象和叶子对象，为它们提供一个一致的操作接口给客户端，方便客户端的使用。

#### 装饰模式与策略模式

这两个模式可以组合使用。

策略模式也可以实现动态的改变对象的功能，但是策略模式只是一层选择，也就是根据策略选择一下具体的实现类而已。而装饰模式不是一层，而是递归调用，无数层都可以，只要组合好装饰器的对象组合，那就可以依次调用下去，所以装饰模式会更灵活。

而且策略模式改变的是原始对象的功能，不像装饰模式，后面一个装饰器，改变的是经过前一个装饰器装饰过后的对象，也就是策略模式改变的是对象的内核，而装饰模式改变的是对象的外壳。

这两个模式可以组合使用，可以在一个具体的装饰器里面使用策略模式，来选择更具体的实现方式。比如前面计算奖金的另外一个问题就是参与计算的基数不同，奖金的计算方式也是不同的。举例来说：假设张三和李四参与同一个奖金的计算，张三的销售总额是2万元，而李四的销售额是8万元，它们的计算公式是不一样的，假设奖金的计算规则是，销售额在5万以下，统一3%，而5万以上，5万内是4%，超过部分是6%。

参与同一个奖金的计算，这就意味着可以使用同一个装饰器，但是在装饰器的内部，不同条件下计算公式不一样，那么怎么选择具体的实现策略呢？自然使用策略模式就好了，也就是装饰模式和策略模式组合来使用。

### 装饰模式与模板方法模式
这是两个功能上有相似点的模式。

模板方法模式主要应用在算法骨架固定的情况，那么要是算法步骤不固定呢，也就是一个相对动态的算法步骤，就可以使用装饰模式了，因为在使用装饰模式的时候，进行装饰器的组装，其实也相当于是一个调用算法步骤的组装，相当于是一个动态的算法骨架。

既然装饰模式可以实现动态的算法步骤的组装和调用，那么把这些算法步骤固定下来，那就是模板方法模式实现的功能了，因此装饰模式可以模拟实现模板方法模式的功能。

但是请注意，仅仅只是可以模拟功能而已，两个模式的设计目的、原本的功能、本质思想等都是不一样的。


  [1]: http://static.zybuluo.com/wangkaihua5/zc16ip763komi92tr92xm8dv/image_1chieqtsf1gg71c1kqh7tpr1g7r9.png
  [2]: http://static.zybuluo.com/wangkaihua5/69nujur3kzpbs6hawfmlei6k/image_1chii4m2e1mmqccp1a32cu1ca19.png
  [3]: http://static.zybuluo.com/wangkaihua5/2opaoqsza8ltuf3s6evdan0d/image_1chii85j21b8e19235j61c84t2em.png
  [4]: http://static.zybuluo.com/wangkaihua5/37gxxgkxw1wyp6fddzlq7dy2/image_1chij4g4e13uq3nriuf91q4b1g.png
  [5]: http://static.zybuluo.com/wangkaihua5/i16cin9s3p99b0f0k9nr8pki/image_1chilhju41lpj1ntmpem10s017831t.png
  [6]: http://static.zybuluo.com/wangkaihua5/8ulai4tbx2ixggaq3pmn03ms/image_1chiljl4icmo4sk1rsk1db7194o2a.png
  [7]: http://static.zybuluo.com/wangkaihua5/23aa8kfr1uqrwvqcugt9w8s2/image_1chill0oamql1703emn10ts4no2n.png
  [8]: http://static.zybuluo.com/wangkaihua5/t2awcvu60kmdcos0re39zmu7/image_1chilm6oc3qqipruci10us1sfp34.png
  [9]: http://static.zybuluo.com/wangkaihua5/dcgky1j8hv15mi4f0h1yymex/image_1chilnc6h11tcl967t7mpe4773h.png