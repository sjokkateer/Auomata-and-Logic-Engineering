package ale2.classes.Automaton;

import ale2.classes.Automaton.Exceptions.FileProcessingException;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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

        int expectedNumberOfWords = 9;
        assertEquals("Since no words were read, the Array of words should be empty ", expectedNumberOfWords, inputFileProcessor.getWords().size());
    }

    @Test(expected = FileProcessingException.class)
    public void process_existingFileGivenWithInvalidFormatAlphabet_expectedFileProcessingExceptionThrown() throws FileProcessingException {
        // Arrange
        InputFileProcessor inputFileProcessor = new InputFileProcessor();

        String existingPathToFile = AutomatonFileManager.getResourceFolder() + "/invalid_format_alphabet.txt";
        File existingFile = new File(existingPathToFile);

        // Act // Assert
        inputFileProcessor.process(existingFile);
    }

    @Test(expected = FileProcessingException.class)
    public void process_existingFileGivenWithInvalidFormatStates_expectedFileProcessingExceptionThrown() throws FileProcessingException {
        // Arrange
        InputFileProcessor inputFileProcessor = new InputFileProcessor();

        String existingPathToFile = AutomatonFileManager.getResourceFolder() + "/invalid_format_states.txt";
        File existingFile = new File(existingPathToFile);

        // Act // Assert
        inputFileProcessor.process(existingFile);
    }

    @Test(expected = FileProcessingException.class)
    public void process_existingFileGivenWithInvalidFormatAcceptingStates_expectedFileProcessingExceptionThrown() throws FileProcessingException {
        // Arrange
        InputFileProcessor inputFileProcessor = new InputFileProcessor();

        String existingPathToFile = AutomatonFileManager.getResourceFolder() + "/invalid_format_accepting_states.txt";
        File existingFile = new File(existingPathToFile);

        // Act // Assert
        inputFileProcessor.process(existingFile);
    }

    @Test(expected = FileProcessingException.class)
    public void process_existingFileGivenForTransitionsWithoutEndIndicator_expectedFileProcessingExceptionThrown() throws FileProcessingException {
        // Arrange
        InputFileProcessor inputFileProcessor = new InputFileProcessor();

        String existingPathToFile = AutomatonFileManager.getResourceFolder() + "/invalid_format_transitions_end.txt";
        File existingFile = new File(existingPathToFile);

        // Act // Assert
        inputFileProcessor.process(existingFile);
    }

    @Test(expected = FileProcessingException.class)
    public void process_existingFileGivenForTransitionsInvalidTransitionLine_expectedFileProcessingExceptionThrown() throws FileProcessingException {
        // Arrange
        InputFileProcessor inputFileProcessor = new InputFileProcessor();

        String existingPathToFile = AutomatonFileManager.getResourceFolder() + "/invalid_format_transitions_arrow.txt";
        File existingFile = new File(existingPathToFile);

        // Act // Assert
        inputFileProcessor.process(existingFile);
    }

    // All of these could be refactored into a parameterized test where the filename is input.
    @Test(expected = FileProcessingException.class)
    public void process_existingFileGivenForInvalidWordsFormat_expectedFileProcessingExceptionThrown() throws FileProcessingException {
        // Arrange
        InputFileProcessor inputFileProcessor = new InputFileProcessor();

        String existingPathToFile = AutomatonFileManager.getResourceFolder() + "/invalid_format_words.txt";
        File existingFile = new File(existingPathToFile);

        // Act // Assert
        inputFileProcessor.process(existingFile);
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

        int expectedNumberOfWords = 8;
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