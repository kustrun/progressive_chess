import Rules.Chessboard;

import java.io.BufferedReader;
import java.io.FileReader;


public class Main {

    public static void main(String[] args) {
        Seminar1 solver = new Seminar1();
        InputData[] input = getInput();

        int len = 1;
        for(int i=0; i<len; i++) {
            String solution = solver.solve(input[i].getFen());
            System.out.printf("id: %d \t solution: %s \t calculated solution: %s\n", input[i].getId(), input[i].getSolution(), solution);
        }

        /*
        String output = solver.solve(input);
        boolean correct = checkOutput(input, output);
        System.out.println("Input: " + input);
        System.out.println("Output: " + output);
        System.out.println("Result: " + correct);
        */
    }

    private static boolean checkOutput(String input, String output) {
        // TODO: Create a method that checks if all moves are legal and the end state is checkmate (use provided library)
        Chessboard cb = Chessboard.getChessboardFromFEN(input);
        return false;
    }

    private static InputData[] getInput() {

        InputData[] podatki = new InputData[60];

        try {
            BufferedReader br = new BufferedReader(new FileReader("/Users/domenb/Desktop/Faks/ALG/seminarska_1/inputs/progressive_checkmates.csv"));
            br.readLine(); //skip first line

            int i = 0;
            String line = br.readLine();

            while(line != null) {
                String[] lineString = line.split(",");

                InputData lineData = new InputData();
                lineData.setId(Integer.parseInt(lineString[0]));
                lineData.setFen(lineString[1].replace("\"", ""));
                lineData.setSolution(lineString[2].replace("\"", ""));

                podatki[i] = lineData;

                i++;
                line = br.readLine();

                //System.out.printf("id: %d \t fen: %s \t solution: %s\n", lineData.getId(), lineData.getFen(), lineData.getSolution());
            }

        } catch (Exception e) {
            System.out.println(e);
        }


        return podatki;
    }
}
