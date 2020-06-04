package parsing;

import core.Application;
import core.Expression;
import core.Lambda;
import core.Variable;

import java.util.*;
import java.util.regex.Pattern;

public class LambdaParser {

	public static Pattern splitter = Pattern.compile("\\s+|\\.|(?<!\\s|\\.)((?=\\\\)|(?=[()]))|(?<=[()])(?!=\\s|\\.)");
	private static int i;
	private static List<String> tokens;
	private static Deque<Expression> expressionStack;
	private static Deque<Variable> variableStack;

	public static Expression parseExpression(String input) {
		i = 0;
		tokens = new ArrayList<>(Arrays.asList(splitter.split(input.replace('\u03BB', '\\'))));
		expressionStack = new LinkedList<>();
		variableStack = new LinkedList<>();
		Expression output = parseToParen(false);
		if (i == tokens.size())
			return output;
		else
			throw new IllegalStateException("Did not finish parsing " + input + " with tokens " + tokens + " at location " + i + "; too many close parentheses.");
	}

	public static  Expression parseToParen(boolean incrementOnEnd) {
		boolean hasOne = false;
//		int curExpStackSize = expressionStack.size();
		while (i < tokens.size()) {
			if (")".equals(tokens.get(i))) {
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
		}
		if (incrementOnEnd)
			throw new IllegalStateException("Finished parsing tokens " + tokens + " without returning to top of stack, ending at location " + i + "; too many open parentheses.");
		else
			return expressionStack.pop();
	}

	public static Expression getNext() {
		if ("(".equals(tokens.get(i))) {
			i++;
			return parseToParen(true);
		} else if ('\\' == tokens.get(i).charAt(0)) {
			variableStack.push(new Variable(tokens.get(i).substring(1), Integer.hashCode(i)));
			i++;
			return new Lambda(variableStack.pop(), parseToParen(false));
		} else {
			i++;
			return getVar(tokens.get(i - 1));
		}
	}

	public static Variable getVar(String name) {
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
