import core.Application;
import core.Expression;
import core.Lambda;
import core.Variable;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class CoreTest {
    Expression e0 = new Lambda(new Variable("f"), new Lambda(new Variable("x"), new Variable("x")));
    Expression e1 = new Lambda(new Variable("f"), new Lambda(new Variable("x"), new Application(new Variable("f"), new Variable("x"))));
    Expression e2 = new Lambda(new Variable("f"), new Lambda(new Variable("x"), new Application(new Variable("f"), new Application(new Variable("f"), new Variable("x")))));
    Expression e3 = new Lambda(new Variable("f"), new Lambda(new Variable("x"), new Application(new Variable("f"), new Application(new Variable("f"), new Variable("x")))));
    Expression succ = new Lambda(new Variable("n"), new Lambda(new Variable("f"), new Lambda(new Variable("x"), new Application(new Variable("f"), new Application(new Application(new Variable("n"), new Variable("f")), new Variable(("x")))))));
    Expression pred = new Lambda(new Variable("n"), new Lambda(new Variable("f"), new Lambda(new Variable("x"), new Application(new Application(new Lambda(new Variable("g"), new Lambda(new Variable("h"), new Application(new Variable("h"), new Application(new Variable("g"), new Variable("f"))))), new Lambda(new Variable("u"), new Variable("x"))), new Lambda(new Variable("u"), new Variable("u"))))));
    Expression TRUE = new Lambda(new Variable("x"), new Lambda(new Variable("y"), new Variable("x")));
    Expression FALSE = new Lambda(new Variable("x"), new Lambda(new Variable("y"), new Variable("y")));
    Expression AND = new Lambda(new Variable("p"), new Lambda(new Variable("q"), new Application(new Application(new Variable("p"), new Variable("q")), new Variable("p"))));
    Expression OR = new Lambda(new Variable("p"), new Lambda(new Variable("q"), new Application(new Application(new Variable("p"), new Variable("p")), new Variable("q"))));
    Expression NOT = new Lambda(new Variable("p"), new Application(new Application(new Variable("p"), FALSE), TRUE));
    Expression IF = new Lambda(new Variable("p"), new Lambda(new Variable("t"), new Lambda(new Variable("f"), new Application(new Application(new Variable("p"), new Variable("t")), new Variable("f")))));
    Expression Y = new Lambda(new Variable("f"), new Application(new Lambda(new Variable("x"), new Application(new Variable("f"), new Application(new Variable("x"), new Variable("x")))), new Lambda(new Variable("x"), new Application(new Variable("f"), new Application(new Variable("x"), new Variable("x"))))));
    Expression ZERO = new Lambda(new Variable("j"), new Application(new Application(new Variable("j"), new Lambda(new Variable("k"), FALSE)), TRUE));
    Expression MULT=new Lambda(new Variable("m"), new Lambda(new Variable("n"), new Lambda(new Variable("f"), new Application(new Variable("m"), new Application(new Variable("n"), new Variable("f"))))));

    @Test
    public void toStringTest() {
        assertEquals("x", new Variable("x").toString());
        assertEquals("(x y)", new Application(new Variable("x"), new Variable("y")).toString());
        assertEquals("(λx.y)", new Lambda(new Variable("x"), new Variable("y")).toString());
    }

    @Test
    public void manualSucc() {
        assertEquals("(λf.(λx.(f x)))", new Application(succ, e0).executeAll().toString());
        assertEquals("(λf.(λx.(f (f x))))", new Application(succ, e1).executeAll().toString());
    }

    @Test
    public void manualPred() {
        assertEquals("(λf.(λx.x))", new Application(pred, e1).executeAll().toString());
        assertEquals("(λf.(λx.(f x)))", new Application(pred, e2).executeAll().toString());
    }

    @Test
    public void manualTest() {
        Expression TEST = new Application(Y, new Lambda(new Variable("w"), new Lambda(new Variable("x"), new Application(new Application(new Application(IF, new Application(ZERO, new Variable("x"))), e0), new Application(new Variable("w"), new Application(pred, new Variable("x")))))));
        assertEquals("(λf3.(λx4.x4))", new Application(TEST, e0).executeAll().toString());
        assertEquals("(λf4.(λx5.x5))", new Application(TEST, e1).executeAll().toString());
        assertEquals("(λf4.(λx5.x5))", new Application(TEST, e2).executeAll().toString());
    }

    @Test
    public void manualFactorial() {
        //;factorial = Y \y.\z.(((if (zero? z)) 1) (* z (y (pred z))))
        // (y, lambda (var y, lambda (var z, app(app(app(if, app(zero, var z), 1), app(var y, app(pred, z)))))
        //(Y, lambda(var y, lambda(var z, app( app( app(IF, app(ZERO, var z)), e1), app(
        Expression factorial=new Application(Y, new Lambda(new Variable("y"), new Lambda(new Variable("z"),new Application(new Application(new Application(IF, new Application(ZERO, new Variable("z"))), e1), new Application(new Variable("y"), new Application(pred, new Variable("z")))))));
        assertEquals("(λf3.(λx3.(f3 x3)))", new Application(factorial, e0).executeAll().toString());
        assertEquals("(λf4.(λx4.(f4 x4)))", new Application(factorial, e1).executeAll().toString());
        assertEquals("(λf4.(λx4.(f4 (f4 x4))))", new Application(factorial, e2).executeAll().toString());

    }
}