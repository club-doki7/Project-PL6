package club.doki7.pl6;

import club.doki7.pl6.naive.Term;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class TestParse {
    @Test
    public void testParseBasic() throws Parse.ParseException {
        String source = "λx.x";
        Term term = Objects.requireNonNull(Parse.parse(source));
        Assertions.assertEquals(Term.ID, term);
    }

    @Test
    public void testIfTrueFalse() throws Parse.ParseException {
        String trueSource = "λt.λf.t";
        String falseSource = "λt.λf.f";
        String ifSource = "λb.λx.λy.b x y";

        Term trueTerm = Objects.requireNonNull(Parse.parse(trueSource));
        Term falseTerm = Objects.requireNonNull(Parse.parse(falseSource));
        Term ifTerm = Objects.requireNonNull(Parse.parse(ifSource));

        Assertions.assertEquals(Term.TRUE, trueTerm);
        Assertions.assertEquals(Term.FALSE, falseTerm);
        Assertions.assertEquals(Term.IF, ifTerm);
    }

    @Test
    public void testParseZeroSucc() throws Parse.ParseException {
        String zeroSource = "λf.λx.x";
        String succSource = "λn.λf.λx.f (n f x)";

        Term zeroTerm = Objects.requireNonNull(Parse.parse(zeroSource));
        Term succTerm = Objects.requireNonNull(Parse.parse(succSource));

        Assertions.assertEquals(Term.ZERO, zeroTerm);
        Assertions.assertEquals(Term.SUCC, succTerm);
    }

    @Test
    public void testApplication() throws Parse.ParseException {
        String source = "(λx.x) λy.y";
        Term term = Objects.requireNonNull(Parse.parse(source));
        Term expectedTerm = new Term.App(
                new Term.Abs("x", new Term.Var("x")),
                new Term.Abs("y", new Term.Var("y"))
        );

        Assertions.assertEquals(expectedTerm, term);
    }
}
