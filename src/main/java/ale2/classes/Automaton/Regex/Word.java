package ale2.classes.Automaton.Regex;

import java.util.Objects;

public class Word {
    public static int WORD = 0;
    public static int EXPECTED_PART_OF_LANGUAGE = 1;

    private String word;
    private boolean isPartOfLanguage;
    private boolean isExpectedToBePartOfLanguage;

    public Word(String word, boolean isExpectedToBePartOfLanguage) {
        if (word == null) {
            throw new IllegalArgumentException("Word's content may not be null!");
        }

        this.word = word;
        this.isExpectedToBePartOfLanguage = isExpectedToBePartOfLanguage;
    }

    public String getWord() {
        return word;
    }

    public boolean isPartOfLanguage() {
        return isPartOfLanguage;
    }

    public void setPartOfLanguage(boolean partOfLanguage) {
        isPartOfLanguage = partOfLanguage;
    }

    public boolean isExpectedToBePartOfLanguage() {
        return isExpectedToBePartOfLanguage;
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
                ", isPartOfLanguage=" + isPartOfLanguage +
                ", isExpectedToBePartOfLanguage=" + isExpectedToBePartOfLanguage +
                '}';
    }
}
