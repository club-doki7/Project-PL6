package club.doki7.pl6.dbj;

import org.jetbrains.annotations.NotNull;

public sealed interface DTerm {
    record DVar(@NotNull String name, int index) implements DTerm {
        @Override
        public @NotNull String toString() {
            return name + index;
        }
    }

    record DAbs(@NotNull DVar param, @NotNull DTerm body) implements DTerm {
        @Override
        public @NotNull String toString() {
            return "λ" + param + "." + body;
        }
    }

    record DApp(@NotNull DTerm f, @NotNull DTerm arg) implements DTerm {
        @Override
        public @NotNull String toString() {
            return "(" + f + " " + arg + ")";
        }
    }
}
