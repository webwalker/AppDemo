package com.webwalker.java.pattern.Behavioral.Interpreter;

// "TerminalExpression" 
public class TerminalExpression extends AbstractExpression {

	@Override
	public void Interpret(Context context) {
		System.out.println("Called Terminal.Interpret()");

	}
}