public class Node {
    public int id;
    public int index;
    public int lowlink;
    public boolean inStack;

    public String toString() {
        return Integer.valueOf(this.id).toString();
    }
}
