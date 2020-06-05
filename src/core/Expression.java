package core;

import java.util.Set;

public interface Expression {
	Expression substitute(Variable variable, Expression expression);

	Expression executeAll();

	void getAllVariables(Set<Variable> variables);
}
