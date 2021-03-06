package core;

import java.util.Set;

public class Variable implements Expression {

    protected final String name;

    public Variable(String name) {
        if (name.isBlank())
            throw new IllegalArgumentException("Created a variable with an empty name.");
        for (char c : name.toCharArray())
            if (!Character.isLetterOrDigit(c)) {
                throw new IllegalArgumentException("Invalid variable name " + name + ": Variable names must be alphanumeric.");
            }
        this.name = name;
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

    public SubstitutionWrapper executeAll() {
        return new SubstitutionWrapper(false, this);
    }

    public boolean equals(Object that) {
        if (that instanceof Variable var)
            return name.equals(var.name);
        else
            return false;
    }

    public int hashCode() {
        return getClass().hashCode() + 3 * name.hashCode();
    }

    public String toString() {
        return name;
    }

}
