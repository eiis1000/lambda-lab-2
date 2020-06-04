import core.Application;
import core.Lambda;
import core.Variable;
import org.junit.Test;
import parsing.LambdaParser;

import java.awt.*;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class ParsingTest {

    @Test
    public void splitterTest() {
        assertArrayEquals(new String[]{"\\x", "(", "y", "z", ")"}, LambdaParser.splitter.split("\\x.(y z)"));
    }

    @Test
    public void parser1SingleVar() {
        assertEquals("x", LambdaParser.parseExpression("x").toString());
    }

    @Test
    public void parser2OneApp() {
        assertEquals("(a b)", LambdaParser.parseExpression("a b").toString());
    }

    @Test
    public void parser3BasicParens() {
        assertEquals("((a b) c)", LambdaParser.parseExpression("a b c").toString());
    }

    @Test
    public void parser4IdentityLambda() {
        assertEquals("(λx.x)", LambdaParser.parseExpression("\\x.x").toString());
    }

    @Test
    public void parser5LeftAsst() {
        assertEquals("((a b) c)", LambdaParser.parseExpression("((a b) c)").toString());
    }

    @Test
    public void parser5bLeftAsst() {
        assertEquals("((a b) c)", LambdaParser.parseExpression("(a b) c").toString());
    }

    @Test
    public void parser6RightAsst() {
        assertEquals("(a (b c))", LambdaParser.parseExpression("" + "(a (b c))").toString());
    }

    @Test
    public void parser6bRightAsst() {
        assertEquals("(a (b c))", LambdaParser.parseExpression("a (b c)").toString());
    }

    @Test
    public void parser7MoreComplex() {
        assertEquals("((a (b (λx.x))) c)", LambdaParser.parseExpression("a (b \\x.x) c").toString());
    }

    @Test
    public void parser8VarWithSpaces() {
        assertEquals("cat", LambdaParser.parseExpression("cat       ").toString());
    }

    @Test
    public void parser9FunctionSpacing() {
        assertEquals("(λf.(f x))", LambdaParser.parseExpression("\\f.f x").toString());
        assertEquals("(λf.(f x))", LambdaParser.parseExpression("\\f .  f x").toString());
        assertEquals("(λf.(f x))", LambdaParser.parseExpression("  \\f.(f x)").toString());
        assertEquals("(λf.(f x))", LambdaParser.parseExpression("   \\       f    .    f      x       ").toString());
    }

    @Test
    public void parser9bFunctionSpacing() {
        assertEquals("(λf.(f x))", LambdaParser.parseExpression("(λf.(f x))").toString());
        assertEquals("(λf.(f x))", LambdaParser.parseExpression("λf.f x").toString());
        assertEquals("(λf.(f x))", LambdaParser.parseExpression("λf   . f x").toString());
        assertEquals("(λf.(f x))", LambdaParser.parseExpression("λf.(f x)").toString());
        assertEquals("(λf.(f x))", LambdaParser.parseExpression("      (λf.(f x))").toString());
    }


}
