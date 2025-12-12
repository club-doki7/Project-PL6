package club.doki7.pl6;

import club.doki7.pl6.naive.EvalNaive;
import club.doki7.pl6.naive.Term;

import java.util.NoSuchElementException;
import java.util.Scanner;

public final class Application {
    public static void main(String[] args) {
        System.out.println("PL6 - Lambda Calculus Interpreter");
        System.out.println("Type 'exit' to quit.");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String line = scanner.nextLine();
                if (line.equalsIgnoreCase("exit")) {
                    break;
                }

                if (line.isBlank()) {
                    continue;
                }

                try {
                    Term term = Parse.parse(line);
                    if (term == null) {
                        continue;
                    }

                    Term current = term;
                    while (true) {
                        Term next = EvalNaive.evalStep(current);
                        if (next.equals(current)) {
                            break;
                        } else {
                            System.out.println("  = " + next);
                        }
                        current = next;
                    }
                } catch (Parse.ParseException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (NoSuchElementException _) {}
    }
}
