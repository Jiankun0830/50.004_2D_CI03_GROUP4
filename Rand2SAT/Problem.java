import java.lang.StringBuilder;
import java.util.*;
import java.lang.Math;
import java.util.Random;

public class Problem {
    public List<List<Integer>> clauses;
    public int numOfVars;

    public Map<Integer, Boolean> assignment = new HashMap<Integer, Boolean>(); // for use in solving

    Problem(List<List<Integer>> clauses, int numOfVars) {
        this.clauses = clauses;
        this.numOfVars = numOfVars;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(List<Integer> clause: clauses) {
            builder.append('(');
            Integer literal = clause.get(0);
            if(literal > 0) {
                builder.append(literal.toString());
            } else {
                builder.append('!');
                builder.append(Integer.toString(-literal));
            }
            builder.append('+');
            literal = clause.get(1);
            if(literal > 0) {
                builder.append(literal.toString());
            } else {
                builder.append('!');
                builder.append(Integer.toString(-literal));
            }
            builder.append(')');
        }
        return builder.toString();
    }

    public boolean solve() {
        Random rand = new Random();
        // initialize the assignments
        for(int i = 1; i <= numOfVars; i++) {
            assignment.put(i, false);
        }
        // try to solve
        for(int i = 0; i < (int)Math.pow(numOfVars, 2)*100; i++) {
            // look for an unsatisfied clause
            List<Integer> clause = null;
            for(List<Integer> clauseFor: clauses) {
                if(!evalClause(clauseFor)) {
                    // found an unsatisfied one
                    clause = clauseFor;
                    break;
                }
            }
            // check if it found one or just ran out of clauses
            if(clause == null) {
                return true;
            }
            // flip the coin
            int index = -1;
            if(rand.nextInt() <= 0) {
                index = 0;
            } else {
                index = 1;
            }
            int var = Math.abs(clause.get(index));
            // flip the value of the var
            Boolean value = assignment.get(var);
            assignment.put(var, !value);
        }
        return false;
    }

    private boolean evalClause(List<Integer> clause) {
        return evalLiteral(clause.get(0)) || evalLiteral(clause.get(1));
    }

    private boolean evalLiteral(int a) {
        if(a > 0) {
            return assignment.get(a);
        } else {
            return !assignment.get(-a);
        }
    }

}
