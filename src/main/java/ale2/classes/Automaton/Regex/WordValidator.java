package ale2.classes.Automaton.Regex;

import ale2.classes.Automaton.Diagram.State;
import ale2.classes.Automaton.Diagram.StateDiagram;
import ale2.classes.Automaton.Diagram.Transition;

import java.util.List;
import java.util.Set;

public class WordValidator {
    // This class will take a word
    // and based on the state diagram (root/initial state)
    // This object will check whether the word belongs to the language of the diagram/automaton
    private StateDiagram stateDiagram;

    public WordValidator(StateDiagram stateDiagram) {
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
        // This is more or less a wrapper which will call the actual validation method (recursive)
        State initialState = stateDiagram.getInitialState();
        word.setBelongsToLanguage(belongsToLanguage(initialState, word.getWord()));
    }

    private boolean belongsToLanguage(State currentState, String currentWord) {
        // This can get into an infinite loop if _ is traversed in a cycle.
        // thus we should keep track if we visited already used that character without further result
        if (currentWord.length() <= 0) {
            // Then current state has to be accepting or otherwise it's false.
            if (currentState.isAccepting()) {
                return true;
            }
        } else {
            char currentCharacter = currentWord.charAt(0);
            // Should be sorted with _ at the end of list for convenience
            List<Transition> possibleTransitions = stateDiagram.getTransitions(currentState);

            for (Transition possibleTransition: possibleTransitions) {
                // Or if we have an unvisited empty character '_' we can take it.
                if (possibleTransition.getLabel().equals(currentCharacter)) {
                    State destination = possibleTransition.getDestination();
                    String remainder = currentWord.substring(1);
                    return true && belongsToLanguage(destination, remainder);
                }
            }
        }

        return false;
    }
}
