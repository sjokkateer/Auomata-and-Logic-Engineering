package ale2.classes.Automaton.Diagram;

public class EmptyStringTransition extends Transition {
    private boolean visited;

    public EmptyStringTransition(State source, char letter, State destination) {
        super(source, letter, destination);
        visited = false;
    }

    public boolean isVisited() {
        return visited;
    }
}
