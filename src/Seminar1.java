import Rules.Chessboard;
import Rules.Move;

import java.util.ArrayList;

public class Seminar1 {

    private static String solveRandom(String fen) {
        // Solution
        String solution = "";
        // Chessboard
        Chessboard cb = Chessboard.getChessboardFromFEN(fen);
        // Loop while there are still moves left
        while (cb.getGameStatus() == Chessboard.GAME) {
            // Get all possible moves
            // ArrayList<Move> moves = Rules.getPossibleMoves(cb);
            ArrayList<Move> moves = cb.getMoves();
            // Break if no possible moves left
            if (moves.size() == 0)
                break;
            // Select a random move
            int random = (int) (Math.random() * moves.size());
            Move move = moves.get(random);
            // Add to solution
            solution += move.toString() + ";";
            // Do the move and update the chessboard (game state)
            cb.makeMove(move);
            // Print this move
            // System.out.println(move);
            // System.out.println(cb);
        }
        // Return the solution and pray for the checkmate :)
        return solution.substring(0, solution.length() - 1);
    }

    public String solve(String fen) {
        // TODO: Solve using A*
        // For now return a random solution
        return solveRandom(fen);
    }

    public static String studentId() {
        return "I forgot to change this, please find my ID and deduct 5 points from my score.";
    }
}
