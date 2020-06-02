package parsing;

import java.util.regex.Pattern;

public class LambdaParser {

	public static Pattern splitter = Pattern.compile("\\s+|\\.|(?<!\\s|\\.)((?=[\\u03BB\\\\])|(?=[()]))|(?<=[()])(?!=\\s|\\.)");


}
