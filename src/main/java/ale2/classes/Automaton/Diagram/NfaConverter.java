package ale2.classes.Automaton.Diagram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NfaConverter {
    private StateDiagram stateDiagram;
    private Set<State> epsilonClosures;

    public NfaConverter(StateDiagram stateDiagram) {
        this.stateDiagram = stateDiagram;
        epsilonClosures = new HashSet<>();

        createEpsilonClosures();
    }

    public Set<State> getEpsilonClosures() {
        return epsilonClosures;
    }

    private void createEpsilonClosures() {
        for (State state : stateDiagram.getStates()) {
            EpsilonClosure epsilonClosure = createEpsilonClosureFrom(state);
            epsilonClosures.add(epsilonClosure);
        }
    }

    private EpsilonClosure createEpsilonClosureFrom(State state) {
        Set<State> statesInClosure = new HashSet<>();
        statesInClosure.add(state);

        List<State> queue = new ArrayList<>();

        int head = 0;
        queue.add(state);
        State current;

        while (queue.size() > 0) {
            current = queue.get(head);
            queue.remove(head);

            Set<State> destinations = stateDiagram.getDestinations(current, '_');

            for (State destination : destinations) {
                if (!statesInClosure.contains(destination)) {
                    queue.add(destination);
                    statesInClosure.add(destination);
                }
            }
        }

        return new EpsilonClosure(state, statesInClosure);
    }

    public EpsilonClosure getEpsilonClosureOf(State state) {
        for (State epsilonClosure: epsilonClosures) {
            EpsilonClosure eClosure = (EpsilonClosure) epsilonClosure;

            if (eClosure.isClosureOf(state)) {
                return eClosure;
            }
        }

        return null;
    }
}
