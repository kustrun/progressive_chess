import Rules.Chessboard;
import Rules.Move;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

class SahovnicaMeta {

    private int g;
    private int h;
    private int f;
    private int hash;
    private int stPotez;
    private int stVsehPotez;
    private Chessboard sahovnica;
    private Chessboard sahovnicaBrezNasprotnika;
    private String zaporedjePotez;
    private int[][] matniKvadrat;

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

    public int getStPotez() {
        return stPotez;
    }

    public void setStPotez(int stPotez) {
        this.stPotez = stPotez;
    }

    public int getStVsehPotez() {
        return stVsehPotez;
    }

    public void setStVsehPotez(int stVsehPotez) {
        this.stVsehPotez = stVsehPotez;
    }

    public Chessboard getSahovnica() {
        return sahovnica;
    }

    public void setSahovnica(Chessboard sahovnica) {
        this.sahovnica = sahovnica;
    }

    public Chessboard getSahovnicaBrezNasprotnika() {
        return sahovnicaBrezNasprotnika;
    }

    public void setSahovnicaBrezNasprotnika(Chessboard sahovnicaBrezNasprotnika) {
        this.sahovnicaBrezNasprotnika = sahovnicaBrezNasprotnika;
    }

    public String getZaporedjePotez() {
        return zaporedjePotez;
    }

    public void setZaporedjePotez(String zaporedjePotez) {
        this.zaporedjePotez = zaporedjePotez;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public int[][] getMatniKvadrat() {
        return matniKvadrat;
    }

    public void setMatniKvadrat(int[][] matniKvadrat) {
        this.matniKvadrat = matniKvadrat;
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

class Zobrist {

    private final int[][] tabela = new int[64][12];

    public Zobrist() {
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 12; j++) {
                tabela[i][j] = (int) (((long) (Math.random() * Long.MAX_VALUE)) & 0xFFFFFFFF);
            }
        }
    }

    public int vrednostZobrist(Chessboard sahovnica) {
        int hash = 0;
        for(int i = 0; i < 8; i++) {
            for(int j=0; j < 8; j++) {
                int figura = sahovnica.getBoard()[i][j];

                if(figura != 0) {
                    if (figura > 0) {
                        figura -= 1;
                    } else if (figura < 0) { //CRNE FIGURE: 6=kmet, 11=kralj
                        figura = Math.abs(figura) + 5;
                    }

                    hash = hash ^ tabela[i * 8 + j][figura];
                }
            }
        }

        return hash;
    }

    public int posodobitevZobrist(int hash, Chessboard sahovnica, int[] koordinate) {
        //premik iz polja i na polje j
        int from_y = koordinate[0];
        int from_x = koordinate[1];

        int to_y = koordinate[2];
        int to_x = koordinate[3];

        int i = (from_y*8) + from_x;
        int j = (to_y*8) + to_x;

        int figura = sahovnica.getBoard()[from_y][from_x];
        int figuraPojedena = sahovnica.getBoard()[to_y][to_x];

        if (figura > 0) {   //BELE FIGURE: 0=kmet, 1=tekac, 2=konj, 3=trdnjava, 4=kraljica, 5=kralj
            figura -= 1;
        } else if (figura < 0) { //CRNE FIGURE: 6=kmet, 7=tekac, 8=konj, 9=trdnjava, 10=kraljica, 11=kralj
            figura = Math.abs(figura) + 5;
        }

        if (figuraPojedena > 0) {   //BELE FIGURE: 0=kmet, 1=tekac, 2=konj, 3=trdnjava, 4=kraljica, 5=kralj
            figuraPojedena -= 1;
        } else if (figuraPojedena < 0) { //CRNE FIGURE: 6=kmet, 7=tekac, 8=konj, 9=trdnjava, 10=kraljica, 11=kralj
            figuraPojedena = Math.abs(figuraPojedena) + 5;
        }

        if(figuraPojedena != 0) {
            hash = hash ^ tabela[j][figuraPojedena];
        }

        hash = hash ^ tabela[i][figura] ^ tabela[j][figura];

        return hash;
    }

}

public class Seminar1 {

    public static int[] poisciKralja(Chessboard sahovnica) {

        int[] kralj = new int[2];
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                if((sahovnica.getColor() == Chessboard.BLACK && sahovnica.getBoard()[i][j] == Chessboard.KING)
                        || (sahovnica.getColor() == Chessboard.WHITE && sahovnica.getBoard()[i][j] == Chessboard.KING_B)) {
                    kralj[0] = i;
                    kralj[1] = j;

                    return kralj;
                }
            }
        }

        return null;
    }

    public static int[][] poisciMatniKvadrat(int[] kralj) {

        int[][] matniKvadrat = new int[8][8];

        for(int i=-1; i<2; i++) {
            int y = kralj[0]+i;
            if(y < 0 || y > 7) {
                continue;
            }

            for(int j=-1; j<2; j++) {
                int x = kralj[1]+j;
                if(x < 0 || x > 7) {
                    continue;
                }

                if(!(y == kralj[0] && x == kralj[1])) {
                    matniKvadrat[y][x] = 1;
                }
            }
        }

        /*for(int i=0; i<8; i++){
            for(int j=0; j<8; j++) {
                System.out.print(matniKvadrat[7-i][j] + " ");
            }

            System.out.println();
        }*/

        return matniKvadrat;
    }

    public static int pokritostMatnegaKvadrata(SahovnicaMeta sahovnicaMeta) {

        int pokritost = 0;

        List<Move> premiki = sahovnicaMeta.getSahovnica().getMoves();
        for(Move premik : premiki) {
            int[] koordinate = premik.getCoordinates();

            pokritost += sahovnicaMeta.getMatniKvadrat()[koordinate[2]][koordinate[3]];
        }


        return pokritost;
    }

    public static int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[8][8];

        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++){
                newBoard[i][j] = board[i][j];
            }
        }

        return newBoard;
    }

    public static String poisciMat(PriorityQueue<SahovnicaMeta> sahovnice) {

        Zobrist zobrist = new Zobrist();
        int hash = zobrist.vrednostZobrist(sahovnice.peek().getSahovnica());
        sahovnice.peek().setHash(hash);

        HashMap<Integer, SahovnicaMeta> preiskaneSahovnice = new HashMap<Integer, SahovnicaMeta>();

        while(!sahovnice.isEmpty()) {
            SahovnicaMeta sahovnica = sahovnice.poll();
            String fen = sahovnica.getSahovnica().getFEN();
            hash = sahovnica.getHash();
            preiskaneSahovnice.put(hash, sahovnica);

            if(sahovnica.getSahovnica().getGameStatus() == Chessboard.CHECKMATE && sahovnica.getSahovnica().getMovesLeft() == 0) {
                System.out.println("konec");
                return sahovnica.getZaporedjePotez();

            } else {
                List<Move> mozniPremiki = sahovnica.getSahovnica().getMoves();

                for (Move premik : mozniPremiki) {
                    hash = zobrist.posodobitevZobrist(hash, sahovnica.getSahovnica(), premik.getCoordinates());

                    if(!preiskaneSahovnice.containsKey(hash)) {

                        Chessboard novaPostavitev = new Chessboard(copyBoard(sahovnica.getSahovnica().getBoard()), sahovnica.getSahovnica().getColor(), sahovnica.getSahovnica().getMovesLeft());
                        novaPostavitev.makeMove(premik);

                        SahovnicaMeta novaSahovnicaMeta = new SahovnicaMeta();
                        novaSahovnicaMeta.setHash(hash);
                        novaSahovnicaMeta.setSahovnica(novaPostavitev);
                        novaSahovnicaMeta.setMatniKvadrat(sahovnica.getMatniKvadrat());

                        novaSahovnicaMeta.setStPotez(sahovnica.getStPotez() + 1);
                        novaSahovnicaMeta.setStVsehPotez(sahovnica.getStVsehPotez() - 1);

                        int[] koordinate = premik.getCoordinates();
                        int g = sahovnica.getF() + 2*sahovnica.getStPotez()/sahovnica.getStVsehPotez();
                        int h = pokritostMatnegaKvadrata(novaSahovnicaMeta);

                        novaSahovnicaMeta.setF(g+h);
                        novaSahovnicaMeta.setG(g);
                        novaSahovnicaMeta.setH(h);

                        String opravljenePoteze = sahovnica.getZaporedjePotez();
                        opravljenePoteze = opravljenePoteze.equals("") ? premik.toString() : opravljenePoteze + ";" + premik.toString();
                        novaSahovnicaMeta.setZaporedjePotez(opravljenePoteze);

                        sahovnice.add(novaSahovnicaMeta);
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
        sahovnica.setStPotez(0);
        sahovnica.setStVsehPotez(sahovnica.getSahovnica().getMovesLeft());

        int[] nasprotnikovKralj = poisciKralja(sahovnica.getSahovnica());
        int[][] matniKvadrat = poisciMatniKvadrat(nasprotnikovKralj);
        sahovnica.setMatniKvadrat(matniKvadrat);

        sahovnice.add(sahovnica);

        return poisciMat(sahovnice);

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
