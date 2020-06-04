package ale2.classes.Automaton.Diagram;

public class NfaConverter {
    private StateDiagram originalStateDiagram;
    private NfaToDfaStrategyBase conversionStrategy;
    private StateDiagram convertedStateDiagram;

    public NfaConverter(StateDiagram stateDiagram) {
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
