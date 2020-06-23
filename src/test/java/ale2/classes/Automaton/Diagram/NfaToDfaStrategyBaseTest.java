package ale2.classes.Automaton.Diagram;

import ale2.classes.Automaton.Automaton;
import ale2.classes.Automaton.AutomatonFileManager;
import ale2.classes.Automaton.Exceptions.FileProcessingException;
import ale2.classes.Automaton.Exceptions.NotConvertibleEpsilonNfa;
import ale2.classes.Automaton.Exceptions.NotConvertibleNfaException;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NfaToDfaStrategyBaseTest {

    @Test(expected = NotConvertibleEpsilonNfa.class)
    public void determineStrategy_stateDiagramWithOnlyOneEpsilonTransitionsGiven_expectedNotConvertibleEpsilonNfaThrown() throws NotConvertibleNfaException {
        // Arrange
        State s1 = new State("1");
        State s2 = new State("2");

        Transition transition = new Transition(s1, '_', s2);

        List<Transition> transitions = new ArrayList<>();
        transitions.add(transition);

        StateDiagram stateDiagram = StateDiagram.fromTransitions(transitions);

        // Act // Assert
        NfaToDfaStrategyBase.determineStrategy(stateDiagram);
    }

    @Test(expected = NotConvertibleEpsilonNfa.class)
    public void determineStrategy_stateDiagramWithMultipleEpsilonTransitionsOnlyGiven_expectedNotConvertibleEpsilonNfaThrown() throws NotConvertibleNfaException {
        // Arrange
        State s1 = new State("1");
        State s2 = new State("2");

        Transition t1 = new Transition(s1, '_', s2);
        Transition t2 = new Transition(s1, '_', s1);
        Transition t3 = new Transition(s2, '_', s2);

        List<Transition> transitions = new ArrayList<>();
        transitions.add(t1);
        transitions.add(t2);
        transitions.add(t3);

        StateDiagram stateDiagram = StateDiagram.fromTransitions(transitions);

        // Act // Assert
        NfaToDfaStrategyBase.determineStrategy(stateDiagram);
    }

    @Test(expected = NotConvertibleNfaException.class)
    public void determineStrategy_stateDiagramWithNoTransitionsGiven_expectedNotConvertibleNfaExceptionThrown() throws NotConvertibleNfaException, FileProcessingException {
        // Arrange
        Automaton automaton = Automaton.fromFile(new File(AutomatonFileManager.getResourceFolder() + "/" + "test_input.txt"));
        StateDiagram stateDiagram = automaton.getStateDiagram();

        // Act // Assert
        NfaToDfaStrategyBase.determineStrategy(stateDiagram);
    }

    @Test
    public void determineStrategy_stateDiagramWithMixtureOfTransitionsGiven_expectedNfaToDfaWithEpsilonConversionStrategyReturned() throws NotConvertibleNfaException, FileProcessingException {
        // Arrange
        Automaton automaton = Automaton.fromFile(new File(AutomatonFileManager.getResourceFolder() + "/" + "dfa_georgiana.txt"));
        StateDiagram stateDiagram = automaton.getStateDiagram();

        // Act
        NfaToDfaStrategyBase strategy = NfaToDfaStrategyBase.determineStrategy(stateDiagram);

        // Assert
        assertTrue("Since the strategy should use that of the epsilon closure conversion for converting an NFA to DFA", strategy instanceof NfaToDfaWithEpsilonConversionStrategy);
    }
}