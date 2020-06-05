package ale2.classes.Automaton.Diagram;

import ale2.classes.Automaton.Exceptions.NotConvertibleEpsilonNfa;

public class NfaConverter {
    private StateDiagram originalStateDiagram;
    private NfaToDfaStrategyBase conversionStrategy;
    private StateDiagram convertedStateDiagram;

    public NfaConverter(StateDiagram stateDiagram) throws NotConvertibleEpsilonNfa {
        originalStateDiagram = stateDiagram;

        conversionStrategy = NfaToDfaStrategyBase.determineStrategy(stateDiagram);
    }

    public StateDiagram getOriginalStateDiagram() {
        return originalStateDiagram;
    }

    public StateDiagram convertToDfa() {
        if (convertedStateDiagram == null) {
            convertedStateDiagram = conversionStrategy.convertToDfa();
        }

        return convertedStateDiagram;
    }
}
