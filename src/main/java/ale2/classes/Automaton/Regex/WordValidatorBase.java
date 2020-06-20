package ale2.classes.Automaton.Regex;

import ale2.classes.Automaton.Diagram.PushDownAutomata;
import ale2.classes.Automaton.Diagram.State;
import ale2.classes.Automaton.Diagram.StateDiagram;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

abstract public class WordValidatorBase {
    protected String currentWord;
    private Set<Character> alphabet;
    protected StateDiagram stateDiagram;

    protected WordValidatorBase(Set<Character> alphabet, StateDiagram stateDiagram) {
        this.alphabet = alphabet;
        this.stateDiagram = stateDiagram;
        this.currentWord = null;
    }

    public void check(Set<Word> wordCollection) {
        for (Word word: wordCollection) {
            validate(word);
        }
    }

    public void validate(Word word) {
        currentWord = null;
        State initialState = stateDiagram.getInitialState();

        // We should exit this method (the word will remain false (not belonging to the language)
        // in case there is no final/accepting state in the automata and or it contains letters not belonging
        // to the alphabet.
        if (areLettersInAlphabet(word) && stateDiagramHasAcceptingState()) {
            belongsToLanguage(initialState, word.getWord(), word, new ArrayList<>());
        }
    }

    abstract protected void belongsToLanguage(State initialState, String currentWord, Word wordObject, List<State> currentPath);

    // Maybe we need to ask whether or not the empty character _ is represented by an empty string word or not.
    protected boolean areLettersInAlphabet(Word word) {
        boolean result = true;
        String wordContent = word.getWord();

        for (int i = 0; i < wordContent.length(); i++) {
            result = result && isLetterInAlphabet(wordContent.charAt(i));
        }

        return result;
    }

    private boolean isLetterInAlphabet(char letter) {
        for (char alphabetCharacter: alphabet) {
            if (letter == alphabetCharacter) return true;
        }

        return false;
    }

    protected boolean stateDiagramHasAcceptingState() {
        for (State state: stateDiagram.getStates()) {
            if (state.isAccepting()) return true;
        }

        return false;
    }

    public static WordValidatorBase create(Set<Character> alphabet, StateDiagram stateDiagram) {
        if (alphabet == null) {
            throw new IllegalArgumentException("Alphabet is required for this class to work");
        }

        if (stateDiagram == null) {
            throw new IllegalArgumentException("State diagram is required for this class to work");
        }

        WordValidatorBase wordValidator = null;

        if (stateDiagram instanceof PushDownAutomata) {
            wordValidator = new WordValidatorPushDownAutomata(alphabet, stateDiagram);
        } else {
            wordValidator = new WordValidatorFiniteAutomata(alphabet, stateDiagram);
        }

        return wordValidator;
    }
}
