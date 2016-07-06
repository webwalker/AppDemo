package com.webwalker.java.pattern.Behavioral.Visitor;

// "ConcreteElementA" 
public class ConcreteElementA extends Element {

	public void OperationA() {
	}

	@Override
	public void Accept(Visitor visitor) {
		visitor.VisitConcreteElementA(this);
	}
}
