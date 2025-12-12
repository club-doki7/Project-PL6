package club.doki7.pl6.dbj;

import org.jetbrains.annotations.NotNull;

public sealed interface DTerm {
    record Var(@NotNull String name, int index) implements DTerm {
        @Override
        public int hashCode() {
            return Integer.hashCode(index);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Var v)) return false;
            return this.index == v.index;
        }

        @Override
        public @NotNull String toString() {
            return name + index;
        }
    }

    record Abs(@NotNull DTerm body) implements DTerm {
        @Override
        public @NotNull String toString() {
            return "λ." + body;
        }
    }

    record App(@NotNull DTerm f, @NotNull DTerm arg) implements DTerm {
        @Override
        public @NotNull String toString() {
            return "(" + f + " " + arg + ")";
        }
    }
}
