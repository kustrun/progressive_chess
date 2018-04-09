import Rules.Chessboard;
import Rules.Move;
import Rules.Rules;

import java.util.*;

class SahovnicaMeta {

    private int g;
    private int h;
    private int f;
    private int hash;
    private Chessboard sahovnica;
    private String zaporedjePotez;
    private int[] nasprotnikovKralj;

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

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public int[] getNasprotnikovKralj() {
        return nasprotnikovKralj;
    }

    public void setNasprotnikovKralj(int[] nasprotnikovKralj) {
        this.nasprotnikovKralj = nasprotnikovKralj;
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
            figuraPojedena = Math.abs(figura) + 5;
        }

        if(figuraPojedena != 0) {
            hash = hash ^ tabela[j][figuraPojedena];
        }

        hash = hash ^ tabela[i][figura] ^ tabela[j][figura];

        return hash;
    }

}

public class Seminar1 {

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
                    kralj[0] = i;
                    kralj[1] = j;

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

    public static int pokritostMatnegaKvadrataIgnoriranje(SahovnicaMeta sahovnicaMeta) {

        Chessboard sahovnica = sahovnicaMeta.getSahovnica();

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
        int[] kralj = sahovnicaMeta.getNasprotnikovKralj();

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

        Zobrist zobrist = new Zobrist();
        int hash = zobrist.vrednostZobrist(sahovnice.peek().getSahovnica());
        sahovnice.peek().setHash(hash);

        HashMap<Integer, SahovnicaMeta> preiskaneSahovnice = new HashMap<Integer, SahovnicaMeta>();

        while(!sahovnice.isEmpty()) {
            SahovnicaMeta sahovnica = sahovnice.poll();
            hash = sahovnica.getHash();
            preiskaneSahovnice.put(hash, sahovnica);

            if(sahovnica.getSahovnica().getGameStatus() == Chessboard.CHECKMATE && sahovnica.getSahovnica().getMovesLeft() == 0) {
                System.out.println("konec");
                return sahovnica.getZaporedjePotez();

            } else {
                HashMap<String, Move> mozniPremiki = Rules.getPossibleMovesMap(sahovnica.getSahovnica());
                for (Map.Entry<String, Move> mozniPremik : mozniPremiki.entrySet()) {
                    Move premik = mozniPremik.getValue();
                    hash = zobrist.posodobitevZobrist(hash, sahovnica.getSahovnica(), premik.getCoordinates());

                    if(!preiskaneSahovnice.containsKey(hash)) {

                        Chessboard novaPostavitev = Chessboard.getChessboardFromFEN(sahovnica.getSahovnica().getFEN());
                        novaPostavitev.makeMove(premik);

                        SahovnicaMeta novaSahovnicaMeta = new SahovnicaMeta();
                        novaSahovnicaMeta.setHash(hash);
                        novaSahovnicaMeta.setSahovnica(novaPostavitev);
                        novaSahovnicaMeta.setNasprotnikovKralj(sahovnica.getNasprotnikovKralj());

                        int g = sahovnica.getF();
                        int h = pokritostMatnegaKvadrataIgnoriranje(novaSahovnicaMeta);

                        novaSahovnicaMeta.setF(g+h);
                        novaSahovnicaMeta.setG(g);
                        novaSahovnicaMeta.setH(h);

                        String opravljenePoteze = sahovnica.getZaporedjePotez();
                        opravljenePoteze = opravljenePoteze.equals("") ? mozniPremik.getKey() : opravljenePoteze + ";" + mozniPremik.getKey();
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
        sahovnica.setNasprotnikovKralj(poisciKralja(sahovnica.getSahovnica()));

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
