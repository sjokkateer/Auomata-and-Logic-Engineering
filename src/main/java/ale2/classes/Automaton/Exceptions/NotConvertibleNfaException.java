package ale2.classes.Automaton.Exceptions;

public class NotConvertibleNfaException extends Exception {
    public NotConvertibleNfaException() {
        super("Can't convert NFA with NO transitions to DFA");
    }

    public NotConvertibleNfaException(String message) {
        super(message);
    }
}
