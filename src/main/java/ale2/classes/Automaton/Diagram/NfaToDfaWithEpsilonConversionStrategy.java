package ale2.classes.Automaton.Diagram;

import java.util.*;

public class NfaToDfaWithEpsilonConversionStrategy extends NfaToDfaStrategyBase {
    private List<State> epsilonClosures;

    public NfaToDfaWithEpsilonConversionStrategy(StateDiagram stateDiagram) {
        super(stateDiagram);
        epsilonClosures = new ArrayList<>();

        createEpsilonClosures();
    }

    @Override
    protected List<State> initializeQueue() {
        List<State> queue = new ArrayList<>();
        queue.add(getEpsilonClosureOf(stateDiagram.getInitialState()));

        return queue;
    }

    @Override
    protected State convertToRequiredState(State state) {
        return getEpsilonClosureOf(state);
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
                // To avoid cycles
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
