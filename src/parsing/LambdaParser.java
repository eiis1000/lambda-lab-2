package parsing;

import core.Application;
import core.Expression;
import core.Variable;

import java.util.*;
import java.util.regex.Pattern;

public class LambdaParser {

	public static Pattern splitter = Pattern.compile("\\s+|\\.|(?<!\\s|\\.)((?=\\\\)|(?=[()]))|(?<=[()])(?!=\\s|\\.)");
	private static int i;
	private static List<String> tokens;
	private static Deque<Expression> expressionStack;
	private static Deque<Variable> variableStack;

	public Expression parseExpression(String input) {
		i = 0;
		tokens = new ArrayList<>(Arrays.asList(splitter.split(input.replace('\u03BB', '\\'))));
		expressionStack = new LinkedList<>();
		variableStack = new LinkedList<>();
		Expression output = getNext();
		if (i == tokens.size())
			return output;
		else
			throw new IllegalStateException("Did not finish parsing " + input + " with tokens " + tokens + " at location " + i + "; too many close parentheses.");
	}

	public Expression parseToParen(boolean incrementOnEnd) {
		boolean hasOne = false;
//		int curExpStackSize = expressionStack.size();
		while (i < tokens.size()) {
			if ("(".equals(tokens.get(i))) {
				i++;
				return getNext();
			} else if (")".equals(tokens.get(i))) {
				if (incrementOnEnd)
					i++;
				return expressionStack.pop();
			} else {
				if (hasOne) {
					expressionStack.push(new Application(expressionStack.pop(), getNext()));
				} else {
					expressionStack.push(getNext());
					hasOne = true;
				}
			}
			i++;
		}
		if (incrementOnEnd)
			throw new IllegalStateException("Finished parsing tokens " + tokens + " without returning to top of stack, ending at location " + i + "; too many open parentheses.");
		else
			return expressionStack.pop();
	}

	public Expression getNext() {
		if ("(".equals(tokens.get(i))) {
			i++;
			return parseToParen(true);
		} else if ('\\' == tokens.get(i).charAt(0)) {
			variableStack.push(new Variable(tokens.get(i).substring(1), Integer.hashCode(i)));
			return parseToParen(false);
		} else {
			return getVar(tokens.get(i));
		}
	}

	public Variable getVar(String name) {
		Iterator<Variable> iter = variableStack.descendingIterator();
		Variable toUse = null;
		while (iter.hasNext()) {
			Variable cur = iter.next();
			if (cur.toString().equals(name)) {
				toUse = cur;
				break;
			}
		}
		if (toUse == null) {
			toUse = new Variable(name, Integer.hashCode(i));
//			variableStack.push(toUse);
		}
		return toUse;
	}
}
