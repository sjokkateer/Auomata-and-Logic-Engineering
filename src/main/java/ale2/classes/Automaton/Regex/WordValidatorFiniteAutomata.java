package ale2.classes.Automaton.Regex;

import ale2.classes.Automaton.Diagram.State;
import ale2.classes.Automaton.Diagram.StateDiagram;
import ale2.classes.Automaton.Diagram.Transition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Class is responsible for validating words that belong to an FA.
 */
public class WordValidatorFiniteAutomata extends WordValidatorBase {

    public WordValidatorFiniteAutomata(Set<Character> alphabet, StateDiagram stateDiagram) {
        super(alphabet, stateDiagram);
    }

    @Override
    protected void belongsToLanguage(State currentState, String currentWord, Word wordObject, List<State> currentPath) {

        if (!currentWord.equals("_") && currentPath.contains(currentState)) {
            // Indicates our first cycle occurred.
            if (this.currentWord == null) {
                this.currentWord = currentWord;
            } else {
                // This could be our nth cycle, check if the word and size changed else return.
                if (!this.currentWord.equals(currentWord)) {
                    this.currentWord = currentWord;
                } else {
                    return;
                }
            }
        }

        currentPath.add(currentState);

        if (currentWord.length() <= 0) {
            if (currentState.isAccepting()) {
                wordObject.setBelongsToLanguage(true);
            } else {
                // even if this is not yet an accepting state, we could potentially finish by taking another empty string transition
                // to an actual accepting state.
                // so if we add another empty string symbol to the currentWord, we can reuse the method with the exact
                // same currentstate and let the logic of the recursive backtracker do its work.
                // since if the next has no possibility of using an empty string transition it will result in false which should be the case.

                // We only get here if we have an empty string
                belongsToLanguage(currentState, currentWord + "_", wordObject, currentPath);
            }
        } else {
            char currentCharacter = currentWord.charAt(0);
            List<Transition> possibleTransitions = stateDiagram.getTransitions(currentState);

            for (Transition possibleTransition: possibleTransitions) {
                State destination = possibleTransition.getDestination();

                if (possibleTransition.getLabel().equals(currentCharacter)) {
                    String remainder = currentWord.substring(1);
                    belongsToLanguage(destination, remainder, wordObject, currentPath);
                } else if (isEmptyStringTransitionWithDifferentDestination(possibleTransition)) {
                    belongsToLanguage(destination, currentWord, wordObject, currentPath);
                }

                if (wordObject.doesBelongToLanguage()) {
                    break;
                }
            }
        }
    }

    private boolean isEmptyStringTransitionWithDifferentDestination(Transition transition) {
        return transition.getLabel() == '_'
                && !(transition.getSource().equals(transition.getDestination()));
    }
}
