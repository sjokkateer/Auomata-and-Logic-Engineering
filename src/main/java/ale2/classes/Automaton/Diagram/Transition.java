package ale2.classes.Automaton.Diagram;

import ale2.classes.Automaton.IDotFile;

public class Transition implements IDotFile {
    public static final char EMPTY = '_';

    public static final int SOURCE = 0;
    public static final int LETTER = 1;
    public static final int DESTINATION = 2;

    private State source;
    private State destination;
    private char label;

    public Transition(char letter) {
        label = letter;
    }

    public Transition(State source, char letter, State destination) {
        this(letter);
        this.source = source;
        this.destination = destination;
    }

    public State getDestination() {
        return destination;
    }

    public void setDestination(State newDestination) {
        destination = newDestination;
    }

    public State getSource() {
        return source;
    }

    public void setSource(State newSource) {
        source = newSource;
    }

    @Override
    public String getDotFileString() {
        String result = "\"" + source.getSymbol() + "\"";
        result += " -> " + "\"" + destination.getSymbol() + "\"";
        result += "[label=\"" + label + "\"]\n";

        return result;
    }

    public Character getLabel() {
        return label;
    }
}
