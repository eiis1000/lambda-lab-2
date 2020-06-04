import core.Application;
import core.Lambda;
import core.Variable;
import org.junit.Test;
import parsing.LambdaParser;

import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class ParsingTest {

	@Test
	public void splitterTest() {
		assertArrayEquals(new String[] {"\\x", "(", "y", "z", ")"}, LambdaParser.splitter.split("\\x.(y z)"));
	}

	@Test
	public void parser1() {
		assertEquals("x", LambdaParser.parseExpression("x").toString());
	}

	@Test
	public void parser2() {
		assertEquals("(a b)", LambdaParser.parseExpression("a b").toString());
	}

	@Test
	public void parser3() {
		assertEquals("((a b) c)", LambdaParser.parseExpression("a b c").toString());
	}

	@Test
	public void parser4() {
		assertEquals("λx.x", LambdaParser.parseExpression("\\x.x").toString());
	}
}
