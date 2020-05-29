package core;

public class Lambda implements Expression {

	private final Variable boundVariable;
	private final Expression innerExpression;

	public Lambda(Variable boundVariable, Expression innerExpression) {
		this.boundVariable = boundVariable;
		this.innerExpression = innerExpression;
	}

	public Expression executeWith(Expression toSubstitute) {
		return innerExpression.substitute(boundVariable, toSubstitute);
	}

	public Expression substitute(Variable variable, Expression expression) {
		if (variable.equals(boundVariable))
			return alphaConvert(variable).substitute(variable, expression);
		else
			return new Lambda(boundVariable, innerExpression.substitute(variable, expression));
	}

	public Lambda alphaConvert(Variable variable) {
		Variable newBound = new Variable(variable.toString() + "1", hashCode());
		return new Lambda(newBound, innerExpression.substitute(variable, newBound));
	}
}
