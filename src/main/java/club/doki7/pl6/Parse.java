package club.doki7.pl6;

import club.doki7.pl6.naive.Term;
import club.doki7.pl6.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public final class Parse {
    public static class ParseException extends Exception {
        public ParseException(int line, int col, @NotNull String message) {
            super("Parse error at " + line + ":" + col + ": " + message);
        }

        public ParseException(@NotNull Token tokenLoc, @NotNull String message) {
            this(tokenLoc.line(), tokenLoc.col(), message);
        }
    }

    public static @Nullable Term parse(String inputString) throws ParseException {
        List<Token> tokens = Token.tokenize(inputString);
        if (tokens.isEmpty()) {
            return null;
        }

        ParseContext ctx = new ParseContext(tokens, 0);
        Pair<Term, ParseContext> result = parseImpl(ctx);

        if (!result.second().atEnd()) {
            Token extraToken = Objects.requireNonNull(result.second().current_token());
            throw new ParseException(
                    extraToken,
                    "Unexpected token after end of expression: " + extraToken
            );
        }

        return result.first();
    }

    private static Pair<Term, ParseContext> parseImpl(ParseContext ctx) throws ParseException {
        Pair<Term, ParseContext> result = parseAtom(ctx);
        Term term = result.first();
        ctx = result.second();

        while (true) {
            @Nullable Token peek = ctx.current_token();
            switch (peek) {
                case null -> { return new Pair<>(term, ctx); }
                case Token.RParen _ -> { return new Pair<>(term, ctx); }
                default -> {
                    Pair<Term, ParseContext> nextAtom = parseAtom(ctx);
                    term = new Term.App(term, nextAtom.first());
                    ctx = nextAtom.second();
                }
            }
        }
    }

    private static Pair<Term, ParseContext> parseAtom(ParseContext ctx) throws ParseException {
        @Nullable Token token = ctx.current_token();
        if (token == null) {
            int line = -1;
            int col = -1;

            @Nullable Token lastToken = ctx.prev_token();
            if (lastToken != null) {
                line = lastToken.line();
                col = lastToken.col();
            }

            throw new ParseException(line, col, "Unexpected end of input");
        }

        ctx = ctx.next();

        return switch (token) {
            case Token.Lambda _ -> {
                @Nullable Token paramToken = ctx.current_token();
                if (!(paramToken instanceof Token.Ident paramIdent)) {
                    Token locToken = paramToken != null ? paramToken : token;
                    throw new ParseException(locToken, "Expected identifier after 'λ'");
                }
                ctx = ctx.next();

                @Nullable Token dotToken = ctx.current_token();
                if (!(dotToken instanceof Token.Dot)) {
                    Token locToken = dotToken != null ? dotToken : paramToken;
                    throw new ParseException(locToken, "Expected '.' after lambda parameter");
                }
                ctx = ctx.next();

                Pair<Term, ParseContext> bodyResult = parseImpl(ctx);
                yield new Pair<>(new Term.Abs(paramIdent.name(), bodyResult.first()), bodyResult.second());
            }
            case Token.LParen _ -> {
                Pair<Term, ParseContext> innerResult = parseImpl(ctx);
                ctx = innerResult.second();

                @Nullable Token rparenToken = ctx.current_token();
                if (!(rparenToken instanceof Token.RParen)) {
                    Token locToken = rparenToken != null ? rparenToken : token;
                    throw new ParseException(locToken, "Expected ')' to close '('");
                }
                ctx = ctx.next();

                yield new Pair<>(innerResult.first(), ctx);
            }
            case Token.Ident ident -> new Pair<>(new Term.Var(ident.name()), ctx);
            default -> throw new ParseException(token, "Unexpected token" + token);
        };
    }

    private record ParseContext(@NotNull List<Token> tokens, int position) {
        public @Nullable Token current_token() {
            if (position >= tokens.size()) {
                return null;
            } else {
                return tokens.get(position);
            }
        }

        public @Nullable Token prev_token() {
            if (position - 1 < 0) {
                return null;
            } else {
                return tokens.get(position - 1);
            }
        }

        public @NotNull ParseContext next() {
            if (position >= tokens.size()) {
                return this;
            } else {
                return new ParseContext(tokens, position + 1);
            }
        }

        public boolean atEnd() {
            return position >= tokens.size();
        }
    }
}
