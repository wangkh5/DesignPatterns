# 模板方法模式

标签： 设计模式

---

## 初识模板方法模式
### 定义
    

> 定义一个操作中的算法的骨架，而将一些步骤延迟到子类中。模板方法使得子类可以不改变一个算法的结构即可重定义该算法的某些特定步骤。

### 结构和说明

![2018-06-01_185609.png-19kB][1]
 - AbstractClass：抽象类。用来定义算法骨架和原语操作，在这个类里面，还可以提供算法中通用的实现
 - ConcreteClass：具体实现类。用来实现算法骨架中的某些步骤，完成跟特定子类相关的功能。

```
/**
 * 定义模板方法、原语操作等的抽象类
 */
public abstract class AbstractClass {
    /**
     * 原语操作1，所谓原语操作就是抽象的操作，必须要由子类提供实现
     */
    public abstract void doPrimitiveOperation1();
    /**
     * 原语操作2
     */
    public abstract void doPrimitiveOperation2();
    /**
     * 模板方法，定义算法骨架
     */
    public final void templateMethod() {
        doPrimitiveOperation1();
        doPrimitiveOperation2();
    }
}

/**
 * 具体实现类，实现原语操作
 */
public class ConcreteClass extends AbstractClass {
	public void doPrimitiveOperation1() { 
		//具体的实现
	}
	public void doPrimitiveOperation2() { 
		//具体的实现
	}
}
```



## 体会模板方法模式

### 登录控制  
 - 先看看普通用户登录前台的登录控制的功能：
    - 在前台页面用户能输入用户名和密码；提交登录请求，让系统去进行登录控制。
    - 后台从数据库获取登录人员的信息判断从前台传递过来的登录数据和数据库中已有的数据是否匹配？
    - 前台Action，如果匹配就转向首页，如果不匹配就返回到登录页面，并显示错误提示信息。
 - 再来看看工作人员登录后台的登录控制功能：
    - 前台页面：用户能输入用户名和密码；提交登录请求，让系统去进行登录控制
    - 后台：从数据库获取登录人员的信息。把从前台传递过来的密码数据，使用相应的加密算法进行加密运算，得到加密后的密码数据
    - 后台：判断从前台传递过来的用户名和加密后的密码数据，和数据库中已有的数据是否匹配
    - 前台Action：如果匹配就转向首页，如果不匹配就返回到登录页面，并显示错误提示信息

### 不用模式的解决方案

> 普通用户登录

```
/**
 * 描述登录人员登录时填写的信息的数据模型
 */
public class LoginModel {
	private String userId,pwd;
    //省略getXXX和setXXX方法
}

/**
 * 描述用户信息的数据模型
 */
public class UserModel {
	private String uuid,userId,pwd,name;
	//省略getXXX和setXXX方法
}

/**
 * 普通用户登录控制的逻辑处理
 */
public class NormalLogin {
	/**
	 * 判断登录数据是否正确，也就是是否能登录成功
	 * @param lm 封装登录数据的Model
	 * @return true表示登录成功，false表示登录失败
	 */
	public boolean login(LoginModel lm) {
		//1：从数据库获取登录人员的信息， 就是根据用户编号去获取人员的数据
		UserModel um = this.findUserByUserId(lm.getUserId());
		//2：判断从前台传递过来的登录数据，和数据库中已有的数据是否匹配
		//先判断用户是否存在，如果um为null，说明用户肯定不存在
		if (um != null) {
			//如果用户存在，检查用户编号和密码是否匹配
			if (um.getUserId().equals(lm.getUserId())
					&& um.getPwd().equals(lm.getPwd())) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 根据用户编号获取用户的详细信息
	 * @param userId 用户编号
	 * @return 对应的用户的详细信息
	 */
	private UserModel findUserByUserId(String userId) {
		// 这里省略具体的处理，仅做示意，返回一个有默认数据的对象
		UserModel um = new UserModel();
		um.setUserId(userId);
		um.setName("test");
		um.setPwd("test");
		um.setUuid("User0001");
		return um;
	}
}
```
> 工作人员登录
```
/**
 * 描述登录人员登录时填写的信息的数据模型
 */
public class LoginModel{
	private String workerId,pwd;
}

/**
 * 描述工作人员信息的数据模型
 */
public class WorkerModel {
	private String uuid,workerId,pwd,name;
}

/**
 * 工作人员登录控制的逻辑处理
 */
public class WorkerLogin {
	/**
	 * 判断登录数据是否正确，也就是是否能登录成功
	 * @param lm 封装登录数据的Model
	 * @return true表示登录成功，false表示登录失败
	 */
	public boolean login(LoginModel lm) {
		//1：根据工作人员编号去获取工作人员的数据
		WorkerModel wm = this.findWorkerByWorkerId(lm.getWorkerId());
		//2：判断从前台传递过来的用户名和加密后的密码数据，和数据库中已有的数据是否匹配
		if (wm != null) {
			//3：把从前台传来的密码数据，使用相应的加密算法进行加密运算
			String encryptPwd = this.encryptPwd(lm.getPwd());
			//如果工作人员存在，检查工作人员编号和密码是否匹配
			if (wm.getWorkerId().equals(lm.getWorkerId())
					&& wm.getPwd().equals(encryptPwd)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 对密码数据进行加密
	 * @param pwd 密码数据
	 * @return 加密后的密码数据
	 */
	private String encryptPwd(String pwd){
		//这里对密码进行加密，省略了
		return pwd;
	}

	/**
	 * 根据工作人员编号获取工作人员的详细信息
	 * @param workerId 工作人员编号
	 * @return 对应的工作人员的详细信息
	 */
	private WorkerModel findWorkerByWorkerId(String workerId) {
		// 这里省略具体的处理，仅做示意，返回一个有默认数据的对象
		WorkerModel wm = new WorkerModel();
		wm.setWorkerId(workerId);
		wm.setName("Worker1");
		wm.setPwd("worker1");
		wm.setUuid("Worker0001");
		return wm;
	}
}
```

### 不用模式有何问题

> 看了这里的实现示例，是不是很简单。但是，仔细看看，总会觉得有点问题，两种登录的实现太相似了，现在是完全分开，当作两个独立的模块来实现的，如果今后要扩展功能，比如要添加“控制同一个编号同时只能登录一次”的功能，那么两个模块都需要修改，是很麻烦的。而且，现在的实现中，也有很多相似的地方，显得很重复。另外，具体的实现和判断的步骤混合在一起，不利于今后变换功能，比如要变换加密算法等。总之，上面的实现，有两个很明显的问题：一是重复或相似代码太多；二是扩展起来很不方便。那么该怎么解决呢？该如何实现才能让系统既灵活又能简洁的实现需求功能呢？


### 使用模式的解决方案的类图
![2018-06-01_190552.png-32kB][2]
```
/**
 * 封装进行登录控制所需要的数据
 */
public class LoginModel {
	/**
	 * 登录人员的编号，通用的，可能是用户编号，也可能是工作人员编号
	 */
	private String loginId;
	/**
	 * 登录的密码
	 */
	private String pwd;
}

/**
 *	登录控制的模板
 */
public abstract class LoginTemplate {
	/**
	 * 判断登录数据是否正确，也就是是否能登录成功
	 * @param lm 封装登录数据的Model
	 * @return true表示登录成功，false表示登录失败
	 */
	public final boolean login(LoginModel lm){
		//1：根据登录人员的编号去获取相应的数据
		LoginModel dbLm = this.findLoginUser(lm.getLoginId());
		if(dbLm!=null){
			//2：对密码进行加密
			String encryptPwd = this.encryptPwd(lm.getPwd());
			//把加密后的密码设置回到登录数据模型里面
			lm.setPwd(encryptPwd);
			//3：判断是否匹配
			return this.match(lm, dbLm);
		}
		return false;
	}
	/**
	 * 根据登录编号来查找和获取存储中相应的数据
	 * @param loginId 登录编号
	 * @return 登录编号在存储中相对应的数据
	 */
	public abstract LoginModel findLoginUser(String loginId);
	/**
	 * 对密码数据进行加密
	 * @param pwd 密码数据
	 * @return 加密后的密码数据
	 */
	public String encryptPwd(String pwd){
		return pwd;
	}
	/**
	 * 判断用户填写的登录数据和存储中对应的数据是否匹配得上
	 * @param lm 用户填写的登录数据
	 * @param dbLm 在存储中对应的数据
	 * @return true表示匹配成功，false表示匹配失败
	 */
	public boolean match(LoginModel lm,LoginModel dbLm){
		if(lm.getLoginId().equals(dbLm.getLoginId()) 
				&& lm.getPwd().equals(dbLm.getPwd())){
			return true;
		}
		return false;
	}
}

/**
 * 普通用户登录控制的逻辑处理
 */
public class NormalLogin extends LoginTemplate{
	public LoginModel findLoginUser(String loginId) {
		// 这里省略具体的处理，仅做示意，返回一个有默认数据的对象
		LoginModel lm = new LoginModel();
		lm.setLoginId(loginId);
		lm.setPwd("testpwd");
		return lm;
	}
}

/**
 * 工作人员登录控制的逻辑处理
 */
public class WorkerLogin extends LoginTemplate{
	public LoginModel findLoginUser(String loginId) {
		// 这里省略具体的处理，仅做示意，返回一个有默认数据的对象
		LoginModel lm = new LoginModel();
		lm.setLoginId(loginId);
		lm.setPwd("workerpwd");
		return lm;
	}
	public String encryptPwd(String pwd){
		//覆盖父类的方法，提供真正的加密实现
		//这里对密码进行加密，比如使用：MD5、3DES等等，省略了
		System.out.println("使用MD5进行密码加密");
		return pwd;
	}
}
```
 
## 理解模板方法模式
### 认识模板方法模式

#### 模式的功能

> 模板方法的功能在于固定算法骨架，而让具体算法实现可扩展。这在实际应用中非常广泛，尤其是在设计框架级功能的时候非常有用。框架定义好了算法的步骤，在合适的点让开发人员进行扩展，实现具体的算法。比如在DAO实现中，设计通用的增删改查功能，这个后面会给大家示例。模板方法还额外提供了一个好处，就是可以控制子类的扩展。因为在父类里面定义好了算法的步骤，只是在某几个固定的点才会调用到被子类实现的方法，因此也就只允许在这几个点来扩展功能，这些个可以被子类覆盖以扩展功能的方法通常被称为“钩子”方法，后面也会给大家示例。

#### 为何不是接口

> 首先搞清楚抽象类和接口的关系。其次要明了什么时候使用抽象类，那就是通常在“既要约束子类的行为，又要为子类提供公共功能”的时候使用抽象类。按照这个原则来思考模板方法模式的实现，模板方法模式需要固定定义算法的骨架，这个骨架应该只有一份，算是一个公共的行为，但是里面具体的步骤的实现又可能是各不相同的，恰好符合选择抽象类的原则。把模板实现成为抽象类，为所有的子类提供了公共的功能，就是定义了具体的算法骨架；同时在模板里面把需要由子类扩展的具体步骤的算法定义成为抽象方法，要求子类去实现这些方法，这就约束了子类的行为。因此综合考虑，用抽象类来实现模板是一个很好的选择。

#### 变与不变

> 程序设计的一个很重要的思考点就是“变与不变”，也就是分析程序中哪些功能是可变的，哪些功能是不变的，把不变的部分抽象出来，进行公共的实现，把变化的部分分离出去，用接口来封装隔离，或用抽象类来约束子类行为。模板方法模式很好的体现了这一点。模板类实现的就是不变的方法和算法的骨架，而需要变化的地方，都通过抽象方法，把具体实现延迟到子类，还通过父类的定义来约束了子类的行为，从而使系统能有更好的复用性和扩展性。

#### 好莱坞法则

> 什么是好莱坞法则呢？简单点说，就是“不要找我们，我们会联系你”。模板方法模式很好的体现了这一点，做为父类的模板会在需要的时候，调用子类相应的方法，也就是由父类来找子类，而不是让子类来找父类。这是一种反向的控制结构，按照通常的思路，是子类找父类才对，也就是应该是子类来调用父类的方法，因为父类根本就不知道子类，而子类是知道父类的，但是在模板方法模式里面，是父类来找子类，所以是一种反向的控制结构。在Java里面能实现这样功能的理论依据在哪里呢？理论依据就在于Java的动态绑定采用的是“后期绑定”技术，对于出现子类覆盖父类方法的情况，在编译时是看数据类型，运行时看实际的对象类型（new操作符后跟的构造方法是哪个类的），一句话：new谁就调用谁的方法。因此在使用模板方法模式的时候，虽然用的数据类型是模板类型，但是在创建类实例的时候是创建的具体的子类的实例，因此调用的时候，会被动态绑定到子类的方法上去，从而实现反向控制。其实在写父类的时候，它调用的方法是父类自己的抽象方法，只是在运行的时候被动态绑定到了子类的方法上。

#### 扩展登录控制

> 在使用模板方法模式实现过后，如果想要扩展新的功能，有如下几种情况

 - 一种情况是只需要提供新的子类实现就可以了，比如想要切换不同的加密算法，现在是使用的MD5，想要实现使用3DES的加密算法，那就新做一个子类，然后覆盖实现父类加密的方法，在里面使用3DES来实现即可，已有的实现不需要做任何变化
 - 另外一种情况是想要给两个登录模块都扩展同一个功能，这种情况多属于需要修改模板方法的算法骨架的情况，应该尽量避免，但是万一前面没有考虑周全，后来出现了这种情况，怎么办呢？最好就是重构，也就是考虑修改算法骨架，尽量不要去找其它的替代方式，替代的方式也许能把功能实现了，但是会破坏整个程序的结构。
 - 还有一种情况是既需要加入新的功能，也需要新的数据。比如：现在对于普通人员登录，要实现一个加强版，要求登录人员除了编号和密码外，还需要提供注册时留下的验证问题和验证答案，验证问题和验证答案是记录在数据库中的，不是验证码，一般Web开发中登录使用的验证码会放到session中，这里不去讨论它。
   
```
/**
 * 封装进行登录控制所需要的数据，在公共数据的基础上，
 * 添加具体模块需要的数据
 */
public class NormalLoginModel extends LoginModel{
	/**
	 * 密码验证问题
	 */
	private String question;
	/**
	 * 密码验证答案
	 */
	private String answer;
}

/**
 * 普通用户登录控制加强版的逻辑处理
 */
public class NormalLogin2 extends LoginTemplate{
	public LoginModel findLoginUser(String loginId) {
		//注意一点：这里使用的是自己需要的数据模型了
		NormalLoginModel nlm = new NormalLoginModel();
		nlm.setLoginId(loginId);
		nlm.setPwd("testpwd");
		nlm.setQuestion("testQuestion");
		nlm.setAnswer("testAnswer");
		return nlm;
	}
	public boolean match(LoginModel lm,LoginModel dbLm){
		//这个方法需要覆盖，因为现在进行登录控制的时候，
		//需要检测4个值是否正确，而不仅仅是缺省的2个
		//先调用父类实现好的，检测编号和密码是否正确
		boolean f1 = super.match(lm, dbLm);
		if(f1){
			//如果编号和密码正确，继续检查问题和答案是否正确
			//先把数据转换成自己需要的数据
			NormalLoginModel nlm = (NormalLoginModel)lm;
			NormalLoginModel dbNlm = (NormalLoginModel)dbLm;
			//检查问题和答案是否正确
			if(dbNlm.getQuestion().equals(nlm.getQuestion())
					&& dbNlm.getAnswer().equals(nlm.getAnswer())){
				return true;
			}
		}
		return false;
	}
}
```
   
### 模板的写法

> 通常在模板里面包含如下操作类型：

 1. 模板方法：就是定义算法骨架的方法
 2. 具体的操作：在模板中直接实现某些步骤的方法，通常这些步骤的实现算法是固定的，而且是不怎么变化的，因此就可以当作公共功能实现在模板里面。如果不需提供给子类访问这些方法的话，还可以是private的。这样一来，子类的实现就相对简单些。如果是子类需要访问，可以把这些方法定义为protected final的，因为通常情况下，这些实现不能够被子类覆盖和改变了。
 3. 具体的AbstractClass操作：在模板中实现某些公共功能，可以提供给子类使用，一般不是具体的算法步骤的实现，只是一些辅助的公共功能。
 4. 原语操作：就是在模板中定义的抽象操作，通常是模板方法需要调用的操作，是必需的操作，而且在父类中还没有办法确定下来如何实现，需要子类来真正实现的方法。
 5. 钩子操作：在模板中定义，并提供默认实现的操作。这些方法通常被视为可扩展的点，但不是必须的，子类可以有选择的覆盖这些方法，以提供新的实现来扩展功能。比如：模板方法中定义了5步操作，但是根据需要，某一种具体的实现只需要其中的1、2、3这几个步骤，因此它就只需要覆盖实现1、2、3这几个步骤对应的方法。那么4和5步骤对应的方法怎么办呢，由于有默认实现，那就不用管了。也就是说钩子操作是可以被扩展的点，但不是必须的。
 6. Factory Method：在模板方法中，如果需要得到某些对象实例的话，可以考虑通过工厂方法模式来获取，把具体的构建对象的实现延迟到子类中去。

```
/**
 * 一个较为完整的模版定义示例
 */
public abstract class AbstractTemplate {
	/**
	 * 模板方法，定义算法骨架
	 */
	public final void templateMethod(){
		//第一步
		this.operation1();
		//第二步		
		this.operation2();
		//第三步
		this.doPrimitiveOperation1();
		//第四步
		this.doPrimitiveOperation2();
		//第五步
		this.hookOperation1();
	}
	/**
	 * 具体操作1，算法中的步骤，固定实现，而且子类不需要访问
	 */
	private void operation1(){
		//在这里具体的实现
	}
	/**
	 * 具体操作2，算法中的步骤，固定实现，子类可能需要访问，
	 * 当然也可以定义成public的，不可以被覆盖，因此是final的
	 */
	protected final void operation2(){
		//在这里具体的实现
	}
	/**
	 * 具体的AbstractClass操作，子类的公共功能，
	 * 但通常不是具体的算法步骤
	 */
	protected void commonOperation(){
		//在这里具体的实现
	}
	/**
	 * 原语操作1，算法中的必要步骤，父类无法确定如何真正实现，需要子类来实现
	 */
	protected abstract void doPrimitiveOperation1();
	/**
	 * 原语操作2，算法中的必要步骤，父类无法确定如何真正实现，需要子类来实现
	 */
	protected abstract void doPrimitiveOperation2();
	/**
	 * 钩子操作，算法中的步骤，不一定需要，提供缺省实现
	 * 由子类选择并具体实现
	 */
	protected void hookOperation1(){
		//在这里提供缺省的实现
	}
	/**
	 * 工厂方法，创建某个对象，这里用Object代替了，在算法实现中可能需要
	 * @return 创建的某个算法实现需要的对象
	 */
	protected abstract Object createOneObject();
}
```

### Java回调与模板方法模式

> 模板方法模式的一个目的，就在于让其它类来扩展或具体实现在模板中固定的算法骨架中的某些算法步骤。在标准的模板方法模式实现中，主要是使用继承的方式，来让父类在运行期间可以调用到子类的方法。其实在Java开发中，还有另外一个方法可以实现同样的功能或是效果，那就是——Java回调技术，通过回调在接口中定义的方法，调用到具体的实现类中的方法，其本质同样是利用Java的动态绑定技术，在这种实现中，可以不把实现类写成单独的类，而是使用匿名内部类来实现回调方法。

```
/**
 * 封装进行登录控制所需要的数据
 */
public class LoginModel {
	/**
	 * 登录人员的编号，通用的，可能是用户编号，也可能是工作人员编号
	 */
	private String loginId;
	/**
	 * 登录的密码
	 */
	private String pwd;
}

/**
 * 登录控制的模板方法需要的回调接口，
 * 需要尽可能的把所有需要的接口方法都定义出来,
 * 或者说是所有可以被扩展的方法都需要被定义出来
 */
public interface LoginCallback {
	/**
	 * 根据登录编号来查找和获取存储中相应的数据
	 * @param loginId 登录编号
	 * @return 登录编号在存储中相对应的数据
	 */
	public LoginModel findLoginUser(String loginId);
	/**
	 * 对密码数据进行加密
	 * @param pwd 密码数据
	 * @param template LoginTemplate对象，通过它来调用在
	 * 				LoginTemplate中定义的公共方法或缺省实现
	 * @return 加密后的密码数据
	 */
	public String encryptPwd(String pwd, LoginTemplate template);
	/**
	 * 判断用户填写的登录数据和存储中对应的数据是否匹配得上
	 * @param lm 用户填写的登录数据
	 * @param dbLm 在存储中对应的数据
	 * @param template LoginTemplate对象，通过它来调用在
	 * 				LoginTemplate中定义的公共方法或缺省实现
	 * @return true表示匹配成功，false表示匹配失败
	 */
	public boolean match(LoginModel lm, LoginModel dbLm, LoginTemplate template);
}

/**
 *	登录控制的模板
 */
public class LoginTemplate {
	/**
	 * 判断登录数据是否正确，也就是是否能登录成功
	 * @param lm 封装登录数据的Model
	 * @param callback LoginCallback对象
	 * @return true表示登录成功，false表示登录失败
	 */
	public final boolean login(LoginModel lm,LoginCallback callback){
		//1：根据登录人员的编号去获取相应的数据
		LoginModel dbLm = callback.findLoginUser(lm.getLoginId());
		if(dbLm!=null){
			//2：对密码进行加密
			String encryptPwd = callback.encryptPwd(lm.getPwd(),this);
			//把加密后的密码设置回到登录数据模型里面
			lm.setPwd(encryptPwd);
			//3：判断是否匹配
			return callback.match(lm, dbLm,this);
		}
		return false;
	}
	/**
	 * 对密码数据进行加密
	 * @param pwd 密码数据
	 * @return 加密后的密码数据
	 */
	public String encryptPwd(String pwd){
		return pwd;
	}
	/**
	 * 判断用户填写的登录数据和存储中对应的数据是否匹配得上
	 * @param lm 用户填写的登录数据
	 * @param dbLm 在存储中对应的数据
	 * @return true表示匹配成功，false表示匹配失败
	 */
	public boolean match(LoginModel lm,LoginModel dbLm){
		if(lm.getLoginId().equals(dbLm.getLoginId()) 
				&& lm.getPwd().equals(dbLm.getPwd())){
			return true;
		}
		return false;
	}
}

public class Client {
	public static void main(String[] args) {
		//准备登录人的信息
		LoginModel lm = new LoginModel();
		lm.setLoginId("admin");
		lm.setPwd("workerpwd");
		//准备用来进行判断的对象
		LoginTemplate lt = new LoginTemplate();
		
		//进行登录测试，先测试普通人员登录
		boolean flag = lt.login(lm,new LoginCallback(){
			public String encryptPwd(String pwd, LoginTemplate template) {
				//自己不需要，直接转调模板中的默认实现
				return template.encryptPwd(pwd);
			}
			public LoginModel findLoginUser(String loginId) {
				// 这里省略具体的处理，仅做示意，返回一个有默认数据的对象
				LoginModel lm = new LoginModel();
				lm.setLoginId(loginId);
				lm.setPwd("testpwd");
				return lm;
			}
			public boolean match(LoginModel lm, LoginModel dbLm,
					LoginTemplate template) {
				//自己不需要覆盖，直接转调模板中的默认实现
				return template.match(lm, dbLm);
			}
			
		});
		System.out.println("可以进行普通人员登录="+flag);

		//测试工作人员登录
		boolean flag2 = lt.login(lm,new LoginCallback(){
			public String encryptPwd(String pwd, LoginTemplate template) {
				//覆盖父类的方法，提供真正的加密实现
				//这里对密码进行加密，比如使用：MD5、3DES等等，省略了
				System.out.println("使用MD5进行密码加密");
				return pwd;
			}
			public LoginModel findLoginUser(String loginId) {
				// 这里省略具体的处理，仅做示意，返回一个有默认数据的对象
				LoginModel lm = new LoginModel();
				lm.setLoginId(loginId);
				lm.setPwd("workerpwd");
				return lm;
			}
			public boolean match(LoginModel lm, LoginModel dbLm,
					LoginTemplate template) {
				//自己不需要覆盖，直接转调模板中的默认实现
				return template.match(lm, dbLm);
			}
		});		
		System.out.println("可以登录工作平台="+flag2);
	}
}
```

> 两种实现方式的比较

 1. 使用继承的方式，抽象方法和具体实现的关系，是在编译期间静态决定的，是类级关系；使用Java回调，这个关系是在运行期间动态决定的，是对象级的关系。
 2. 相对而言，使用回调机制会更灵活，因为Java是单继承的，如果使用继承的方式，对于子类而言，今后就不能继承其它对象了，而使用回调，是基于接口的。从另一方面说，回调机制是通过委托的方式来组合功能，它的耦合强度要比继承低一些，这会给我们更多的灵活性。比如某些模板实现的方法，在回调实现的时候可以不调用模板中的方法，而是调用其它实现中的某些功能，也就是说功能不再局限在模板和回调实现上了，可以更灵活组织功能。
 3. 相对而言，使用继承方式会更简单点，因为父类提供了实现的方法，子类如果不想扩展，那就不用管。如果使用回调机制，回调的接口需要把所有可能被扩展的方法都定义进去，这就导致实现的时候，不管你要不要扩展，你都要实现这个方法，哪怕你什么都不做，只是转调模板中已有的实现，都要写出来。事实上，在前面讲命令模式的时候也提到了Java回调，还通过退化命令模式来实现了Java回调的功能，所以也有这样的说法：命令模式可以作为模板方法模式的一种替代实现，那就是因为可以使用Java回调来实现模板方法模式。
 
### 典型应用：排序

> 模板方法模式的一个非常典型的应用，就是实现排序的功能。在java.util包中，有一个Collections类，它里面实现了对列表排序的功能，它提供了一个静态的sort方法，接受一个列表和一个Comparator接口的实例，这个方法
> 实现的大致步骤是：

1. 先把列表转换成为对象数组
2. 通过Arrays的sort方法来对数组进行排序，传入Comparator接口的实例
3. 然后再把排好序的数组的数据设置回到原来的列表对象中去这其中的算法步骤是固定的，也就是算法骨架是固定的了，只是其中具体比较数据大小的步骤，需要由外部来提供，也就是传入的Comparator接口的实例，就是用来实现数据比较的，在算法内部会通过这个接口来回调具体的实现。

```
/**
 * 用户数据模型
 */
public class UserModel {
	private String userId,name;
	private int age;
	public UserModel(String userId,String name,int age) {
		this.userId = userId;
		this.name = name;
		this.age = age;
	}
	@Override
	public String toString(){
		return "userId="+userId+",name="+name+",age="+age;
	}
}

public class Client {
	public static void main(String[] args) {
		//准备要测试的数据
		UserModel um1 = new UserModel("u1","user1",23);
		UserModel um2 = new UserModel("u2","user2",22);
		UserModel um3 = new UserModel("u3","user3",21);
		UserModel um4 = new UserModel("u4","user4",24);
		//添加到列表中
		List<UserModel> list = new ArrayList<UserModel>();
		list.add(um1);
		list.add(um2);
		list.add(um3);
		list.add(um4);
		
		System.out.println("排序前---------------------〉");
		printList(list);
		
		//实现比较器，也可以单独用一个类来实现
		Comparator c = new Comparator(){
			public int compare(Object obj1, Object obj2) {
				//假如实现按照年龄升序排序
				UserModel tempUm1 = (UserModel)obj1;
				UserModel tempUm2 = (UserModel)obj2;
				if(tempUm1.getAge() > tempUm2.getAge()){
					return 1;
				}else if(tempUm1.getAge() == tempUm2.getAge()){
					return 0;
				}else if(tempUm1.getAge() < tempUm2.getAge()){
					return -1;
				}
				return 0;
			}};
		
			//排序	
		Collections.sort(list,c);
		
		System.out.println("排序后---------------------〉");
		printList(list);
		
	}
	private static void printList(List<UserModel> list){
		for(UserModel um : list){
			System.out.println(um);
		}
	}
}
```

> 排序，到底是模板方法模式，还是策略模式的实例，到底哪个说法更合适？

 - 认为是策略模式的实例的理由：
    - 首先上面的排序实现，并没有如同标准的模板方法模式那样，使用子类来扩展父类，至少从表面上看不太像模板方法模式；
    - 其次排序使用的Comparator的实例，可以看成是不同的算法实现，在具体排序时，会选择使用不同的Comparator实现，就相当于是在切换算法的实现。
 - 认为是模板方法模式的实例的理由：
    - 首先，模板方法模式的本质是固定算法骨架，虽然使用继承是标准的实现方式，但是通过回调来实现，也不能说这就不是模板方法模式；
    - 其次，从整体程序上看，排序的算法并没有改变，不过是某些步骤的实现发生了变化，也就是说通过Comparator来切换的是不同的比较大小的实现，相对于整个排序算法而言，它不过是其中的一个步骤而已。
 - 总结：排序的实现，实际上组合使用了模板方法模式和策略模式，从整体来看是模板方法模式，但到了局部，比如排序比较算法的实现上，就是使用的策略模式了。
 
 
### 实现通用增删改查

> 为了突出主题，以免分散大家的注意力，我们不去使用Spring和Hibernate这样的流行框架，也不去使用泛型，只用模板方法模式来实现一个简单的、用JDBC实现的通用增删改查的功能。先在数据库中定义一个演示用的表，演示用的是Oracle数据库，其实你可以用任意的数据库，只是数据类型要做相应的调整，简单的数据字典如下：

![2018-06-01_190617.png-15.8kB][3]
```
/**
 * 描述用户的数据模型
 */
public class UserModel {
	private String uuid;
	private String name;
	private int age;
}

/**
 * 描述查询用户的条件数据的模型
 */
public class UserQueryModel extends UserModel{
	/**
	 * 年龄是一个区间查询，也就是年龄查询的条件可以是：
	 * age >= 条件值1  and  age <= 条件值2 
	 * 把UserModel中的age当作条件值1，
	 * 这里定义的age2当作条件值2
	 */
	private int age2;
}


----------


/**
 * 一个简单的实现JDBC增删改查功能的模板
 */
public abstract class JDBCTemplate {
	/**
	 * 定义当前的操作类型是新增
	 */
	protected final static int TYPE_CREATE = 1;
	/**
	 * 定义当前的操作类型是修改
	 */
	protected final static int TYPE_UPDATE = 2;
	/**
	 * 定义当前的操作类型是删除
	 */
	protected final static int TYPE_DELETE = 3;
	/**
	 * 定义当前的操作类型是按条件查询
	 */
	protected final static int TYPE_CONDITION = 4;
	
/*---------------------模板方法---------------------*/	
	/**
	 * 实现新增的功能
	 * @param obj 需要被新增的数据对象
	 */
	public final void create(Object obj){
		//1：获取新增的sql
		String sql = this.getMainSql(TYPE_CREATE);
		//2：调用通用的更新实现
		this.executeUpdate(sql, TYPE_CREATE,obj);
	}
	/**
	 * 实现修改的功能
	 * @param obj 需要被修改的数据对象
	 */
	public final void update(Object obj){
		//1：获取修改的sql
		String sql = this.getMainSql(TYPE_UPDATE);
		//2：调用通用的更新实现
		this.executeUpdate(sql, TYPE_UPDATE,obj);
	}
	/**
	 * 实现删除的功能
	 * @param obj 需要被删除的数据对象
	 */
	public final void delete(Object obj){
		//1：获取删除的sql
		String sql = this.getMainSql(TYPE_DELETE);
		//2：调用通用的更新实现
		this.executeUpdate(sql, TYPE_DELETE,obj);
	}
	/**
	 * 实现按照条件查询的功能
	 * @param qm 封装查询条件的数据对象
	 * @return 符合条件的数据对象集合
	 */
	public final Collection getByCondition(Object qm){
		//1：获取查询的sql
		String sql = this.getMainSql(TYPE_CONDITION);
		//2：调用通用的查询实现
		return this.getByCondition(sql, qm);
	}
	
	
/*---------------------原语操作---------------------*/		
	/**
	 * 获取操作需要的主干sql
	 * @param type 操作类型
	 * @return 操作对应的主干sql
	 */
	protected abstract String getMainSql(int type);
	/**
	 * 为更新操作的sql中的"?"设置值
	 * @param type 操作类型
	 * @param pstmt PreparedStatement对象
	 * @param obj 操作的数据对象
	 * @throws Exception
	 */
	protected abstract void setUpdateSqlValue(int type,PreparedStatement pstmt,Object obj) throws Exception;
	
	/**
	 * 为通用查询动态的拼接sql的条件部分，基本思路是：
	 * 只有用户填写了相应的条件，那么才在sql中添加对应的条件
	 * @param sql sql的主干部分
	 * @param qm 封装查询条件的数据模型
	 * @return 拼接好的sql语句
	 */
	protected abstract String prepareQuerySql(String sql,Object qm);
	/**
	 * 为通用查询的sql动态设置条件的值
	 * @param pstmt 预处理查询sql的对象
	 * @param qm 封装查询条件的数据模型
	 * @throws Exception
	 */
	protected abstract void setQuerySqlValue(PreparedStatement pstmt,Object qm)throws Exception;
	/**
	 * 把查询返回的结果集转换成为数据对象
	 * @param rs 查询返回的结果集
	 * @return 查询返回的结果集转换成为数据对象
	 * @throws Exception
	 */
	protected abstract Object rs2Object(ResultSet rs)throws Exception;
	
/*---------------------钩子操作---------------------*/		
	/**
	 * 连接数据库的默认实现，可以被子类覆盖
	 * @return 数据库连接
	 * @throws Exception
	 */
	protected Connection getConnection()throws Exception{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		return DriverManager.getConnection(
				"jdbc:oracle:thin:@localhost:1521:orcl",
				"test","test");
	}
	/**
	 * 执行查询
	 * @param sql 查询的主干sql语句
	 * @param qm 封装查询条件的数据模型
	 * @return 查询后的结果对象集合
	 */
	protected  Collection getByCondition(String sql,Object qm){
		Collection col = new ArrayList();
		Connection conn = null;
		try{
			//调用钩子方法
			conn = this.getConnection();
			//调用原语操作
			sql = this.prepareQuerySql(sql, qm);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			//调用原语操作
			this.setQuerySqlValue(pstmt, qm);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				//调用原语操作
				col.add(this.rs2Object(rs));
			}
			rs.close();
			pstmt.close();
		}catch(Exception err){
			err.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return col;
	}
	/**
	 * 执行更改数据的sql语句，包括增删改的功能
	 * @param sql 需要执行的sql语句
	 * @param callback 回调接口，回调为sql语句赋值的方法
	 */
	protected  void executeUpdate(String sql,int type,Object obj){
		Connection conn = null;
		try{
			//调用钩子方法			
			conn = this.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			//调用原语操作
			this.setUpdateSqlValue(type,pstmt,obj);			
			pstmt.executeUpdate();			
			pstmt.close();
		}catch(Exception err){
			err.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}


----------


/**
 * 具体的实现用户管理的增删改查功能
 */
public class UserJDBC extends JDBCTemplate{	
	protected String getMainSql(int type) {
		//根据操作类型，返回相应的主干sql语句
		String sql = "";
		if(type == TYPE_CREATE){
			sql = "insert into tbl_testuser values(?,?,?)";
		}else if(type == TYPE_DELETE){
			sql = "delete from tbl_testuser where uuid=?";
		}else if(type == TYPE_UPDATE){
			sql = "update tbl_testuser set name=?,age=? where uuid=?";
		}else if(type == TYPE_CONDITION){
			sql = "select * from tbl_testuser where 1=1 ";
		}
		return sql;
	}
	protected void setUpdateSqlValue(int type, PreparedStatement pstmt,
			Object obj) throws Exception{
		//设置增、删、改操作的sql中"?"对应的值
		if(type == TYPE_CREATE){
			this.setCreateValue(pstmt, (UserModel)obj);
		}else if(type == TYPE_DELETE){
			this.setDeleteValue(pstmt, (UserModel)obj);
		}else if(type == TYPE_UPDATE){
			this.setUpdateValue(pstmt, (UserModel)obj);
		}
	}
	protected Object rs2Object(ResultSet rs)throws Exception{
		UserModel um = new UserModel();
		String uuid = rs.getString("uuid");
		String name = rs.getString("name");
		int age = rs.getInt("age");
		
		um.setAge(age);
		um.setName(name);
		um.setUuid(uuid);
		
		return um;
	}
	protected String prepareQuerySql(String sql,Object qm){
		UserQueryModel uqm = (UserQueryModel)qm;
		StringBuffer buffer = new StringBuffer();
		buffer.append(sql);
		//绝对匹配
		if(uqm.getUuid()!=null && uqm.getUuid().trim().length()>0){
			buffer.append(" and uuid=? ");
		}
		//模糊匹配
		if(uqm.getName()!=null && uqm.getName().trim().length()>0){
			buffer.append(" and name like ? ");
		}
		//区间匹配
		if(uqm.getAge() > 0){
			buffer.append(" and age >=? ");
		}
		if(uqm.getAge2() > 0){
			buffer.append(" and age <=? ");
		}
		return buffer.toString();
	}
	protected void setQuerySqlValue(PreparedStatement pstmt,Object qm)throws Exception{
		UserQueryModel uqm = (UserQueryModel)qm;
		int count = 1;
		if(uqm.getUuid()!=null && uqm.getUuid().trim().length()>0){
			pstmt.setString(count, uqm.getUuid());
			count++;
		}
		if(uqm.getName()!=null && uqm.getName().trim().length()>0){
			pstmt.setString(count, "%"+uqm.getName()+"%");
			count++;
		}
		if(uqm.getAge() > 0){
			pstmt.setInt(count, uqm.getAge());
			count++;
		}
		if(uqm.getAge2() > 0){
			pstmt.setInt(count, uqm.getAge2());
			count++;
		}
	}
	private void setCreateValue(PreparedStatement pstmt,UserModel um)throws Exception{
		pstmt.setString(1, um.getUuid());
		pstmt.setString(2, um.getName());
		pstmt.setInt(3, um.getAge());
	}
	private void setUpdateValue(PreparedStatement pstmt,UserModel um)throws Exception{
		pstmt.setString(1, um.getName());
		pstmt.setInt(2, um.getAge());
		pstmt.setString(3, um.getUuid());
	}
	private void setDeleteValue(PreparedStatement pstmt,UserModel um)throws Exception{
		pstmt.setString(1, um.getUuid());
	}
}

public class Client {
	public static void main(String[] args) {
		UserJDBC uj = new UserJDBC();
		//先新增几条
		UserModel um1 = new UserModel();
		um1.setUuid("u1");
		um1.setName("张三");
		um1.setAge(22);		
		uj.create(um1);		
		
		UserModel um2 = new UserModel();
		um2.setUuid("u2");
		um2.setName("李四");
		um2.setAge(25);		
		uj.create(um2);
		
		UserModel um3 = new UserModel();
		um3.setUuid("u3");
		um3.setName("王五");
		um3.setAge(32);		
		uj.create(um3);
		
		//测试修改
		um3.setName("王五被改了");
		um3.setAge(35);
		uj.update(um3);
		
		//测试查询
		UserQueryModel uqm = new UserQueryModel();
		uqm.setAge(25);
		uqm.setAge2(36);
		Collection<UserModel> col = uj.getByCondition(uqm);
		for(UserModel tempUm : col){
			System.out.println(tempUm);
		}
	}
}
```
 
### 模板方法模式的优缺点

> 实现代码复用
> 算法骨架不容易升级

### 模板方法模式的本质  

> 固定算法骨架

### 对设计原则的体现

> 模板方法很好的体现了开闭原则和里氏替换原则。

 - 从设计上，先分离变与不变，然后把不变的部分抽取出来，定义到父类里面，比如算法骨架，比如一些公共的、固定的实现等等。这些不变的部分被封闭起来，尽量不去修改它了，要扩展新的功能，那就使用子类来扩展，通过子类来实现可变化的步骤，对于这种新增功能的做法是开放的。
 - 能够实现统一的算法骨架，通过切换不同的具体实现来切换不同的功能，一个根本原因就是里氏替换原则，遵循这个原则，保证所有的子类实现的是同一个算法模板，并能在使用模板的地方，根据需要，切换不同的具体实现。

### 何时选用模板方法模式
1. 需要固定定义算法骨架，实现一个算法的不变的部分，并把可变的行为留给子类来实现的情况。
2. 各个子类中具有公共行为，应该抽取出来，集中在一个公共类中去实现，从而避免代码重复
3. 需要控制子类扩展的情况。模板方法模式会在特定的点来调用子类的方法，这样只允许在这些点进行扩展


  [1]: http://static.zybuluo.com/wangkaihua5/iflz6y47xmhoy98pac84bzoq/2018-06-01_185609.png
  [2]: http://static.zybuluo.com/wangkaihua5/3i95ij89fzkc0skfobfaglyh/2018-06-01_190552.png
  [3]: http://static.zybuluo.com/wangkaihua5/ms32iorbvuddsiwjhx10yfbs/2018-06-01_190617.png