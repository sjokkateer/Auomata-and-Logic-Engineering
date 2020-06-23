package ale2.classes.Automaton.Exceptions;

public class NotConvertibleEpsilonNfa extends NotConvertibleNfaException {
    public NotConvertibleEpsilonNfa() {
        super("Can't convert NFA with only epsilon transitions to DFA");
    }
}
