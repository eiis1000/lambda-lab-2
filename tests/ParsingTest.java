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
    public void recTest0() {
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
        assertEquals("(λa2.(λb2.b2))", list.pop().toString());
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
                run TEST 1
                exit
                 """.getBytes()), list::add, new HashMap<>());
        assertEquals("(λa2.(λb2.b2))", list.pop().toString());
    }

    @Test
    public void recTest2() {
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
                run TEST 2
                exit
                 """.getBytes()), list::add, new HashMap<>());
        assertEquals("(λa2.(λb2.b2))", list.pop().toString());
    }

    @Test
    public void ft0() {
        LinkedList<Expression> list = new LinkedList<>();
        CLI.runCLI(new ByteArrayInputStream("""
                ; BOOLEANS AND BRANCHING
                true = λx.λy.x
                false = \\f.\\x.x ; same as 0
                not = λp.p false true
                and = λp.λq.p q p
                or = λp.λq.p p q
                xor = \\p.\\q.p (not q) q
                if = λb.λT.λF.((b T) F)
                ; NUMBER OPERATIONS
                succ = \\n.\\f.\\x.f (n f x)
                pred = λn.λf.λx.n (λg.λh.h (g f)) (λu.x) (λu.u)
                + = λm.λn.λf.λx.(m f) ((n f) x)
                * = λn.λm.λf.λx.n (m f) x
                - = λm.λn.(n pred) m
                even? = λn.n not true
                odd? = \\x.not (even? x)
                zero? = \\n.n (\\x.false) true
                leq? = \\m.\\n.zero?(- m n) ; "less than or equal to"
                lt? = \\a.\\b.not (leq? b a)
                gt? = \\a.\\b.not (leq? a b)
                eq? = \\m.\\n.and (leq? m n) (leq? n m)
                neq? = (not (eq? a b)) ; "not equal"
                geq? = \\a.\\b.(leq? B a)
                ; GENERATING NUMBERS WITH RUN
                0 = \\f.\\x.x ; same as false
                1 = run succ 0
                2 = run succ 1
                3 = run + 2 1
                4 = run * 2 2
                5 = (λf.(λx.(f (f (f (f (f x)))))))
                7 = run succ (succ 5)
                6 = run pred 7
                10 = run succ (+ 3 6)
                9 = run pred 10
                8 = run - 10 2
                ; LINKED LISTS
                cons = λx.λy.λf.f x y ; makes a cons pair (x y)
                car = λp.p true
                cdr = λp.p false
                null = \\x.true
                null? = λp.p (λx.λy.false) ; true if null, false if a pair, UNDEFINED otherwise
                ; Y COMBINATOR
                Y = λf. (λx. f(x x)) (λx. f(x x))
                ; FUN FUNCTIONS THAT USE Y
                factorial = Y \\f.\\n.(if (zero? n) 1 (* n (f (- n 1))))
                ; divpair returns a cons box of the quotient and remainder of a division
                divpair = Y (λg.λq.λa.λb. lt? a b (cons q a) (g (succ q) (- a b) b)) 0
                / = λa.\\b. car (divpair a b)
                mod = λa.\\b. cdr (divpair a b)
                ; Now we can make statements like
                ; run factorial 3
                ; run + 2 (factorial 3)
                ; run (/ (* 3 6) 2)
                run factorial 0
                exit
                 """.getBytes()), list::add, new HashMap<>());
        assertEquals("(λa2.(λb2.(a2 b2)))", list.pop().toString());
    }

    @Test
    public void ft1() {
        LinkedList<Expression> list = new LinkedList<>();
        CLI.runCLI(new ByteArrayInputStream("""
                ; BOOLEANS AND BRANCHING
                true = λx.λy.x
                false = \\f.\\x.x ; same as 0
                not = λp.p false true
                and = λp.λq.p q p
                or = λp.λq.p p q
                xor = \\p.\\q.p (not q) q
                if = λb.λT.λF.((b T) F)
                ; NUMBER OPERATIONS
                succ = \\n.\\f.\\x.f (n f x)
                pred = λn.λf.λx.n (λg.λh.h (g f)) (λu.x) (λu.u)
                + = λm.λn.λf.λx.(m f) ((n f) x)
                * = λn.λm.λf.λx.n (m f) x
                - = λm.λn.(n pred) m
                even? = λn.n not true
                odd? = \\x.not (even? x)
                zero? = \\n.n (\\x.false) true
                leq? = \\m.\\n.zero?(- m n) ; "less than or equal to"
                lt? = \\a.\\b.not (leq? b a)
                gt? = \\a.\\b.not (leq? a b)
                eq? = \\m.\\n.and (leq? m n) (leq? n m)
                neq? = (not (eq? a b)) ; "not equal"
                geq? = \\a.\\b.(leq? B a)
                ; GENERATING NUMBERS WITH RUN
                0 = \\f.\\x.x ; same as false
                1 = run succ 0
                2 = run succ 1
                3 = run + 2 1
                4 = run * 2 2
                5 = (λf.(λx.(f (f (f (f (f x)))))))
                7 = run succ (succ 5)
                6 = run pred 7
                10 = run succ (+ 3 6)
                9 = run pred 10
                8 = run - 10 2
                ; LINKED LISTS
                cons = λx.λy.λf.f x y ; makes a cons pair (x y)
                car = λp.p true
                cdr = λp.p false
                null = \\x.true
                null? = λp.p (λx.λy.false) ; true if null, false if a pair, UNDEFINED otherwise
                ; Y COMBINATOR
                Y = λf. (λx. f(x x)) (λx. f(x x))
                ; FUN FUNCTIONS THAT USE Y
                factorial = Y \\f.\\n.(if (zero? n) 1 (* n (f (- n 1))))
                ; divpair returns a cons box of the quotient and remainder of a division
                divpair = Y (λg.λq.λa.λb. lt? a b (cons q a) (g (succ q) (- a b) b)) 0
                / = λa.\\b. car (divpair a b)
                mod = λa.\\b. cdr (divpair a b)
                ; Now we can make statements like
                ; run factorial 3
                ; run + 2 (factorial 3)
                ; run (/ (* 3 6) 2)
                run factorial 1
                exit
                 """.getBytes()), list::add, new HashMap<>());
        assertEquals("(λa2.(λb2.(a2 b2)))", list.pop().toString());
    }

    @Test
    public void ft2() {
        LinkedList<Expression> list = new LinkedList<>();
        CLI.runCLI(new ByteArrayInputStream("""
                ; BOOLEANS AND BRANCHING
                true = λx.λy.x
                false = \\f.\\x.x ; same as 0
                not = λp.p false true
                and = λp.λq.p q p
                or = λp.λq.p p q
                xor = \\p.\\q.p (not q) q
                if = λb.λT.λF.((b T) F)
                ; NUMBER OPERATIONS
                succ = \\n.\\f.\\x.f (n f x)
                pred = λn.λf.λx.n (λg.λh.h (g f)) (λu.x) (λu.u)
                + = λm.λn.λf.λx.(m f) ((n f) x)
                * = λn.λm.λf.λx.n (m f) x
                - = λm.λn.(n pred) m
                even? = λn.n not true
                odd? = \\x.not (even? x)
                zero? = \\n.n (\\x.false) true
                leq? = \\m.\\n.zero?(- m n) ; "less than or equal to"
                lt? = \\a.\\b.not (leq? b a)
                gt? = \\a.\\b.not (leq? a b)
                eq? = \\m.\\n.and (leq? m n) (leq? n m)
                neq? = (not (eq? a b)) ; "not equal"
                geq? = \\a.\\b.(leq? B a)
                ; GENERATING NUMBERS WITH RUN
                0 = \\f.\\x.x ; same as false
                1 = run succ 0
                2 = run succ 1
                3 = run + 2 1
                4 = run * 2 2
                5 = (λf.(λx.(f (f (f (f (f x)))))))
                7 = run succ (succ 5)
                6 = run pred 7
                10 = run succ (+ 3 6)
                9 = run pred 10
                8 = run - 10 2
                ; LINKED LISTS
                cons = λx.λy.λf.f x y ; makes a cons pair (x y)
                car = λp.p true
                cdr = λp.p false
                null = \\x.true
                null? = λp.p (λx.λy.false) ; true if null, false if a pair, UNDEFINED otherwise
                ; Y COMBINATOR
                Y = λf. (λx. f(x x)) (λx. f(x x))
                ; FUN FUNCTIONS THAT USE Y
                factorial = Y \\f.\\n.(if (zero? n) 1 (* n (f (- n 1))))
                ; divpair returns a cons box of the quotient and remainder of a division
                divpair = Y (λg.λq.λa.λb. lt? a b (cons q a) (g (succ q) (- a b) b)) 0
                / = λa.\\b. car (divpair a b)
                mod = λa.\\b. cdr (divpair a b)
                ; Now we can make statements like
                ; run factorial 3
                ; run + 2 (factorial 3)
                ; run (/ (* 3 6) 2)
                run factorial 2
                exit
                 """.getBytes()), list::add, new HashMap<>());
        assertEquals("(λa2.(λb2.(a2 (a2 b2))))", list.pop().toString());
    }

    @Test
    public void ft3() {
        LinkedList<Expression> list = new LinkedList<>();
        CLI.runCLI(new ByteArrayInputStream("""
                ; BOOLEANS AND BRANCHING
                true = λx.λy.x
                false = \\f.\\x.x ; same as 0
                not = λp.p false true
                and = λp.λq.p q p
                or = λp.λq.p p q
                xor = \\p.\\q.p (not q) q
                if = λb.λT.λF.((b T) F)
                ; NUMBER OPERATIONS
                succ = \\n.\\f.\\x.f (n f x)
                pred = λn.λf.λx.n (λg.λh.h (g f)) (λu.x) (λu.u)
                + = λm.λn.λf.λx.(m f) ((n f) x)
                * = λn.λm.λf.λx.n (m f) x
                - = λm.λn.(n pred) m
                even? = λn.n not true
                odd? = \\x.not (even? x)
                zero? = \\n.n (\\x.false) true
                leq? = \\m.\\n.zero?(- m n) ; "less than or equal to"
                lt? = \\a.\\b.not (leq? b a)
                gt? = \\a.\\b.not (leq? a b)
                eq? = \\m.\\n.and (leq? m n) (leq? n m)
                neq? = (not (eq? a b)) ; "not equal"
                geq? = \\a.\\b.(leq? B a)
                ; GENERATING NUMBERS WITH RUN
                0 = \\f.\\x.x ; same as false
                1 = run succ 0
                2 = run succ 1
                3 = run + 2 1
                4 = run * 2 2
                5 = (λf.(λx.(f (f (f (f (f x)))))))
                7 = run succ (succ 5)
                6 = run pred 7
                10 = run succ (+ 3 6)
                9 = run pred 10
                8 = run - 10 2
                ; LINKED LISTS
                cons = λx.λy.λf.f x y ; makes a cons pair (x y)
                car = λp.p true
                cdr = λp.p false
                null = \\x.true
                null? = λp.p (λx.λy.false) ; true if null, false if a pair, UNDEFINED otherwise
                ; Y COMBINATOR
                Y = λf. (λx. f(x x)) (λx. f(x x))
                ; FUN FUNCTIONS THAT USE Y
                factorial = Y \\f.\\n.(if (zero? n) 1 (* n (f (- n 1))))
                ; divpair returns a cons box of the quotient and remainder of a division
                divpair = Y (λg.λq.λa.λb. lt? a b (cons q a) (g (succ q) (- a b) b)) 0
                / = λa.\\b. car (divpair a b)
                mod = λa.\\b. cdr (divpair a b)
                ; Now we can make statements like
                ; run factorial 3
                ; run + 2 (factorial 3)
                ; run (/ (* 3 6) 2)
                run factorial 3
                exit
                 """.getBytes()), list::add, new HashMap<>());
        assertEquals("(λa2.(λb2.(a2 (a2 (a2 (a2 (a2 (a2 (b2)))))))))", list.pop().toString());
    }
}