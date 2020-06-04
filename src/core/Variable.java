package core;

import java.util.Set;

public class Variable implements Expression {

    protected final String name;
    protected final int boundHash;

    public Variable(String name, int boundHash) {
        this.name = name;
        this.boundHash = boundHash;
    }

    public Expression substitute(Variable variable, Expression expression) {
        if (this.equals(variable))
            return expression;
        else
            return this;
    }

    public void getAllVariables(Set<Variable> variables) {
        variables.add(this);
    }

    public boolean equals(Object that) {
        if (that instanceof Variable var)
            return name.equals(var.name) && boundHash == var.boundHash;
        else
            return false;
    }

    public int hashCode() {
        return getClass().hashCode() + 3 * name.hashCode() + 31 * Integer.hashCode(boundHash);
    }

    public String toString() {
        return name;
    }

}
