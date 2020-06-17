package ale2.classes.Automaton.Regex;

import ale2.classes.Automaton.Diagram.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class WordValidatorPushDownAutomata extends WordValidatorBase {
    private String currentWord;
    private int stackSize;

    public WordValidatorPushDownAutomata(Set<Character> alphabet, StateDiagram stateDiagram) {
        super(alphabet, stateDiagram);
    }

    @Override
    public void validate(Word word) {
        currentWord = null;
        stackSize = -1;

        State initialState = stateDiagram.getInitialState();

        // We should exit this method (the word will remain false (not belonging to the language)
        // in case there is no final/accepting state in the automata and or it contains letters not belonging
        // to the alphabet.
        if (areLettersInAlphabet(word) && stateDiagramHasAcceptingState()) {
            belongsToLanguage(initialState, word.getWord(), word, new Stack<>(), new ArrayList<>());
        }
    }

    @Override
    protected void belongsToLanguage(State initialState, String currentWord, Word wordObject) {
        throw new NotImplementedException();
    }

    // Always starts with the initial state given, the entire word content, and the word object belonging to the current word.
    private void belongsToLanguage(State currentState, String currentWord, Word wordObject, Stack<Character> currentStack, List<State> currentPath) {
        // Add another case to break out of the method in case we are trapped in a never ending cycle
        // Should most likely also be added to the original but we will expand the tests later on to verify this hypothesis

        // Thus we probably need to add a list and create a copy every time we pass it down such that they are not
        // the same reference.
        // Then in the list we check if we reach a cycle the first time.
        // If this is the case we store the word at that point in time.
        // Then the second time we reach
        if (!currentWord.equals("_") && currentPath.contains(currentState)) {
            // Indicates our first cycle occurred.
            if (this.currentWord == null && this.stackSize == -1) {
                this.currentWord = currentWord;
                this.stackSize = currentStack.size();
            } else {
                // This could be our nth cycle, check if the word and size changed else return.
                if (!this.currentWord.equals(currentWord) || this.stackSize != currentStack.size()) {
                    this.currentWord = currentWord;
                    this.stackSize = currentStack.size();
                } else {
                    return;
                }
            }
        }

        currentPath.add(currentState);

        // Can I just return since I consider this a dead end, and we get back at the point where we can continue our
        // transitions?

        // If backedge, assign the currentWord content and stack size to this object.
        // If we reach the next back edge we check to see if something changed.

        // This would mean that when we backtrack we have the accurate path and do not get strange behavior in that sense.

        // Base case / termination condition
        if (currentWord.length() <= 0) {
            // Has to satisfy the following otherwise we nothing has to change, the word will still be
            // considered not part of the language.
            if (currentState.isAccepting() && currentStack.empty()) {
                wordObject.setBelongsToLanguage(true);
            } else {
                belongsToLanguage(currentState, currentWord + "_", wordObject, currentStack, currentPath);
            }
        } else {
            // We gotta continue our search.
            // Try every transition in our prio list skip epsilon that loops to self since
            // this won't change anything.
            char currentCharacter = currentWord.charAt(0);

            // The method to get the transitions is different.
            List<PushDownTransition> possibleTransitions = getPossibleTransitions(currentState, currentCharacter, currentStack);

            for (PushDownTransition transition: possibleTransitions) {
                // If label of transition is equivalent to currentchar we pass the resulting string
                // to this method with the destination of the transition.
                // otherwise we pass the currentWord on with the destination of the transition.
                State destination = transition.getDestination();

                // And this method call is different, since we gotta update the stack first.
                // Required to update the stack
                Stack<Character> copyOfStack = (Stack<Character>)currentStack.clone();
                takeTransition(transition, copyOfStack);

                if (transition.getLabel() == currentCharacter) {
                    String remainder = currentWord.substring(1);
                    belongsToLanguage(destination, remainder, wordObject, copyOfStack, currentPath);
                } else {
                    belongsToLanguage(destination, currentWord, wordObject, copyOfStack, currentPath);
                }

                if (wordObject.doesBelongToLanguage()) {
                    break;
                }
            }
        }
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
    public List<PushDownTransition> getPossibleTransitions(State state, char letter, Stack<Character> stack) {

        List<PushDownTransition> result = new ArrayList<>();

        List<PushDownTransition> prioOne = new ArrayList<>();
        List<PushDownTransition> prioTwo = new ArrayList<>();
        List<PushDownTransition> prioThree = new ArrayList<>();
        List<PushDownTransition> prioFour = new ArrayList<>();

        for (Transition transition : stateDiagram.getTransitions(state)) {
            PushDownTransition pushDownTransition = (PushDownTransition) transition;

            if (pushDownTransition.getLabel() != '_' && pushDownTransition.getLabel() == letter) {
                // Priority one
                if (
                    !stack.empty()
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
                    !stack.empty()
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

    public boolean takeTransition(PushDownTransition transition, Stack<Character> stack) {
        if (transition.getStackPop() == '_' || (!stack.empty() && transition.getStackPop() == stack.peek())) {
            processStackCharacters(transition, stack);

            return true;
        }

        // Push the stack push char onto the stack
        return false;
    }

    private void processStackCharacters(PushDownTransition transition, Stack<Character> stack) {
        if (transition.getStackPop() != '_') {
            stack.pop();
        }

        if (transition.getStackPush() != '_') {
            stack.push(transition.getStackPush());
        }
    }
}
