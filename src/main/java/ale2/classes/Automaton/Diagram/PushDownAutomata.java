package ale2.classes.Automaton.Diagram;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PushDownAutomata extends StateDiagram {
    private Stack<Character> stack;

    public PushDownAutomata(List<String> states, List<String> acceptingStates, List<String[]> transitions) {
        super(states, acceptingStates, transitions);
        stack = new Stack<>();
    }

    public boolean takeTransition(PushDownTransition transition) {
        if (transition.getStackPop() == '_' || (!isStackEmpty() && transition.getStackPop() == stack.peek())) {
            processStackCharacters(transition);

            return true;
        }

        // Push the stack push char onto the stack
        return false;
    }

    private void processStackCharacters(PushDownTransition transition) {
        if (transition.getStackPop() != '_') {
            stack.pop();
        }

        if (transition.getStackPush() != '_') {
            stack.push(transition.getStackPush());
        }
    }

    public boolean isStackEmpty() {
        return stack.empty();
    }

    /**
     * Get all possible transitions for the given state over provided letter.
     *
     * This will thus include all epsilon transitions as well.
     *
     * The list is returned with the following priority:
     * - Transitions matching the given letter and the current stack pop symbol
     * - Transitions matching the given letter but with epsilon stack pop symbol
     * - Transitions matching the epsilon letter and the current stack pop symbol
     * - Transitions matching the epsilon letter with epsilon stack pop symbol
     *
     * @param state
     * @param letter
     * @return
     */
    public List<PushDownTransition> getPossibleTransitions(State state, char letter) {

        List<PushDownTransition> result = new ArrayList<>();

        List<PushDownTransition> prioOne = new ArrayList<>();
        List<PushDownTransition> prioTwo = new ArrayList<>();
        List<PushDownTransition> prioThree = new ArrayList<>();
        List<PushDownTransition> prioFour = new ArrayList<>();

        for (Transition transition : getTransitions(state)) {
            PushDownTransition pushDownTransition = (PushDownTransition) transition;

            if (pushDownTransition.getLabel() != '_' && pushDownTransition.getLabel() == letter) {
                // Priority one
                if (
                    !isStackEmpty()
                    && pushDownTransition.getStackPop() == stack.peek()
                ) {
                    prioOne.add(pushDownTransition);
                }

                // Priority two
                if (pushDownTransition.getStackPop() == '_') {
                    prioTwo.add(pushDownTransition);
                }
            }

            if (pushDownTransition.getLabel() == '_') {
                // Priority three
                if (
                    !isStackEmpty()
                    && pushDownTransition.getStackPop() == stack.peek()
                ) {
                    prioThree.add(pushDownTransition);
                }

                // Priority four
                if (pushDownTransition.getStackPop() == '_') {
                    prioFour.add(pushDownTransition);
                }
            }
        }

        // To ensure the priority order.
        // Even though according to description only one transition per prio rule
        // can occur for a given state. Thus at most we get 4 transitions back
        // Since this method more or less filters our valid possibilities.
        result.addAll(prioOne);
        result.addAll(prioTwo);
        result.addAll(prioThree);
        result.addAll(prioFour);

        return result;
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

    public void resetStack() {
        stack = new Stack<>();
    }
}
