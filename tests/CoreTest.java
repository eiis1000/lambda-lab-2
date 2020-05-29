import core.Variable;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class CoreTest {

	@Test
	public void constructorTest() {
		assertEquals("x", new Variable("x", 2).toString());
	}
}
