package ale2.classes.Automaton.Diagram;

import java.util.List;

public class PushDownAutomata extends StateDiagram {
    // Needs a stack for keeping track of the characters.
    // WordValidatorFiniteAutomata should also be able to operate on push down automatons

    public PushDownAutomata(List<String> states, List<String> acceptingStates, List<String[]> transitions) {
        super(states, acceptingStates, transitions);
    }

    @Override
    protected void addTransitions(List<String[]> transitions) {
        PushDownTransition t;
        State source = null;
        State destination = null;

        for (String[] transition: transitions) {
            // Example
            // ["q0", "a", _, x, "q1"]
            source = getState(source, transition[PushDownTransition.SOURCE]);
            char letter = transition[PushDownTransition.LETTER].charAt(0); // We should always have one character strings x) or we have invalid values.
            char stackPop = transition[PushDownTransition.STACK_POP_CHARACTER].charAt(0);
            char stackPush = transition[PushDownTransition.STACK_PUSH_CHARACTER].charAt(0);
            destination = getState(destination, transition[PushDownTransition.DESTINATION]);

            t = new PushDownTransition(source, letter, stackPop, stackPush,destination);

            addTransitionToCollection(t);
        }
    }
}
