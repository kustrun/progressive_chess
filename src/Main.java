import Rules.Chessboard;
import Rules.Move;
import Rules.Rules;


public class Main {

    public static void main(String[] args) {
        Seminar1 solver = new Seminar1();
        String input = getInput();
        String output = solver.solve(input);
        boolean correct = checkOutput(input, output);
        System.out.println("Input: " + input);
        System.out.println("Output: " + output);
        System.out.println("Result: " + correct);
    }

    private static boolean checkOutput(String input, String output) {
        // TODO: Create a method that checks if all moves are legal and the end state is checkmate (use provided library)
        Chessboard cb = Chessboard.getChessboardFromFEN(input);
        return false;
    }

    private static String getInput() {
        // TODO: Create a parser for input files
        // For now return this example
        return "rn1qkbnr/1pp1pppp/8/p2P4/8/5N2/PPPP1PPP/RNBbK2R w 5";
    }
}
