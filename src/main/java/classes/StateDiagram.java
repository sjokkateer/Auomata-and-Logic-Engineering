package classes;

import java.util.*;

public class StateDiagram {
    private Set<State> states = new HashSet<>();
    private HashMap<State, List<Transition>> transitions = new HashMap<>();

    public StateDiagram(List<String> states, List<String> acceptingStates, List<String[]> transitions) {
        createStates(states);
        addAcceptingStates(acceptingStates);
        addTransitions(transitions);
    }

    private void createStates(List<String> states) {
        State v;

        for (int i = 0; i < states.size(); i++) {
            v = new State(states.get(i));

            if (i == 0) {
                v = new Initial(v);
            }

            this.states.add(v);
        }
    }

    private void addAcceptingStates(List<String> acceptingStates) {
        // Should iter over a copy and modify the existing.
        Set<State> copy = new HashSet<>(states);
        for (State state: copy) {
            attemptToWrapWithAcceptingState(state, acceptingStates);
        }
    }

    private void attemptToWrapWithAcceptingState(State state, List<String> acceptingStates) {
        if (acceptingStates.contains(state.getSymbol())) {
            states.remove(state);
            states.add(new Accepting(state));
        }
    }

    private void addTransitions(List<String[]> transitions) {
        Transition t;
        List<Transition> transitionList;
        State source = null;
        State destination = null;

        for (String[] transition: transitions) {
            source = getState(source, transition[Transition.SOURCE]);
            char letter = transition[Transition.LETTER].charAt(0); // We should always have one character strings x) or we have invalid values.
            destination = getState(destination, transition[Transition.DESTINATION]);

            t = new Transition(source, letter, destination);

            transitionList = this.transitions.getOrDefault(source, new ArrayList<>());
            transitionList.add(t);
        }
    }

    private State getState(State state, String stateSymbol) {
        if (state == null || !stateSymbol.equals(state.getSymbol())) {
            state = getState(stateSymbol);
        }

        return state;
    }

    private State getState(String symbol) {
        for (State state: states) {
            if (symbol.equals(state.getSymbol())) {
                return state;
            }
        }
        return null;
    }
}
