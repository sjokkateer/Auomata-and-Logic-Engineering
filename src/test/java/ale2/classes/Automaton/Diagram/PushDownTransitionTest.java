package ale2.classes.Automaton.Diagram;

import org.junit.Test;

import static org.junit.Assert.*;

public class PushDownTransitionTest {

    @Test
    public void getDotFileString_pushDownTransitionCreatedFromDataIncludingStackInformation_expectedDotFileStringToDisplayStackInfo() {
        // Arrange
        PushDownTransition transition = new PushDownTransition(new State("q0"), '1', 'y', 'x', new State("q1"));

        // Act
        String expectedDotFileString = "\"q0\" -> \"q1\"[label=\"1 [y/x]\"]\n";
        String actualDotFileString = transition.getDotFileString();

        // Assert
        assertEquals("Strings should match since this is the required format", expectedDotFileString, actualDotFileString);
    }

    @Test
    public void getDotFileString_pushDownTransitionCreatedFromDataExcludingStackInformation_expectedDotFileStringToNotDisplayStackInfo() {
        // Arrange
        PushDownTransition transition = new PushDownTransition(new State("A"), 'b', '_', '_', new State("Y"));

        // Act
        String expectedDotFileString = "\"A\" -> \"Y\"[label=\"b\"]\n";
        String actualDotFileString = transition.getDotFileString();

        // Assert
        assertEquals("Strings should match since this is the required format", expectedDotFileString, actualDotFileString);
    }

    @Test
    public void toString_pushDownTransitionCreatedFromDataIncludingStackInformation_expectedToStringToDisplayStackInfo() {
        // Arrange
        PushDownTransition transition = new PushDownTransition(new State("q0"), '1', 'y', 'x', new State("q1"));


        // Act
        String expectedToString = "q0,1 [y,x] --> q1";
        String actualToString = transition.toString();

        // Assert
        assertEquals("Strings should match since this is the required format", expectedToString, actualToString);
    }

    @Test
    public void toString_pushDownTransitionCreatedFromDataExcludingStackInformation_expectedToStringToNotDisplayStackInfo() {
        // Arrange
        PushDownTransition transition = new PushDownTransition(new State("A"), 'b', '_', '_', new State("Y"));

        // Act
        String expectedToString = "A,b       --> Y";
        String actualToString = transition.toString();

        // Assert
        assertEquals("Strings should match since this is the required format", expectedToString, actualToString);
    }
}