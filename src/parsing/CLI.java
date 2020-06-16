package parsing;

import core.Expression;
import core.SubstitutionWrapper;

import java.io.InputStream;
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
			} else
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
}
