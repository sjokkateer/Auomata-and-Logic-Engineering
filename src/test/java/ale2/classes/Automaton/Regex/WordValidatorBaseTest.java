package ale2.classes.Automaton.Regex;

import ale2.classes.Automaton.Diagram.PushDownAutomata;
import ale2.classes.Automaton.Diagram.State;
import ale2.classes.Automaton.Diagram.StateDiagram;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WordValidatorBaseTest {
    private final Set<Character> ALPHABET;

    public WordValidatorBaseTest() {
        ALPHABET = new HashSet<>();
        Collections.addAll(ALPHABET, '0', '1');
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_nullGivenForStateDiagram_expectedIllegalArgumentExceptionThrown() {
        // Arrange
        StateDiagram stateDiagram = null;

        // Act // Assert
        WordValidatorBase.create(ALPHABET, stateDiagram);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_nullGivenForAlphabet_expectedIllegalArgumentExceptionThrown() {
        // Arrange
        Set<Character> alphabet = null;
        StateDiagram stateDiagram = Mockito.mock(StateDiagram.class);

        // Act // Assert
        WordValidatorBase.create(alphabet, stateDiagram);
    }

    @Test
    public void create_pushDownAutomataGiven_expectedWordValidatorPushDownAutomataReturned() {
        // Arrange
        StateDiagram stateDiagram = Mockito.mock(PushDownAutomata.class);

        // Act
        WordValidatorBase actualWordValidator = WordValidatorBase.create(ALPHABET, stateDiagram);

        // Assert
        assertTrue("Since a push down automaton was given to the factory method", actualWordValidator.getClass() == WordValidatorPushDownAutomata.class);
    }

    @Test
    public void create_stateDiagramGiven_expectedWordValidatorFiniteAutomataReturned() {
        // Arrange
        StateDiagram stateDiagram = Mockito.mock(StateDiagram.class);

        // Act
        WordValidatorBase actualWordValidator = WordValidatorBase.create(ALPHABET, stateDiagram);

        // Assert
        assertTrue("Since a finite automaton's state diagram was given to the factory method", actualWordValidator.getClass() == WordValidatorFiniteAutomata.class);
    }

    @Test
    public void check_zeroWordsInSet_expectedValidateToBeCalledZeroTimes() {
        // Arrange
        StateDiagram stateDiagram = Mockito.mock(StateDiagram.class);

        State initialState = new State("q0");
        when(stateDiagram.getInitialState()).thenReturn(initialState);

        WordValidatorBase wordValidatorBase = Mockito.mock(WordValidatorBase.class);

        Set<Word> words = new HashSet<>();

        // Act
        wordValidatorBase.check(words);

        // Assert
        verify(wordValidatorBase, times(0)).validate(null);
    }
}