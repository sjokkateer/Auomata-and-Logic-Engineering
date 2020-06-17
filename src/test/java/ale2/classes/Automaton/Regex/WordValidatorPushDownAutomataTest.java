package ale2.classes.Automaton.Regex;

import ale2.classes.Automaton.Automaton;
import ale2.classes.Automaton.AutomatonFileManager;
import ale2.classes.Automaton.Exceptions.FileProcessingException;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class WordValidatorPushDownAutomataTest {
    private WordValidatorBase wordValidator;

    public WordValidatorPushDownAutomataTest() {
        String pdaFileName = "example_pda.txt";
        try {
            wordValidator = createWordValidatorFromFAFile(pdaFileName);
        } catch (FileProcessingException e) {
            e.printStackTrace();
        }
    }

    // Test word that belongs to the langague
    @Test
    public void validate_wordIsPartOfTheLanguage_expectedWordsFlagToBeSetToTrueIndicatingItBelongsToTheLanguage() {

    }

    // Test word that does not belong to the language no transition can be made

    // Test word does not belong to the language transitions can be made but no final state.

    // Test word that has letters not part of the alphabet

    // Test other PDA's and words

    private WordValidatorBase createWordValidatorFromFAFile(String fileName) throws FileProcessingException {
        Automaton automaton = Automaton.fromFile(new File(AutomatonFileManager.getResourceFolder() + fileName));

        return WordValidatorBase.create(automaton.getAlphabet(), automaton.getStateDiagram());
    }
}