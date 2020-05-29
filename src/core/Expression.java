package core;

public interface Expression {
	Expression substitute(Variable variable, Expression expression);
}
