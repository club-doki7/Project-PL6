package club.doki7.pl6.naive;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class EvalNaive {
    public static @NotNull Term evalStep(@NotNull Term term) {
        switch (term) {
            case Term.App app -> {
                Term f = app.f();
                Term arg = app.arg();
                if (f instanceof Term.Abs abs) {
                    return applyStep(abs, arg);
                } else {
                    Term newF = evalStep(f);
                    if (!newF.equals(f)) {
                        return new Term.App(newF, arg);
                    } else {
                        Term newArg = evalStep(arg);
                        if (!newArg.equals(arg)) {
                            return new Term.App(f, newArg);
                        } else {
                            return term;
                        }
                    }
                }
            }
            case Term.Abs abs -> {
                Term newBody = evalStep(abs.body());
                if (!newBody.equals(abs.body())) {
                    return new Term.Abs(abs.param(), newBody);
                } else {
                    return term;
                }
            }
            case Term.Var var -> {
                return var;
            }
        }
    }

    private static @NotNull Term applyStep(
            @NotNull Term.Abs abs,
            @NotNull Term arg
    ) {
        HashSet<String> argFreeVars = freeVars(arg);
        Term newAbs = avoidFreeVars(abs, argFreeVars);
        if (!(newAbs instanceof Term.Abs(String param, Term body))) {
            throw new IllegalStateException("avoidFreeVars must return Term.Abs");
        }

        if (!newAbs.equals(abs)) {
            return new Term.App(newAbs, arg);
        } else {
            return subst(body, param, arg);
        }
    }

    private static @NotNull Term avoidFreeVars(
            @NotNull Term term,
            @NotNull Set<String> freeVars
    ) {
        switch (term) {
            case Term.Abs abs -> {
                if (freeVars.contains(abs.param())) {
                    Set<String> bodyFreeVars = freeVars(abs.body());

                    int n = 1;
                    String newParam = abs.param() + n;
                    while (freeVars.contains(newParam) || bodyFreeVars.contains(newParam)) {
                        n++;
                        newParam = abs.param() + n;
                    }

                    Term newBody = subst(abs.body(), abs.param(), new Term.Var(newParam));
                    return new Term.Abs(newParam, avoidFreeVars(newBody, freeVars));
                } else {
                    return new Term.Abs(abs.param(), avoidFreeVars(abs.body(), freeVars));
                }
            }
            case Term.App app -> {
                Term newF = avoidFreeVars(app.f(), freeVars);
                Term newArg = avoidFreeVars(app.arg(), freeVars);
                return new Term.App(newF, newArg);
            }
            case Term.Var var -> {
                return var;
            }
            default -> throw new IllegalStateException("Unexpected term: " + term);
        }
    }

    private static @NotNull Term subst(
            @NotNull Term term,
            @NotNull String varName,
            @NotNull Term replacement
    ) {
        switch (term) {
            case Term.Abs abs -> {
                if (abs.param().equals(varName)) {
                    return abs;
                } else {
                    Term newBody = subst(abs.body(), varName, replacement);
                    return new Term.Abs(abs.param(), newBody);
                }
            }
            case Term.App app -> {
                Term newF = subst(app.f(), varName, replacement);
                Term newArg = subst(app.arg(), varName, replacement);
                return new Term.App(newF, newArg);
            }
            case Term.Var var -> {
                if (var.name().equals(varName)) {
                    return replacement;
                } else {
                    return var;
                }
            }
            default -> throw new IllegalStateException("Unexpected term: " + term);
        }
    }

    private static @NotNull HashSet<String> freeVars(@NotNull Term term) {
        HashSet<String> freeVars = new HashSet<>();
        collectFreeVars(term, freeVars, new HashSet<>());
        return freeVars;
    }

    private static void collectFreeVars(
            @NotNull Term term,
            @NotNull Set<String> freeVars,
            @NotNull Set<String> boundVars
    ) {
        switch (term) {
            case Term.Abs abs -> {
                boundVars.add(abs.param());
                collectFreeVars(abs.body(), freeVars, boundVars);
                boundVars.remove(abs.param());
            }
            case Term.App app -> {
                collectFreeVars(app.f(), freeVars, boundVars);
                collectFreeVars(app.arg(), freeVars, boundVars);
            }
            case Term.Var var -> {
                if (!boundVars.contains(var.name())) {
                    freeVars.add(var.name());
                }
            }
        }
    }
}
