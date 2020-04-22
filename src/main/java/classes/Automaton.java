package classes;

import classes.Exceptions.InvalidLineFormatException;
import classes.Exceptions.InvalidTransitionFormatException;
import classes.Exceptions.NoTransitionEndFoundException;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Automaton {
    private static InputFileProcessor inputFileProcessor;
    private Set<Character> alphabet;
    private StateDiagram stateDiagram;

    public Automaton(Set<Character> alphabet, StateDiagram stateDiagram) {
        this.alphabet = alphabet;
        this.stateDiagram = stateDiagram;
    }

    public static Automaton fromFile(File file) throws InvalidTransitionFormatException, InvalidLineFormatException, NoTransitionEndFoundException {
        if (inputFileProcessor == null) {
            inputFileProcessor = new InputFileProcessor();
        }

        inputFileProcessor.process(file);
        Set<Character> alphabet = createAlphabetSet(inputFileProcessor.getAlphabet());
        // Create state diagram.

        StateDiagram stateDiagram = new StateDiagram(
                inputFileProcessor.getStates(),
                inputFileProcessor.getAcceptingStates(),
                inputFileProcessor.getTransitions()
        );

        Automaton automaton = new Automaton(alphabet, stateDiagram);
        return automaton;
    }

    private static Set<Character> createAlphabetSet(String alphabet) {
        Set<Character> alphabetSet = new HashSet<>();

        for (int i = 0; i < alphabet.length(); i++) {
            alphabetSet.add(alphabet.charAt(i));
        }

        return alphabetSet;
    }
}
