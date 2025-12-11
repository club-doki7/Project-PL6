package club.doki7.pl6;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public sealed interface Token {
    int line();
    int col();

    record LParen(int line, int col) implements Token {
        @Override
        public @NotNull String toString() {
            return "(";
        }
    }

    record RParen(int line, int col) implements Token {
        @Override
        public @NotNull String toString() {
            return ")";
        }
    }

    record Lambda(int line, int col) implements Token {
        @Override
        public @NotNull String toString() {
            return "λ";
        }
    }

    record Dot(int line, int col) implements Token {
        @Override
        public @NotNull String toString() {
            return ".";
        }
    }

    record Ident(String name, int line, int col) implements Token {
        @Override
        public @NotNull String toString() {
            return name;
        }
    }

    static List<Token> tokenize(String input) {
        ArrayList<Token> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        int line = 1;
        int col = 1;

        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                if (!currentToken.isEmpty()) {
                    tokens.add(new Ident(currentToken.toString(), line, col - currentToken.length()));
                    currentToken.setLength(0);
                }

                if (c == '\n') {
                    line++;
                    col = 1;
                } else {
                    col++;
                }
            } else if (c == '(') {
                if (!currentToken.isEmpty()) {
                    tokens.add(new Ident(currentToken.toString(), line, col - currentToken.length()));
                    currentToken.setLength(0);
                }
                tokens.add(new LParen(line, col));
                col++;
            } else if (c == ')') {
                if (!currentToken.isEmpty()) {
                    tokens.add(new Ident(currentToken.toString(), line, col - currentToken.length()));
                    currentToken.setLength(0);
                }
                tokens.add(new RParen(line, col));
                col++;
            } else if (c == 'λ') {
                if (!currentToken.isEmpty()) {
                    tokens.add(new Ident(currentToken.toString(), line, col - currentToken.length()));
                    currentToken.setLength(0);
                }
                tokens.add(new Lambda(line, col));
                col++;
            } else if (c == '.') {
                if (!currentToken.isEmpty()) {
                    tokens.add(new Ident(currentToken.toString(), line, col - currentToken.length()));
                    currentToken.setLength(0);
                }
                tokens.add(new Dot(line, col));
                col++;
            } else {
                currentToken.append(c);
                col++;
            }
        }

        if (!currentToken.isEmpty()) {
            tokens.add(new Ident(currentToken.toString(), line, col - currentToken.length()));
        }

        return tokens;
    }
}
