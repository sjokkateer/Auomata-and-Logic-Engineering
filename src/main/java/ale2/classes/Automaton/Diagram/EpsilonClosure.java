package ale2.classes.Automaton.Diagram;

import java.util.Set;

public class EpsilonClosure extends State {
    State source;
    Set<State> states;

    public EpsilonClosure(State source, Set<State> statesInClosure) {
        this.source = source;
        states = statesInClosure;
    }

    public boolean isClosureOf(State state) {
        return source.equals(state);
    }

    public Set<State> getStates() {
        return states;
    }

    @Override
    public int hashCode() {
        String result = source.getSymbol();

        for(State state : getStates()) {
            result +=  state.getSymbol();
        }

        return result.hashCode();
    }
}
