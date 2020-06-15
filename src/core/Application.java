package core;

import java.util.Set;

public class Application implements Expression {

    protected final Expression left;
    protected final Expression right;

    public Application(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression substitute(Variable variable, Expression expression) {
        return new Application(left.substitute(variable, expression), right.substitute(variable, expression))
//                .execute()
                ;
    }

    public void getAllVariables(Set<Variable> variables) {
        left.getAllVariables(variables);
        right.getAllVariables(variables);
    }

    public Expression executeAll() {
        if (left instanceof Lambda lambda) {
            return lambda.executeWith(right).executeAll();
        } else {
//            Application current = new Application(left.executeAll(), right);
//            Expression executed = current.execute();
//            if (executed instanceof Application)
//                return new Application(current.left, current.right.executeAll());
//            else
//                return executed.executeAll();
//            return new Application(left.executeAll(), right.executeAll()).execute();
//            return new Application(left.executeAll(), right).execute();
            return null;
        }
    }

    public Expression execute() { // TODO this is also a janky fix
        if (left instanceof Lambda lambda) {
            return lambda.executeWith(right)
                    ;
        } else {
            return this;
        }
    }

    public boolean equals(Object that) {
        if (that instanceof Application application)
            return left.equals(application.left) && right.equals(application.right);
        else
            return false;
    }

    public int hashCode() {
        return getClass().hashCode() + 3 * left.hashCode() + 31 * right.hashCode();
    }

    public String toString() {
        return "(" + left + " " + right + ")";
    }
}
