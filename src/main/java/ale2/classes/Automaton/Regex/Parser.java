package ale2.classes.Automaton.Regex;

import ale2.classes.Automaton.Diagram.*;
import ale2.classes.Automaton.Diagram.Thompson.*;
import ale2.classes.Automaton.Exceptions.IllegalCharacterInRegularExpressionException;
import ale2.classes.Automaton.Exceptions.ParenthesisMismatchException;
import ale2.classes.Automaton.Exceptions.RegularExpressionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
    private static final String SPECIAL_SYMBOLS = ".|*";

    private static String VALID_CHARACTERS = SPECIAL_SYMBOLS + ", ()";

    private Stack<Character> symbolStack;
    private Stack<StateDiagram> stateDiagramsStack;

    private int stateCounter;

    public StateDiagram parse(String regularExpression) throws RegularExpressionException {
        regularExpression = regularExpression.toLowerCase();
        checkMatchingNumberOfParenthesis(regularExpression);
        checkLegalCharacters(regularExpression);

        symbolStack = new Stack<>();
        stateDiagramsStack = new Stack<>();

        stateCounter = 0;

        parseHelper(regularExpression);

        StateDiagram finalStateDiagram = stateDiagramsStack.pop();

        State source = AbstractThompsonConstructionStrategy.getSource(finalStateDiagram);
        State sink = AbstractThompsonConstructionStrategy.getSink(finalStateDiagram);

        source.setInitial();
        sink.setAccepting();

        // Thus maybe the relabeling should actually happen here.
        finalStateDiagram.relabel();

        return finalStateDiagram;
    }

    private void checkMatchingNumberOfParenthesis(String regularExpression) throws ParenthesisMismatchException {
        int parenthesisSum = 0;

        for(char c: regularExpression.toCharArray()) {
            if (c == '(') {
                parenthesisSum++;
            } else if (c == ')') {
                parenthesisSum--;
            }
        }

        if (parenthesisSum != 0) {
            String openOrClose = parenthesisSum > 0 ? "opening" : "closing";
            throw new ParenthesisMismatchException("Too many " + openOrClose + " parenthesis in regular expression");
        }

        // Maybe this should be made a little smarter since now, *((x, y)) would pass as valid because it has equal
        // opening and closing.
        // But for this to work properly, first a special symbol comes, followed by an opening and at some point a closing.
    }

    private void checkLegalCharacters(String regularExpression) throws IllegalCharacterInRegularExpressionException {
        for (char c: regularExpression.toCharArray()) {
            if (!Character.isLetter(c) && VALID_CHARACTERS.indexOf(c) < 0) {
                throw new IllegalCharacterInRegularExpressionException(c + " is not a valid character for a regular expression");
            }
        }
    }

    private void parseHelper(String regularExpression) {
        char targetCharacter = regularExpression.charAt(0);

        if (isSpecialSymbol(targetCharacter)) {
            symbolStack.add(targetCharacter);
        } else {
            processCharacter(targetCharacter);
        }

        regularExpression = regularExpression.substring(1);

        if (regularExpression.length() > 0) {
            parseHelper(regularExpression);
        }
    }

    private void processCharacter(char targetCharacter) {
        if (Character.isLetter(targetCharacter)) {
            processExpression(targetCharacter);
        } else if (isClosingParenthesis(targetCharacter)) {
            processStackedSymbol();
        }
    }

    private void processExpression(char targetCharacter) {
        List<Transition> transitionList = new ArrayList<>();

        State source = createStateWithTemporarySymbol();
        State destination = createStateWithTemporarySymbol();

        Transition transition = new Transition(source, targetCharacter, destination);
        transitionList.add(transition);
        StateDiagram stateDiagram = StateDiagram.fromTransitions(transitionList);

        stateDiagramsStack.push(stateDiagram);
    }

    private State createStateWithTemporarySymbol() {
        return new State(String.valueOf(++stateCounter));
    }

    private boolean isSpecialSymbol(char targetCharacter) {
        return SPECIAL_SYMBOLS.indexOf(targetCharacter) >= 0;
    }

    private boolean isClosingParenthesis(char targetCharacter) {
        return targetCharacter == ')';
    }

    private void processStackedSymbol() {
        char specialSymbol = symbolStack.pop();

        switch(specialSymbol) {
            case '|':
                createUnionExpression();
                break;
            case '.':
                createConcatenationExpression();
                break;
            case '*':
                createKleeneStarExpression();
                break;
        }
    }

    private void createUnionExpression() {
        StateDiagram stateDiagramB = stateDiagramsStack.pop();
        StateDiagram stateDiagramA = stateDiagramsStack.pop();

        StateDiagram.setThompsonConstructionStrategy(
            new UnionExpressionStrategy(
                    stateDiagramA,
                    stateDiagramB,
                    stateCounter,
                    new StateCounterListener() {
                        @Override
                        public void onStateDiagramCreation(int stateCounter) {
                            updateStateCounter(stateCounter);
                        }
                    })
        );

        StateDiagram unionExpression = StateDiagram.create();

        addNewStateDiagramToStack(unionExpression);
    }

    private void updateStateCounter(int stateCounter) {
        this.stateCounter = stateCounter;
    }

    private void createConcatenationExpression() {
        StateDiagram stateDiagramB = stateDiagramsStack.pop();
        StateDiagram stateDiagramA = stateDiagramsStack.pop();

        StateDiagram.setThompsonConstructionStrategy(new ConcatenationExpressionStrategy(stateDiagramA, stateDiagramB));
        StateDiagram concatenationExpression = StateDiagram.create();

        addNewStateDiagramToStack(concatenationExpression);
    }

    private void addNewStateDiagramToStack(StateDiagram stateDiagram) {
        stateDiagramsStack.push(stateDiagram);
    }

    private void createKleeneStarExpression() {
        StateDiagram stateDiagram = stateDiagramsStack.pop();

        StateDiagram.setThompsonConstructionStrategy(new KleeneStarExpressionStrategy(stateDiagram, stateCounter, new StateCounterListener() {
            @Override
            public void onStateDiagramCreation(int stateCounter) {
                updateStateCounter(stateCounter);
            }
        }));

        StateDiagram kleeneStarExpression = StateDiagram.create();

        addNewStateDiagramToStack(kleeneStarExpression);
    }
}
