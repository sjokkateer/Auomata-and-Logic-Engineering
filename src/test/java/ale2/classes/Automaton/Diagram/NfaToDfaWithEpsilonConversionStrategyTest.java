package ale2.classes.Automaton.Diagram;

import ale2.classes.Automaton.Automaton;
import ale2.classes.Automaton.AutomatonFileManager;
import ale2.classes.Automaton.Exceptions.FileProcessingException;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class NfaToDfaWithEpsilonConversionStrategyTest {

    private StateDiagram getStateDiagramFromFile(String fileName) {
        StateDiagram stateDiagram = null;

        try {
            Automaton automaton = Automaton.fromFile(new File(AutomatonFileManager.getResourceFolder() + "/" + fileName));
            stateDiagram = automaton.getStateDiagram();
        } catch (FileProcessingException e) {
            e.printStackTrace();
        }

        return stateDiagram;
    }

    @Test
    public void convertToDfa_nfaWithOnlyEpsilonTransitionsGiven_expectedNotConvertibleEpsilonNfaThrown() {
        // Arrange
        StateDiagram stateDiagram = getStateDiagramFromFile("wikipedia.txt");
        NfaToDfaWithEpsilonConversionStrategy nfaToDfa = new NfaToDfaWithEpsilonConversionStrategy(stateDiagram);

        // Act // Assert
        StateDiagram dfa = nfaToDfa.convertToDfa();
    }

    @Test
    public void convertToDfa_wikipediaExampleWithMultipleCyclesAndEpsilonTransitionsNfaGiven_expectedDfaWithFourSubSetStatesAndOneDeadState() {
        // Arrange
        StateDiagram stateDiagram = getStateDiagramFromFile("wikipedia.txt");
        NfaToDfaWithEpsilonConversionStrategy nfaToDfa = new NfaToDfaWithEpsilonConversionStrategy(stateDiagram);

        // Act
        StateDiagram dfa = nfaToDfa.convertToDfa();
        Set<State> states = dfa.getStates();

        int expectedNumberOfSubsetStates = 4;
        int expectedNumberOfDeadStates = 1;
        int expectedNumberOfStates = expectedNumberOfSubsetStates + expectedNumberOfDeadStates;

        // Assert
        int actualNumberOfStates = states.size();
        assertEquals("Since the example input should convert into five states total", expectedNumberOfStates, actualNumberOfStates);

        // Assert we got the correct states (symbols), in the result dfa
        List<String> subsetStateStrings = new ArrayList<>();
        for (State state: states) {
            subsetStateStrings.add(state.toString());
        }

        assertTrue(subsetStateStrings.contains("{ 1 2 3 }"));
        assertTrue(subsetStateStrings.contains("{ 2 4 }"));
        assertTrue(subsetStateStrings.contains("{ 2 3 }"));
        assertTrue(subsetStateStrings.contains("{ 4 }"));
        assertTrue(subsetStateStrings.contains(DeadState.SYMBOL)); // Phi for dead state / sink

        // Assert it matches the actual DFA pattern that we implemented in our automaton class to
        Automaton automaton = Automaton.fromStateDiagram(dfa);
        assertTrue("Since we supposedly converted the NFA to DFA, it should match our DFA pattern implementation", automaton.isDFA());
    }

    @Test
    public void convertToDfa_lectureExampleNfaWithNonEpsilonCycleGiven_expectedDfaWithFourSubSetStatesAndOneDeadState() {
        // Arrange
        StateDiagram stateDiagram = getStateDiagramFromFile("dfa_georgiana.txt");
        NfaToDfaWithEpsilonConversionStrategy nfaToDfa = new NfaToDfaWithEpsilonConversionStrategy(stateDiagram);

        // Act
        StateDiagram dfa = nfaToDfa.convertToDfa();
        Set<State> states = dfa.getStates();

        int expectedNumberOfSubsetStates = 4;
        int expectedNumberOfDeadStates = 1;
        int expectedNumberOfStates = expectedNumberOfSubsetStates + expectedNumberOfDeadStates;

        // Assert
        int actualNumberOfStates = states.size();
        assertEquals("Since the example input should convert into five states total", expectedNumberOfStates, actualNumberOfStates);

        // Assert we got the correct states (symbols), in the result dfa
        List<String> subsetStateStrings = new ArrayList<>();
        for (State state: states) {
            subsetStateStrings.add(state.toString());
        }

        assertTrue(subsetStateStrings.contains("{ 1 3 }"));
        assertTrue(subsetStateStrings.contains("{ 2 5 }"));
        assertTrue(subsetStateStrings.contains("{ 4 }"));
        assertTrue(subsetStateStrings.contains("{ 5 }"));
        assertTrue(subsetStateStrings.contains(DeadState.SYMBOL));

        // Assert it matches the actual DFA pattern that we implemented in our automaton class to
        Automaton automaton = Automaton.fromStateDiagram(dfa);
        assertTrue("Since we supposedly converted the NFA to DFA, it should match our DFA pattern implementation", automaton.isDFA());
    }

    @Test
    public void convertToDfa_exampleWithEpsilonCycle_expectedThreeSubsetStatesAndOneConnectedDeadState() {
        // Arrange
        StateDiagram stateDiagram = getStateDiagramFromFile("dfa_test_1.txt");
        NfaToDfaWithEpsilonConversionStrategy nfaToDfa = new NfaToDfaWithEpsilonConversionStrategy(stateDiagram);

        // Act
        StateDiagram dfa = nfaToDfa.convertToDfa();
        Set<State> states = dfa.getStates();

        int expectedNumberOfSubsetStates = 3;
        int expectedNumberOfDeadStates = 1;
        int expectedNumberOfStates = expectedNumberOfSubsetStates + expectedNumberOfDeadStates;

        // Assert
        int actualNumberOfStates = states.size();
        assertEquals("Since the example input should convert into five states total", expectedNumberOfStates, actualNumberOfStates);

        // Assert we got the correct states (symbols), in the result dfa
        List<String> subsetStateStrings = new ArrayList<>();
        for (State state: states) {
            subsetStateStrings.add(state.toString());
        }

        assertTrue(subsetStateStrings.contains("{ q1 q2 q0 }"));
        assertTrue(subsetStateStrings.contains("{ q3 }"));
        assertTrue(subsetStateStrings.contains("{ q4 }"));
        assertTrue(subsetStateStrings.contains(DeadState.SYMBOL));

        // Assert it matches the actual DFA pattern that we implemented in our automaton class to
        Automaton automaton = Automaton.fromStateDiagram(dfa);
        assertTrue("Since we supposedly converted the NFA to DFA, it should match our DFA pattern implementation", automaton.isDFA());
    }

    @Test
    public void convertToDfa_nfaToDfaSinkNotUtilized_expectedThreeSubsetStatesOneSinkNotConnected() {
        // Arrange
        StateDiagram stateDiagram = getStateDiagramFromFile("geek_for_geeks_1.txt");
        NfaToDfaWithEpsilonConversionStrategy nfaToDfa = new NfaToDfaWithEpsilonConversionStrategy(stateDiagram);

        // Act
        StateDiagram dfa = nfaToDfa.convertToDfa();
        Set<State> states = dfa.getStates();

        int expectedNumberOfSubsetStates = 3;
        int expectedNumberOfDeadStates = 1;
        int expectedNumberOfStates = expectedNumberOfSubsetStates + expectedNumberOfDeadStates;

        // Assert
        int actualNumberOfStates = states.size();
        assertEquals("Since the example input should convert into five states total", expectedNumberOfStates, actualNumberOfStates);

        // Assert we got the correct states (symbols), in the result dfa
        List<String> subsetStateStrings = new ArrayList<>();
        for (State state: states) {
            subsetStateStrings.add(state.toString());
        }

        assertTrue(subsetStateStrings.contains("{ q0 }"));
        assertTrue(subsetStateStrings.contains("{ q1 q0 }"));
        assertTrue(subsetStateStrings.contains("{ q2 q0 }"));
        assertTrue(subsetStateStrings.contains(DeadState.SYMBOL));

        // Assert dead state has no incoming transitions since it should not be utilized.
        for(Transition transition: stateDiagram.getAllTransitions()) {
            assertTrue("Since the dead state should not be used", !(transition.getDestination() instanceof DeadState));
        }

        // Assert it matches the actual DFA pattern that we implemented in our automaton class to
        Automaton automaton = Automaton.fromStateDiagram(dfa);
        assertTrue("Since we supposedly converted the NFA to DFA, it should match our DFA pattern implementation", automaton.isDFA());
    }

    @Test
    public void convertToDfa_javaTPointOneNfaToDfaSinkNotUtilized_expectedThreeSubsetStatesOneSinkNotConnected() {
        // Arrange
        StateDiagram stateDiagram = getStateDiagramFromFile("javatpoint_1.txt");
        NfaToDfaWithEpsilonConversionStrategy nfaToDfa = new NfaToDfaWithEpsilonConversionStrategy(stateDiagram);

        // Act
        StateDiagram dfa = nfaToDfa.convertToDfa();
        Set<State> states = dfa.getStates();

        int expectedNumberOfSubsetStates = 3;
        int expectedNumberOfDeadStates = 1;
        int expectedNumberOfStates = expectedNumberOfSubsetStates + expectedNumberOfDeadStates;

        // Assert
        int actualNumberOfStates = states.size();
        assertEquals("Since the example input should convert into five states total", expectedNumberOfStates, actualNumberOfStates);

        // Assert we got the correct states (symbols), in the result dfa
        List<String> subsetStateStrings = new ArrayList<>();
        for (State state: states) {
            subsetStateStrings.add(state.toString());
        }

        assertTrue(subsetStateStrings.contains("{ q0 }"));
        assertTrue(subsetStateStrings.contains("{ q1 }"));
        assertTrue(subsetStateStrings.contains("{ q1 q2 }"));
        assertTrue(subsetStateStrings.contains(DeadState.SYMBOL));

        // Assert dead state has no incoming transitions since it should not be utilized.
        for(Transition transition: stateDiagram.getAllTransitions()) {
            assertTrue("Since the dead state should not be used", !(transition.getDestination() instanceof DeadState));
        }

        // Assert it matches the actual DFA pattern that we implemented in our automaton class to
        Automaton automaton = Automaton.fromStateDiagram(dfa);
        assertTrue("Since we supposedly converted the NFA to DFA, it should match our DFA pattern implementation", automaton.isDFA());
    }

    @Test
    public void convertToDfa_javaTPointTwoNfaToDfaSinkNotUtilized_expectedThreeSubsetStatesOneSinkNotConnected() {
        // Arrange
        StateDiagram stateDiagram = getStateDiagramFromFile("javatpoint_2.txt");
        NfaToDfaWithEpsilonConversionStrategy nfaToDfa = new NfaToDfaWithEpsilonConversionStrategy(stateDiagram);

        // Act
        StateDiagram dfa = nfaToDfa.convertToDfa();
        Set<State> states = dfa.getStates();

        int expectedNumberOfSubsetStates = 3;
        int expectedNumberOfDeadStates = 1;
        int expectedNumberOfStates = expectedNumberOfSubsetStates + expectedNumberOfDeadStates;

        // Assert
        int actualNumberOfStates = states.size();
        assertEquals("Since the example input should convert into five states total", expectedNumberOfStates, actualNumberOfStates);

        // Assert we got the correct states (symbols), in the result dfa
        List<String> subsetStateStrings = new ArrayList<>();
        for (State state: states) {
            subsetStateStrings.add(state.toString());
        }

        assertTrue(subsetStateStrings.contains("{ q0 }"));
        assertTrue(subsetStateStrings.contains("{ q1 }"));
        assertTrue(subsetStateStrings.contains("{ q1 q0 }"));
        assertTrue(subsetStateStrings.contains(DeadState.SYMBOL));

        // Assert dead state has no incoming transitions since it should not be utilized.
        for(Transition transition: stateDiagram.getAllTransitions()) {
            assertTrue("Since the dead state should not be used", !(transition.getDestination() instanceof DeadState));
        }

        // Assert it matches the actual DFA pattern that we implemented in our automaton class to
        Automaton automaton = Automaton.fromStateDiagram(dfa);
        assertTrue("Since we supposedly converted the NFA to DFA, it should match our DFA pattern implementation", automaton.isDFA());
    }
}