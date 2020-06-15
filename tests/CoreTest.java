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
		Expression e0 = new Lambda(new Variable("a"), new Lambda(new Variable("b"), new Variable("b")));
	}
}
