package ale2.classes.Automaton.Regex;

import ale2.classes.Automaton.Diagram.PushDownAutomata;
import ale2.classes.Automaton.Diagram.PushDownTransition;
import ale2.classes.Automaton.Diagram.State;
import ale2.classes.Automaton.Diagram.StateDiagram;

import java.util.List;
import java.util.Set;

public class WordValidatorPushDownAutomata extends WordValidatorBase {
    public WordValidatorPushDownAutomata(Set<Character> alphabet, StateDiagram stateDiagram) {
        super(alphabet, stateDiagram);
    }

    @Override
    public void validate(Word word) {
        ((PushDownAutomata)stateDiagram).resetStack();

        super.validate(word);
    }

    @Override
    // Always starts with the initial state given, the entire word content, and the word object belonging to the current word.
    protected void belongsToLanguage(State currentState, String currentWord, Word wordObject) {
        PushDownAutomata pushDownAutomata = (PushDownAutomata)stateDiagram;

        // Add another case to break out of the method in case we are trapped in a never ending cycle
        // Should most likely also be added to the original but we will expand the tests later on to verify this hypothesis

        // Thus we probably need to add a list and create a copy every time we pass it down such that they are not
        // the same reference.
        // Then in the list we check if we reach a cycle the first time.
        // If this is the case we store the word at that point in time.
        // Then the second time we reach

        // This would mean that when we backtrack we have the accurate path and do not get strange behavior in that sense.

        // Base case / termination condition
        if (currentWord.length() <= 0) {
            // Has to satisfy the following otherwise we nothing has to change, the word will still be
            // considered not part of the language.
            if (currentState.isAccepting() && ((PushDownAutomata)stateDiagram).isStackEmpty()) {
                wordObject.setBelongsToLanguage(true);
            } else {
                belongsToLanguage(currentState, currentWord + "_", wordObject);
            }
        } else {
            // We gotta continue our search.
            // Try every transition in our prio list skip epsilon that loops to self since
            // this won't change anything.
            char currentCharacter = currentWord.charAt(0);

            // The method to get the transitions is different.
            List<PushDownTransition> possibleTransitions = pushDownAutomata.getPossibleTransitions(currentState, currentCharacter);

            for (PushDownTransition transition: possibleTransitions) {
                // If label of transition is equivalent to currentchar we pass the resulting string
                // to this method with the destination of the transition.
                // otherwise we pass the currentWord on with the destination of the transition.
                State destination = transition.getDestination();

                // And this method call is different, since we gotta update the stack first.
                // Required to update the stack
                pushDownAutomata.takeTransition(transition);

                if (transition.getLabel() == currentCharacter) {
                    String remainder = currentWord.substring(1);
                    belongsToLanguage(destination, remainder, wordObject);
                } else {
                    belongsToLanguage(destination, currentWord, wordObject);
                }

                if (wordObject.doesBelongToLanguage()) {
                    break;
                }
            }
        }
    }
}
