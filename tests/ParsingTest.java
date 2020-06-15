import core.Application;
import core.Expression;
import core.Lambda;
import core.Variable;
import org.junit.Test;
import parsing.CLI;
import parsing.LambdaParser;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class ParsingTest {

    @Test
    public void splitterTest() {
        assertArrayEquals(new String[]{"\\", "x", "(", "y", "z", ")"}, LambdaParser.splitter.split("\\x.(y z)"));
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
        assertEquals("(a (b c))", LambdaParser.parseExpression("(a (b c))").toString());
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

    @Test
    public void parser10FunctionParens() {
        assertEquals("((λf.f) x)", LambdaParser.parseExpression("(\\f.f) x").toString());
        assertEquals("(λa.((((a b) c) d) e))", LambdaParser.parseExpression("\\a.a b c d e").toString());
        assertEquals("(((λa.a) (λb.b)) c)", LambdaParser.parseExpression("(\\a.a) (\\b.b) c").toString());
        assertEquals("(((λa.a) (λb.b)) c)", LambdaParser.parseExpression("(\\a.a) (\\b.b) c").toString());
        assertEquals("(λa.((a (λb.b)) c))", LambdaParser.parseExpression("\\a.a (\\b.b) c").toString());
        assertEquals("((λa.(a (λb.b))) c)", LambdaParser.parseExpression("(\\a.a \\b.b) c").toString());
        assertEquals("(λa.(((((a b) c) d) e) (λh.((((f g) h) i) j))))", LambdaParser.parseExpression("\\a.a b c d e \\h. f g h i j").toString());
    }

    @Test
    public void parser11VarParens() {
        assertEquals("a", LambdaParser.parseExpression("(((a)))").toString());
        assertEquals("(a b)", LambdaParser.parseExpression("(((a))) ((((b))))").toString());
    }

    @Test
    public void CLITest1() {
        LinkedList<Expression> list = new LinkedList<>();
        CLI.runCLI(new ByteArrayInputStream("""
                \\x.x
                exit
                """.getBytes()), list::add, new HashMap<>());
        assertEquals(1, list.size());
        assertEquals("(λx.x)", list.getFirst().toString());
    }

    @Test
    public void CLITest2() {
        LinkedList<Expression> list = new LinkedList<>();
        CLI.runCLI(new ByteArrayInputStream("""
                run cat
                exit
                """.getBytes()), list::add, new HashMap<>());
        assertEquals(1, list.size());
        assertEquals("cat", list.getFirst().toString());
    }

    @Test
    public void CLITest3() {
        LinkedList<Expression> list = new LinkedList<>();
        CLI.runCLI(new ByteArrayInputStream("""
                run (x y)
                exit
                """.getBytes()), list::add, new HashMap<>());
        assertEquals(1, list.size());
        assertEquals("(x y)", list.getFirst().toString());
    }

    @Test
    public void CLITest4() {
        LinkedList<Expression> list = new LinkedList<>();
        CLI.runCLI(new ByteArrayInputStream("""
                run \\x.x y
                exit
                """.getBytes()), list::add, new HashMap<>());
        assertEquals(1, list.size());
        assertEquals("(λx.(x y))", list.getFirst().toString());
    }

    @Test
    public void CLITest5() {
        LinkedList<Expression> list = new LinkedList<>();
        CLI.runCLI(new ByteArrayInputStream("""
                run (\\x . x y) (\\v.v)
                exit
                """.getBytes()), list::add, new HashMap<>());
        assertEquals(1, list.size());
        assertEquals("y", list.getFirst().toString());
    }

    @Test
    public void CLITest6() {
        LinkedList<Expression> list = new LinkedList<>();
        CLI.runCLI(new ByteArrayInputStream("""
                run (\\x. x (\\x.x)) y
                exitR
                """.getBytes()), list::add, new HashMap<>());
        assertEquals(1, list.size());
        assertEquals("(y (λx1.x1))", list.getFirst().toString());
    }

    @Test
    public void CLITest7() {
        LinkedList<Expression> list = new LinkedList<>();
        CLI.runCLI(new ByteArrayInputStream("""
                run (λy.λx.(x y)) x
                exit
                """.getBytes()), list::add, new HashMap<>());
        assertEquals(1, list.size());
        assertEquals("(λx1.(x1 x))", list.getFirst().toString());
    }

    @Test
    public void CLITest8() {
        LinkedList<Expression> list = new LinkedList<>();
        CLI.runCLI(new ByteArrayInputStream("""
                run (λy.λx.(x y)) (x x)
                 exit
                 """.getBytes()), list::add, new HashMap<>());
        assertEquals(1, list.size());
        assertEquals("(λx1.(x1 (x x)))", list.getFirst().toString());
    }

    @Test
    public void recTest1() {
        LinkedList<Expression> list = new LinkedList<>();
        CLI.runCLI(new ByteArrayInputStream("""
                0 = \\a.\\b.b
                succ = \\c.\\d.\\e.d(c d e)
                1 = succ 0
                2 = succ 1
                3 = succ 2
                true = \\f.\\g.f
                false = \\h.\\i.i
                zero? = \\j.j(\\k.false) true
                if = \\P.\\T.\\F.(P T) F
                pred = \\m\\n\\o.m (\\p\\q.q (p n)) (\\r.o) (\\s.s)
                Y = \\t.(\\u.t(u u)) (\\v.(t(v v)))
                TEST = Y \\w.\\x(if (zero? x) 0 (w (pred x)))
                ;factorial = Y \\y.\\z.(if (zero? z) 1 (* z (y (pred z))))
                run TEST 0
                exit
                 """.getBytes()), list::add, new HashMap<>());
        assertEquals("(λf.(x x))", list.pop().toString());
    }
}