package ale2.classes.Automaton.Diagram;

public class PdaTransition extends Transition {
    public static final int SOURCE = 0;
    public static final int LETTER = 1;
    public static final int STACK_POP_CHARACTER = 2;
    public static final int STACK_PUSH_CHARACTER = 3;
    public static final int DESTINATION = 4;


    public PdaTransition(State source, char letter, State destination) {
        super(source, letter, destination);
    }
}
