package ale2.classes.Automaton.Diagram;

import java.util.*;

public class NfaConverter {
    private StateDiagram stateDiagram;
    private NfaToDfaStrategyBase conversionStrategy;

    public NfaConverter(StateDiagram stateDiagram) {
        this.stateDiagram = stateDiagram;

        // Or factory method on the base
        conversionStrategy = NfaToDfaStrategyBase.determineStrategy(stateDiagram);

//        if (alphabet.size() == 0) {
//            throw new IllegalArgumentException("This can not be converted to a DFA!");
//        }
    }

    public StateDiagram convertToDfa() {
        return conversionStrategy.convertToDfa();
    }
}
