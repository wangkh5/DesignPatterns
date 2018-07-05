# 简单工厂模式

标签（空格分隔）：设计模式

---
## 什么是简单工厂模式

> 简单工厂模式属于类的创建性模式，又叫作静态工厂方法模式。通过专门定义一个类来负责创建其他类的实例，被创建的实例通常都具有共同的父类。

## 简单工厂模式中包含的角色及其职责

![image_1cf03f0f3ku17kmfrv2nr1cdv9.png-54kB][1]

  Api：定义客户所需要的功能接口
  Impl：具体实现Api的实现类，可能会有多个
  Factory：工厂，选择合适的实现类来创建Api接口对象
  Client：客户端，通过Factory去获取Api接口对象，然后面向Api接口编程
 
```
/**
 * 接口的定义，该接口可以通过简单工厂来创建
 */
public interface Api {
	public void operation(String s);
}
/**
 * 接口的具体实现对象A 
 */
public class ImplA implements Api{
	public void operation(String s) {
		System.out.println("ImplA s=="+s);
	}
}
/**
 * 接口的具体实现对象B 
 */
public class ImplB implements Api{
	public void operation(String s) {
		System.out.println("ImplB s=="+s);
	}
}
/**
 * 工厂类，用来创造Api对象
 */
public class Factory {
	/**
	 * 具体的创造Api对象的方法
	 * @param condition 示意，从外部传入的选择条件
	 * @return 创造好的Api对象
	 */
	public static Api createApi(int condition){
		//应该根据某些条件去选择究竟创建哪一个具体的实现对象
		//这些条件可以从外部传入，也可以从其它途径获取
		//如果只有一个实现，可以省略条件，因为没有选择的必要
		
		Api api = null;
		if(condition == 1){
			api = new ImplA();
		}else if(condition == 2){
			api = new ImplB();
		}
		return api;
	}
}
/**
 * 客户端，使用Api接口
 */
public class Client {
	public static void main(String[] args) {
		//通过简单工厂来获取接口对象
		Api api = Factory.createApi(1);
		api.operation("正在使用简单工厂");
	}
}
```

## 常见的疑问

> 可能有朋友会认为，上面示例中的简单工厂看起来不就是把客户端里面的“new Impl()”移动到简单工厂里面吗？不还是一样通过new一个实现类来得到接口吗？把“new Impl()”这句话放到客户端和放到简单工厂里面有什么不同吗？理解这个问题的重点就在于理解简单工厂所处的位置。

![image_1cf040lu32mk1lvr1ea11oe6183pm.png-68.8kB][2]   

## 工厂中利用反射技术来创建对象
```
/**
 * 某个接口(通用的、抽象的、非具体的功能的) 
 */
public interface Api {
	/**
	 * 某个具体的功能方法的定义，用test1来演示一下。
	 * 这里的功能很简单，把传入的s打印输出即可 
	 * @param s 任意想要打印输出的字符串
	 */
	public void test1(String s);
}


----------
/**
 * 对某个接口的一种实现 
 */
public class Impl implements Api{
	
	public void test1(String s) {
		System.out.println("Now In Impl. The input s=="+s);
	}
}


----------
/**
 * 对某个接口的一种实现 
 */
public class Impl2 implements Api{
	
	public void test1(String s) {
		System.out.println("Now In Impl222222. The input s=="+s);
	}
}


----------
/**
 * 工厂类，用来创造Api对象
 */
public class Factory {
	/**
	 * 具体的创造Api的方法，根据配置文件的参数来创建接口
	 * @return 创造好的Api对象
	 */
	public static Api createApi(){
		//直接读取配置文件来获取需要创建实例的类
		
		//至于如何读取Properties还有如何反射这里就不解释了
		Properties p = new Properties(); 
		InputStream in = null;
		try {
			in = Factory.class.getResourceAsStream("\\FactoryTest.properties");
			p.load(in);
		} catch (IOException e) {
			System.out.println("装载工厂配置文件出错了，具体的堆栈信息如下：");
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//用反射去创建，那些例外处理等完善的工作这里就不做了
		Api api = null;
		try {
			api = (Api)Class.forName(p.getProperty("ImplClass")).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return api;
	}
}


----------
/**
 * 客户端：测试使用Api接口
 */
public class Client {
	public static void main(String[] args) {
		//重要改变，没有new Impl()了，取而代之Factory.createApi()
		Api api = Factory.createApi();
		api.test1("哈哈，不要紧张，只是个测试而已！");
	}
}

----------
在classes目录下新建名为FactoryTest.properties的配置文件，对要实例化的具体类进行配置：
ImplClass=com.wkh.designpatterns.simplefactory.example2.Impl2


----------


运行结果：
Now In Impl222222. The input s==哈哈，不要紧张，只是个测试而已！
```
 
## 简单工厂命名的建议

 1. 类名建议为“模块名称+Factory”，比如：用户模块的工厂就称为：UserFactory
 2. 方法名称通常为“get+接口名称”或者是“create+接口名称”
 3. 不建议把方法名称命名为“new+接口名称” 


## 简单工厂模式的优缺点

 1. 工厂类是整个模式的关键，它包含必要的判断逻辑，能够根据外界给定的信息，决定究竟应该创建哪个具体类的对象。用户在使用时可以直接根据工厂类去创建所需的实例，而无需了解这些对象是如何创建和组织的，有利于整个软件体系结构的优化。
 2. 简单工厂模式的缺点也体现在工厂类上，由于工厂类集中了所有实例的创建逻辑，所以高内聚方面做得并不好。另外当系统中的具体产品类不断增多时，可能会出现要求工厂类也要做相应的修改，扩展性能不是很好（工厂类代码中的第三种创建方式可以很好解决这个问题）


  [1]: http://static.zybuluo.com/wangkaihua5/08bxem8upul4o3spdxy4vwuj/image_1cf03f0f3ku17kmfrv2nr1cdv9.png
  [2]: http://static.zybuluo.com/wangkaihua5/vfok0zu6jkufxmr7n23vcof0/image_1cf040lu32mk1lvr1ea11oe6183pm.png