package ale2.classes.Automaton.Diagram;

import java.util.*;

abstract public class NfaToDfaStrategyBase {
    protected StateDiagram stateDiagram;
    protected Set<Character> alphabet;

    public NfaToDfaStrategyBase(StateDiagram stateDiagram) {
        this.stateDiagram = stateDiagram;
        alphabet = new HashSet<>();

        determineAlphabet();
    }

    public static NfaToDfaStrategyBase determineStrategy(StateDiagram stateDiagram) {
        for (Transition transition : stateDiagram.getAllTransitions()) {
            if (transition.getLabel() == '_') {
                return new NfaToDfaWithEpsilonConversionStrategy(stateDiagram);
            }
        }

        return new NfaToDfaWithoutEpsilonConversionStrategy(stateDiagram);
    }

    protected abstract List<State> initializeQueue();
    protected abstract State convertToRequiredState(State state);

    private void determineAlphabet() {
        for (Transition transition : stateDiagram.getAllTransitions()) {
            if (transition.getLabel() != '_') {
                alphabet.add(transition.getLabel());
            }
        }
    }

    public StateDiagram convertToDfa() {
        HashMap<State, List<State>> stateToDestinations = new HashMap<>();

        List<State> queue = initializeQueue();

        List<State> destinations;

        List<Character> alphabet = new ArrayList<>(this.alphabet);

        while (queue.size() > 0) {
            State current = queue.get(0);
            queue.remove(0);

            destinations = stateToDestinations.getOrDefault(current, new ArrayList<>());

            for (Character letter: alphabet) {
                State destination = generateDestination((SubSetState) current, letter);
                destinations.add(destination);
                stateToDestinations.put(current, destinations);

                if (destination != null && !stateToDestinations.containsKey(destination) && !queue.contains(destination)) {
                    queue.add(destination);
                }
            }
        }

        List<Transition> transitions = new ArrayList<>();
        DeadState sink = new DeadState();

        for (State state : stateToDestinations.keySet()) {
            for (int i = 0; i < alphabet.size(); i++) {
                State destination = stateToDestinations.get(state).get(i);

                if (destination == null) {
                    destination = sink;
                }

                transitions.add(new Transition(state, alphabet.get(i), destination));
            }
        }

        for (Character letter: alphabet) {
            transitions.add(new Transition(sink, letter, sink));
        }

        return StateDiagram.fromTransitions(transitions);
    }

    private State generateDestination(SubSetState current, Character letter) {
        Set<State> destinations = new HashSet<>();

        for (State state : current.getStates()) {
            Set<State> destinationsOverLetter = stateDiagram.getDestinations(state, letter);

            for (State s: destinationsOverLetter) {
                destinations.add(convertToRequiredState(s));
            }
        }

        if (destinations.size() == 0) {
            return null;
        }

        return new SubSetState(destinations);
    }

}
