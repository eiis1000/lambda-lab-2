package core;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Lambda implements Expression {

    private static final Pattern lastNumberSplit = Pattern.compile("(?<!\\d)(?=\\d*$)");
    private Set<Variable> vars = null;
    protected final Variable boundVariable;
    protected final Expression innerExpression;

    public Lambda(Variable boundVariable, Expression innerExpression) {
        this.boundVariable = boundVariable;
        this.innerExpression = innerExpression;
    }

    public SubstitutionWrapper executeWith(Expression toSubstitute) {
        return new SubstitutionWrapper(true, innerExpression.substitute(boundVariable, toSubstitute));
    }

    public Expression substitute(Variable variable, Expression expression) {
        if (vars == null) {
            vars = new HashSet<>();
            expression.getAllVariables(vars);
        }
        if (vars.contains(boundVariable))
            return alphaConvert().substitute(variable, expression);
        if (variable.equals(boundVariable))
            return alphaConvert().substitute(variable, expression);
        else {
            return new Lambda(boundVariable, innerExpression.substitute(variable, expression));
        }
    }

    public Lambda alphaConvert() {
        Variable newBound;
        if (boundVariable.toString().isEmpty())
            newBound = new Variable("1");
        else {
            String[] s = lastNumberSplit.split(boundVariable.toString(), 2);
            if (s.length > 1 && s[1].length() > 0) {
                newBound = new Variable(s[0] + (Integer.parseInt(s[1]) + 1));
            } else
                newBound = new Variable(s[0] + "1");
        }

        return new Lambda(newBound, innerExpression.substitute(boundVariable, newBound));
    }

    public void getAllVariables(Set<Variable> variables) {
        variables.add(boundVariable);
        innerExpression.getAllVariables(variables);
    }

    public SubstitutionWrapper executeAll() {
        SubstitutionWrapper wrapper = innerExpression.executeAll();
        return new SubstitutionWrapper(wrapper.isUpdated, new Lambda(boundVariable, wrapper.expression));
    }

    public boolean equals(Object that) { // TODO \x.x and \y.y
        if (that instanceof Lambda lambda)
            return boundVariable.equals(lambda.boundVariable) && innerExpression.equals(lambda.innerExpression);
        else
            return false;
    }

    public int hashCode() {
        return getClass().hashCode() + 3 * innerExpression.hashCode() + 31 * boundVariable.hashCode();
    }

    public String toString() {
        return "(λ" + boundVariable + "." + innerExpression + ")";
    }
}
