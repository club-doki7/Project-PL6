package club.doki7.pl6.dbj;

import org.jetbrains.annotations.NotNull;

public sealed interface DTerm {
    record DVar(int index) implements DTerm {
        @Override
        public @NotNull String toString() {
            return Integer.toString(index);
        }
    }

    record DAbs(@NotNull DTerm body) implements DTerm {
        @Override
        public @NotNull String toString() {
            return "λ." + body;
        }
    }

    record DApp(@NotNull DTerm f, @NotNull DTerm arg) implements DTerm {
        @Override
        public @NotNull String toString() {
            return "(" + f + " " + arg + ")";
        }
    }
}
