package ale2.classes.Automaton.Diagram;

import ale2.classes.Automaton.AutomatonFileManager;
import ale2.classes.Automaton.Exceptions.FileProcessingException;
import ale2.classes.Automaton.InputFileProcessor;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Set;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class StateDiagramTest {

    @Test(expected = IllegalArgumentException.class)
    public void getDestinations_nullGivenForState_expectedIllegalArgumentExceptionThrown()
    {
        // Arrange
        StateDiagram stateDiagram = new StateDiagram();
        State state = null;
        char letter = 'a';

        // Act // Assert
        stateDiagram.getDestinations(state, letter);
    }

    @Test
    public void getDestinations_existingStateNoTransitionOverGivenLetter_expectedEmptySetReturned() {
        // Arrange
        StateDiagram stateDiagram = new StateDiagram();

        State state = new State("q0");
        stateDiagram.getStates().add(state);

        char nonExistingLetter = '1';

        // Act
        Set<State> destinations = stateDiagram.getDestinations(state, nonExistingLetter);
        int expectedNumberOfDestinations = 0;

        // Assert
        assertEquals("Because there is no transition from the given state over letter " + nonExistingLetter, expectedNumberOfDestinations, destinations.size());
    }

    @Test
    @Parameters(method = "getNumberOfDestinationsOverTransition")
    public void getDestinations_existingStateWithDifferentNumberOfTransitionOverLetter_expectedSetOfSizeEqualToNumberOfTransitionsMadeReturned(int numberOfDestinations) {
        // Arrange
        StateDiagram stateDiagram = new StateDiagram();

        State state = new State("q0");
        stateDiagram.getStates().add(state);

        char existingLetter = 'a';

        State destination;
        // Creates a transition for a a number equal to numberOfDestinations and adds them to the stateDiagram
        for (int i = 1; i <= numberOfDestinations; i++) {
            destination = new State("q" + i);
            stateDiagram.getStates().add(destination);

            stateDiagram.addTransitionToCollection(new Transition(state, existingLetter, destination));
        }

        // Act
        Set<State> destinations = stateDiagram.getDestinations(state, existingLetter);
        int expectedNumberOfDestinations = numberOfDestinations;

        // Assert
        assertEquals("Because there should be " + expectedNumberOfDestinations + " transitions over letter " + existingLetter, expectedNumberOfDestinations, destinations.size());
    }

    private static final Object[] getNumberOfDestinationsOverTransition() {
        return $(
                $(1),
                $(3),
                $(5),
                $(10)
        );
    }

    @Test
    public void getDestinations_existingStateMultipleTransitionsToSelf_expectedSetOfSizeOneReturned() {
        // Arrange
        StateDiagram stateDiagram = new StateDiagram();

        State state = new State("q0");
        stateDiagram.getStates().add(state);

        char existingLetter = 'a';

        int numberOfTransitionsToSelf = 3;
        for (int i = 0; i < numberOfTransitionsToSelf; i++) {
            stateDiagram.addTransitionToCollection(new Transition(state, existingLetter, state));
        }

        // Act
        Set<State> destinations = stateDiagram.getDestinations(state, existingLetter);
        int expectedNumberOfDestinations = 1;

        // Assert
        assertEquals("Because there should be " + expectedNumberOfDestinations + " transition over letter " + existingLetter + " because they all go to the same state", expectedNumberOfDestinations, destinations.size());
    }
}