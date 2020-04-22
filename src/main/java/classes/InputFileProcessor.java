package classes;

import classes.Exceptions.InvalidLineFormatException;
import classes.Exceptions.InvalidTransitionFormatException;
import classes.Exceptions.NoTransitionEndFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputFileProcessor {
    private static final String ALPHABET = "alphabet";
    private static final String STATES = "states";
    private static final String ACCEPTING_STATES = "final";
    private static final String TRANSITIONS = "transitions";
    private static final String END = "end.";

    private Scanner scanner;

    private String alphabet;
    private List<String> states;
    private List<String> acceptingStates;
    private List<String[]> transitions;

    public void process(File file) throws InvalidTransitionFormatException, NoTransitionEndFoundException, InvalidLineFormatException {
        resetFields();

        try {
            scanner = new Scanner(file);
            String line;

            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                processLine(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private void processLine(String line) throws InvalidTransitionFormatException, NoTransitionEndFoundException, InvalidLineFormatException {
        String lineToLower = line.toLowerCase();

        if (lineToLower.contains(ALPHABET)) {
            processAlphabet(line);
        } else if (lineToLower.contains(STATES)) {
            processStates(line);
        } else if (lineToLower.contains(ACCEPTING_STATES)) {
            processAcceptingStates(line);
        } else if (lineToLower.contains(TRANSITIONS)) {
            processTransitions();
        }
    }

    private void resetFields() {
        alphabet = "";
        states = new ArrayList<>();
        acceptingStates = new ArrayList<>();
        transitions = new ArrayList<>();
    }

    private void processAlphabet(String line) throws InvalidLineFormatException {
        this.alphabet = getAppropriatePart(line);
    }

    private String getAppropriatePart(String line) throws InvalidLineFormatException {
        String[] split = line.split(":");
        String targetPart = "";

        if (split.length < 2) throw new InvalidLineFormatException("Could not process line: '" + line + "'");

        targetPart = split[1].trim();

        return targetPart;
    }

    private void processStates(String line) throws InvalidLineFormatException {
        String states = getAppropriatePart(line);
        this.states = splitStates(states);
    }

    private List<String> splitStates(String states) {
        return Arrays.asList(states.split(","));
    }

    private void processAcceptingStates(String line) throws InvalidLineFormatException {
        String acceptingStates = getAppropriatePart(line);
        this.acceptingStates = splitStates(acceptingStates);
    }

    private void processTransitions() throws InvalidTransitionFormatException, NoTransitionEndFoundException {
        String line = "";

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.toLowerCase().contains(END)) break;
            transitions.add(processTransitionLine(line));
        }

        if (!line.toLowerCase().contains(END)) throw new NoTransitionEndFoundException("Your file did not contain an 'end.' for reading the transitions");
    }

    private String[] processTransitionLine(String transitionLine) throws InvalidTransitionFormatException {
        String[] transitionElements = new String[3];

        String[] temporary = splitTransition(",", transitionLine, transitionLine);
        transitionElements[Transition.SOURCE] = temporary[0].trim();

        temporary = splitTransition("-->", temporary[1], transitionLine);
        transitionElements[Transition.LETTER] = temporary[0].trim();
        transitionElements[Transition.DESTINATION] = temporary[1].trim();

        return transitionElements;
    }

    private String[] splitTransition(String splitPattern, String partToSplit, String transitionLine) throws InvalidTransitionFormatException {
        String[] temporary = partToSplit.split(splitPattern);

        if (temporary.length < 2) {
            throw new InvalidTransitionFormatException("The given transition is invalid: " + transitionLine);
        }

        return temporary;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public List<String> getStates() {
        return states;
    }

    public List<String> getAcceptingStates() {
        return acceptingStates;
    }

    public List<String[]> getTransitions() {
        return transitions;
    }
}
