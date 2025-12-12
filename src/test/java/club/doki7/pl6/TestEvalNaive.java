package club.doki7.pl6;

import club.doki7.pl6.naive.Term;
import org.junit.jupiter.api.Test;

public class TestEvalNaive {
    @Test
    public void testEval() {
        Term and = new Term.Abs("p", new Term.Abs("q",
                new Term.App(
                        new Term.App(
                                new Term.App(
                                        Term.IF,
                                        new Term.Var("p")
                                ),
                                new Term.Var("q")
                        ),
                        Term.FALSE
                )
        ));

        Term andTrueFalse = new Term.App(
                new Term.App(
                        and,
                        Term.TRUE
                ),
                Term.FALSE
        );

        System.out.println(andTrueFalse);
    }
}
