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
        Term expectedTerm = new Term.Abs("x", new Term.Var("x"));

        Assertions.assertEquals(expectedTerm, term);
    }

    @Test
    public void testIfTrueFalse() throws Parse.ParseException {
        String trueSource = "λt.λf.t";
        String falseSource = "λt.λf.f";
        String ifSource = "λb.λx.λy.b x y";

        Term trueTerm = Objects.requireNonNull(Parse.parse(trueSource));
        Term falseTerm = Objects.requireNonNull(Parse.parse(falseSource));
        Term ifTerm = Objects.requireNonNull(Parse.parse(ifSource));

        Term expectedTrueTerm = new Term.Abs("t", new Term.Abs("f", new Term.Var("t")));
        Term expectedFalseTerm = new Term.Abs("t", new Term.Abs("f", new Term.Var("f")));
        Term expectedIfTerm = new Term.Abs("b", new Term.Abs("x", new Term.Abs("y",
                new Term.App(
                        new Term.App(new Term.Var("b"), new Term.Var("x")),
                        new Term.Var("y")
                )
        )));

        Assertions.assertEquals(expectedTrueTerm, trueTerm);
        Assertions.assertEquals(expectedFalseTerm, falseTerm);
        Assertions.assertEquals(expectedIfTerm, ifTerm);
    }

    @Test
    public void testParseZeroSucc() throws Parse.ParseException {
        String zeroSource = "λf.λx.x";
        String succSource = "λn.λf.λx.f (n f x)";

        Term zeroTerm = Objects.requireNonNull(Parse.parse(zeroSource));
        Term succTerm = Objects.requireNonNull(Parse.parse(succSource));

        Term expectedZeroTerm = new Term.Abs("f", new Term.Abs("x", new Term.Var("x")));
        Term expectedSuccTerm = new Term.Abs("n", new Term.Abs("f", new Term.Abs("x",
                new Term.App(
                        new Term.Var("f"),
                        new Term.App(
                                new Term.App(new Term.Var("n"), new Term.Var("f")),
                                new Term.Var("x")
                        )
                )
        )));

        Assertions.assertEquals(expectedZeroTerm, zeroTerm);
        Assertions.assertEquals(expectedSuccTerm, succTerm);
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
