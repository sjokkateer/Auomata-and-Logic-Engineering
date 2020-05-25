package ale2.classes.Automaton;

import ale2.classes.Automaton.Diagram.State;
import ale2.classes.Automaton.Diagram.StateDiagram;
import ale2.classes.Automaton.Diagram.Transition;
import ale2.classes.Automaton.Regex.Word;

import java.util.ArrayList;
import java.util.List;

public class LanguageChecker {
    private StateDiagram stateDiagram;
    private List<List<State>> cycles;
    private boolean alreadySearched;

    private List<Word> languageWords;

    public List<Word> getWords() {
        if (isFinite()) {
            generateWords();
        }

        return languageWords;
    }

    private void generateWords() {
        resetAllVisited();

        State initial = stateDiagram.getInitialState();

        generateWordsRecursively(initial, "");
    }

    private void generateWordsRecursively(State state, String word) {
        state.setVisited(true);

        // Try to go as deep as possible first
        for (Transition t: stateDiagram.getTransitions(state)) {
            State destination = t.getDestination();

            if (!destination.isVisited()) {
                generateWordsRecursively(destination, word + t.getLabel());
            }
        }

        // Then once we exhausted our options, check if we can make a word.
        if (state.isAccepting() && !word.equals("")) {
            languageWords.add(new Word(word));
        }
    }

    public boolean isFinite() {
        for (List<State> cycle: getCycles()) {
            if (acceptingStateInCycle(cycle)) return false;
            if (pathToAcceptingStateFromCycle(cycle)) return false;
        }

        return true;
    }

    private void resetAllVisited() {
        for (State state: stateDiagram.getStates()) {
            state.setVisited(false);
        }
    }

    private boolean pathToAcceptingStateFromCycle(List<State> cycle) {
        int last = cycle.size() - 1;
        State initialState = cycle.get(last);

        resetVisited(cycle);

        return hasPathToAcceptingState(initialState);
    }

    private void resetVisited(List<State> cycle) {
        for (State s: stateDiagram.getStates()) {
            if (notInCycle(s, cycle)) {
                s.setVisited(false);
            }
        }
    }

    private boolean notInCycle(State s, List<State> cycle) {
        for (State state: cycle) {
            if (state.equals(s)) return false;
        }

        return true;
    }

    private boolean hasPathToAcceptingState(State state) {
        state.setVisited(true);

        if (state.isAccepting()) {
            return true;
        } else {
            for (Transition t: stateDiagram.getTransitions(state)) {
                State destination = t.getDestination();

                if (!destination.isVisited()) {
                    if (hasPathToAcceptingState(destination)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean acceptingStateInCycle(List<State> cycle) {
        for (State state: cycle) {
            if (state.isAccepting()) return true;
        }

        return false;
    }

    public void setStateDiagram(StateDiagram stateDiagram) {
        // Only if we got a new state diagram object we want to reset all stuff.
        if (this.stateDiagram == null || !this.stateDiagram.equals(stateDiagram)) {
            cycles = new ArrayList<>();
            languageWords = new ArrayList<>();
            alreadySearched = false;

            this.stateDiagram = stateDiagram;
        }
    }

    public List<List<State>> getCycles() {
        if (!alreadySearched) {
            searchForCycles();
        }

        return cycles;
    }

    private void searchForCycles() {
        State initial = stateDiagram.getInitialState();
        findCycles(initial, new ArrayList<>());
        alreadySearched = true;
    }

    private void findCycles(State currentState, List<State> currentPath) {
        currentState.setVisited(true);
        currentPath.add(currentState);

        for (Transition t: stateDiagram.getTransitions(currentState)) {
            // And probably what needs to be added is if the visited is already in the path.
            State destination = t.getDestination();

            // Only then do we have a cycle, otherwise we could also have visited
            // the state without result.
            if (destination.isVisited() && isBackEdge(destination, currentPath)) {
                currentPath.add(t.getDestination());
                cycles.add(currentPath);
            } else {
                findCycles(t.getDestination(), new ArrayList<>(currentPath));
            }
        }
    }

    private boolean isBackEdge(State destination, List<State> currentPath) {
        for (State state: currentPath) {
            if (state.equals(destination)) return true;
        }
        return false;
    }
}
