package ale2.classes.Automaton.Regex;

import ale2.classes.Automaton.Automaton;
import ale2.classes.Automaton.AutomatonFileManager;
import ale2.classes.Automaton.Exceptions.FileProcessingException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class WordValidatorFiniteAutomataTest {

    private WordValidatorBase createWordValidatorFromFAFile(String fileName) {
        WordValidatorBase wordValidator = null;

        try {
            Automaton automaton = Automaton.fromFile(new File(AutomatonFileManager.getResourceFolder() + "/" + fileName));
            wordValidator = WordValidatorBase.create(automaton.getAlphabet(), automaton.getStateDiagram());
        } catch (FileProcessingException e) {
            e.printStackTrace();
        }

        return wordValidator;
    }

    @Test
    @Parameters(method = "invalidWordsTestInput")
    public void validate_onlyOneStateNoTransitions_expectedNoWordsToBelongToTheLanguage(String wordContent) {
        // Arrange
        WordValidatorBase wordValidator = createWordValidatorFromFAFile("test_input.txt");
        Word word = new Word(wordContent);

        // Act
        wordValidator.validate(word);

        boolean belongsToLanguage = word.doesBelongToLanguage();

        // Assert
        assertFalse("Since the given word '" + word.getWord() + "' would not belong to the test input FA's language", belongsToLanguage);
    }

    private static final Object[] invalidWordsTestInput() {
        return $(
                $("bskajbskajbskjbak"),
                $("_")
        );
    }

    @Test
    @Parameters(method = "getEmptyStringWords")
    public void validate_oneStateOneEpsilonTransition_expectedWordsToBePartOfTheLanguage(String wordContent) {
        // Arrange
        WordValidatorBase wordValidator = createWordValidatorFromFAFile("test_input_2.txt");
        Word word = new Word(wordContent);

        // Act
        wordValidator.validate(word);

        boolean belongsToLanguage = word.doesBelongToLanguage();

        // Assert
        assertTrue("Since all empty words belong to the input FA's language", belongsToLanguage);
    }

    private static final Object[] getEmptyStringWords() {
        return $(
                $(""),
                $("_"),
                $("__"),
                $("___"),
                $("____________________")
        );
    }

    @Test
    @Parameters(method = "getTestInputThreeInvalidWords")
    public void validate_oneStateEpsilonTransitionNoAccepting_expectedNoWordsToBePartOfTheLanguage(String wordContent) {
        // Arrange
        WordValidatorBase wordValidator = createWordValidatorFromFAFile("test_input_3.txt");
        Word word = new Word(wordContent);

        // Act
        wordValidator.validate(word);

        boolean belongsToLanguage = word.doesBelongToLanguage();

        // Assert
        assertFalse("Since input FA has no accepting state, no word should be part of the language", belongsToLanguage);
    }

    private static final Object[] getTestInputThreeInvalidWords() {
        return $(
                $("_"),
                $("a"),
                $("test"),
                $("123")
        );
    }

    @Test
    @Parameters(method = "getTestInputFourValidWords")
    public void validate_multipleStatesMultipleTransitions_expectedOnlySequencesOfBAccepted(String wordContent) {
        // Arrange
        WordValidatorBase wordValidator = createWordValidatorFromFAFile("test_input_4.txt");
        Word word = new Word(wordContent);

        // Act
        wordValidator.validate(word);

        boolean belongsToLanguage = word.doesBelongToLanguage();

        // Assert
        assertTrue("Since input FA has no accepting state, no word should be part of the language", belongsToLanguage);
    }

    private static final Object[] getTestInputFourValidWords() {
        return $(
                $("_"),
                $("b"),
                $("bbbb"),
                $("bbbbbbbbbbbbbbbbbb")
        );
    }

    @Test
    public void validate_infiniteSelfLoopEpsilonTransitionInNonAcceptingState_validationAlgorithmShouldHaltWordNotBelongingToLanguage() {
        // Arrange
        WordValidatorBase wordValidator = createWordValidatorFromFAFile("test_input_7_endless_self_loop.txt");
        Word word = new Word("a");

        // Act
        wordValidator.validate(word);

        boolean belongsToLanguage = word.doesBelongToLanguage();

        // Assert
        assertFalse("Since there is no accepting state to reach after reaching state q1", belongsToLanguage);
    }

    @Test
    public void validate_infiniteCycleOverEpsilonTransitionInNonAcceptingStates_validationAlgorithmShouldHaltWordNotBelongingToLanguage() {
        // Arrange
        WordValidatorBase wordValidator = createWordValidatorFromFAFile("test_input_8_endless_cycle.txt");
        Word word = new Word("1");

        // Act
        wordValidator.validate(word);

        boolean belongsToLanguage = word.doesBelongToLanguage();

        // Assert
        assertFalse("Since there is no accepting state to reach after reaching state 2", belongsToLanguage);
    }
}