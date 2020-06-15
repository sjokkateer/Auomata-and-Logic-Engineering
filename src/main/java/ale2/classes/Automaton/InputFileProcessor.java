package ale2.classes.Automaton;

import ale2.classes.Automaton.Diagram.Transition;
import ale2.classes.Automaton.Exceptions.*;

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
    private static final String WORDS = "words";

    private Scanner scanner;

    private String alphabet;
    private List<String> states;
    private List<String> acceptingStates;
    private List<String[]> transitions;
    private List<String[]> words;

    public void process(File file) throws FileProcessingException {
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

    // Always default to the regular parsing of lines unless we find stack
    private void processLine(String line) throws FileProcessingException {
        String lineToLower = line.toLowerCase();

        if (lineToLower.contains(ALPHABET)) {
            processAlphabet(line);
        } else if (lineToLower.contains(STATES)) {
            processStates(line);
        } else if (lineToLower.contains(ACCEPTING_STATES)) {
            processAcceptingStates(line);
        } else if (lineToLower.contains(TRANSITIONS)) {
            processTransitions();
        } else if (lineToLower.contains(WORDS)) {
            processWords();
        }
    }

    private void resetFields() {
        alphabet = "";
        states = new ArrayList<>();
        acceptingStates = new ArrayList<>();
        transitions = new ArrayList<>();
        words = new ArrayList<>();
    }

    private void processAlphabet(String line) throws FileProcessingException {
        this.alphabet = getAppropriatePart(line);
    }

    private String getAppropriatePart(String line) throws FileProcessingException {
        String[] split = line.split(":");
        String targetPart = "";

        if (split.length < 2) throw new InvalidLineFormatException("Could not process line: '" + line + "'");

        targetPart = split[1].trim();

        return targetPart;
    }

    private void processStates(String line) throws FileProcessingException {
        String states = getAppropriatePart(line);
        this.states = splitStates(states);
    }

    private List<String> splitStates(String states) {
        return Arrays.asList(states.split(","));
    }

    private void processAcceptingStates(String line) throws FileProcessingException {
        String acceptingStates = getAppropriatePart(line);
        this.acceptingStates = splitStates(acceptingStates);
    }

    private void processTransitions() throws FileProcessingException {
        AddLine transitionMethod = (line) -> {
            transitions.add(processTransitionLine(line));
        };

        processSequenceOfLines(transitionMethod, "transitions");
    }

    private void processSequenceOfLines(AddLine method, String topic) throws FileProcessingException {
        String line = "";

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.toLowerCase().contains(END)) break;
            method.add(line);
        }

        if (!line.toLowerCase().contains(END)) throw new NoEndFoundException("Could not find the end. indication after your sequence of " + topic);
    }

    interface AddLine {
        void add(String line) throws FileProcessingException;
    }

    private String[] processTransitionLine(String transitionLine) throws FileProcessingException {
        String[] transitionElements = new String[3];

        String[] temporary = splitLine(",", transitionLine, transitionLine);
        transitionElements[Transition.SOURCE] = temporary[0].trim();

        temporary = splitLine("-->", temporary[1], transitionLine);
        transitionElements[Transition.LETTER] = temporary[0].trim();
        transitionElements[Transition.DESTINATION] = temporary[1].trim();

        return transitionElements;
    }

    private String[] splitLine(String splitPattern, String partToSplit, String transitionLine) throws FileProcessingException {
        String[] temporary = partToSplit.split(splitPattern);

        if (temporary.length != 2) {
            throw new InvalidLineFormatException("The given line is invalid: " + transitionLine);
        }

        return temporary;
    }

    private void processWords() throws FileProcessingException {
        AddLine wordMethod = (line) -> {
            words.add(processWordLine(line));
        };

        processSequenceOfLines(wordMethod, "words");
    }

    private String[] processWordLine(String line) throws FileProcessingException {
        return splitLine(",", line, line);
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

    public List<String[]> getWords() { return words; }
}
