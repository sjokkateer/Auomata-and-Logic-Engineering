package ale2.classes.Automaton.Diagram;

import ale2.classes.Automaton.IDotFile;

public class Transition implements IDotFile {
    public static final int SOURCE = 0;
    public static final int LETTER = 1;
    public static final int DESTINATION = 2;

    private State source;
    private State destination;
    private char label;

    public Transition(State source, char letter, State destination)
    {
        this.source = source;
        label = letter;
        this.destination = destination;
    }

    public static Transition fromLetter(State source, char letter, State destination) {
        Transition t;

        if (letter == '_') {
            t = new EmptyStringTransition(source, letter, destination);
        } else {
            t = new Transition(source, letter, destination);
        }

        return t;
    }

    public State getDestination() {
        return destination;
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
