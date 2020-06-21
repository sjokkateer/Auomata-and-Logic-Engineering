package ale2.classes.Automaton.Diagram;

import java.util.HashSet;
import java.util.Set;

public class SubSetState extends State {
    Set<State> states;

    public SubSetState(Set<State> states) {
        if (states == null || states.size() == 0) {
            throw new IllegalArgumentException("Set of states may not be empty or null");
        }

        this.states = new HashSet<>();
        addStates(states);
    }

    private void addStates(Set<State> states) {
        for (State state : states) {
            if (state instanceof SubSetState) {
                SubSetState ec = (SubSetState) state;

                for (State s : ec.getStates()) {
                    this.states.add(s);
                }
            } else {
                this.states.add(state);
            }
        }
    }

    public Set<State> getStates() {
        return states;
    }

    @Override
    public boolean isInitial() {
        for (State state : getStates()) {
            if (state.isInitial()) return true;
        }

        return false;
    }

    @Override
    public boolean isAccepting() {
        for (State state : getStates()) {
            if (state.isAccepting()) return true;
        }

        return false;
    }

    @Override
    public String getSymbol() {
        String result = "{ ";

        for (State state: getStates()) {
            result += state.getSymbol() + " ";
        }

        return result.substring(0, result.length() - 1) + " }";
    }

    @Override
    public int hashCode() {
        String result = "";

        for(State state : getStates()) {
            result += state.getSymbol();
        }

        return result.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o instanceof SubSetState) {
            SubSetState sss = (SubSetState)o;
            if (sss.getStates().size() != getStates().size()) return false;
            else {
                for (State s : sss.getStates()) {
                    if (!getStates().contains(s)) return false;
                }
                return true;
            }
        }

        return false;
    }
}
