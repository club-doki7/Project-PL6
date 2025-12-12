package club.doki7.pl6;

import club.doki7.pl6.naive.Term;
import org.junit.jupiter.api.Test;

public class TestEvalNaive {
    @Test
    public void testEval() {
        Term two = new Term.App(
                Term.SUCC,
                new Term.App(
                        Term.SUCC,
                        Term.ZERO
                )
        );

        System.out.println(two);
    }
}
