import java.util.*;
import java.lang.Math;

public class Graph {

    Map<Node, Set<Node>> adjacencyLists;

    Map<Integer, Node> nodes;

    private int index;  // index used in Tarjan's algorithm
    private Stack<Node> stack = new Stack<Node>();
    public List<List<Node>> SCCs = new ArrayList<List<Node>>();

    Graph(List<Integer> vertices, List<List<Integer>> edges) {

        this.adjacencyLists = new HashMap<Node, Set<Node>>();
        nodes = new HashMap<Integer, Node>();
        // create all vertices
        for(Integer id: vertices) {
            Node vertex = new Node();
            vertex.id = id;
            this.adjacencyLists.put(vertex, new HashSet<Node>());
            nodes.put(id, vertex);
        }
        // add all edges
        for(List<Integer> edge: edges) {
            Node from = nodes.get(edge.get(0));
            Node to = nodes.get(edge.get(1));

            Set<Node> set = this.adjacencyLists.get(from);
            set.add(to);
        }
    }

    @Override
    public String toString() {
        return this.adjacencyLists.toString();
    }

    public void tarjan() {
        index = 1;
        Set<Node> nodes = this.adjacencyLists.keySet();

        for(Node node: nodes) {
            if(node.index == 0) {
                visit(node);
            }
        }
    }

    private void visit(Node node) {
        node.index = index;
        node.lowlink = index;
        index += 1;
        stack.push(node);
        node.inStack = true;

        for(Node next: adjacencyLists.get(node)) {
            if(next.index == 0) {
                visit(next);
                node.lowlink = Math.min(node.lowlink, next.lowlink);
            } else if(node.inStack) {
                node.lowlink = Math.min(node.lowlink, next.index);
            }
        }

        if(node.lowlink == node.index) {
            List<Node> SCC = new ArrayList<Node>();
            Node poppedNode = null;
            while(node != poppedNode) {
                poppedNode = stack.pop();
                poppedNode.inStack = false;
                SCC.add(poppedNode);
            }
            SCCs.add(SCC);
        }
    }

    public boolean satisfiable() {
        for(List<Node> SCC: SCCs) {
            HashSet<Integer> variables = new HashSet<Integer>();
            for(Node n: SCC) {
                int varId = Math.abs(n.id);
                if(variables.contains(varId)) {
                    return false;
                } else {
                    variables.add(varId);
                }
            }
        }
        return true;
    }

    public Map<Integer, Boolean> solve() {
        Map<Integer, Boolean> solution = new HashMap<Integer, Boolean>();

        for(List<Node> SCC: SCCs) {
            if(solution.containsKey(SCC.get(0).id)) {
                // variables in this SCC are already assigned, skip
                continue;
            }
            for(Node n: SCC) {
                int id = n.id;
                if(id > 0) {
                    solution.put(id, true);
                } else {
                    solution.put(-id, false);
                }
            }
        }
        return solution;
    }

}

