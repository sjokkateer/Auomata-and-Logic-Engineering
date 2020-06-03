package ale2.classes.Automaton;

import ale2.classes.Automaton.Diagram.State;
import ale2.classes.Automaton.Diagram.StateDiagram;
import ale2.classes.Automaton.Diagram.Transition;
import ale2.classes.Automaton.Regex.Word;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LanguageCheckerTest {

    @Test
    public void isFinite_finiteLanguageNoCyclesInStateDiagram_trueShouldBeReturned() {
        // Arrange
        List<Transition> transitions = new ArrayList<>();
        State s1 = new State("1");
        State s2 = new State("2");
        State s4 = new State("4");

        s1.setInitial();
        s4.setAccepting();

        transitions.add(new Transition(s1, 'a', s2));
        transitions.add(new Transition(s2, 'd', s4));

        StateDiagram sd = StateDiagram.fromTransitions(transitions);

        LanguageChecker checker = new LanguageChecker();
        checker.setStateDiagram(sd);

        // Act
        boolean isFinite = checker.isFinite();

        // Assert
        assertTrue("This state diagram is finite but got false returned!", isFinite);
    }

    @Test
    public void isFinite_infiniteLanguageAsCycleCanTerminate_falseShouldBeReturned() {
        List<Transition> transitions = new ArrayList<>();
        State s1 = new State("1");
        State s2 = new State("2");
        State s3 = new State("3");
        State s4 = new State("4");

        s1.setInitial();
        s4.setAccepting();

        transitions.add(new Transition(s1, 'a', s2));
        transitions.add(new Transition(s2, 'b', s3));
        transitions.add(new Transition(s2, 'd', s4));
        transitions.add(new Transition(s3, 'a', s2));

        StateDiagram sd = StateDiagram.fromTransitions(transitions);

        LanguageChecker checker = new LanguageChecker();
        checker.setStateDiagram(sd);

        // Act
        boolean isFinite = checker.isFinite();

        // Assert
        assertFalse("This state diagram is infinite but got true returned!", isFinite);
    }

    @Test
    // ADJUST
    public void isFinite_infiniteLanguageWithEpsilonCycleCanTerminate_falseShouldBeReturned() {
        List<Transition> transitions = new ArrayList<>();
        State s1 = new State("1");
        State s2 = new State("2");
        State s3 = new State("3");
        State s4 = new State("4");

        s1.setInitial();
        s4.setAccepting();

        transitions.add(new Transition(s1, 'a', s2));
        transitions.add(new Transition(s2, 'b', s3));
        transitions.add(new Transition(s3, '_', s1));
        transitions.add(new Transition(s3, '_', s4));

        StateDiagram sd = StateDiagram.fromTransitions(transitions);

        LanguageChecker checker = new LanguageChecker();
        checker.setStateDiagram(sd);

        // Act
        boolean isFinite = checker.isFinite();

        // Assert
        assertFalse("This state diagram is infinite but got true returned!", isFinite);
    }

    @Test
    public void isFinite_infiniteLanguageSinceOneAcceptingStateInOneOfTheCycles_falseShouldBeReturned() {
        // Arrange
        List<Transition> transitions = new ArrayList<>();
        State s1 = new State("1");
        State s2 = new State("2");
        State s3 = new State("3");
        State s4 = new State("4");
        State s5 = new State("5");

        s1.setInitial();
        s5.setAccepting();

        transitions.add(new Transition(s1, 'a', s2));
        transitions.add(new Transition(s2, 'b', s3));
        transitions.add(new Transition(s3, 'd', s4));
        transitions.add(new Transition(s4, 'a', s2));
        transitions.add(new Transition(s3, 'a', s5));
        transitions.add(new Transition(s5, 'a', s2));

        StateDiagram sd = StateDiagram.fromTransitions(transitions);

        LanguageChecker checker = new LanguageChecker();
        checker.setStateDiagram(sd);

        // Act
        boolean isFinite = checker.isFinite();

        // Assert
        assertFalse("This state diagram is infinite but got true returned!", isFinite);
    }

    @Test
    public void isFinite_infiniteLanguageCycleFromAcceptingStateToItself_falseShouldBeReturned() {
        List<Transition> transitions = new ArrayList<>();
        State s1 = new State("1");

        s1.setInitial();
        s1.setAccepting();

        transitions.add(new Transition(s1, 'a', s1));

        StateDiagram sd = StateDiagram.fromTransitions(transitions);

        LanguageChecker checker = new LanguageChecker();
        checker.setStateDiagram(sd);

        // Act
        boolean isFinite = checker.isFinite();

        // Assert
        assertFalse("This state diagram is infinite but got true returned!", isFinite);
    }

    @Test
    public void isFinite_finiteLanguageCycleOnSink_trueShouldBeReturned() {
        List<Transition> transitions = new ArrayList<>();
        State s1 = new State("1");
        State s2 = new State("2");
        State s3 = new State("3");

        s1.setInitial();
        s2.setAccepting();

        transitions.add(new Transition(s1, 'a', s2));
        transitions.add(new Transition(s1, 'b', s3));
        transitions.add(new Transition(s3, 'd', s3));

        StateDiagram sd = StateDiagram.fromTransitions(transitions);

        LanguageChecker checker = new LanguageChecker();
        checker.setStateDiagram(sd);

        // Act
        boolean isFinite = checker.isFinite();

        // Assert
        assertTrue("This state diagram is finite but got false returned!", isFinite);
    }

    @Test
    public void getWords_finiteLanguageNoCyclesInStateDiagram_ListWithOneWordShouldBeReturned() {
        // Arrange
        List<Transition> transitions = new ArrayList<>();
        State s1 = new State("1");
        State s2 = new State("2");
        State s4 = new State("4");

        s1.setInitial();
        s4.setAccepting();

        transitions.add(new Transition(s1, 'a', s2));
        transitions.add(new Transition(s2, 'd', s4));

        StateDiagram sd = StateDiagram.fromTransitions(transitions);

        LanguageChecker checker = new LanguageChecker();
        checker.setStateDiagram(sd);

        // Act
        List<Word> languageWords = checker.getWords();
        int expectedNumberOfWords = 1;
        int actualNumberOfWords = languageWords.size();

        String expectedWordContent = "ad";
        String actualWordContent = languageWords.get(0).getWord();

        // Assert
        assertEquals("Only one word possible!", expectedNumberOfWords, actualNumberOfWords);
        assertEquals("Only word that can be generated is ad, got: " + actualWordContent, expectedWordContent, actualWordContent);
    }

    @Test
    public void getWords_finiteLanguageWithCycleInStateDiagram_listWithOneWordReturned() {
        List<Transition> transitions = new ArrayList<>();
        State s1 = new State("1");
        State s2 = new State("2");
        State s3 = new State("3");

        s1.setInitial();
        s2.setAccepting();

        transitions.add(new Transition(s1, 'a', s2));
        transitions.add(new Transition(s1, 'b', s3));
        transitions.add(new Transition(s3, 'd', s3));

        StateDiagram sd = StateDiagram.fromTransitions(transitions);

        LanguageChecker checker = new LanguageChecker();
        checker.setStateDiagram(sd);

        // Act
        List<Word> languageWords = checker.getWords();
        int expectedNumberOfWords = 1;
        int actualNumberOfWords = languageWords.size();

        String expectedWordContent = "a";
        String actualWordContent = languageWords.get(0).getWord();

        // Assert
        assertEquals("Only one word possible!", expectedNumberOfWords, actualNumberOfWords);
        assertEquals("Only word that can be generated is a, got: " + actualWordContent, expectedWordContent, actualWordContent);
    }

    @Test
    public void getWords_finiteLanguageWithMultiplePossibleWordsDepthOnly_listWithMultipleWordsReturned() {
        List<Transition> transitions = new ArrayList<>();
        State s1 = new State("1");
        State s2 = new State("2");
        State s3 = new State("3");
        State s4 = new State("4");
        State s5 = new State("5");
        State s6 = new State("6");

        s1.setInitial();

        s2.setAccepting();
        s4.setAccepting();
        s6.setAccepting();

        transitions.add(new Transition(s1, 'a', s2));
        transitions.add(new Transition(s2, 'b', s3));
        transitions.add(new Transition(s3, 'c', s4));

        transitions.add(new Transition(s4, 'd', s5));
        transitions.add(new Transition(s5, 'e', s6));

        StateDiagram sd = StateDiagram.fromTransitions(transitions);

        LanguageChecker checker = new LanguageChecker();
        checker.setStateDiagram(sd);

        // Act
        List<Word> languageWords = checker.getWords();
        int expectedNumberOfWords = 3;
        int actualNumberOfWords = languageWords.size();

        // Assert
        // Expect to get a, abc, abcde
        assertEquals("Only one word possible!", expectedNumberOfWords, actualNumberOfWords);
    }

    @Test
    public void getWords_finiteLanguageWithMultiplePossibleWordsWideAndDeep_listWithMultipleWordsReturned() {
//        List<Transition> transitions = new ArrayList<>();
//        State s1 = new State("1");
//        State s2 = new State("2");
//        State s3 = new State("3");
//        State s4 = new State("4");
//        State s5 = new State("5");
//        State s6 = new State("6");
//
//        s1.setInitial();
//
//        s2.setAccepting();
//        s4.setAccepting();
//        s6.setAccepting();
//
//        transitions.add(new Transition(s1, 'a', s2));
//        transitions.add(new Transition(s2, 'b', s3));
//        transitions.add(new Transition(s3, 'c', s4));
//
//        transitions.add(new Transition(s4, 'd', s5));
//        transitions.add(new Transition(s5, 'e', s6));
//
//        StateDiagram sd = StateDiagram.fromTransitions(transitions);
//
//        LanguageChecker checker = new LanguageChecker();
//        checker.setStateDiagram(sd);
//
//        // Act
//        List<Word> languageWords = checker.getWords();
//        int expectedNumberOfWords = 3;
//        int actualNumberOfWords = languageWords.size();
//
//        // Assert
//        // Expect to get a, abc, abcde
//        assertEquals("Only one word possible!", expectedNumberOfWords, actualNumberOfWords);
    }

    @Test
    public void getWords_infiniteLanguageWithCycleInStateDiagram_emptyListShouldBeReturned() {
        List<Transition> transitions = new ArrayList<>();
        State s1 = new State("1");
        State s2 = new State("2");
        State s3 = new State("3");
        State s4 = new State("4");

        s1.setInitial();
        s4.setAccepting();

        transitions.add(new Transition(s1, 'a', s2));
        transitions.add(new Transition(s2, 'b', s3));
        transitions.add(new Transition(s2, 'd', s4));
        transitions.add(new Transition(s3, 'a', s2));

        StateDiagram sd = StateDiagram.fromTransitions(transitions);

        LanguageChecker checker = new LanguageChecker();
        checker.setStateDiagram(sd);

        // Act
        List<Word> languageWords = checker.getWords();
        int expectedNumberOfWords = 0;
        int actualNumberOfWords = languageWords.size();

        // Assert
        assertEquals("Only one word possible!", expectedNumberOfWords, actualNumberOfWords);
    }
}