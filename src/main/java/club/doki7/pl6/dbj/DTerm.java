package club.doki7.pl6.dbj;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public sealed interface DTerm {
    record Var(@NotNull String name, int index) implements DTerm {
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Var v)) return false;
            return this.index == v.index;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(index);
        }

        @Override
        public @NotNull String toString() {
            return name + index;
        }
    }

    record Abs(@NotNull String name, @NotNull DTerm body) implements DTerm {
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Abs a)) return false;
            return this.body.equals(a.body);
        }

        @Override
        public int hashCode() {
            // \x.M == \y.M[y/x], but \x.M != M, so introduce a dummy hash component
            return Objects.hash("λ", body);
        }

        @Override
        public @NotNull String toString() {
            return "λ" + name + "." + body;
        }
    }

    record App(@NotNull DTerm f, @NotNull DTerm arg) implements DTerm {
        @Override
        public @NotNull String toString() {
            StringBuilder sb = new StringBuilder();
            if (!(f instanceof Var)) {
                sb.append("(").append(f).append(")");
            } else {
                sb.append(f);
            }
            sb.append(" ");
            if (!(arg instanceof Var)) {
                sb.append("(").append(arg).append(")");
            } else {
                sb.append(arg);
            }

            return sb.toString();
        }
    }
}
