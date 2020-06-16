package parsing;

import core.*;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class CLI {
	public static void main(String[] args) {
		Map<String, Expression> definedExpressions = new HashMap<>();
		runCLI(
				System.in,
				output -> {
					definedExpressions.put("_", output);
					System.out.println(output);
				},
				definedExpressions
		);
	}

	@SuppressWarnings("UnnecessaryContinue")
	public static void runCLI(InputStream source, Consumer<Expression> output, Map<String, Expression> definedExpressions) {
		Scanner scan = new Scanner(source);
		while (true) {
			System.out.print("> ");
			String input = scan.nextLine().strip().replaceAll(";.*", "");
			if  (input.length() == 0)
				continue;
			else if (input.length() >= 4 && "exit".equals(input.substring(0, 4)))
				break;
			else if (input.contains("=")) {
				String[] tokens = input.split("\\s*=\\s*", 2);
				if (definedExpressions.containsKey(tokens[0])) {
					System.out.println(tokens[0] + " is already defined.");
					continue;
				}
				Expression parsed = parseInput(tokens[1], definedExpressions);
				definedExpressions.put(tokens[0], parsed);
				System.out.println("Added " + parsed + " as " + tokens[0]);
			}  else if (input.length() > 8 && "populate".equals(input.substring(0, 8))) {
				int[] ints = Arrays.stream(input.substring(9).split("\\s+")).mapToInt(Integer::parseInt).toArray();
				for (int i = ints[0]; i <= ints[1]; i++)
					createNumeral(i, definedExpressions);
				System.out.println("Populated numbers " + ints[0] + " to " + ints[1]);
			}else
				output.accept(parseInput(input, definedExpressions));
		}
		System.out.println("Goodbye!");
	}

	public static Expression parseInput(String input, Map<String, Expression> definedExpressions) {
		String[] spaceSplit = input.split("\\s+", 2);
		if ("run".equals(spaceSplit[0].strip())) {
			SubstitutionWrapper current = parseInput(spaceSplit[1].strip(), definedExpressions).executeAll();
			while (current.isUpdated)
				current = current.expression.executeAll();
			return current.expression;
		} else
			return LambdaParser.parseExpression(input, definedExpressions);
	}

	public static void createNumeral(int num, Map<String, Expression> definedExpressions) {
		if (definedExpressions.containsKey(String.valueOf(num)))
			System.out.println(num + " is already defined.");
		Expression cur = new Variable("x");
		Variable f = new Variable("f");
		for (int i = 0; i < num; i++)
			cur = new Application(f, cur);
		definedExpressions.put(String.valueOf(num), new Lambda(new Variable("f"), new Lambda(new Variable("x"), cur)));
	}

}
