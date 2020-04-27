package ale2.classes.Automaton.Diagram;

import ale2.classes.Automaton.IDotFile;

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
}
