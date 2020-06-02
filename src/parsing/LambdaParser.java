package parsing;

import core.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class LambdaParser {

	public static Pattern splitter = Pattern.compile("\\s+|\\.|(?<!\\s|\\.)((?=\\\\)|(?=[()]))|(?<=[()])(?!=\\s|\\.)");
	private static int i;
	private static List<String> tokens;

	public Expression parseExpression(String input) {
		i = 0;
		tokens = new ArrayList<>(Arrays.asList(splitter.split(input.replace('\u03BB', '\\') + ")")));
		Expression output = getNext();
		if (i == tokens.size())
			return output;
		else
			throw new IllegalStateException("Did not finish parsing " + input + " with tokens " + tokens + " at location " + i + ".");
	}

	public Expression getNext() {
		throw new IllegalStateException("Not implemented");
	}
}
