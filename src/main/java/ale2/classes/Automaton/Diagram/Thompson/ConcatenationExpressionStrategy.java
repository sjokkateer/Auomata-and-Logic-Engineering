package ale2.classes.Automaton.Diagram.Thompson;

import ale2.classes.Automaton.Diagram.State;
import ale2.classes.Automaton.Diagram.StateDiagram;
import ale2.classes.Automaton.Diagram.Transition;

import java.util.List;

public class ConcatenationExpressionStrategy extends AbstractThompsonConstructionStrategy {
    private StateDiagram stateDiagramA;
    private StateDiagram stateDiagramB;

    public ConcatenationExpressionStrategy(StateDiagram stateDiagramA, StateDiagram stateDiagramB) {
        this.stateDiagramA = stateDiagramA;
        this.stateDiagramB = stateDiagramB;
    }

    public StateDiagram create()
    {
        State sink = getSink(stateDiagramA);
        State source = getSource(stateDiagramB);

        List<Transition> transitions = stateDiagramB.getAllTransitions();
        replaceSourceWithSink(source, sink, transitions);

        // Merge all transitions and return a new state diagram.
        List<Transition> combinedTransitions = stateDiagramA.getAllTransitions();
        combinedTransitions.addAll(stateDiagramB.getAllTransitions());
        return StateDiagram.fromTransitions(combinedTransitions);
    }

    private void replaceSourceWithSink(State source, State sink, List<Transition> transitions) {
        for (Transition transition: transitions) {
            if (transition.getSource().equals(source)) {
                transition.setSource(sink);
            }
        }
    }
}
