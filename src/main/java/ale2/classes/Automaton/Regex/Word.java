package ale2.classes.Automaton.Regex;

import java.util.Objects;

public class Word {
    public static int WORD = 0;
    public static int EXPECTED_PART_OF_LANGUAGE = 1;

    private String word;
    private boolean belongsToLanguage;
    private boolean isExpectedToBelongToLanguage;

    // Then maybe the word should be related to a state diagram rather than a non
    // indicating isPartOfLanguage
    public Word(String word, boolean isExpectedToBelongToLanguage) {
        if (word == null) {
            throw new IllegalArgumentException("Word's content may not be null!");
        }

        this.word = word;
        this.isExpectedToBelongToLanguage = isExpectedToBelongToLanguage;
    }

    public String getWord() {
        return word;
    }

    public boolean BelongsToLanguage() {
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
        Word word1 = (Word) o;
        return Objects.equals(word, word1.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", isPartOfLanguage=" + belongsToLanguage +
                ", isExpectedToBePartOfLanguage=" + isExpectedToBelongToLanguage +
                '}';
    }
}
