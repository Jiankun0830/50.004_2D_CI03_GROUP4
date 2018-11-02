import java.io.*;
import java.util.*;

public class TwoSAT {
    public static void main(String[] args) {
        String path = args[0];
        try {
            // create graph from file
            Graph g = Loader.load(path);
            // run Tarjan's strongly connected components algorithm
            g.tarjan();
            // check for satisfiability
            if(g.satisfiable()) {
                System.out.println("FORMULA SATISFIABLE");
                // solve
                Map<Integer, Boolean> solution = g.solve();
                // print solution
                for(int i = 1; i <= solution.size(); i++) {
                    Boolean value = solution.get(i);
                    if(value) {
                        System.out.print('1');
                    } else {
                        System.out.print('0');
                    }
                    // print a space to separate values
                    if(i != solution.size()) {
                        System.out.print(' ');
                    }
                }
                System.out.println("");
            } else {
                System.out.println("FORMULA UNSATISFIABLE");
            }
        } catch(InvalidInputException e) {
        } catch(IOException e) {
            System.out.println("Unexpected IOException");
        }

    }
}
