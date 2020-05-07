package ale2.classes.Automaton.Regex;

import ale2.classes.Automaton.Diagram.State;
import ale2.classes.Automaton.Diagram.StateDiagram;
import ale2.classes.Automaton.Diagram.Transition;

import java.util.List;
import java.util.Set;

public class WordValidator {
    private Set<Character> alphabet;
    private StateDiagram stateDiagram;

    public WordValidator(Set<Character> alphabet, StateDiagram stateDiagram) {
        this.alphabet = alphabet;
        this.stateDiagram = stateDiagram;
    }

    public void check(Set<Word> wordCollection) {
        for (Word word: wordCollection) {
            validate(word);
        }
    }

    /**
     * The input word modified to indicate if
     * it belongs to the language or not.
     *
     * @param word
     * @return the input word
     */
    public void validate(Word word) {
        State initialState = stateDiagram.getInitialState();

        // We should exit this method (the word will remain false (not belonging to the language)
        // in case there is no final/accepting state in the automata and or it contains letters not belonging
        // to the alphabet.

        belongsToLanguage(initialState, word.getWord(), word);
    }

    private void belongsToLanguage(State currentState, String currentWord, Word wordObject) {
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
                belongsToLanguage(currentState, currentWord + "_", wordObject);
            }
        } else {
            char currentCharacter = currentWord.charAt(0);
            List<Transition> possibleTransitions = stateDiagram.getTransitions(currentState);

            for (Transition possibleTransition: possibleTransitions) {
                State destination = possibleTransition.getDestination();

                if (possibleTransition.getLabel().equals(currentCharacter)) {
                    String remainder = currentWord.substring(1);
                    belongsToLanguage(destination, remainder, wordObject);
                } else if (isEmptyStringTransitionWithDifferentDestination(possibleTransition)) {
                    belongsToLanguage(destination, currentWord, wordObject);
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
