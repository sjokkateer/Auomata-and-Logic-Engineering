package ale2.classes.Automaton.Diagram;

import ale2.classes.Automaton.IDotFile;

import java.util.Objects;

public class State implements IDotFile {
    private String symbol;
    private boolean initial;
    private boolean accepting;

    private boolean visited;

    public State()
    {
        initial = false;
        accepting = false;

        visited = false;
    }

    public State(String symbol) {
        this();
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDotFileString() {
        return "\"" + getSymbol() + "\" [shape=" + getShape() + "]\n";
    }

    private String getShape() {
        String shape = "circle";

        if (isAccepting()) {
            shape = "double" + shape;
        }

        return shape;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }

    public boolean isInitial() {
        return initial;
    }

    public void setInitial() {
        initial = true;
    }

    public boolean isAccepting() {
        return accepting;
    }

    public void setAccepting() {
        accepting = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(getSymbol(), state.getSymbol());
    }

    @Override
    public int hashCode() {
        return getSymbol().hashCode();
    }

    @Override
    public String toString() {
        return getSymbol();
    }
}
