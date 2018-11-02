import java.io.*;
import java.util.*;

public class Loader {

    private static void checkInputConstraint(boolean val) throws InvalidInputException {
        // this method throws `InvalidInputException` when `val` is false
        if(val == false) {
            System.out.println("INVALID INPUT");
            throw new InvalidInputException();
        }
    }

    public static Graph load(String path) throws InvalidInputException, IOException {
        // load a CNF file, parse, and return a `Graph` object
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));

            String problemLine = skipToProblemLine(br);

            int[] nums = parseProblemLine(problemLine);
            int numOfVars = nums[0];
            int numOfClauses = nums[1];

            return readClauses(br, numOfVars, numOfClauses);

        } catch(FileNotFoundException e) {
            checkInputConstraint(false);
            return null;
        }
    }


    private static String skipToProblemLine(BufferedReader br) throws InvalidInputException, IOException {
        // skip all the comment lines
        String line = "c";
        while(line.charAt(0) == 'c') {
            line = br.readLine();
        }
        checkInputConstraint(line != null);
        return line;
    }

    private static int[] parseProblemLine(String line) throws InvalidInputException, IOException {
        // process the problem line
        final String reference = "p cnf ";
        String[] words = line.split(" ");
        checkInputConstraint(
            words.length == 4 && 
            words[0].equals("p") &&
            words[1].equals("cnf")
        );
        int numOfVars = Integer.parseInt(words[2]);
        int numOfClauses = Integer.parseInt(words[3]);
        return new int[]{numOfVars, numOfClauses};
    }

    private static Graph readClauses(
        BufferedReader br, 
        int numOfVars, 
        int numOfClauses
    ) throws InvalidInputException, IOException {
        // process the clauses
        //   build the clauses string
        StringBuilder remaining = new StringBuilder();
        String line = "";
        while(true) {
            line = br.readLine();
            if(line != null) {
                remaining.append(line + '\n');
            } else {
                break;
            }
        }
        //   do the parsing
        List<List<Integer>> edges = new ArrayList<List<Integer>>();

        String clausesString = remaining.toString();
        
        boolean allowMinus = true;
        boolean allowDigit = true;
        boolean allowWhite = false;
        boolean negSign = false;
        StringBuilder accumulator = new StringBuilder();
        List<Integer> literals = new ArrayList<Integer>(2);
        for(int i = 0; i < clausesString.length(); i++) {
            char c = clausesString.charAt(i);
            boolean isDigit = Character.isDigit(c);
            boolean isWhite = Character.isWhitespace(c);

            if(c == '-'){
                checkInputConstraint(allowMinus);
                // set negSign
                negSign = true;
                // expect a digit
                allowMinus = false;
                allowDigit = true;
                allowWhite = false;
            } else if (isDigit) {
                checkInputConstraint(allowDigit);
                // add character to accumulator to be parsed
                accumulator.append(c);
                // expect more digits or a whitespace
                allowMinus = false;
                allowDigit = true;
                allowWhite = true;
            } else if (isWhite) {
                checkInputConstraint(allowWhite);
                // parse current accumulator, and clean it up
                Integer id = Integer.parseInt(accumulator.toString());
                accumulator = new StringBuilder();
                // do something with the last token
                if(id == 0) {
                    // add current pair to edges, and clean it up
                    checkInputConstraint(literals.size() == 2);
                    edges.add(
                        List.of(
                            -literals.get(0),
                            literals.get(1)
                        )
                    );
                    edges.add(
                        List.of(
                            -literals.get(1),
                            literals.get(0)
                        )
                    );
                    literals = new ArrayList<Integer>(2);
                } else if(id <= numOfVars) {
                    // add the last token to current pair
                    if(negSign) {
                        id *= -1;
                    }
                    literals.add(Integer.valueOf(id));
                } else {
                    // something's wrong
                    checkInputConstraint(false);
                }
                // reset negSign, expect literal
                negSign = false;
                allowMinus = true;
                allowDigit = true;
                allowWhite = false;
            } else {
                // invalid char
                checkInputConstraint(false);
            }
        }
        if(literals.size() > 0) {
            // add current pair to edges
            checkInputConstraint(literals.size() == 2);
            edges.add(
                List.of(
                    -literals.get(0),
                    literals.get(1)
                )
            );
            edges.add(
                List.of(
                    -literals.get(1),
                    literals.get(0)
                )
            );
        }

        // check if the number of clauses is satisfied
        checkInputConstraint(numOfClauses * 2 == edges.size());

        // create all literals vertices
        List<Integer> vertices = new ArrayList<Integer>(numOfVars);
        for(int i = 1; i <= numOfVars; i++) {
            vertices.add(i);
            vertices.add(-i);
        }
        return new Graph(vertices, edges);
    }

}

