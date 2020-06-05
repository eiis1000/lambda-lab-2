package parsing;

import core.Expression;

import java.io.InputStream;
import java.util.Scanner;
import java.util.function.Consumer;

public class CLI {
	public static void main(String[] args) {
		runCLI(System.in, System.out::println);
	}

	@SuppressWarnings("UnnecessaryContinue")
	public static void runCLI(InputStream source, Consumer<Expression> output) {
		Scanner scan = new Scanner(source);
		while (true) {
			System.out.print("> ");
			String input = scan.nextLine().strip().replaceAll(";.*", "");
			if  (input.length() == 0)
				continue;
			else if (input.length() >= 4 && "exit".equals(input.substring(0, 4)))
				break;
			else {
				output.accept(parseInput(input));
			}
		}
		System.out.println("Goodbye!");
	}

	public static Expression parseInput(String input) {
		String[] spaceSplit = input.split("\\s+", 2);
		if ("run".equals(spaceSplit[0].strip())) {
			return parseInput(spaceSplit[1].strip()).executeAll();
		} else if (input.indexOf('=') != -1) {
			// TODO define the thing
			return null;
		} else {
			return LambdaParser.parseExpression(input);
		}
	}
}
