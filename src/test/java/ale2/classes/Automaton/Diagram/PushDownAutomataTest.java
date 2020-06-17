package ale2.classes.Automaton.Diagram;

import ale2.classes.Automaton.Automaton;
import ale2.classes.Automaton.AutomatonFileManager;
import ale2.classes.Automaton.Exceptions.FileProcessingException;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class PushDownAutomataTest {
    private PushDownAutomata pda;

    // Test making transitions works such that we already have a good start for the final word validation.
    public PushDownAutomataTest() {
        String pdaFileName = "example_pda.txt";
        pda = getPdaFromFile(pdaFileName);
    }

    private PushDownAutomata getPdaFromFile(String fileName) {
        PushDownAutomata pda = null;

        try {
            Automaton automaton = Automaton.fromFile(new File(AutomatonFileManager.getResourceFolder() + "/" + fileName));
            pda = (PushDownAutomata) automaton.getStateDiagram();
        } catch (FileProcessingException e) {
            e.printStackTrace();
        }

        return pda;
    }

    @Test
    public void getPossibleTransitions_initialStateGivenOfExamplePdaWithLetterA_shouldReturnCollectionOfTwoPrioritizedAccordingly() {
        // Arrange
        State initialState = pda.getInitialState();
        List<PushDownTransition> possibleTransitions = pda.getPossibleTransitions(initialState, 'a');

        // Act
        int actualNumberOfTransitions = possibleTransitions.size();
        int expectedNumberOfTransitions = 2;

        // Assert
        assertEquals("Since this state has two possible transitions given the letter a", expectedNumberOfTransitions, actualNumberOfTransitions);
        assertTrue("Since the first of two returned transitions should be the one matching our letter of choice", possibleTransitions.get(0).getLabel() == 'a');
        assertTrue("Since the second of two returned transitions should be the one matching the epsilon character", possibleTransitions.get(1).getLabel() == '_');
    }

    @Test
    public void getPossibleTransitions_initialStateGivenOfExamplePdaWithLetterB_shouldReturnCollectionOfOneOverEmptyTransition() {
        // Arrange
        State initialState = pda.getInitialState();
        List<PushDownTransition> possibleTransitions = pda.getPossibleTransitions(initialState, 'b');

        // Act
        int actualNumberOfTransitions = possibleTransitions.size();
        int expectedNumberOfTransitions = 1;

        // Assert
        assertEquals("Since this state has two possible transitions given the letter a", expectedNumberOfTransitions, actualNumberOfTransitions);
        assertTrue("Since the first and only returned transition should be the one matching the epsilon character", possibleTransitions.get(0).getLabel() == '_');
    }

    @Test
    public void takeTransition_nonExistingTransition_shouldReturnFalse() {
        // Arrange
        PushDownTransition nonExistingTransition = new PushDownTransition(new State("S"), 'a', 'x','y', new State("C"));

        // Act
        boolean transitionTaken = pda.takeTransition(nonExistingTransition);

        // Assert
        assertFalse("Because the transition is not possible", transitionTaken);
    }

    @Test
    public void takeTransition_epsilonStackPopCharacter_shouldReturnTrue() {
        // Arrange
        // get initial state, get the transition over _ with pushing x onto the stack letter a.
        State initial = pda.getInitialState();
        List<PushDownTransition> transitions = pda.getPossibleTransitions(initial, 'a');

        PushDownTransition existingTransition = transitions.get(0);

        // Act
        boolean transitionTaken = pda.takeTransition(existingTransition);

        // Assert
        assertTrue("Because the transition should be possible as it has epsilon pop character", transitionTaken);
    }

    @Test
    public void takeTransition__shouldReturnTrue() {
        // Arrange
        // get initial state, get the transition over _ with pushing x onto the stack letter a.
        State initial = pda.getInitialState();
        List<PushDownTransition> transitions = pda.getPossibleTransitions(initial, 'a');

        PushDownTransition existingTransition = transitions.get(0);

        // Act
        boolean transitionTaken = pda.takeTransition(existingTransition);

        // Assert
        assertTrue("Because the transition should be possible as it has epsilon pop character", transitionTaken);
    }
}