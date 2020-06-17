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
public class WordValidatorPushDownAutomataTest {
    private WordValidatorBase wordValidator;

    public WordValidatorPushDownAutomataTest() {
        String pdaFileName = "example_pda.txt";
        try {
            wordValidator = createWordValidatorFromFAFile(pdaFileName);
        } catch (FileProcessingException e) {
            e.printStackTrace();
        }
    }

    private WordValidatorBase createWordValidatorFromFAFile(String fileName) throws FileProcessingException {
        Automaton automaton = Automaton.fromFile(new File(AutomatonFileManager.getResourceFolder() + "/" + fileName));

        return WordValidatorBase.create(automaton.getAlphabet(), automaton.getStateDiagram());
    }

    // For the example PDA any word following: (a**n)(b**m)(c**(n + m))
    // And of course then the empty word would result in 0 as, 0 bs and 0 cs according to the formula.
    // Examples in language:
    // - aaaccc
    // - aabccc
    // - abbccc
    //
    // As long as the number of (actual) characters preceding c is equal to the number of cs, the word should
    // be accepted.

    @Test
    @Parameters(method = "getValidWords")
    public void validate_wordIsPartOfTheLanguage_expectedWordsFlagToBeSetToTrueIndicatingItBelongsToTheLanguage(String wordContent) {
        // Arrange
        Word word = new Word(wordContent);

        // Act
        wordValidator.validate(word);
        boolean isAccepted = word.doesBelongToLanguage();

        // Assert
        assertTrue("Since this is a valid word, accepted by the pda's language", isAccepted);
    }

    public static final Object[] getValidWords() {
        return $(
            $("aabbbbcccccc"),
            $("abcc"),
            $("ac"),
            $("aaaccc"),
            $("bc"),
            $("bbbccc")
        );
    }

    @Test
    @Parameters(method = "getInvalidWords")
    public void validate_wordIsNotPartOfTheLanguage_expectedWordsFlagToBeSetToFalseIndicatingItDoesNotBelongsToTheLanguage(String wordContent) {
        // Arrange
        Word word = new Word(wordContent);

        // Act
        wordValidator.validate(word);
        boolean isAccepted = word.doesBelongToLanguage();

        // Assert
        assertFalse("Since this is a word NOT accepted by the pda's language", isAccepted);
    }

    // Tests:
    // - Transition possible but not final state
    public static final Object[] getInvalidWords() {
        return $(
                $("a"),
                $("b"),
                $("c"),
                $("abc"),
                $("acccc"),
                $("bcc"),
                $("cccccc"),
                $("noletter")
            );
    }

    // Test other PDA's and words
    @Test
    public void validate_wordIsPartOfTheLanguageWithSelfLoops_expectedWordToBePartOfLanguage() throws FileProcessingException {
        // Arrange
        wordValidator = createWordValidatorFromFAFile("example_pda_2.txt");
        Word word = new Word("11");

        // Act
        wordValidator.validate(word);
        boolean isAccepted = word.doesBelongToLanguage();

        // Assert
        assertTrue("Since this is a valid word, accepted by the pda's language", isAccepted);
    }

    @Test
    public void validate_wordIsPartOfTheLanguageWithPdaEmptyingStackBasedOnEpsilonTransitionsOnAcceptingState_expectedWordToBePartOfLanguage() throws FileProcessingException {
        // Arrange
        wordValidator = createWordValidatorFromFAFile("example_pda_3.txt");
        Word word = new Word("000");

        // Act
        wordValidator.validate(word);
        boolean isAccepted = word.doesBelongToLanguage();

        // Assert
        assertTrue("Since this is a valid word, accepted by the pda's language", isAccepted);
    }

    @Test
    @Parameters(method = "getExamplesFromReader")
    public void validate_wordIsPartOfTheLanguagePriorityRulesApplied_expectedWordsToBePartOfTheLanguage(String wordContent) throws FileProcessingException {
        // Arrange
        wordValidator = createWordValidatorFromFAFile("example_pda_4.txt");
        Word word = new Word(wordContent);

        // Act
        wordValidator.validate(word);
        boolean isAccepted = word.doesBelongToLanguage();

        // Assert
        assertTrue("Since this is a valid word, accepted by the pda's language", isAccepted);
    }

    public Object[] getExamplesFromReader() {
        return $(
            $("ad"),
            $("be"),
            $("cef"),
            $("ag"),
            $("b"),
            $("ch")
        );
    }
}