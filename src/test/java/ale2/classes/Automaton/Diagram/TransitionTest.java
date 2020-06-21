package ale2.classes.Automaton.Diagram;

import org.junit.Test;

import static org.junit.Assert.*;

public class TransitionTest {
    @Test
    public void toString_twoDifferentStatesGivenAndAValidLetter_expectedFormatReturned() {
        // Arrange
        State s1 = new State("q0");
        State s2 = new State("q1");

        char letter = 'x';

        Transition transition = new Transition(s1, letter, s2);

        // Act
        String expectedToString = "q0,x --> q1";
        String actualToString = transition.toString();

        // Assert
        assertEquals("Since: "+ expectedToString +" is the format we use to output it to a file", expectedToString, actualToString);
    }

    @Test
    public void getDotFileString_twoDifferentStatesGivenAndAValidLetter_expectedFormatReturned() {
        // Arrange
        State s1 = new State("q0");
        State s2 = new State("q1");

        char letter = 'x';

        Transition transition = new Transition(s1, letter, s2);

        // Act
        String expectedDotFileString = "\"q0\" -> \"q1\"[label=\"x\"]\n";
        String actualDotFileString = transition.getDotFileString();

        // Assert
        assertEquals("Since: "+ expectedDotFileString +" is the format we need to successfully create an image of the FA", expectedDotFileString, actualDotFileString);
    }
}