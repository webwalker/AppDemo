/**
 * 
 */
package com.webwalker.java.pattern;

import java.util.ArrayList;

import com.webwalker.java.pattern.Behavioral.Chain.ConcreteHandler1;
import com.webwalker.java.pattern.Behavioral.Chain.ConcreteHandler2;
import com.webwalker.java.pattern.Behavioral.Chain.ConcreteHandler3;
import com.webwalker.java.pattern.Behavioral.Chain.Handler;
import com.webwalker.java.pattern.Behavioral.Command.Command;
import com.webwalker.java.pattern.Behavioral.Command.ConcreteCommand;
import com.webwalker.java.pattern.Behavioral.Command.Invoker;
import com.webwalker.java.pattern.Behavioral.Command.Receiver;
import com.webwalker.java.pattern.Behavioral.Interpreter.AbstractExpression;
import com.webwalker.java.pattern.Behavioral.Interpreter.Context;
import com.webwalker.java.pattern.Behavioral.Interpreter.NonterminalExpression;
import com.webwalker.java.pattern.Behavioral.Interpreter.TerminalExpression;
import com.webwalker.java.pattern.Behavioral.Iterator.ConcreteAggregate;
import com.webwalker.java.pattern.Behavioral.Iterator.ConcreteIterator;
import com.webwalker.java.pattern.Behavioral.Mediator.ConcreteColleague1;
import com.webwalker.java.pattern.Behavioral.Mediator.ConcreteColleague2;
import com.webwalker.java.pattern.Behavioral.Mediator.ConcreteMediator;
import com.webwalker.java.pattern.Behavioral.Memento.Caretaker;
import com.webwalker.java.pattern.Behavioral.Memento.Originator;
import com.webwalker.java.pattern.Behavioral.Observer.ConcreteObserver;
import com.webwalker.java.pattern.Behavioral.Observer.ConcreteSubject;
import com.webwalker.java.pattern.Behavioral.State.ConcreteStateA;
import com.webwalker.java.pattern.Behavioral.Strategy.ConcreteStrategyA;
import com.webwalker.java.pattern.Behavioral.Strategy.ConcreteStrategyB;
import com.webwalker.java.pattern.Behavioral.Strategy.ConcreteStrategyC;
import com.webwalker.java.pattern.Behavioral.Template.AbstractClass;
import com.webwalker.java.pattern.Behavioral.Template.ConcreteClassA;
import com.webwalker.java.pattern.Behavioral.Template.ConcreteClassB;
import com.webwalker.java.pattern.Behavioral.Visitor.ConcreteElementA;
import com.webwalker.java.pattern.Behavioral.Visitor.ConcreteElementB;
import com.webwalker.java.pattern.Behavioral.Visitor.ConcreteVisitor1;
import com.webwalker.java.pattern.Behavioral.Visitor.ConcreteVisitor2;
import com.webwalker.java.pattern.Behavioral.Visitor.ObjectStructure;
import com.webwalker.java.pattern.Creational.AbstractFactory.AbstractFactory;
import com.webwalker.java.pattern.Creational.AbstractFactory.Client;
import com.webwalker.java.pattern.Creational.AbstractFactory.ConcreteFactory1;
import com.webwalker.java.pattern.Creational.AbstractFactory.ConcreteFactory2;
import com.webwalker.java.pattern.Creational.Builder.Builder;
import com.webwalker.java.pattern.Creational.Builder.ConcreteBuilder1;
import com.webwalker.java.pattern.Creational.Builder.ConcreteBuilder2;
import com.webwalker.java.pattern.Creational.Builder.Director;
import com.webwalker.java.pattern.Creational.Factory.ConcreteCreatorA;
import com.webwalker.java.pattern.Creational.Factory.ConcreteCreatorB;
import com.webwalker.java.pattern.Creational.Factory.Creator;
import com.webwalker.java.pattern.Creational.Prototype.ConcretePrototype1;
import com.webwalker.java.pattern.Creational.Prototype.ConcretePrototype2;
import com.webwalker.java.pattern.Creational.Singleton.Singleton;
import com.webwalker.java.pattern.Structural.Adapter.Adapter;
import com.webwalker.java.pattern.Structural.Adapter.Target;
import com.webwalker.java.pattern.Structural.Bridge.Abstraction;
import com.webwalker.java.pattern.Structural.Bridge.ConcreteImplementorA;
import com.webwalker.java.pattern.Structural.Bridge.ConcreteImplementorB;
import com.webwalker.java.pattern.Structural.Bridge.RefinedAbstraction;
import com.webwalker.java.pattern.Structural.Composite.Composite;
import com.webwalker.java.pattern.Structural.Composite.Leaf;
import com.webwalker.java.pattern.Structural.Decorator.ConcreteComponent;
import com.webwalker.java.pattern.Structural.Decorator.ConcreteDecoratorA;
import com.webwalker.java.pattern.Structural.Decorator.ConcreteDecoratorB;
import com.webwalker.java.pattern.Structural.Facade.Facade;
import com.webwalker.java.pattern.Structural.Flyweight.Flyweight;
import com.webwalker.java.pattern.Structural.Flyweight.FlyweightFactory;
import com.webwalker.java.pattern.Structural.Flyweight.UnsharedConcreteFlyweight;
import com.webwalker.java.pattern.Structural.Proxy.Proxy;

/**
 * @author Administrator
 * 
 */
public class PatternTest {
	// / <summary>
	// / ������ģʽ��
	// / �������ķ����ߺͽ�����֮�����ϣ���ʹ��������л��ᴦ
	// / ��������󡣽���Щ��������һ���������������������ݸ�����ֱ���и�����������
	// / </summary>
	void Chain() {
		// Setup Chain of Responsibility
		Handler h1 = new ConcreteHandler1();
		Handler h2 = new ConcreteHandler2();
		Handler h3 = new ConcreteHandler3();
		h1.SetSuccessor(h2);
		h2.SetSuccessor(h3);

		// Generate and process request
		int[] requests = { 2, 5, 14, 22, 18, 3, 27, 20 };

		for (int request : requests) {
			h1.HandleRequest(request);
		}
	}

	// / <summary>
	// / ����ģʽ��
	// / ��һ�������װΪһ�����󣬴Ӷ�ʹ������ò�ͬ������Կͻ����в�����
	// / �������Ŷӻ��¼������־���Լ�֧�ֿ���ȡ���Ĳ���
	// / </summary>
	void Command() {
		// Create receiver, command, and invoker
		Receiver receiver = new Receiver();
		Command command = new ConcreteCommand(receiver);
		Invoker invoker = new Invoker();

		// Set and execute command
		invoker.SetCommand(command);
		invoker.ExecuteCommand();
	}

	// / <summary>
	// / ������ģʽ��
	// / ����һ�����ԣ����������ķ���һ�ֱ�ʾ��������һ�����������ڽ����ض��ķ�
	// / </summary>
	void Interpreter() {
		Context context = new Context();

		// Usually a tree
		ArrayList<AbstractExpression> list = new ArrayList<AbstractExpression>();

		// Populate 'abstract syntax tree'
		list.add(new TerminalExpression());
		list.add(new NonterminalExpression());
		list.add(new TerminalExpression());
		list.add(new TerminalExpression());

		// Interpret
		for (AbstractExpression exp : list) {
			exp.Interpret(context);
		}
	}

	// / <summary>
	// / ������ģʽ��
	// / �ṩһ�ַ���˳�����һ���ۺ϶����еĸ���Ԫ�أ������豩¶�ö�����ڲ���ʾ
	// / </summary>
	void Iterator() {
		ConcreteAggregate a = new ConcreteAggregate();
		a.setItems(0, "Item A");
		a.setItems(1, "Item B");
		a.setItems(2, "Item C");
		a.setItems(3, "Item D");

		// Create Iterator and provide aggregate
		ConcreteIterator i = new ConcreteIterator(a);

		System.out.println("Iterating over collection:");

		Object item = i.First();
		while (item != null) {
			System.out.println(item);
			item = i.Next();
		}
	}

	// / <summary>
	// / ��ͣ��ģʽ��
	// / ��һ���н��������װһϵ�еĶ��󽻻����н���ʹ��������Ҫ��ʽ�໥���ã�
	// / �Ӷ�ʹ�������ɢ�����Զ����ı��໥֮��Ľ���
	// / </summary>
	void Mediator() {
		ConcreteMediator m = new ConcreteMediator();

		ConcreteColleague1 c1 = new ConcreteColleague1(m);
		ConcreteColleague2 c2 = new ConcreteColleague2(m);

		m.setColleague1(c1);
		m.setColleague2(c2);

		c1.Send("How are you?");
		c2.Send("Fine, thanks");
	}

	// / <summary>
	// / ����¼ģʽ��
	// / ���ƻ���װ��ǰ���£����������ڲ�״̬�����ڸö���֮�Ᵽ�����״̬
	// / </summary>
	void Memento() {
		Originator o = new Originator();
		o.setState("On");

		// Store internal state
		Caretaker c = new Caretaker();
		c.setMemento(o.CreateMemento());

		// Continue changing originator
		o.setState("Off");

		// Restore saved state
		o.SetMemento(c.getMemento());
	}

	// / <summary>
	// / �۲���ģʽ��
	// / ���������һ��һ�Զ��������ϵ��
	// / �Ա㵱һ�������״̬�����仯ʱ�������������еĶ��󶼵õ�֪ͨ��ˢ��
	// / </summary>
	void Observer() {
		// Configure Observer pattern
		ConcreteSubject s = new ConcreteSubject();

		s.Attach(new ConcreteObserver(s, "X"));
		s.Attach(new ConcreteObserver(s, "Y"));
		s.Attach(new ConcreteObserver(s, "Z"));

		// Change subject and notify observers
		s.setSubjectState("ABC");
		s.Notify();
	}

	// / <summary>
	// / ״̬ģʽ��
	// / ����һ���������ڲ�״̬�ı��ʱ��ı�������Ϊ�����������ƺ��޸����������ࡣ
	// / </summary>
	void State() {
		// Setup context in a state

		com.webwalker.java.pattern.Behavioral.State.Context c = new com.webwalker.java.pattern.Behavioral.State.Context(
				new ConcreteStateA());

		// Issue requests, which toggles state
		c.Request();
		c.Request();
		c.Request();
		c.Request();
	}

	// / <summary>
	// / ����ģʽ��
	// / ����һϵ�е��㷨�������Ƿ�װ�������������໥�滻��ʹ�㷨�����ڿͻ�
	// / </summary>
	void Strategy() {

		com.webwalker.java.pattern.Behavioral.Strategy.Context context;

		// Three contexts following different strategies
		context = new com.webwalker.java.pattern.Behavioral.Strategy.Context(
				new ConcreteStrategyA());
		context.ContextInterface();

		context = new com.webwalker.java.pattern.Behavioral.Strategy.Context(
				new ConcreteStrategyB());
		context.ContextInterface();

		context = new com.webwalker.java.pattern.Behavioral.Strategy.Context(
				new ConcreteStrategyC());
		context.ContextInterface();
	}

	// / <summary>
	// / ģ�巽��ģʽ��
	// / ����һ�������е��㷨�Ǽܣ�������Щ�㷨�ľ���ʵ�ֵĴ����ӳٵ����������
	// / </summary>
	void Template() {
		AbstractClass c;

		c = new ConcreteClassA();
		c.TemplateMethod();

		c = new ConcreteClassB();
		c.TemplateMethod();
	}

	// / <summary>
	// / ������ģʽ��
	// / ʶһ��������ĳ����ṹ�еĸ�Ԫ�صĲ�����
	// / �ڲ��ı��Ԫ�ص����ǰ���¶������������Ԫ�ص��²���
	// / </summary>
	void Visitor() {
		// Setup structure
		ObjectStructure o = new ObjectStructure();
		o.Attach(new ConcreteElementA());
		o.Attach(new ConcreteElementB());

		// Create visitor objects
		ConcreteVisitor1 v1 = new ConcreteVisitor1();
		ConcreteVisitor2 v2 = new ConcreteVisitor2();

		// Structure accepting visitors
		o.Accept(v1);
		o.Accept(v2);
	}

	// Creational

	// / <summary>
	// / ����ģʽ��
	// / �ͻ���͹�����ֿ����������κ�ʱ����Ҫĳ�ֲ�Ʒ��ֻ���򹤳����󼴿ɡ�
	// / �����������޸ľͿ��Խ����²�Ʒ��ȱ���ǵ���Ʒ�޸�ʱ��������ҲҪ����Ӧ���޸ġ�
	// / �磺��δ����������ͻ����ṩ��
	// / </summary>
	void AbstractFactory() {
		// Abstract factory #1
		AbstractFactory factory1 = new ConcreteFactory1();
		Client client1 = new Client(factory1);
		client1.Run();

		// Abstract factory #2
		AbstractFactory factory2 = new ConcreteFactory2();
		Client client2 = new Client(factory2);
		client2.Run();
	}

	// / <summary>
	// / ������ģʽ��һ�����Ӷ���Ĺ��������ı�ʾ���룬ʹ��ͬ���Ĺ������̿��Դ�����ͬ�ı�ʾ
	// / </summary>
	void Builder() {
		// Create director and builders
		Director director = new Director();

		Builder b1 = new ConcreteBuilder1();
		Builder b2 = new ConcreteBuilder2();

		// Construct two products
		director.Construct(b1);

		com.webwalker.java.pattern.Creational.Builder.Product p1 = b1.GetResult();
		p1.Show();

		director.Construct(b2);
		com.webwalker.java.pattern.Creational.Builder.Product p2 = b2.GetResult();
		p2.Show();
	}

	// / <summary>
	// / ��������ģʽ��
	// / ����һ�����ڴ�������Ľӿڣ��������������һ����ʵ������
	// / Factory Method��һ�����ʵ�����ӳٵ�����
	// / </summary>
	void Factory() {
		// An array of creators
		Creator[] creators = new Creator[2];
		creators[0] = new ConcreteCreatorA();
		creators[1] = new ConcreteCreatorB();

		// Iterate over creators and create products
		for (Creator creator : creators) {
			com.webwalker.java.pattern.Creational.Factory.Product product = creator
					.FactoryMethod();
			System.out.println("Created " + product.getClass().getName());
		}
	}

	// / <summary>
	// / ԭʼģ��ģʽ��
	// / ��ԭ��ʵ��ָ��������������࣬����ͨ���������ԭ���������µĶ���
	// / </summary>
	void Prototype() {
		// Create two instances and clone each

		ConcretePrototype1 p1 = new ConcretePrototype1("I");
		ConcretePrototype1 c1 = (ConcretePrototype1) p1.Clone();
		System.out.println("Cloned: " + c1.getId());

		ConcretePrototype2 p2 = new ConcretePrototype2("II");
		ConcretePrototype2 c2 = (ConcretePrototype2) p2.Clone();
		System.out.println("Cloned: " + c2.getId());
	}

	// / <summary>
	// / ����ģʽ��
	// / ��֤һ�������һ��ʵ�������ṩһ����������ȫ�ֵ㡣
	// / </summary>
	void Single() {
		// Constructor is protected -- cannot use new
		Singleton s1 = Singleton.Instance();
		Singleton s2 = Singleton.Instance();

		if (s1 == s2) {
			System.out.println("Objects are the same instance");
		}
	}

	// Structural

	// / <summary>
	// / ������ģʽ��
	// / ��һ����Ľӿ�ת���ɿͻ�ϣ������һ���ӿڣ�
	// / ʹ��ԭ�����ڽӿڲ����ݶ�����һ��������Щ�����һ����
	// / </summary>
	void Adapter() {
		Target target = new Adapter();
		target.Request();
	}

	// / <summary>
	// / ����ģʽ�������󲿷�������ʵ�ֲ��ַ��룬ʹ֮���Զ����仯
	// / </summary>
	void Bridge() {
		Abstraction ab = new RefinedAbstraction();

		// Set implementation and call
		ab.setImplementor(new ConcreteImplementorA());
		ab.Operation();

		// Change implemention and call
		ab.setImplementor(new ConcreteImplementorB());
		ab.Operation();
	}

	// / <summary>
	// / �ϳ�ģʽ��
	// / ��������ϳ����νṹ�Ա�ʾ�����֣����塱�Ĳ�νṹ��
	// / Compositeʹ�ÿͻ��Ե�������͸��϶����ʹ�þ���һ����
	// / </summary>
	void Composite() {
		// Create a tree structure
		Composite root = new Composite("root");
		root.Add(new Leaf("Leaf A"));
		root.Add(new Leaf("Leaf B"));

		Composite comp = new Composite("Composite X");
		comp.Add(new Leaf("Leaf XA"));
		comp.Add(new Leaf("Leaf XB"));

		root.Add(comp);
		root.Add(new Leaf("Leaf C"));

		// Add and remove a leaf
		Leaf leaf = new Leaf("Leaf D");
		root.Add(leaf);
		root.Remove(leaf);

		// Recursively display tree
		root.Display(1);
	}

	// / <summary>
	// / װ��ģʽ��
	// / ��̬�ظ�һ���������һЩ�����ְ��
	// / ����չ���ܶ��ԣ�Decoratorģʽ���������෽ʽ�������
	// / </summary>
	void Decorator() {
		// Create ConcreteComponent and two Decorators
		ConcreteComponent c = new ConcreteComponent();
		ConcreteDecoratorA d1 = new ConcreteDecoratorA();
		ConcreteDecoratorB d2 = new ConcreteDecoratorB();

		// Link decorators
		d1.SetComponent(c);
		d2.SetComponent(d1);

		d2.Operation();
	}

	// / <summary>
	// / ���ģʽ��
	// / Ϊ��ϵͳ�е�һ��ӿ��ṩһ��һ�µĽ��棬
	// / Facadeģʽ������һ���߲�ӿڣ�ʹ�������ϵͳ��������ʹ��
	// / </summary>
	void Facade() {
		Facade facade = new Facade();

		facade.MethodA();
		facade.MethodB();
	}

	// / <summary>
	// / ��Ԫģʽ��
	// / ���ù�������Ч��֧�ִ���ϸ���ȵĶ���
	// / </summary>
	void Flyweight() {
		// Arbitrary extrinsic state
		int extrinsicstate = 22;

		FlyweightFactory factory = new FlyweightFactory();

		// Work with different flyweight instances
		Flyweight fx = factory.GetFlyweight("X");
		fx.Operation(--extrinsicstate);

		Flyweight fy = factory.GetFlyweight("Y");
		fy.Operation(--extrinsicstate);

		Flyweight fz = factory.GetFlyweight("Z");
		fz.Operation(--extrinsicstate);

		UnsharedConcreteFlyweight fu = new UnsharedConcreteFlyweight();

		fu.Operation(--extrinsicstate);
	}

	// / <summary>
	// / ����ģʽ��
	// / Ϊ���������ṩһ�������Կ��ƶ��������ķ���
	// / </summary>
	void Proxy() {
		// Create proxy and request a service
		Proxy proxy = new Proxy();
		proxy.Request();
	}
}
