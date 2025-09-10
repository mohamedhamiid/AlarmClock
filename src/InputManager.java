import java.util.Scanner;

public class InputManager
{
    private static final Scanner SCANNER = new Scanner(System.in);
    private InputManager(){}
    public static int nextInt() {
        int value = SCANNER.nextInt();
        SCANNER.nextLine(); // consume leftover newline
        return value;
    }

    public static double nextDouble() {
        double value = SCANNER.nextDouble();
        SCANNER.nextLine(); // consume leftover newline
        return value;
    }

    public static String nextLine() {
        return SCANNER.nextLine();
    }
}
