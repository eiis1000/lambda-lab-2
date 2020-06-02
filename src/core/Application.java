package core;

public class Application implements Expression {
    public final Expression left;
    public final Expression right;

    public Application(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression execute() {
        if(left instanceof Lambda) {
            return ((Lambda) left).executeWith(right);
        }
        else {
            return this;
        }

    }

    public Expression substitute(Variable variable, Expression expression) {
        if (left instanceof Application) {
            return new Application((left).substitute(variable, expression), right);
        }
        else {
            return this;
        }

    }

    public String toString() {
        return "(" + left + " " + right + ")";
    }
}
