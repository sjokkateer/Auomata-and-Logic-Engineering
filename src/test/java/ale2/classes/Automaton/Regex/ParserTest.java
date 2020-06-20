package ale2.classes.Automaton.Regex;

import ale2.classes.Automaton.Diagram.State;
import ale2.classes.Automaton.Diagram.StateDiagram;
import ale2.classes.Automaton.Diagram.Thompson.AbstractThompsonConstructionStrategy;
import ale2.classes.Automaton.Diagram.Transition;
import ale2.classes.Automaton.Exceptions.IllegalCharacterInRegularExpressionException;
import ale2.classes.Automaton.Exceptions.ParenthesisMismatchException;
import ale2.classes.Automaton.Exceptions.RegularExpressionException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Set;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class ParserTest {

    @Test(expected = ParenthesisMismatchException.class)
    @Parameters(method = "getParenthesisMismatchStrings")
    public void parse_nonMatchingNumberOfParenthesisGiven_expectedParenthesisMismatchExceptionThrown(String inputString) throws RegularExpressionException {
        // Arrange
        Parser parser = new Parser();

        // Act // Assert
        StateDiagram stateDiagram = parser.parse(inputString);
    }

    private static final Object[] getParenthesisMismatchStrings() {
        return $(
                $("((()"),
                $("())")
        );
    }

    @Test(expected = IllegalCharacterInRegularExpressionException.class)
    @Parameters(method = "getIllegalCharacterInputString")
    public void parse_illegalInputCharactersGiven_expectedIllegalCharacterInRegularExpressionExceptionThrown(String illegalInputString) throws RegularExpressionException {
        // Arrange
        Parser parser = new Parser();

        // Act // Assert
        StateDiagram stateDiagram = parser.parse(illegalInputString);
    }

    private static final Object[] getIllegalCharacterInputString() {
        return $(
                $("&(b, b)"),
                $("*({a, b})"),
                $("@!#$")
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void parse_emptyStringGiven_expectedIllegalArgumentExceptionThrown() throws RegularExpressionException {
        // Arrange
        Parser parser = new Parser();

        // Act // Assert
        StateDiagram stateDiagram = parser.parse("");
    }

    @Test
    public void parse_symbolGiven_expressionShouldReturnAStateDiagramMatchingSymbol() throws RegularExpressionException {
        // Arrange
        Parser parser = new Parser();

        // Act
        StateDiagram stateDiagram = parser.parse("a");

        int expectedNumberOfTransitions = 1;
        int actualNumberOfTransitions = stateDiagram.getAllTransitions().size();

        // Assert
        assertEquals("Since only one transition connects the source and sink", expectedNumberOfTransitions, actualNumberOfTransitions);

        Transition transition = stateDiagram.getAllTransitions().get(0);
        assertTrue("Since a transition over a should be created", 'a' == transition.getLabel());

        State source = AbstractThompsonConstructionStrategy.getSource(stateDiagram);
        State sink = AbstractThompsonConstructionStrategy.getSink(stateDiagram);

        assertTrue("Since the source should be the initial state", source.isInitial());
        assertEquals("Since the source should connect the transition to the destination", transition.getSource(), source);

        assertTrue("Since the sink should be the accepting state", sink.isAccepting());
        assertEquals("Since the sink should connect the transition with the source", transition.getDestination(), sink);
    }

    @Test
    public void parse_unionExpressionGiven_shouldReturnAStateDiagramMatchingAUnionExpression() throws RegularExpressionException {
        // Arrange
        Parser parser = new Parser();
        StateDiagram stateDiagram = parser.parse("|(a, b)");

        // Act
        Set<State> states = stateDiagram.getStates();

        int expectedNumberOfStates = 6;
        int actualNumberOfStates = states.size();

        List<Transition> transitions = stateDiagram.getAllTransitions();

        int expectedNumberOfTransitions = 6;
        int actualNumberOfTransitions = transitions.size();

        int expectedLetterTransitions = 2; // for a and b.
        int actualLetterCounter = 0;

        // Assert
        assertEquals("Since there should be " + expectedNumberOfStates + " states total", expectedNumberOfStates, actualNumberOfStates);

        assertEquals("Since there should be 4 epsilon transitions and 2 with a letter", expectedNumberOfTransitions, actualNumberOfTransitions);

        for (Transition transition : transitions) {
            if (transition.getLabel() != 'a' && transition.getLabel() != 'b') {
                assertTrue("Since all other transitions should be epsilon ones",'_' == transition.getLabel());
            } else {
                actualLetterCounter++;
            }
        }

        assertEquals("Since only two transitions should have an actual letter as label", expectedLetterTransitions ,actualLetterCounter);
    }

    @Test
    public void parse_concatenationExpressionGiven_shouldReturnAStateDiagramMatchingAConcatenationExpression() throws RegularExpressionException {
        // Arrange
        Parser parser = new Parser();
        StateDiagram stateDiagram = parser.parse(".(a, b)");

        // Act
        Set<State> states = stateDiagram.getStates();

        int expectedNumberOfStates = 3;
        int actualNumberOfStates = states.size();

        List<Transition> transitions = stateDiagram.getAllTransitions();

        int expectedNumberOfTransitions = 2; // one for a and one for b
        int actualNumberOfTransitions = transitions.size();

        // Assert
        assertEquals("Since there should be " + expectedNumberOfStates + " states total", expectedNumberOfStates, actualNumberOfStates);

        assertEquals("Since there should be 2 transitions with a letter", expectedNumberOfTransitions, actualNumberOfTransitions);

        for (Transition transition : transitions) {
            assertTrue("Since there are only transitions with either letter",'a' == transition.getLabel() || 'b' == transition.getLabel());
        }
    }

    @Test
    public void parse_kleeneStarGiven_expressionShouldReturnAStateDiagramMatchingAKleeneStarExpression() throws RegularExpressionException {
        // Arrange
        Parser parser = new Parser();
        StateDiagram stateDiagram = parser.parse("*(a)");

        // Act
        Set<State> states = stateDiagram.getStates();

        int expectedNumberOfStates = 4;
        int actualNumberOfStates = states.size();

        List<Transition> transitions = stateDiagram.getAllTransitions();

        int expectedNumberOfTransitions = 5;
        int actualNumberOfTransitions = transitions.size();

        int expectedLetterTransitions = 1; // for a and b.
        int actualLetterCounter = 0;

        // Assert
        assertEquals("Since there should be " + expectedNumberOfStates + " states total", expectedNumberOfStates, actualNumberOfStates);

        assertEquals("Since there should be 4 epsilon transitions and 2 with a letter", expectedNumberOfTransitions, actualNumberOfTransitions);

        for (Transition transition : transitions) {
            if (transition.getLabel() != 'a') {
                assertTrue("Since all other transitions should be epsilon ones",'_' == transition.getLabel());
            } else {
                actualLetterCounter++;
            }
        }

        assertEquals("Since only two transitions should have an actual letter as label", expectedLetterTransitions ,actualLetterCounter);
    }

    @Test
    public void parse_kleeneStarWithNestedExpressionGiven_shouldReturnAStateDiagramMatchingAKleeneStarWithNestedExpression() throws RegularExpressionException {
        // Arrange
        Parser parser = new Parser();
        StateDiagram stateDiagram = parser.parse("*(.(a, b))");

        // Act
        Set<State> states = stateDiagram.getStates();

        int expectedNumberOfStates = 5;
        int actualNumberOfStates = states.size();

        List<Transition> transitions = stateDiagram.getAllTransitions();

        int expectedNumberOfTransitions = 6;
        int actualNumberOfTransitions = transitions.size();

        int expectedLetterTransitions = 2; // for a and b.
        int actualLetterCounter = 0;

        // Assert
        assertEquals("Since there should be " + expectedNumberOfStates + " states total", expectedNumberOfStates, actualNumberOfStates);

        assertEquals("Since there should be 4 epsilon transitions and 2 with a letter", expectedNumberOfTransitions, actualNumberOfTransitions);

        for (Transition transition : transitions) {
            if (transition.getLabel() != 'a' && transition.getLabel() != 'b') {
                assertTrue("Since all other transitions should be epsilon ones",'_' == transition.getLabel());
            } else {
                actualLetterCounter++;
            }
        }

        assertEquals("Since only two transitions should have an actual letter as label", expectedLetterTransitions ,actualLetterCounter);
    }
}
