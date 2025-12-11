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
            return "(" + f + " " + arg + ")";
        }
    }
}
