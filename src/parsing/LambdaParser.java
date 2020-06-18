package parsing;

import core.Application;
import core.Expression;
import core.Lambda;
import core.Variable;

import java.util.*;
import java.util.regex.Pattern;

public class LambdaParser {

	public static final Pattern splitter = Pattern.compile("\\s+|\\.|(?<!\\s|\\.)(?=[()\\\\])|(?<=[()\\\\])(?!=\\s|\\.)");
	private static int i;
	private static List<String> tokens;
	private static Deque<Expression> expressionStack;
	private static Deque<Variable> variableStack;

	public static Expression parseExpression(String input) {
		return parseExpression(input, new HashMap<>());
	}

	public static Expression parseExpression(String input, Map<String, Expression> definedExpressions) {
		i = 0;
		tokens = new ArrayList<>(Arrays.asList(splitter.split(input.replace('\u03BB', '\\').replaceAll("\uFEFF", ""))));
		tokens.removeIf(s -> s.length() == 0); // TODO this is a really janky solution to the problem that can be fixed in lvl 2
		expressionStack = new LinkedList<>();
		variableStack = new LinkedList<>();
		Expression output = parseToParen(definedExpressions, false);
		if (i == tokens.size())
			return output;
		else if (i < tokens.size())
			throw new IllegalStateException("Did not finish parsing " + input + " with tokens " + tokens + " at location " + i + "; too many close parentheses.");
		else
			throw new IllegalStateException("Parsing has escaped the bounds of the input, indicating an error in the parser. i = " + i + ", tokens.size() = " + tokens.size());
	}

	public static  Expression parseToParen(Map<String, Expression> definedExpressions, boolean incrementOnEnd) {
		boolean hasOne = false;
		while (i < tokens.size()) {
			if (")".equals(tokens.get(i))) {
				if (incrementOnEnd)
					i++;
				return expressionStack.pop();
			} else {
				if (hasOne) {
					expressionStack.push(new Application(expressionStack.pop(), getNext(definedExpressions)));
				} else {
					expressionStack.push(getNext(definedExpressions));
					hasOne = true;
				}
			}
		}
		if (incrementOnEnd)
			throw new IllegalStateException("Finished parsing tokens " + tokens + " without returning to top of stack, ending at location " + i + "; too many open parentheses.");
		else
			return expressionStack.pop();
	}

	public static Expression getNext(Map<String, Expression> definedExpressions) {
		if ("(".equals(tokens.get(i))) {
			i++;
			return parseToParen(definedExpressions, true);
		} else if ("\\".equals(tokens.get(i))) {
			i++;
			if (tokens.get(i).length() > 0)
				variableStack.push(new Variable(tokens.get(i)));
			else
				throw new IllegalStateException("Tried to create a variable with an empty name. Parsing tokens " + tokens + " at location " + i + ".");
			i++;
			return new Lambda(variableStack.pop(), parseToParen(definedExpressions, false));
		} else {
			i++;
			return getVar(tokens.get(i - 1), definedExpressions);
		}
	}

	public static Expression getVar(String name, Map<String, Expression> definedExpressions) {
		for (Map.Entry<String, Expression> entry : definedExpressions.entrySet())
			if (entry.getKey().equals(name))
				return entry.getValue();

		Iterator<Variable> iter = variableStack.descendingIterator();
		Variable toUse = null;
		while (iter.hasNext()) {
			Variable cur = iter.next();
			if (cur.toString().equals(name)) {
				toUse = cur;
				break;
			}
		}
		if (toUse == null)
			return new Variable(name);
		else
			return toUse;
	}
}
