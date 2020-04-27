package ale2.classes.Automaton.Regex;

public class Word {
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
