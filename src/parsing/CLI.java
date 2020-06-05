package parsing;

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
				System.out.println(LambdaParser.parseExpression(input));
			}
		}
		System.out.println("Goodbye!");
	}
}
