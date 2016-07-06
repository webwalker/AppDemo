package com.webwalker.java.pattern.Creational.Factory;

// "ConcreteCreator"
public class ConcreteCreatorA extends Creator {

	@Override
	public Product FactoryMethod() {
		return new ConcreteProductA();
	}
}
