package ale2.classes.Automaton;

import ale2.classes.Automaton.Diagram.State;
import ale2.classes.Automaton.Diagram.StateDiagram;
import ale2.classes.Automaton.Diagram.Transition;
import ale2.classes.Automaton.Exceptions.FileProcessingException;
import ale2.classes.Automaton.Regex.Word;
import ale2.classes.Automaton.Regex.WordValidator;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Automaton implements IDotFile {
    public static final String NAME = "myAutomaton";
    private static InputFileProcessor inputFileProcessor;

    private Set<Character> alphabet;
    private StateDiagram stateDiagram;
    private Set<Word> wordCollection;

    private WordValidator wordValidator;

    public Automaton(Set<Character> alphabet, StateDiagram stateDiagram, Set<Word> wordCollection) {
        this.alphabet = alphabet;
        this.stateDiagram = stateDiagram;
        this.wordCollection = wordCollection;

        wordValidator = new WordValidator(stateDiagram);
        wordValidator.check(wordCollection);
    }

    public static Automaton fromFile(File file) throws FileProcessingException {
        if (inputFileProcessor == null) {
            inputFileProcessor = new InputFileProcessor();
        }

        inputFileProcessor.process(file);

        Set<Character> alphabet = createAlphabetSet(inputFileProcessor.getAlphabet());
        StateDiagram stateDiagram = new StateDiagram(
                inputFileProcessor.getStates(),
                inputFileProcessor.getAcceptingStates(),
                inputFileProcessor.getTransitions()
        );
        Set<Word> wordCollection = createWordCollection(inputFileProcessor.getWords());

        return new Automaton(alphabet, stateDiagram, wordCollection);
    }

    private static Set<Character> createAlphabetSet(String alphabet) {
        Set<Character> alphabetSet = new HashSet<>();

        for (int i = 0; i < alphabet.length(); i++) {
            alphabetSet.add(alphabet.charAt(i));
        }

        return alphabetSet;
    }

    private static Set<Word> createWordCollection(List<String[]> words) {
        Set<Word> wordCollection = new HashSet<>();

        for (String[] wordElements: words) {
            String wordContent = wordElements[Word.WORD];
            // So any other letter than y and will default to false
            boolean expectedInLanguage = wordElements[Word.EXPECTED_PART_OF_LANGUAGE].equals("y") ? true : false;

            wordCollection.add(new Word(wordContent, expectedInLanguage));
        }

        return wordCollection;
    }

    public boolean isDFA() {
        for (State state: stateDiagram.getStates()) {
            if (doesNotMatchDFA(state)) return false;
        }

        return true;
    }

    private boolean doesNotMatchDFA(State state) {
        List<Transition> transitionList = stateDiagram.getTransitions(state);
        Set<Character> alphabet = getAlphabet();

        if (transitionList.size() != alphabet.size()) return true;

        for (Transition transition: transitionList) {
            if (!alphabet.contains(transition.getLabel())) return true;

            alphabet.remove(transition.getLabel());
        }

        return false;
    }

    public Set<Character> getAlphabet() {
        return new HashSet<>(alphabet);
    }

    public Set<Word> getWordCollection() {
        return wordCollection;
    }

    @Override
    public String getDotFileString() {
        String result = "digraph " + NAME + " { \n";
        result += stateDiagram.getDotFileString();
        result += "}\n";

        return result;
    }
}
