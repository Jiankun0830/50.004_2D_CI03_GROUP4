import java.io.*;
import java.util.*;

public class Rand2SAT {
    public static void main(String[] args) {
        String path = args[0];
        try {
            // create graph from file
            Problem p = Loader.load(path);
            // chech for satisfiability
            if(p.solve()) {
                System.out.println("FORMULA SATISFIABLE");
                // print solution
                for(int i = 1; i <= p.assignment.size(); i++) {
                    Boolean value = p.assignment.get(i);
                    if(value) {
                        System.out.print('1');
                    } else {
                        System.out.print('0');
                    }
                    // print a space to separate values
                    if(i != p.assignment.size()) {
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
