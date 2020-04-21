package classes;

import classes.Exceptions.InvalidLineFormatException;
import classes.Exceptions.InvalidTransitionFormatException;
import classes.Exceptions.NoTransitionEndFoundException;

import java.io.File;

public class Automaton {
    private static InputFileProcessor inputFileProcessor;
    private String alphabet;
    private StateDiagram stateDiagram;

    public static Automaton fromFile(File file) throws InvalidTransitionFormatException, InvalidLineFormatException, NoTransitionEndFoundException {
        if (inputFileProcessor == null) {
            inputFileProcessor = new InputFileProcessor();
        }

        inputFileProcessor.process(file);

        return new Automaton();
    }
}
