
//COMPILING & RUNNING: javac Main.java && java Main < input.txt

import java.util.*;

public class Main {

    public static Scanner in;

    public static void main(String[] args) {
        in = new Scanner(System.in);
        doStuff();
    }

    public static void doStuff() {
        int NumberOfInputs = in.nextInt();
        int Queens;
        for(int count=1;count<=NumberOfInputs;count++){
            int N=in.nextInt();
            Queens = (int)(Math.floor((2 * N + 1)/3));
            System.out.println(count+" "+N+" "+Queens);
            QueenSolver(N,Queens);
			System.out.println("");
        }
    }

    public static void QueenSolver(int N, int Queens) {
        int Row=N-Queens+1;
        int Column = 1;
        for (int count = 1; count <= Queens; count++) {
            System.out.print("[" + Row + "," + Column + "] ");
            if ((count%8)==0){
                System.out.println("");
            }
            Row++;
            Column=Column+2;
            if (Column > Row) {
                Column = 2;
            }
    }
}
}