package ale2.classes.Automaton.Diagram;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NfaConverterTest {

    @Test
    public void getEpsilonClosureOf_NoEpsilonClosurePresent_expectedEmptyHashMapReturned()
    {
        // Arrange
        List<Transition> transitionList = new ArrayList<>();

        State s1 = new State("q0");
        State s2 = new State("q1");
        State s3 = new State("q2");

        Transition t = new Transition(s1, 'b', s2);
        Transition t2 = new Transition(s1, 'a', s3);

        transitionList.add(t);
        transitionList.add(t2);

        StateDiagram stateDiagram = StateDiagram.fromTransitions(transitionList);

        NfaConverter nfaConverter = new NfaConverter(stateDiagram);

        // Act // Assert
        String message = "Should be size one, only consisting of it's own state";
        assertEquals(message, 1, nfaConverter.getEpsilonClosureOf(s1).getStates().size());
        assertEquals(message, 1, nfaConverter.getEpsilonClosureOf(s2).getStates().size());
        assertEquals(message, 1, nfaConverter.getEpsilonClosureOf(s3).getStates().size());
    }

    @Test
    public void getEpsilonClosureOf_OneEpsilonClosurePresent_expectedEpsilonClosureToConsistsOfThoseStates()
    {
        // Arrange
        List<Transition> transitionList = new ArrayList<>();

        State s1 = new State("q0");
        State s2 = new State("q1");
        State s3 = new State("q2");

        Transition t = new Transition(s1, '_', s2);
        Transition t2 = new Transition(s1, 'a', s3);

        transitionList.add(t);
        transitionList.add(t2);

        StateDiagram stateDiagram = StateDiagram.fromTransitions(transitionList);

        NfaConverter nfaConverter = new NfaConverter(stateDiagram);

        // Act
        EpsilonClosure eClosure = nfaConverter.getEpsilonClosureOf(s1);
        int expectedNumberOfStatesInClosure = 2;

        // Assert
        assertEquals("Since q0 and q1 should be combined as an epsilon closure state.", expectedNumberOfStatesInClosure, eClosure.getStates().size());
    }

    @Test
    // Appended: q0 --> q1 --> q2
    public void getEpsilonClosureOf_TwoEpsilonClosuresOneOfThreeStatesOneOfTwoStatesAppended_expectedThreeTotalAndTwoWithMoreStatesThanOne()
    {
        // Arrange
        List<Transition> transitionList = new ArrayList<>();

        State s1 = new State("q0");
        State s2 = new State("q1");
        State s3 = new State("q2");

        Transition t = new Transition(s1, '_', s2);
        Transition t2 = new Transition(s2, '_', s3);

        transitionList.add(t);
        transitionList.add(t2);

        StateDiagram stateDiagram = StateDiagram.fromTransitions(transitionList);

        NfaConverter nfaConverter = new NfaConverter(stateDiagram);

        // Act
        EpsilonClosure firstClosure = nfaConverter.getEpsilonClosureOf(s1);
        int expectedNumberOfStatesInFirstClosure = 3;

        EpsilonClosure secondClosure = nfaConverter.getEpsilonClosureOf(s2);
        int expectedNumberOfStatesInSecondClosure = 2;

        // Assert
        assertEquals("Since q0, q1, and q2 should be combined as an epsilon closure state.", expectedNumberOfStatesInFirstClosure, firstClosure.getStates().size());
        assertEquals("Since q1, and q2 should be combined as an epsilon closure state.", expectedNumberOfStatesInSecondClosure, secondClosure.getStates().size());
    }

    @Test
    public void getEpsilonClosureOf_TwoEpsilonClosuresOneResultingInACycleToTheInitial_ExpectedTwoIdenticalEpsilonClosuresDifferentSource()
    {
        // Arrange
        List<Transition> transitionList = new ArrayList<>();

        State s1 = new State("q0");
        State s2 = new State("q1");
        State s3 = new State("q2");
        State s4 = new State("q3");
        State s5 = new State("q4");


        Transition t = new Transition(s1, '_', s2);
        Transition t2 = new Transition(s2, '_', s3);
        Transition t3 = new Transition(s2, '_', s1);
        Transition t4 = new Transition(s3, '1', s4);
        Transition t5 = new Transition(s3, '0', s5);

        transitionList.add(t);
        transitionList.add(t2);
        transitionList.add(t3);
        transitionList.add(t4);
        transitionList.add(t5);

        StateDiagram stateDiagram = StateDiagram.fromTransitions(transitionList);

        NfaConverter nfaConverter = new NfaConverter(stateDiagram);

        // Act
        EpsilonClosure firstClosure = nfaConverter.getEpsilonClosureOf(s1);
        int expectedNumberOfStatesInFirstClosure = 3;

        EpsilonClosure secondClosure = nfaConverter.getEpsilonClosureOf(s2);
        int expectedNumberOfStatesInSecondClosure = 3;

        // Assert
        assertEquals("Since q0, q1, and q2 should be combined as an epsilon closure state.", expectedNumberOfStatesInFirstClosure, firstClosure.getStates().size());
        assertEquals("Since q1, q0, and q2 should be combined as an epsilon closure state.", expectedNumberOfStatesInSecondClosure, secondClosure.getStates().size());
    }

    @Test
    public void getEpsilonClosureOf_threeEpsilonClosuresOneWithFourStatesTwoWithOneState_expectedThreeTotalClosuresOneWithFourStatesTwoWithOneStates()
    {
        // Arrange
        List<Transition> transitionList = new ArrayList<>();

        State s1 = new State("q0");
        State s2 = new State("q1");
        State s3 = new State("q2");
        State s4 = new State("q3");
        State s5 = new State("q4");
        State s6 = new State("q5");

        Transition t = new Transition(s1, '_', s2);
        Transition t2 = new Transition(s1, '_', s3);
        Transition t3 = new Transition(s2, '_', s5);
        Transition t4 = new Transition(s3, '_', s4);

        Transition t5 = new Transition(s4, 'a', s6);
        Transition t6 = new Transition(s5, 'b', s6);

        transitionList.add(t);
        transitionList.add(t2);
        transitionList.add(t3);
        transitionList.add(t4);
        transitionList.add(t5);
        transitionList.add(t6);

        StateDiagram stateDiagram = StateDiagram.fromTransitions(transitionList);

        NfaConverter nfaConverter = new NfaConverter(stateDiagram);

        // Act
        EpsilonClosure firstClosure = nfaConverter.getEpsilonClosureOf(s1);
        int expectedNumberOfStatesInFirstClosure = 5;

        EpsilonClosure secondClosure = nfaConverter.getEpsilonClosureOf(s2);
        int expectedNumberOfStatesInSecondClosure = 2;

        EpsilonClosure thirdClosure = nfaConverter.getEpsilonClosureOf(s2);
        int expectedNumberOfStatesInThirdClosure = 2;

        // Assert
        assertEquals("Since q0, q1, q2, q3, a4 should be combined as an epsilon closure state.", expectedNumberOfStatesInFirstClosure, firstClosure.getStates().size());
        assertEquals("Since q1, and q4 should be combined as an epsilon closure state.", expectedNumberOfStatesInSecondClosure, secondClosure.getStates().size());
        assertEquals("Since q2, and q3 should be combined as an epsilon closure state.", expectedNumberOfStatesInThirdClosure, thirdClosure.getStates().size());
    }

    @Test
    // Checks out
    public void convertToDfa()
    {
        // Arrange
        List<Transition> transitionList = new ArrayList<>();

        State s1 = new State("q0");
        State s2 = new State("q1");
        State s3 = new State("q2");
        State s4 = new State("q3");
        State s5 = new State("q4");

        s1.setInitial();

        Transition t = new Transition(s1, '_', s2);
        Transition t2 = new Transition(s2, '_', s1);
        Transition t3 = new Transition(s2, '_', s3);
        Transition t4 = new Transition(s3, '1', s4);
        Transition t5 = new Transition(s3, '0', s5);

        transitionList.add(t);
        transitionList.add(t2);
        transitionList.add(t3);
        transitionList.add(t4);
        transitionList.add(t5);

        StateDiagram stateDiagram = StateDiagram.fromTransitions(transitionList);

        NfaConverter nfaConverter = new NfaConverter(stateDiagram);

        // Act
        StateDiagram afterConversion = nfaConverter.convertToDfa();

        // Assert
    }

    @Test
    // This one checks out
    public void convertToDfa_exampleGeorgiana()
    {
        // Arrange
        List<Transition> transitionList = new ArrayList<>();

        State s1 = new State("1");
        State s2 = new State("2");
        State s3 = new State("3");
        State s4 = new State("4");
        State s5 = new State("5");

        s1.setInitial();

        Transition t = new Transition(s1, 'a', s2);
        Transition t2 = new Transition(s2, 'b', s1);
        Transition t3 = new Transition(s2, '_', s5);
        Transition t4 = new Transition(s1, '_', s3);
        Transition t5 = new Transition(s3, 'b', s4);
        Transition t6 = new Transition(s4, 'a', s5);

        transitionList.add(t);
        transitionList.add(t2);
        transitionList.add(t3);
        transitionList.add(t4);
        transitionList.add(t5);
        transitionList.add(t6);

        StateDiagram stateDiagram = StateDiagram.fromTransitions(transitionList);

        NfaConverter nfaConverter = new NfaConverter(stateDiagram);

        // Act
        StateDiagram afterConversion = nfaConverter.convertToDfa();

        // Assert
    }

    @Test
    // This one checks out.
    public void convertToDfa_exampleWikipedia()
    {
        // Arrange
        List<Transition> transitionList = new ArrayList<>();

        State s1 = new State("1");
        State s2 = new State("2");
        State s3 = new State("3");
        State s4 = new State("4");

        s1.setInitial();
        s3.setAccepting();

        Transition t = new Transition(s1, '0', s2);
        Transition t2 = new Transition(s1, '_', s3);
        Transition t3 = new Transition(s2, '1', s2);
        Transition t4 = new Transition(s2, '1', s4);
        Transition t5 = new Transition(s4, '0', s3);
        Transition t6 = new Transition(s3, '0', s4);
        Transition t7 = new Transition(s3, '_', s2);

        transitionList.add(t);
        transitionList.add(t2);
        transitionList.add(t3);
        transitionList.add(t4);
        transitionList.add(t5);
        transitionList.add(t6);
        transitionList.add(t7);

        StateDiagram stateDiagram = StateDiagram.fromTransitions(transitionList);

        NfaConverter nfaConverter = new NfaConverter(stateDiagram);

        // Act
        StateDiagram afterConversion = nfaConverter.convertToDfa();

        // Assert

    }

    @Test
    // This one does not check, since it never checks q2 individually and misses it
    public void convertToDfa_exampleJavaTPoint1()
    {
        // Arrange
        List<Transition> transitionList = new ArrayList<>();

        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");

        q0.setInitial();

        Transition t = new Transition(q0, '0', q0);
        Transition t2 = new Transition(q0, '1', q1);

        Transition t3 = new Transition(q1, '0', q1);
        Transition t4 = new Transition(q1, '1', q1);
        Transition t5 = new Transition(q1, '0', q2);

        Transition t6 = new Transition(q2, '0', q2);
        Transition t7 = new Transition(q2, '1', q2);
        Transition t8 = new Transition(q2, '1', q1);

        transitionList.add(t);
        transitionList.add(t2);
        transitionList.add(t3);
        transitionList.add(t4);
        transitionList.add(t5);
        transitionList.add(t6);
        transitionList.add(t7);
        transitionList.add(t8);

        StateDiagram stateDiagram = StateDiagram.fromTransitions(transitionList);

        NfaConverter nfaConverter = new NfaConverter(stateDiagram);

        // Act
        StateDiagram afterConversion = nfaConverter.convertToDfa();

        // Assert
    }

    @Test
    // This one checks
    public void convertToDfa_exampleJavaTPoint2()
    {
        // Arrange
        List<Transition> transitionList = new ArrayList<>();

        State q0 = new State("q0");
        State q1 = new State("q1");

        q0.setInitial();

        Transition t = new Transition(q0, '0', q0);
        Transition t2 = new Transition(q0, '0', q1);
        Transition t3 = new Transition(q0, '1', q1);
        Transition t4 = new Transition(q1, '1', q1);
        Transition t5 = new Transition(q1, '1', q0);

        transitionList.add(t);
        transitionList.add(t2);
        transitionList.add(t3);
        transitionList.add(t4);
        transitionList.add(t5);

        StateDiagram stateDiagram = StateDiagram.fromTransitions(transitionList);

        NfaConverter nfaConverter = new NfaConverter(stateDiagram);

        // Act
        StateDiagram afterConversion = nfaConverter.convertToDfa();

        // Assert
    }
}