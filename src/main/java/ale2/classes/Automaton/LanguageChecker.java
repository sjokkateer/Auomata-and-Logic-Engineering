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

        for (Transition t: stateDiagram.getTransitions(state)) {
            State destination = t.getDestination();

            if (!destination.isVisited()) {
                generateWordsRecursively(destination, word + t.getLabel());
            }
        }

        state.setVisited(false);

        if (state.isAccepting() && !word.equals("")) {
            Word w = new Word(word);
            w.setBelongsToLanguage(true);

            languageWords.add(w);
        }
    }

    public boolean isFinite() {
        for (List<State> cycle: getCycles()) {
            // if (acceptingStateInCycle(cycle)) return false;
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

        resetAllVisited();

        return hasPathToAcceptingState(initialState);
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
            State destination = t.getDestination();

            // Check whether it is a back edge, otherwise we can ignore it.
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
