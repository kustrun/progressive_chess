import Rules.Chessboard;
import Rules.Move;

import java.util.ArrayList;
import java.util.Comparator;

class SahovnicaMeta {

    private int g;
    private int h;
    private int f;
    private Chessboard sahovnica;
    private String zaporedjePotez;

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public Chessboard getSahovnica() {
        return sahovnica;
    }

    public void setSahovnica(Chessboard sahovnica) {
        this.sahovnica = sahovnica;
    }

    public String getZaporedjePotez() {
        return zaporedjePotez;
    }

    public void setZaporedjePotez(String zaporedjePotez) {
        this.zaporedjePotez = zaporedjePotez;
    }
}

class fComparator implements Comparator<Integer> {

    @Override
    public int compare(Integer x, Integer y) {

        if(x < y) {
            return -1;
        }

        if(x > y) {
            return 1;
        }

        return 0;
    }

}

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
        String solution = "";


        return solveRandom(fen);
    }

    public static String studentId() {
        return "I forgot to change this, please find my ID and deduct 5 points from my score.";
    }
}
