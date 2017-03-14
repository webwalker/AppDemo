package com.webwalker.java.pattern.Behavioral.Visitor;

// "ConcreteElementB" 
public class ConcreteElementB extends Element {
	@Override
	public void Accept(Visitor visitor) {
		visitor.VisitConcreteElementB(this);
	}

	public void OperationB() {
	}
}
