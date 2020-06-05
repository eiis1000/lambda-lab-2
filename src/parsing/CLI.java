package parsing;

import core.Application;
import core.Expression;

import java.util.Scanner;

public class CLI {
	@SuppressWarnings("UnnecessaryContinue")
	public static void main(String[] args) {
		System.out.println("Welcome to CASprzak. Run 'help' for a command list, or 'demo' for a tutorial.");
		Scanner scan = new Scanner(System.in);
		while (true) {
			System.out.print("> ");
			String input = scan.nextLine().strip().replaceAll(";.*", "");
			if  (input.length() == 0)
				continue;
			else if (input.length() >= 4 && "exit".equals(input.substring(0, 4)))
				break;
			else {
				System.out.println(parseInput(input));
			}
		}
		System.out.println("Goodbye!");
	}

	public static Expression parseInput(String input) {
		String[] spaceSplit = input.split("\\s+", 2);
		if ("run".equals(spaceSplit[0].strip())) {
			Expression expression = parseInput(spaceSplit[1].strip());
			if (expression instanceof Application application)
				return application.execute();
			else
				return expression;
		} else if (input.indexOf('=') != -1) {
			// TODO define the thing
			return null;
		} else {
			return LambdaParser.parseExpression(input);
		}
	}
}
