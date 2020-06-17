package ale2.classes.Automaton.Regex;

import ale2.classes.Automaton.Diagram.State;
import ale2.classes.Automaton.Diagram.StateDiagram;

import java.util.Set;

public class WordValidatorPushDownAutomata extends WordValidatorBase {

    public WordValidatorPushDownAutomata(Set<Character> alphabet, StateDiagram stateDiagram) {
        super(alphabet, stateDiagram);
    }

    @Override
    protected void belongsToLanguage(State initialState, String currentWord, Word wordObject) {
        //
        // When true take the destination of the transition and get the next set of transitions.
        //

    }

    // Or maybe just take transition into returning boolean, if not possible the word validator
    // will take the next transition to see if that is possible.

    // if all possible ones run out the word could not be validated.

}
