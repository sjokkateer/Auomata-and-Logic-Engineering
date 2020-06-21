package ale2.classes.Automaton.Diagram;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Set;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class SubSetStateTest {

    @Test(expected = IllegalArgumentException.class)
    @Parameters(method = "getInvalidSets")
    public void constructor_constructingSubsetFromAnIllegalSetOfStates_expectedIllegalArgumentExceptionThrown(Set<State> set) {
        // Arrange // Act // Assert
        SubSetState sss = new SubSetState(set);
    }

    private final static Object[] getInvalidSets() {
        return $(
                $(new HashSet<State>()),
                $(null)
        );
    }

    @Test
    @Parameters(method = "getNumberOfStates")
    public void constructor_constructingSubsetFromASetOfStates_expectedEquivalentNumberOfStatesInSet(int numberOfStates) {
        // Arrange
        Set<State> states = new HashSet<>();

        for (int i = 0; i < numberOfStates; i++) {
            states.add(new State(String.valueOf(i)));
        }

        SubSetState sss = new SubSetState(states);

        // Act
        int actualNumberOfStates = sss.getStates().size();
        int expectedNumberOfStates = numberOfStates;

        // Assert
        assertEquals("Since we should have created " + numberOfStates + " of unique states", expectedNumberOfStates, actualNumberOfStates);
    }

    private static final Object[] getNumberOfStates() {
        return $(
                $(1),
                $(2),
                $(4),
                $(5),
                $(11)
        );
    }

    @Test
    public void constructor_subsetCreatedFromASubsetStateAndRegularStates_expectedNewSubsetStateToContainTheUnionOfAllStates() {
        // Arrange
        Set<State> states = new HashSet<>();
        states.add(new State("1"));
        states.add(new State("2"));
        states.add(new State("3"));

        SubSetState sss1 = new SubSetState(states);

        Set<State> statesForConstruction = new HashSet<>();
        statesForConstruction.add(sss1);
        statesForConstruction.add(new State("4"));

        SubSetState sss = new SubSetState(statesForConstruction);

        // Act
        int expectedNumberOfStates = 4;
        int actualNumberOfStates = sss.getStates().size();

        // Assert
        assertEquals("Since the subset state and states should be merged into one new subset state", expectedNumberOfStates, actualNumberOfStates);
    }

    @Test
    public void isInitial_subsetCreatedWithOneInitialStateInIt_expectedSubsetStateToAlsoBeInitialState() {
        // Arrange
        Set<State> states = new HashSet<>();
        states.add(new State("1"));
        states.add(new State("3"));

        State initial = new State("2");
        initial.setInitial();
        states.add(initial);

        SubSetState sss = new SubSetState(states);

        // Act
        boolean isInitial = sss.isInitial();

        // Assert
        assertTrue("Since it contains one state in it's set that is an initial state", isInitial);
    }

    @Test
    public void isInitial_subsetCreatedWithNoInitialStateInIt_expectedSubsetStateToNotBeInitialState() {
        // Arrange
        Set<State> states = new HashSet<>();
        states.add(new State("1"));
        states.add(new State("2"));
        states.add(new State("3"));

        SubSetState sss = new SubSetState(states);

        // Act
        boolean isInitial = sss.isInitial();

        // Assert
        assertFalse("Since it DOES NOT contain one state in it's set that is an initial state", isInitial);
    }

    @Test
    public void isAccepting_subsetCreatedWithOneAcceptingStateInIt_expectedSubsetStateToAlsoBeAcceptingState() {
        // Arrange
        Set<State> states = new HashSet<>();
        states.add(new State("1"));
        states.add(new State("2"));

        State accepting = new State("3");
        accepting.setAccepting();
        states.add(accepting);

        SubSetState sss = new SubSetState(states);

        // Act
        boolean isAccepting = sss.isAccepting();

        // Assert
        assertTrue("Since it contains one state in it's set that is an accepting state", isAccepting);
    }

    @Test
    public void isAccepting_subsetCreatedWithNoAcceptingStateInIt_expectedSubsetStateToNotBeAcceptingState() {
        // Arrange
        Set<State> states = new HashSet<>();
        states.add(new State("1"));
        states.add(new State("2"));
        states.add(new State("3"));

        SubSetState sss = new SubSetState(states);

        // Act
        boolean isAccepting = sss.isAccepting();

        // Assert
        assertFalse("Since it DOES NOT contain one state in it's set that is an accepting state", isAccepting);
    }

    @Test
    @Parameters(method = "getNumberOfStates")
    public void getSymbol_testingFormatOfgetSymbolForAGivenNumberOfStates_expectedFormatToMatch(int numberOfStates) {
        // Arrange
        Set<State> states = new HashSet<>();

        String expectedSymbol = "{ ";

        for (int i = 0; i < numberOfStates; i++) {
            states.add(new State(String.valueOf(i)));

            expectedSymbol += i + " ";
        }

        expectedSymbol += "}";

        SubSetState sss = new SubSetState(states);

        // Act
        String actualSymbol = sss.getSymbol();

        // Assert
        assertEquals("Since that's the format we chose to represent the symbol", expectedSymbol, actualSymbol);
    }

    @Test
    public void equals_twoSubsetStatesWithSameSetDifferentOrder_expectedTrueReturned() {
        // Arrange
        State s1 = new State("1");
        State s2 = new State("2");
        State s3 = new State("3");
        State s4 = new State("4");

        Set<State> set1 = new HashSet<>();
        set1.add(s1);
        set1.add(s2);
        set1.add(s3);
        set1.add(s4);

        Set<State> set2 = new HashSet<>();
        set2.add(s2);
        set2.add(s3);
        set2.add(s1);
        set2.add(s4);

        SubSetState sss1 = new SubSetState(set1);
        SubSetState sss2 = new SubSetState(set2);

        // Act
        boolean equal = sss1.equals(sss2);

        // Assert
        assertTrue("Since both subset states contain the same states", equal);
    }

    @Test
    public void equals_twoSubsetStatesDifferentSizesOfSets_expectedFalseReturned() {
        // Arrange
        State s1 = new State("1");
        State s2 = new State("2");

        Set<State> set1 = new HashSet<>();
        set1.add(s1);
        set1.add(s2);

        State s3 = new State("3");

        Set<State> set2 = new HashSet<>();
        set2.add(s1);
        set2.add(s2);
        set2.add(s3);

        SubSetState sss1 = new SubSetState(set1);
        SubSetState sss2 = new SubSetState(set2);

        // Act
        boolean equal = sss1.equals(sss2);

        // Assert
        assertFalse("Since the subset states have a different number of states", equal);
    }

    @Test
    public void equals_twoSubsetStatesSameSizesOfSetsDifferentStates_expectedFalseReturned() {
        // Arrange
        State s1 = new State("1");
        State s2 = new State("2");

        Set<State> set1 = new HashSet<>();
        set1.add(s1);
        set1.add(s2);

        State s3 = new State("3");

        Set<State> set2 = new HashSet<>();
        set2.add(s2);
        set2.add(s3);

        SubSetState sss1 = new SubSetState(set1);
        SubSetState sss2 = new SubSetState(set2);

        // Act
        boolean equal = sss1.equals(sss2);

        // Assert
        assertFalse("Since the subset states have a different states in their set", equal);
    }

    @Test
    public void equals_differentTypeOfStateObjectGiven_expectedFalseReturned() {
        // Arrange
        State s1 = new State("1");
        State s2 = new State("2");

        Set<State> set1 = new HashSet<>();
        set1.add(s1);
        set1.add(s2);

        SubSetState sss1 = new SubSetState(set1);

        // Act
        boolean equal = sss1.equals(s1);

        // Assert
        assertFalse("Since the objects are different types of states", equal);
    }
}