package ale2.classes.Automaton.Regex;

import ale2.classes.Automaton.Diagram.StateDiagram;
import ale2.classes.Automaton.Exceptions.RegularExpressionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

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