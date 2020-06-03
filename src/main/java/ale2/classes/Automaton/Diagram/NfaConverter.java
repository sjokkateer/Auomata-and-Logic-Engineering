package ale2.classes.Automaton.Diagram;

import java.util.*;

public class NfaConverter {
    private StateDiagram stateDiagram;
    private List<State> epsilonClosures;
    private Set<Character> alphabet;
    private List<Transition> transitions;

    public NfaConverter(StateDiagram stateDiagram) {
        this.stateDiagram = stateDiagram;
        epsilonClosures = new ArrayList<>();
        alphabet = new HashSet<>();
        transitions = new ArrayList<>();

        determineAlphabet();
        createEpsilonClosures();

//        if (alphabet.size() == 0) {
//            throw new IllegalArgumentException("This can not be converted to a DFA!");
//        }
    }

//    public StateDiagram convertToDfa() {
//        List<Transition> newTransitions = new ArrayList<>();
//
//        State initialEpsilonClosure = getEpsilonClosureOfInitialState();
//        List<State> queue = new ArrayList<>();
//
//        if (initialEpsilonClosure != null) {
//            queue.add(initialEpsilonClosure);
//        }
//
//        DeadState sink = new DeadState();
//        State current;
//
//        Set<State> alreadyProcessedOrInQueue = new HashSet<>();
//        alreadyProcessedOrInQueue.add(initialEpsilonClosure);
//        // States that have already been processed should not be added to the queue anymore.
//        // And states as destination should be checked for their epsilon closure equivalent.
//        while (queue.size() > 0) {
//            current = queue.get(0);
//            queue.remove(0);
//
//            for (Character letter : alphabet){
//                State destination = generateDestination((SubSetState)current, letter);
//
//                if (destination != null) {
//                    if (!alreadyProcessedOrInQueue.contains(destination)) {
//                        alreadyProcessedOrInQueue.add(destination);
//                        queue.add(destination);
//                    }
//                } else {
//                    destination = sink;
//                }
//
//                newTransitions.add(new Transition(current, letter, destination));
//            }
//        }
//
//        // For every state, generate the destinations.
//
//        // Then generate all destinations for the generated states
//        // until we find no new states.
//
//        // So we could make a hash map for each state -> each letter -> destination subset
//        // if that state -> letter -> results in null we assign sink at the end.
//
//
//        StateDiagram dfa = StateDiagram.fromTransitions(newTransitions);
//        for (State state : dfa.getStates()) {
//            // If there actually is a sink present
//            if (state instanceof DeadState) {
//                sink = (DeadState)state;
//
//                // Add a transition for every character in the alphabet
//                for (Character letter : alphabet) {
//                    dfa.addTransitionToCollection(new Transition(sink, letter, sink));
//                }
//            }
//        }
//
//        return dfa;
//    }

    public StateDiagram convertToDfa() {
        HashMap<State, List<State>> stateToDestinations = new HashMap<>();

        List<State> queue = new ArrayList<>();

        State initialState = getEpsilonClosureOf(stateDiagram.getInitialState());
        queue.add(initialState);

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
                // Assuming the alphabet is always in sorted order in our application.
                // Would be safer to make another hash map where State -> hashmap and hashmap is keyed by letters in alphabet to subsetstates
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
                // Only epsilon closure is not enough
                // We also have to take into account that it can go to another state through transition with same label.
                destinations.add(getEpsilonClosureOf(s));
            }
        }

        if (destinations.size() == 0) {
            return null;
        }

        return new SubSetState(destinations);
    }

    private State getEpsilonClosureOfInitialState() {
        State initial = stateDiagram.getInitialState();
        return getEpsilonClosureOf(initial);
    }

    private void determineAlphabet() {
        for (Transition transition : stateDiagram.getAllTransitions()) {
            if (transition.getLabel() != '_') {
                alphabet.add(transition.getLabel());
            }
        }
    }

    public List<State> getEpsilonClosures() {
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
