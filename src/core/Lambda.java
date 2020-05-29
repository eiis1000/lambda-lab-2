package core;

public class Lambda {

	private Variable boundVariable;
	private Expression innerExpression;

	public Expression executeWith(Expression toSubstitute) {
		return innerExpression.substitute(boundVariable, toSubstitute);
	}
}
