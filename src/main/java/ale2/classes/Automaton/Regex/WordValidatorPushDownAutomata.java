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

    }
}
