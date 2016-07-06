package com.webwalker.java.pattern.Structural.Decorator;

// "ConcreteDecoratorA" 
public class ConcreteDecoratorA extends Decorator {
	private String addedState;

	public void Operation() {
		super.Operation();
		addedState = "New State";
		System.out.println("ConcreteDecoratorA.Operation()");
	}
}
