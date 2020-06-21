package ale2.classes.Automaton.Diagram;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class StateTest {

    @Test
    public void constructor_defaultConstructor_expectedAllfieldsToDefaultToFalse() {
        // Arrange
        State state = new State();

        // Act // Assert
        assertFalse("Should default to false", state.isAccepting());
        assertFalse("Should default to false", state.isInitial());
        assertFalse("Should default to false", state.isVisited());
    }

    @Test
    public void constructor_constructingWithSymbol_expectedSymbolToMatch() {
        // Arrange
        State state = new State("1");

        // Act
        String actualSymbol = state.getSymbol();
        String expectedSymbol = "1";

        // Assert
        assertEquals("Since it should return the same symbol", expectedSymbol, actualSymbol);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_nullGivenForSymbol_expectedIllegalArgumentExceptionThrown() {
        // Arrange // Act // Assert
        State state = new State(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_emptyStringGivenForSymbol_expectedIllegalArgumentExceptionThrown() {
        // Arrange // Act // Assert
        State state = new State("");
    }

    @Test
    @Parameters(method = "getNonAcceptingStates")
    public void getDotFileString_nonAcceptingStateGiven_expectedCircleShape(State state, String expectedDotFileString) {
        // Arrange
        if (state.getSymbol().equals("2")) {
            state.setInitial();
        }

        // Act
        String actualDotFileString = state.getDotFileString();

        // Assert
        assertEquals("Since that's the required format for GraphViz to work with", expectedDotFileString, actualDotFileString);
    }

    public static final Object[] getNonAcceptingStates() {
        return $(
                $(new State("1"), "\"1\" [shape=circle]\n"),
                $(new State("2"), "\"2\" [shape=circle]\n")
        );
    }

    @Test
    public void getDotFileString_acceptingStateGiven_expectedCircleShape() {
        // Arrange
        State state = new State("1");
        state.setAccepting();

        // Act
        String actualDotFileString = state.getDotFileString();
        String expectedDotFileString = "\"1\" [shape=doublecircle]\n";

        // Assert
        assertEquals("Since an accepting state will be marked with a double circle in addition", expectedDotFileString, actualDotFileString);
    }

    @Test
    public void equals_differentTypeOfStateObjectGiven_expectedFalseReturned() {
        // Arrange
        State s1 = new State("1");
        State deadState = new DeadState();

        // Act
        boolean equal = s1.equals(deadState);

        // Assert
        assertFalse("Since the states are different types of states", equal);
    }

    @Test
    public void equals_statesWithDifferentSymbolsGiven_expectedFalseReturned() {
        // Arrange
        State s1 = new State("1");
        State s2 = new State("2");

        // Act
        boolean equal = s1.equals(s2);

        // Assert
        assertFalse("Since the states got different symbols", equal);
    }

    @Test
    public void equals_statesWithSameSymbolGiven_expectedTrueReturned() {
        // Arrange
        State s1 = new State("1");
        State s2 = new State("1");

        // Act
        boolean equal = s1.equals(s2);

        // Assert
        assertTrue("Since the states got identical symbols", equal);
    }
}