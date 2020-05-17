package ale2.classes.Automaton.Diagram.Thompson;

import ale2.classes.Automaton.Diagram.State;
import ale2.classes.Automaton.Diagram.StateDiagram;
import ale2.classes.Automaton.Diagram.Transition;

import java.util.ArrayList;
import java.util.List;

public class KleeneStarExpressionStrategy extends AbstractThompsonConstructionStrategy {
    private StateDiagram stateDiagram;
    private int stateCounter;
    private StateCounterListener stateCounterListener;

    public KleeneStarExpressionStrategy(StateDiagram stateDiagram, int stateCounter, StateCounterListener stateCounterListener) {
        this.stateDiagram = stateDiagram;
        this.stateCounter = stateCounter;
        this.stateCounterListener = stateCounterListener;
    }

    @Override
    public StateDiagram create() {
        List<Transition> combinedTransitions = new ArrayList<>();

        Transition emptyTransition;
        State newSource = new State(String.valueOf(++stateCounter));
        State newSink = new State(String.valueOf(++stateCounter));

        // Get original source and sink.
        State source = getSource(stateDiagram);
        State sink = getSink(stateDiagram);

        combinedTransitions.add(new Transition(newSource, Transition.EMPTY, source));
        combinedTransitions.add(new Transition(newSource, Transition.EMPTY, newSink));
        combinedTransitions.add(new Transition(sink, Transition.EMPTY, source));
        combinedTransitions.add(new Transition(sink, Transition.EMPTY, newSink));

        combinedTransitions.addAll(stateDiagram.getAllTransitions());

        if (stateCounterListener != null) {
            stateCounterListener.onStateDiagramCreation(stateCounter);
        }

        return StateDiagram.fromTransitions(combinedTransitions);
    }
}
