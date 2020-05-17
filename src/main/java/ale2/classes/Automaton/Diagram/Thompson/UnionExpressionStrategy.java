package ale2.classes.Automaton.Diagram.Thompson;

import ale2.classes.Automaton.Diagram.State;
import ale2.classes.Automaton.Diagram.StateDiagram;
import ale2.classes.Automaton.Diagram.Transition;

import java.util.ArrayList;
import java.util.List;

public class UnionExpressionStrategy extends AbstractThompsonConstructionStrategy {
    private StateDiagram stateDiagramA;
    private StateDiagram stateDiagramB;
    private int stateCounter;
    private StateCounterListener stateCounterListener;

    public UnionExpressionStrategy(StateDiagram stateDiagramA, StateDiagram stateDiagramB, int initialStateCounter, StateCounterListener stateCounterListener) {
        this.stateDiagramA = stateDiagramA;
        this.stateDiagramB = stateDiagramB;

        this.stateCounter = initialStateCounter;
        this.stateCounterListener = stateCounterListener;
    }

    @Override
    public StateDiagram create() {
        List<Transition> combinedTransitions = new ArrayList<>();

        // Connect god source with the sources of both state diagrams.
        State godSource = new State(String.valueOf(++stateCounter));
        State sourceOfA = getSource(stateDiagramA);
        combinedTransitions.add(new Transition(godSource, Transition.EMPTY, sourceOfA));

        State sourceOfB = getSource(stateDiagramB);
        combinedTransitions.add(new Transition(godSource, Transition.EMPTY, sourceOfB));

        // Connecting sinks to new god sink.
        State godSink = new State(String.valueOf(++stateCounter));
        State sinkOfA = getSink(stateDiagramA);
        combinedTransitions.add(new Transition(sinkOfA, Transition.EMPTY, godSink));

        State sinkOfB = getSink(stateDiagramB);
        combinedTransitions.add(new Transition(sinkOfB, Transition.EMPTY, godSink));

        // Combine all transitions to create new state diagram eventually.
        combinedTransitions.addAll(stateDiagramA.getAllTransitions());
        combinedTransitions.addAll(stateDiagramB.getAllTransitions());

        if (stateCounterListener != null) {
            stateCounterListener.onStateDiagramCreation(stateCounter);
        }

        // Return the statediagram.
        return StateDiagram.fromTransitions(combinedTransitions);
    }
}
