package ale2.classes.Automaton.Regex;

import ale2.classes.Automaton.Diagram.StateDiagram;
import ale2.classes.Automaton.Exceptions.ParenthesisMismatchException;
import ale2.classes.Automaton.Exceptions.RegularExpressionException;
import junitparams.Parameters;
import org.junit.Test;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;

public class ParserTest {

    @Test(expected = ParenthesisMismatchException.class)
    @Parameters(method = "getParenthesisMissmatchStrings")
    public void parse_nonMatchingNumberOfParenthesis_expectedParenthesisMismatchExceptionThrown(String inputString) throws RegularExpressionException {
        // Arrange
        Parser parser = new Parser();

        // Act // Assert
        StateDiagram stateDiagram = parser.parse(inputString);
    }

    private Object[] getParenthesisMissmatchStrings() {
        return $(
                $("((()"),
                $("())")
        );
    }

    @Test
    public void parseExpressionShouldReturnAStateDiagramMatchingExpression() throws RegularExpressionException {
        Parser parser = new Parser();
        StateDiagram stateDiagram = parser.parse("a");

    }

    @Test
    public void parseUnionExpressionShouldReturnAStateDiagramMatchingAUnionExpression() throws RegularExpressionException {
        Parser parser = new Parser();
        StateDiagram stateDiagram = parser.parse("|(a, b)");
    }

    @Test
    public void parseConcatenationExpressionShouldReturnAStateDiagramMatchingAConcatenationExpression() throws RegularExpressionException {
        Parser parser = new Parser();
        StateDiagram stateDiagram = parser.parse(".(a, b)");
    }

    @Test
    public void parseKleeneStarExpressionShouldReturnAStateDiagramMatchingAKleeneStarExpression() throws RegularExpressionException {
        Parser parser = new Parser();
        StateDiagram stateDiagram = parser.parse("*(a)");
    }

    @Test
    public void parseKleeneStarWithNestedExpressionShouldReturnAStateDiagramMatchingAKleeneStarWithNestedExpression() throws RegularExpressionException {
        Parser parser = new Parser();
        StateDiagram stateDiagram = parser.parse("*(.(a, b))");
    }
}