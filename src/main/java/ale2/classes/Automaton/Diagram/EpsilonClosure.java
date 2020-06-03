package ale2.classes.Automaton.Diagram;

import java.util.Set;

public class EpsilonClosure extends SubSetState {
    private State source;

    public EpsilonClosure(State source, Set<State> statesInClosure) {
        super(statesInClosure);

        this.source = source;
    }

    public boolean isClosureOf(State state) {
        return source.equals(state);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
