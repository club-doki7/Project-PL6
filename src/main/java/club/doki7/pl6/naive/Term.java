package club.doki7.pl6.naive;

import org.jetbrains.annotations.NotNull;

public sealed interface Term {
    record Var(@NotNull String name) implements Term {
        @Override
        public @NotNull String toString() {
            return name;
        }
    }

    record Abs(@NotNull String param, @NotNull Term body) implements Term {
        @Override
        public @NotNull String toString() {
            return "λ" + param + "." + body;
        }
    }

    record App(@NotNull Term f, @NotNull Term arg) implements Term {
        @Override
        public @NotNull String toString() {
            if (f instanceof Abs) {
                return "(" + f + ") " + arg;
            } else if (arg instanceof App || arg instanceof Abs) {
                return f + " (" + arg + ")";
            } else {
                return f + " " + arg;
            }
        }
    }

    Term ID = new Term.Abs("x", new Term.Var("x"));

    Term TRUE = new Term.Abs("t", new Term.Abs("f", new Term.Var("t")));
    Term FALSE = new Term.Abs("t", new Term.Abs("f", new Term.Var("f")));
    Term IF = new Term.Abs("b", new Term.Abs("x", new Term.Abs("y",
            new Term.App(
                    new Term.App(new Term.Var("b"), new Term.Var("x")),
                    new Term.Var("y")
            )
    )));

    Term ZERO = new Term.Abs("f", new Term.Abs("x", new Term.Var("x")));
    Term SUCC = new Term.Abs("n", new Term.Abs("f", new Term.Abs("x",
            new Term.App(
                    new Term.Var("f"),
                    new Term.App(
                            new Term.App(new Term.Var("n"), new Term.Var("f")),
                            new Term.Var("x")
                    )
            )
    )));
}
