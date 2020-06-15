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

    @Override
    public String getDotFileString() {
        String result = super.getDotFileString();

        if (getStackPop() != '_' || getStackPush() != '_') {
            result = result.substring(0, result.length() - 3);

            result += " [" + getStackPop() + "/" + getStackPush() + "]";
            result += "\"]\n";
        }

        return result;
    }

    @Override
    public String toString() {
        String result = getSource().getSymbol() + "," + getLabel() + " ";

        if (getStackPop() == '_' && getStackPush() == '_') {
            result += "     ";
        } else {
            result += "[" + getStackPop() + "," + getStackPush() + "]";
        }

        result += " --> " + getDestination().getSymbol();

        return result;
    }
}
