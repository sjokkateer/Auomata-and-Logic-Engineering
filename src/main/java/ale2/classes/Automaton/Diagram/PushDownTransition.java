package ale2.classes.Automaton.Diagram;

public class PushDownTransition extends Transition {
    public static final int SOURCE = 0;
    public static final int LETTER = 1;
    public static final int STACK_POP_CHARACTER = 2;
    public static final int STACK_PUSH_CHARACTER = 3;
    public static final int DESTINATION = 4;

    private char stackPop;
    private char stackPush;

    public PushDownTransition(State source, char letter, char stackPop, char stackPush, State destination) {
        super(source, letter, destination);
        this.stackPop = stackPop;
        this.stackPush = stackPush;
    }

    public char getStackPop() {
        return stackPop;
    }

    public char getStackPush() {
        return stackPush;
    }

    // Probably override the getDotFileString method
}
