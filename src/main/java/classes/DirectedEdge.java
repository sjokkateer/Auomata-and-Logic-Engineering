package classes;

public class DirectedEdge {
    private Vertex source;
    private Vertex destination;
    private char label;

    public DirectedEdge(char letter) {
        label = letter;
    }
}
