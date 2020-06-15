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

    @Test
    public void manualLogic() {
        Expression TRUE=new Lambda(new Variable("x"), new Lambda(new Variable("y"), new Variable("x")));
        Expression FALSE=new Lambda(new Variable("x"), new Lambda(new Variable("y"), new Variable("y")));
        Expression AND=new Lambda(new Variable("p"), new Lambda(new Variable("q"), new Application(new Application(new Variable("p"), new Variable("q")), new Variable("p"))));
        Expression OR=new Lambda(new Variable("p"), new Lambda(new Variable("q"), new Application(new Application(new Variable("p"), new Variable("p")), new Variable("q"))));
        Expression NOT=new Lambda(new Variable("p"), new Application(new Application(new Variable("p"), FALSE), TRUE));
    }
}