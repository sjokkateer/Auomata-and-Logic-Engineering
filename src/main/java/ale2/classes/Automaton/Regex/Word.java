package ale2.classes.Automaton.Regex;

import java.util.Objects;

public class Word {
    public static int WORD = 0;
    public static int EXPECTED_PART_OF_LANGUAGE = 1;

    private String word;
    private boolean belongsToLanguage;
    private boolean isExpectedToBelongToLanguage;

    public Word(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Word's content may not be null!");
        }

        this.word = processEmptyWord(word);
    }

    private String processEmptyWord(String word) {
        // If we deal with an empty word we want it to be of length 1 so just _
        // Then we can make a conditional in get word that will return words with length 1 immediately.
        // Such that word validation will not incorrectly include empty words as valid.
        char epsilon = '_';

        // Lets recognize this as the empty word too.
        if (word.length() == 0) {
            return String.valueOf(epsilon);
        }

        // We can just return the word, since if it's _ it is already in the form we would want it to be.
        if (word.length() == 1) {
            return word;
        }

        // We want to make sure any sequence of characters that represent epsilon > 1 is converted to _
        if (word.charAt(0) == epsilon) {
            for (int i = 1; i < word.length(); i++) {
                char currentChar = word.charAt(i);

                if (currentChar != epsilon) return word;
            }

            // if we exhausted all following characters were epsilon characters and thus this is an empty word equivalent.
            return String.valueOf(epsilon);
        }

        return word;
    }

    // Then maybe the word should be related to a state diagram rather than a non
    // indicating isPartOfLanguage
    public Word(String word, boolean isExpectedToBelongToLanguage) {
        this(word);
        this.isExpectedToBelongToLanguage = isExpectedToBelongToLanguage;
    }

    public String getWord() {
        String result = "";

        if (word.length() == 1) {
            result = word;
        } else {
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) != '_') result += word.charAt(i);
            }
        }

        return result;
    }

    public boolean doesBelongToLanguage() {
        return belongsToLanguage;
    }

    public void setBelongsToLanguage(boolean partOfLanguage) {
        belongsToLanguage = partOfLanguage;
    }

    public boolean isExpectedToBelongToLanguage() {
        return isExpectedToBelongToLanguage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word otherWord = (Word) o;
        return Objects.equals(getWord(), otherWord.getWord());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWord());
    }

    @Override
    public String toString() {
        return getWord() + " -- " + (doesBelongToLanguage() ? "\u2713" : "\u2717");
    }
}
