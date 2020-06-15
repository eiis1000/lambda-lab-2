import core.Application;
import core.Expression;
import core.Lambda;
import core.Variable;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class CoreTest {

    @Test
    public void toStringTest() {
        assertEquals("x", new Variable("x").toString());
        assertEquals("(x y)", new Application(new Variable("x"), new Variable("y")).toString());
        assertEquals("(λx.y)", new Lambda(new Variable("x"), new Variable("y")).toString());
    }

    @Test
    public void manual1() {
        Expression e0 = new Lambda(new Variable("f"), new Lambda(new Variable("x"), new Variable("x")));
        Expression e1 = new Lambda(new Variable("f"), new Lambda(new Variable("x"), new Application(new Variable("f"), new Variable("x"))));
        Expression e2 = new Lambda(new Variable("f"), new Lambda(new Variable("x"), new Application(new Variable("f"), new Application(new Variable("f"), new Variable("x")))));
        Expression e3 = new Lambda(new Variable("f"), new Lambda(new Variable("x"), new Application(new Variable("f"), new Application(new Variable("f"), new Variable("x")))));
    }

    @Test
    public void manualSucc() {
        Expression succ = new Lambda(new Variable("n"), new Lambda(new Variable("f"), new Lambda(new Variable("x"), new Application(new Variable("f"), new Application(new Application(new Variable("n"), new Variable("f")), new Variable(("x")))))));
    }

    @Test
    public void manualPred() {
        Expression pred = new Lambda(new Variable("n"), new Lambda(new Variable("f"), new Lambda(new Variable("x"), new Application( new Application(new Lambda(new Variable("g"), new Lambda(new Variable("h"), new Application(new Variable("h"), new Application(new Variable("g"), new Variable("f"))))), new Lambda(new Variable("u"), new Variable("x"))), new Lambda(new Variable("u"), new Variable("u"))))));
    }

}