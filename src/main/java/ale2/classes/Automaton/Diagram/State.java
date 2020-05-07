package ale2.classes.Automaton.Diagram;

import ale2.classes.Automaton.IDotFile;

import java.util.Objects;

public class State implements IDotFile {
    private String symbol;

    public State()
    { }

    public State(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDotFileString() {
        return "\"" + getSymbol() + "\" [shape=" + getShape() + "]\n";
    }

    protected String getShape() {
        return "circle";
    }

    public boolean isInitial() {
        return false;
    }

    public boolean isAccepting() {
        return false;
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
}
