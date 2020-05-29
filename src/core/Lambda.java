package core;

public class Lambda implements Expression{

	private Variable boundVariable;
	private Expression innerExpression;

	public Expression executeWith(Expression toSubstitute) {
		return innerExpression.substitute(boundVariable, toSubstitute);
	}

	public Expression substitute(Variable variable, Expression expression) {
		throw new UnsupportedOperationException();
	}
}
