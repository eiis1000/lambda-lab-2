package core;

public class Variable implements Expression {

	private String name;
	private int boundHash;

	public Expression substitute(Variable variable, Expression expression) {
		if (this.equals(variable))
			return expression;
		else
			return this;
	}

	public boolean equals(Object that) {
		if (that instanceof Variable)
			return name.equals(((Variable) that).name) && boundHash == ((Variable) that).boundHash;
		else
			return false;
	}

	public int hashCode() {
		return name.hashCode() + Integer.hashCode(boundHash);
	}

}
