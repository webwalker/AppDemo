package com.webwalker.java.pattern.Behavioral.Visitor;

// "Element" 
public abstract class Element {
	public abstract void Accept(Visitor visitor);
}
