import java.util.Scanner;

public class InputManager
{
    private static final Scanner SCANNER = new Scanner(System.in);
    private InputManager(){}
    public static int nextInt() {
        while (true) {
            if (SCANNER.hasNextInt()) {
                int value = SCANNER.nextInt();
                SCANNER.nextLine(); // consume leftover newline
                return value;
            } else {
                System.out.print("Please enter a number: ");
                SCANNER.nextLine(); // discard invalid input
            }
        }
    }

    public static double nextDouble() {
        double value = SCANNER.nextDouble();
        SCANNER.nextLine(); // consume leftover newline
        return value;
    }

    public static synchronized String nextLine() {
        if (SCANNER.hasNextLine()) {
            return SCANNER.nextLine();
        } else {
            // Handle no input case
        }
        return "";
    }

    public static int nextIntBetween(int minInclusive, int maxInclusive) {
        while (true) {
            int v = nextInt();
            if (v >= minInclusive && v <= maxInclusive) return v;
            System.out.print("Please enter a number between " + minInclusive + " and " + maxInclusive + ": ");
        }
    }
}
