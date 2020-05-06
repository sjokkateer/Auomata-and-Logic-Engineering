package ale2.classes.Automaton.Regex;

public class Word {
    public static int WORD = 0;
    public static int EXPECTED_PART_OF_LANGUAGE = 1;

    private String word;
    private boolean isPartOfLanguage;
    private boolean isExpectedToBePartOfLanguage;

    public Word(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", isPartOfLanguage=" + isPartOfLanguage +
                '}';
    }
}
