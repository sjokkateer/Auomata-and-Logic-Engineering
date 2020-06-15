package ale2.classes.Automaton;

import ale2.classes.Automaton.Diagram.PushDownAutomata;
import ale2.classes.Automaton.Diagram.StateDiagram;
import ale2.classes.Automaton.Exceptions.FileProcessingException;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class AutomatonTest {
    @Test
    public void fromFile_pushDownAutomataInputFile_expectedPushDownAutomataWithPushDownTransitionsCreated() throws FileProcessingException {
        // Arrange
        String existingPathToFile = AutomatonFileManager.getResourceFolder() + "/example_pda.txt";
        File existingFile = new File(existingPathToFile);

        Automaton automaton = Automaton.fromFile(existingFile);

        // Act
        StateDiagram stateDiagram = automaton.getStateDiagram();

        // Assert
        assertTrue("Since we expect based on the input file and processed data that we get back a push down automaton", stateDiagram.getClass() == PushDownAutomata.class);

        int expectedNumberOfTransitions = 5;
        assertEquals("Since " + expectedNumberOfTransitions + " transitions should be parsed and created", expectedNumberOfTransitions, stateDiagram.getAllTransitions().size());
    }

    @Test
    public void create_stateDiagramFromInputFile_expectedAutomatonWithRegularTransitionsCreated() throws FileProcessingException {
        // Arrange
        String existingPathToFile = AutomatonFileManager.getResourceFolder() + "/example_ndfa6.txt";
        File existingFile = new File(existingPathToFile);

        Automaton automaton = Automaton.fromFile(existingFile);

        // Act
        StateDiagram stateDiagram = automaton.getStateDiagram();

        // Assert
        assertTrue("Since we expect based on the input file and processed data that we get back a automaton's state diagram", stateDiagram.getClass() == StateDiagram.class);

        int expectedNumberOfTransitions = 8;
        assertEquals("Since " + expectedNumberOfTransitions + " transitions should be parsed and created", expectedNumberOfTransitions, stateDiagram.getAllTransitions().size());
    }
}