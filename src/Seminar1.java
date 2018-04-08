import Rules.Chessboard;
import Rules.Move;
import Rules.Rules;

import java.util.*;

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

class fComparator implements Comparator<SahovnicaMeta> {

    @Override
    public int compare(SahovnicaMeta x, SahovnicaMeta y) {

        if(x.getF() > y.getF()) {
            return -1;
        }

        if(x.getF() < y.getF()) {
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


    public static boolean checkIfChessboardExists(PriorityQueue<SahovnicaMeta> sahovnice, Chessboard sahovnica) {

        String fen1 = sahovnica.getFEN().split(" ")[0];
        for(SahovnicaMeta trenutna : sahovnice) {
            String fen2 = trenutna.getSahovnica().getFEN().split(" ")[0];

            if(fen1.equals(fen2)) {
                return true;
            }

        }

        return false;

    }

    public static String sestejZaporedneStevilke(String fen) {

        String noviFen = "";
        int len = fen.length();
        for(int i=0; i<len; i++) {
            char znak = fen.charAt(i);
            if(Character.isDigit(znak)) {
                while((i+1) < len && Character.isDigit(fen.charAt(i+1))) {
                    znak = (char) (znak + fen.charAt(i+1) - '0');
                    i++;
                }

            }

            noviFen += znak;
        }

        return noviFen;
    }

    public static int[] poisciKralja(Chessboard sahovnica) {

        int[] kralj = new int[2];
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                if((sahovnica.getColor() == Chessboard.BLACK && sahovnica.getBoard()[i][j] == Chessboard.KING)
                        || (sahovnica.getColor() == Chessboard.WHITE && sahovnica.getBoard()[i][j] == Chessboard.KING_B)) {
                    kralj[0] = j;
                    kralj[1] = i;

                    return kralj;
                }
            }
        }

        return null;

    }

    public static int pokritostPolja(Chessboard sahovnica, int to_x, int to_y) {

        int stevec = 0;

        HashMap<String, Move> mozniPremiki = Rules.getPossibleMovesMap(sahovnica);
        for (Map.Entry<String, Move> mozniPremik : mozniPremiki.entrySet()) {
            Move premik = mozniPremik.getValue();
            int[] koordinate = premik.getCoordinates();

            if(koordinate[2] == to_x && koordinate[3] == to_y) {
                stevec += 1;
            }
        }

        return stevec;
    }

    public static int pokritostMatnegaKvadrata(Chessboard sahovnica) {

        String fen = sahovnica.getFEN();

        //poisci kralja
        int x = -1;
        int y = -1;
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                if(sahovnica.getColor() == Chessboard.BLACK && sahovnica.getBoard()[i][j] == Chessboard.KING) {
                    x = j;
                    y = i;
                    break;
                } else if(sahovnica.getColor() == Chessboard.WHITE && sahovnica.getBoard()[i][j] == Chessboard.KING_B) {
                    x = j;
                    y = i;
                    break;
                }
            }

            if(x != -1 && y != -1) {
                break;
            }
        }

        int st = 0;
        for(int i=-1; i<2; i++) {
            if((x+i) < 0 || (x+i) > 7) {
                continue;
            }

            for(int j=-1; j<2; j++) {
                if((y+j) < 0 || (y+j) > 7) {
                    continue;
                }

                if(sahovnica.getColor() == Chessboard.BLACK) {

                    //Preveri ali je morda polje ze pokrito z nasprotnikovo figuro
                    if (sahovnica.getBoard()[y + j][x + i] > Chessboard.EMPTY && sahovnica.getBoard()[y + j][x + i] < Chessboard.KING) {
                        st += 1;
                    }
                    //polje se ni pokrito z nasprotnikovo figuro
                    else if (sahovnica.getBoard()[y + j][x + i] == Chessboard.EMPTY) {
                        st += pokritostPolja(sahovnica, y + j, x + i);
                    }

                } else if(sahovnica.getColor() == Chessboard.WHITE) {

                    //Preveri ali je morda polje ze pokrito z nasprotnikovo figuro
                    if (sahovnica.getBoard()[y + j][x + i] < Chessboard.EMPTY && sahovnica.getBoard()[y + j][x + i] > Chessboard.KING_B) {
                        st += 1;
                    }
                    //polje se ni pokrito z nasprotnikovo figuro
                    else if (sahovnica.getBoard()[y + j][x + i] == Chessboard.EMPTY) {
                        st += pokritostPolja(sahovnica, y + j, x + i);
                    }

                }
            }
        }

        return st;
    }

    public static int pokritostMatnegaKvadrataIgnoriranje(Chessboard sahovnica) {

        String fen = sahovnica.getFEN();
        String[] fenData = fen.split(" ");
        String noviFen = "";

        if(sahovnica.getColor() == Chessboard.BLACK) {
            noviFen = fenData[0].replaceAll("[A-Z&&[^K]]", "1");
            noviFen = sestejZaporedneStevilke(noviFen);
            noviFen += " " + fenData[1] + " " + fenData[2];
        } else if(sahovnica.getColor() == Chessboard.WHITE) {
            noviFen = fenData[0].replaceAll("[a-z&&[^k]]", "1");
            noviFen = sestejZaporedneStevilke(noviFen);
            noviFen += " " + fenData[1] + " " + fenData[2];
        }

        Chessboard novaSahovnica = Chessboard.getChessboardFromFEN(noviFen);

        //poisci kralja
        int[] kralj = poisciKralja(novaSahovnica);

        int st = 0;
        for(int i=-1; i<2; i++) {
            if((kralj[0]+i) < 0 || (kralj[0]+i) > 7) {
                continue;
            }

            for(int j=-1; j<2; j++) {
                if((kralj[1]+j) < 0 || (kralj[1]+j) > 7) {
                    continue;
                }

                if (novaSahovnica.getBoard()[kralj[1] + j][kralj[0] + i] == Chessboard.EMPTY) {
                    st += pokritostPolja(novaSahovnica, kralj[1] + j, kralj[0] + i);
                }
            }
        }

        return st;
    }

    public static String findCheckmate(PriorityQueue<SahovnicaMeta> sahovnice) {

        PriorityQueue<SahovnicaMeta> preiskaneSahovnice = new PriorityQueue<>(1, new fComparator());

        //pokritostMatnegaKvadrataIgnoriranje(Chessboard.getChessboardFromFEN("B7/2k5/1N6/1K6/8/8/8/8 w 4"));

        while(!sahovnice.isEmpty()) {
            SahovnicaMeta sahovnica = sahovnice.poll();
            preiskaneSahovnice.add(sahovnica);

            if(sahovnica.getSahovnica().getGameStatus() == Chessboard.CHECKMATE && sahovnica.getSahovnica().getMovesLeft() == 0) {
                System.out.println("konec");
                return sahovnica.getZaporedjePotez();

            } else {
                HashMap<String, Move> mozniPremiki = Rules.getPossibleMovesMap(sahovnica.getSahovnica());
                for (Map.Entry<String, Move> mozniPremik : mozniPremiki.entrySet()) {
                    Move premik = mozniPremik.getValue();
                    //System.out.println("premik: " + premik.toString());

                    Chessboard novaPostavitev = Chessboard.getChessboardFromFEN(sahovnica.getSahovnica().getFEN());
                    novaPostavitev.makeMove(premik);
                    //System.out.println(novaPostavitev.getFEN());

                    if(!checkIfChessboardExists(sahovnice, novaPostavitev)) {
                        int g = sahovnica.getF();
                        int h = pokritostMatnegaKvadrataIgnoriranje(novaPostavitev);

                        SahovnicaMeta novaSahovnica = new SahovnicaMeta();
                        novaSahovnica.setF(g+h);
                        novaSahovnica.setG(g);
                        novaSahovnica.setH(h);
                        novaSahovnica.setSahovnica(novaPostavitev);

                        String opravljenePoteze = sahovnica.getZaporedjePotez();
                        opravljenePoteze = opravljenePoteze.equals("") ? mozniPremik.getKey() : opravljenePoteze + ";" + mozniPremik.getKey();
                        novaSahovnica.setZaporedjePotez(opravljenePoteze);

                        sahovnice.add(novaSahovnica);
                    }
                }

            }
        }

        return "";
    }

    public String solve(String fen) {
        // TODO: Solve using A*
        // For now return a random solution
        String solution = "";

        PriorityQueue<SahovnicaMeta> sahovnice = new PriorityQueue<SahovnicaMeta>(1, new fComparator());

        SahovnicaMeta sahovnica = new SahovnicaMeta();
        sahovnica.setF(0);
        sahovnica.setG(0);
        sahovnica.setH(0);
        sahovnica.setSahovnica(Chessboard.getChessboardFromFEN(fen));
        sahovnica.setZaporedjePotez("");

        sahovnice.add(sahovnica);

        return findCheckmate(sahovnice);

        /*
        HashMap<String, Move> mozniPremiki = Rules.getPossibleMovesMap(zacetnaPostavitev);
        for (Map.Entry<String, Move> mozniPremik: mozniPremiki.entrySet()) {
            System.out.println(mozniPremik.getKey());
            Move premik = mozniPremik.getValue();

            System.out.println("wait");
        }


        SahovnicaMeta sahovnica = new SahovnicaMeta();
        sahovnica.setG(0);
        sahovnica.setH(0);
        sahovnica.setF(0);


        System.out.println("wait");
        */
    }

    public static String studentId() {
        return "I forgot to change this, please find my ID and deduct 5 points from my score.";
    }
}
