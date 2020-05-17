package ale2.classes.Automaton;

import ale2.classes.Automaton.Diagram.State;
import ale2.classes.Automaton.Diagram.StateDiagram;
import ale2.classes.Automaton.Diagram.Transition;
import ale2.classes.Automaton.Exceptions.FileProcessingException;
import ale2.classes.Automaton.Regex.Word;
import ale2.classes.Automaton.Regex.WordValidator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

    // Maybe alphabet should be removed from the automaton and be part of the wordValidator (which more or less manages all word/letter
    // related stuff. The automaton could then still call as client to obtain the alphabet from the wordvalidator obj.
    // Perhaps the word collection then too, and forward the call from automaton to wordvalidator as client.
    // And perhaps the wordvalidator should become a subclass of a management kind of class that only interacts with classes
    // by forwarding methods.
    public Automaton(Set<Character> alphabet, StateDiagram stateDiagram, Set<Word> wordCollection) {
        this.alphabet = alphabet;
        this.stateDiagram = stateDiagram;
        this.wordCollection = wordCollection;

        wordValidator = new WordValidator(this.alphabet, stateDiagram);
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

    public static Automaton fromStateDiagram(StateDiagram stateDiagram) {
        Set<Character> alphabet = new HashSet<Character>();

        for (Transition t: stateDiagram.getAllTransitions()) {
            alphabet.add(t.getLabel());
        }

        return new Automaton(alphabet, stateDiagram, new HashSet<Word>());
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
            boolean expectedInLanguage = wordElements[Word.EXPECTED_PART_OF_LANGUAGE].equals("y");

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

    public WordValidator getWordValidator() {
        return wordValidator;
    }

    @Override
    public String getDotFileString() {
        String result = "digraph " + NAME + " { \n";
        result += stateDiagram.getDotFileString();
        result += "}\n";

        return result;
    }

    public void exportToFile() {
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(AutomatonFileManager.getResourceFolder() + "/RE-export.txt", "UTF-8");
            writeAlphabet(writer);
            writeStates(writer);
            writeAcceptingStates(writer);
            writeTransitions(writer);
            writeIfDfa(writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

    private void writeIfDfa(PrintWriter writer) {
        String result = "dfa: ";

        if (isDFA()) {
            result += "y";
        } else {
            result += "n";
        }

        writer.println(result);
    }

    private void writeTransitions(PrintWriter writer) {
        String result = "transitions:\n";

        for (Transition transition: stateDiagram.getAllTransitions()) {
            result += transition + "\n";
        }

        result += "end.\n";

        writer.println(result);
    }

    private void writeAcceptingStates(PrintWriter writer) {
        String result = "final: ";

        for (State state: stateDiagram.getStates()) {
            if (state.isAccepting()) {
                result += state.getSymbol() + ",";
            }
        }

        result = trimTrailingCharacter(result);

        writer.println(result);
    }

    private String trimTrailingCharacter(String s) {
        return s.substring(0, s.length() - 1);
    }

    private void writeStates(PrintWriter writer) {
        String result = "states: ";
        State initial = stateDiagram.getInitialState();
        result += initial.getSymbol() + ",";

        for (State state: stateDiagram.getStates()) {
            if (!state.isInitial()) {
                result += state.getSymbol() + ",";
            }
        }

        // Trim trailing ','
        result = trimTrailingCharacter(result);

        writer.println(result);
    }

    private void writeAlphabet(PrintWriter writer) {
        String result = "alphabet: ";

        for (Character letter: alphabet) {
            result += letter;
        }

        writer.println(result);
    }
}
