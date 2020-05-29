package core;

public interface Expression {
	void substitute(Variable variable, Expression expression);
}
