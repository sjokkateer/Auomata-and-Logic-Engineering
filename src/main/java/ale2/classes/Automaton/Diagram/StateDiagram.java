package ale2.classes.Automaton.Diagram;

import ale2.classes.Automaton.Diagram.Thompson.IThompsonConstructionStrategy;
import ale2.classes.Automaton.IDotFile;

import java.util.*;

public class StateDiagram implements IDotFile {
    private static IThompsonConstructionStrategy thompsonConstructionStrategy;
    private Set<State> states = new HashSet<>();
    private HashMap<State, List<Transition>> transitions = new HashMap<>();

    public StateDiagram() {

    }

    public StateDiagram(List<String> states, List<String> acceptingStates, List<String[]> transitions) {
        createStates(states);
        addAcceptingStates(acceptingStates);
        addTransitions(transitions);
    }

    public static StateDiagram fromTransitions(List<Transition> transitionList) {
        // For all transitions we add the sources and destinations to states set
        // We also add all transitions to the hashmap
        StateDiagram stateDiagram = new StateDiagram();
        Set<State> states = stateDiagram.getStates();

        for(Transition transition: transitionList) {
            states.add(transition.getSource());
            states.add(transition.getDestination());

            stateDiagram.addTransitionToCollection(transition);
        }

        return stateDiagram;
    }

    public static void setThompsonConstructionStrategy(IThompsonConstructionStrategy strategy) {
        thompsonConstructionStrategy = strategy;
    }

    public static StateDiagram create() {
        return thompsonConstructionStrategy.create();
    }

    private void createStates(List<String> states) {
        State v;

        for (int i = 0; i < states.size(); i++) {
            v = new State(states.get(i));

            if (i == 0) {
                v.setInitial();
            }

            this.states.add(v);
        }
    }

    private void addAcceptingStates(List<String> acceptingStates) {
        Set<State> copy = new HashSet<>(states);
        for (State state: copy) {
            attemptToWrapWithAcceptingState(state, acceptingStates);
        }
    }

    private void attemptToWrapWithAcceptingState(State state, List<String> acceptingStates) {
        if (acceptingStates.contains(state.getSymbol())) {
            state.setAccepting();
        }
    }

    private void addTransitions(List<String[]> transitions) {
        Transition t;
        State source = null;
        State destination = null;

        for (String[] transition: transitions) {
            // Example
            // ["q0", "a", "q1"]
            source = getState(source, transition[Transition.SOURCE]);
            char letter = transition[Transition.LETTER].charAt(0); // We should always have one character strings x) or we have invalid values.
            destination = getState(destination, transition[Transition.DESTINATION]);

            t = new Transition(source, letter, destination);

            addTransitionToCollection(t);
        }
    }

    public void addTransitionToCollection(Transition transition) {
        List<Transition> transitionList;
        State source = transition.getSource();

        transitionList = this.transitions.getOrDefault(source, new ArrayList<>());

        if (transitionList.size() == 0) {
            this.transitions.put(source, transitionList);
        }

        transitionList.add(transition);
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

    public List<Transition> getAllTransitions() {
        List<Transition> allTransitions = new ArrayList<>();

        for(State state: getStates()) {
            allTransitions.addAll(getTransitions(state));
        }

        return allTransitions;
    }

    @Override
    public String getDotFileString() {
        String result = "\trankdir=LR;\n";
        result += "\t\"\" [shape=none]\n";

        for (State state: states) {
            result += "\t" + state.getDotFileString();
        }

        result += "\n";
        State initialState = getInitialState();
        result += "\t\"\" -> \"" + initialState.getSymbol() + "\"\n";

        for (State state: states) {
            for (Transition transition: transitions.getOrDefault(state, new ArrayList<>())) {
                result += "\t" + transition.getDotFileString();
            }
        }

        return result;
    }

    public State getInitialState() {
        for (State state: states) {
            if (state.isInitial()) return state;
        }

        return null;
    }

    public Set<State> getStates() {
        return states;
    }

    public List<Transition> getTransitions(State state) {
        return transitions.getOrDefault(state, new ArrayList<>());
    }

    public void relabel() {
        // Apply a BFS traversal starting at the initial.
        // int stateCounter = 0;

        // State initialState = getInitialState();


    }
}
