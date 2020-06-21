package ale2.classes.Automaton;

import ale2.classes.Automaton.Exceptions.FileProcessingException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class InputFileProcessorTest {

    @Test
    public void process_nonExistingFileGiven_expectedNoFieldsProvidedWithData() throws FileProcessingException {
        // Arrange
        InputFileProcessor inputFileProcessor = new InputFileProcessor();

        String nonExistingPathToFile = "blabla.txt";
        File nonExistingFile = new File(nonExistingPathToFile);

        // Act
        inputFileProcessor.process(nonExistingFile);

        // Assert
        String expectedAlphabet = "";
        assertEquals("Since no characters were read, the alphabet should be an empty String", expectedAlphabet, inputFileProcessor.getAlphabet());

        int expectedNumberOfStates = 0;
        assertEquals("Since no states were read, the List of states should be empty", expectedNumberOfStates, inputFileProcessor.getStates().size());

        int expectedNumberOfAcceptingStates = 0;
        assertEquals("Since no states were read, the List of accepting states should be empty", expectedNumberOfAcceptingStates, inputFileProcessor.getAcceptingStates().size());

        int expectedNumberOfTransitions = 0;
        assertEquals("Since no transitions were read, the Array of transitions should be empty", expectedNumberOfTransitions, inputFileProcessor.getTransitions().size());

        int expectedNumberOfWords = 0;
        assertEquals("Since no words were read, the Array of words should be empty ", expectedNumberOfWords, inputFileProcessor.getWords().size());
    }

    @Test
    public void process_existingFileGiven_expectedAllFieldsProvidedWithData() throws FileProcessingException {
        // Arrange
        InputFileProcessor inputFileProcessor = new InputFileProcessor();

        String existingPathToFile = AutomatonFileManager.getResourceFolder() + "/example_dfa1.txt";
        File existingFile = new File(existingPathToFile);

        // Act
        inputFileProcessor.process(existingFile);

        // Assert
        String expectedAlphabet = "ab";
        assertEquals("Since those " + expectedAlphabet.length() + " characters are given", expectedAlphabet, inputFileProcessor.getAlphabet());

        int expectedNumberOfStates = 1;
        assertEquals("Since the file should contain " + expectedNumberOfStates + " states", expectedNumberOfStates, inputFileProcessor.getStates().size());

        int expectedNumberOfAcceptingStates = 1;
        assertEquals("Since the file should contain " + expectedNumberOfAcceptingStates + " accepting states", expectedNumberOfAcceptingStates, inputFileProcessor.getAcceptingStates().size());

        int expectedNumberOfTransitions = 2;
        assertEquals("Since the file should contain " + expectedNumberOfTransitions + " transitions", expectedNumberOfTransitions, inputFileProcessor.getTransitions().size());

        List<String[]> actualTransitions = inputFileProcessor.getTransitions();
        List<String[]> transitions = getTransitions();

        for (int i = 0; i < transitions.size(); i++) {
            String[] transitionElements = transitions.get(i);
            String[] actualTransitionElements = actualTransitions.get(i);

            for (int j = 0; j < transitionElements.length; j++) {
                assertEquals("Both should be equal as this is the format we expected", transitionElements[j], actualTransitionElements[j]);
            }
        }

        int expectedNumberOfWords = 9;
        assertEquals("Since no words were read, the Array of words should be empty ", expectedNumberOfWords, inputFileProcessor.getWords().size());
    }

    private List<String[]> getTransitions() {
        List<String[]> transitions = new ArrayList<>();

        transitions.add(new String[] { "q0", "a", "q0" });
        transitions.add(new String[] { "q0", "b", "q0" });

        return transitions;
    }

    @Test(expected = FileProcessingException.class)
    @Parameters(method = "getInvalidFormatFiles")
    public void process_existingFileGivenWithInvalidFormatAlphabet_expectedFileProcessingExceptionThrown(String fileName) throws FileProcessingException {
        // Arrange
        InputFileProcessor inputFileProcessor = new InputFileProcessor();

        String existingPathToFile = AutomatonFileManager.getResourceFolder() + "/" + fileName;
        File existingFile = new File(existingPathToFile);

        // Act // Assert
        inputFileProcessor.process(existingFile);
    }

    private static final Object[] getInvalidFormatFiles() {
        return $(
                $( "/invalid_format_alphabet.txt"),
                $( "/invalid_format_states.txt"),
                $( "/invalid_format_accepting_states.txt"),
                $( "/invalid_format_transitions_end.txt"),
                $( "/invalid_format_transitions_arrow.txt"),
                $( "/invalid_format_words.txt")
        );
    }

    @Test
    public void process_existingFileContainingPDAFormat_expectedAllFieldsProvidedWithDataAndTransitionsCorrectlyParsed() throws FileProcessingException {
        // Arrange
        InputFileProcessor inputFileProcessor = new InputFileProcessor();

        String existingPathToFile = AutomatonFileManager.getResourceFolder() + "/example_pda.txt";
        File existingFile = new File(existingPathToFile);

        // Act
        inputFileProcessor.process(existingFile);

        // Assert
        String expectedAlphabet = "abc";
        assertEquals("Since those " + expectedAlphabet.length() + " characters are given", expectedAlphabet, inputFileProcessor.getAlphabet());

        int expectedNumberOfStates = 3;
        assertEquals("Since the file should contain " + expectedNumberOfStates + " states", expectedNumberOfStates, inputFileProcessor.getStates().size());

        int expectedNumberOfAcceptingStates = 1;
        assertEquals("Since the file should contain " + expectedNumberOfAcceptingStates + " accepting states", expectedNumberOfAcceptingStates, inputFileProcessor.getAcceptingStates().size());

        int expectedNumberOfTransitions = 5;
        assertEquals("Since the file should contain " + expectedNumberOfTransitions + " transitions", expectedNumberOfTransitions, inputFileProcessor.getTransitions().size());

        List<String[]> actualPdaTransitions = inputFileProcessor.getTransitions();
        List<String[]> pdaTransitions = getPdaTransitions();

        // Assert all individual elements are in the right format
        for (int i = 0; i < pdaTransitions.size(); i++) {
            String[] pdaTransitionElements = pdaTransitions.get(i);
            String[] actualPdaTransitionElement = actualPdaTransitions.get(i);

            for (int j = 0; j < pdaTransitionElements.length; j++) {
                assertEquals("Both should be equal as this is the format we expected", pdaTransitionElements[j], actualPdaTransitionElement[j]);
            }
        }

        int expectedNumberOfWords = 0;
        assertEquals("Since no words were read, the Array of words should be empty ", expectedNumberOfWords, inputFileProcessor.getWords().size());
    }

    private List<String[]> getPdaTransitions() {
        List<String[]> pdaTransitions = new ArrayList<>();

        pdaTransitions.add(new String[] { "S", "a", "_", "x", "S" });
        pdaTransitions.add(new String[] { "S", "_", "_", "_", "B" });
        pdaTransitions.add(new String[] { "B", "b", "_", "x", "B" });
        pdaTransitions.add(new String[] { "B", "_", "_", "_", "C" });
        pdaTransitions.add(new String[] { "C", "c", "x", "_", "C" });

        return pdaTransitions;
    }
}