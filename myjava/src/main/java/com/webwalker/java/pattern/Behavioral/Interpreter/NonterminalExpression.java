package com.webwalker.java.pattern.Behavioral.Interpreter;

// "NonterminalExpression" 
public class NonterminalExpression extends AbstractExpression {
	@Override
	public void Interpret(Context context) {
		System.out.println("Called Nonterminal.Interpret()");

	}
}
