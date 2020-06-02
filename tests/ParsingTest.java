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
}
