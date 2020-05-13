package ale2.classes.Automaton.Regex;

import ale2.classes.Automaton.Diagram.State;
import ale2.classes.Automaton.Exceptions.IllegalCharacterInRegularExpressionException;
import ale2.classes.Automaton.Exceptions.ParenthesisMismatchException;
import ale2.classes.Automaton.Exceptions.RegularExpressionException;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static final String SPECIAL_SYMBOLS = ".|*";
    private List<Character> symbolStack;

    private State source;
    private State sink;

    public State parse(String regularExpression) throws RegularExpressionException {
        regularExpression = regularExpression.toLowerCase();
        checkMatchingNumberOfParenthesis(regularExpression);
        checkLegalCharacters(regularExpression);

        symbolStack = new ArrayList<>();
        parseHelper(regularExpression);

        // Then we

        return source;
    }

    private void checkMatchingNumberOfParenthesis(String regularExpression) throws ParenthesisMismatchException {
        int parenthesisSum = 0;

        for(char c: regularExpression.toCharArray()) {
            if (c == '(') {
                parenthesisSum++;
            } else if (c == ')') {
                parenthesisSum--;
            }
        }

        if (parenthesisSum != 0) {
            String openOrClose = parenthesisSum > 0 ? "opening" : "closing";
            throw new ParenthesisMismatchException("Too many " + openOrClose + " parenthesis in regular expression");
        }
    }

    private void checkLegalCharacters(String regularExpression) throws IllegalCharacterInRegularExpressionException {
        for (char c: regularExpression.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                throw new IllegalCharacterInRegularExpressionException(c + " is not a valid character for a regular expression");
            }
        }
    }

    private void parseHelper(String regularExpression) {
        // Elk character parsen
        // als dit een speciaal symbool is maken we een state aan
        // *(|(w,x))
        // * is special symbol thus we create a star matching state
        // we return the point of insertion?
        // the last symbol on the stack will get an initial state and accepting state

    }
}
