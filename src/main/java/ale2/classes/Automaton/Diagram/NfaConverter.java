package ale2.classes.Automaton.Diagram;

import ale2.classes.Automaton.Exceptions.NotConvertibleNfaException;

public class NfaConverter {
    private NfaToDfaStrategyBase conversionStrategy;
    private StateDiagram convertedStateDiagram;

    public NfaConverter(StateDiagram stateDiagram) throws NotConvertibleNfaException {
        conversionStrategy = NfaToDfaStrategyBase.determineStrategy(stateDiagram);
    }

    public StateDiagram convertToDfa() {
        if (convertedStateDiagram == null) {
            convertedStateDiagram = conversionStrategy.convertToDfa();
        }

        return convertedStateDiagram;
    }
}
