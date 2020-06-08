package core;

import java.util.HashSet;
import java.util.Set;

public class Lambda implements Expression {

    protected final Variable boundVariable;
    protected final Expression innerExpression;

    public Lambda(Variable boundVariable, Expression innerExpression) {
        this.boundVariable = boundVariable;
        this.innerExpression = innerExpression;
    }

    public Expression executeWith(Expression toSubstitute) {
        Set<Variable> toSubstituteVars = new HashSet<>();
        toSubstitute.getAllVariables(toSubstituteVars);
        if (toSubstituteVars.contains(boundVariable))
            return alphaConvert().executeWith(toSubstitute);
        else
            return innerExpression.substitute(boundVariable, toSubstitute);
    }

    public Expression substitute(Variable variable, Expression expression) {
        if (variable.equals(boundVariable))
            return alphaConvert().substitute(variable, expression);
        else
            return new Lambda(boundVariable, innerExpression.substitute(variable, expression));
    }

    public Lambda alphaConvert() { // TODO fix stuff like x11111111111
        Variable newBound = new Variable(boundVariable.toString() + "1");
        return new Lambda(newBound, innerExpression.substitute(boundVariable, newBound));
    }

    public void getAllVariables(Set<Variable> variables) {
        variables.add(boundVariable);
        innerExpression.getAllVariables(variables);
    }

    public Expression executeAll() {
        return new Lambda(boundVariable, innerExpression.executeAll());
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
