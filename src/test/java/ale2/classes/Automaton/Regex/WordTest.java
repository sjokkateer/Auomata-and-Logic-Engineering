package ale2.classes.Automaton.Regex;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class WordTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructor_nullGiven_expectedIllegalArgumentExceptionThrown()
    {
        // Arrange // Act // Assert
        Word word = new Word(null);
    }

    @Test
    @Parameters(method = "getEmptyWords")
    public void constructor_emptyWordGivenWithDifferentEpsilonCharacters_expectedEmptyWordLengthOneReturned(String emptyWord) {
        // Arrange
        Word word = new Word(emptyWord);

        // Act
        String expectedWordContent = "_";
        String actualWordContent = word.getWord();

        // Assert
        assertEquals("Since any empty word longer than one epsilon character should be converted to that of length 1", expectedWordContent, actualWordContent);
    }

    private static final Object[] getEmptyWords() {
        return $(
                $("_"),
                $("__"),
                $("___"),
                $("______________")
        );
    }

    @Test
    public void getWord_wordWithEpsilonCharactersGiven_shouldReturnContentWithoutEpsilonCharacters() {
        // Arrange
        Word word = new Word("_aa_b___c_");

        // Act
        String actualWordContent = word.getWord();
        String expectedWordContent = "aabc";

        // Assert
        assertEquals("Since the contents without epsilon characters should be returned", expectedWordContent, actualWordContent);
    }

    @Test
    public void getWord_wordWithoutEpsilonCharactersGiven_shouldReturnWordContent() {
        // Arrange
        Word word = new Word("yyyas");

        // Act
        String actualWordContent = word.getWord();
        String expectedWordContent = "yyyas";

        // Assert
        assertEquals("Since the contents without epsilon characters should be returned", expectedWordContent, actualWordContent);
    }

    @Test
    public void equals_equivalentWordsDifferentEpsilonCharacters_shouldReturnTrue() {
        // Arrange
        Word word = new Word("a__b_c");
        Word otherWord = new Word("_a_b_c");

        // Act
        boolean areEqual = word.equals(otherWord);

        // Assert
        assertTrue("Since epsilon characters should have no impact on the actual word content", areEqual);
    }

    @Test
    public void equals_equivalentWordsGiven_shouldReturnTrue() {
        // Arrange
        Word word = new Word("zzzth");
        Word otherWord = new Word("zzzth");

        // Act
        boolean areEqual = word.equals(otherWord);

        // Assert
        assertTrue("Since both words have identical contents", areEqual);
    }

    @Test
    public void equals_differingWordContentsGivenNoEpsilonCharacters_shouldReturnFalse() {
        // Arrange
        Word word = new Word("kla");
        Word otherWord = new Word("yuis");

        // Act
        boolean areEqual = word.equals(otherWord);

        // Assert
        assertFalse("Since both words have different contents", areEqual);
    }

    @Test
    public void equals_differingWordContentsGivenWithEpsilonCharacters_shouldReturnFalse() {
        // Arrange
        Word word = new Word("z__as_as__12");
        Word otherWord = new Word("a__hjs_a");

        // Act
        boolean areEqual = word.equals(otherWord);

        // Assert
        assertFalse("Since they have different contents with epsilon characters", areEqual);
    }

    @Test
    public void equals_differingObjectsGiven_shouldReturnFalse() {
        // Arrange
        Word word = new Word("zzzth");
        String otherObject = "test";

        // Act
        boolean areEqual = word.equals(otherObject);

        // Assert
        assertFalse("Since they are differing types of objects", areEqual);
    }

    @Test
    public void equals_nullGiven_shouldReturnFalse() {
        // Arrange
        Word word = new Word("zzzth");

        // Act
        boolean areEqual = word.equals(null);

        // Assert
        assertFalse("Since null is given", areEqual);
    }

    @Test
    @Parameters(method = "getWordContents")
    public void toString_wordBelongsToLanguage_expectedFormatWithCheckMark(String wordContent) {
        // Arrange
        Word word = new Word(wordContent);
        word.setBelongsToLanguage(true);

        // Act
        String expectedToString = word.getWord() + " -- " + "\u2713";
        String actualToString = word.toString();

        // Assert
        assertEquals("Since that's our format for representing it", expectedToString, actualToString);
    }

    private static final Object[] getWordContents() {
        return $(
                $("test"),
                $("t___e_st_")
        );
    }

    @Test
    @Parameters(method = "getWordContents")
    public void toString_wordDoesNotBelongToLanguage_expectedFormatWithCross(String wordContent) {
        // Arrange
        Word word = new Word(wordContent);
        word.setBelongsToLanguage(false);

        // Act
        String expectedToString = word.getWord() + " -- " + "\u2717";
        String actualToString = word.toString();

        // Assert
        assertEquals("Since that's our format for representing it", expectedToString, actualToString);
    }
}