package ale2.classes.Automaton.Diagram;

import java.util.ArrayList;
import java.util.List;

public class NfaToDfaWithoutEpsilonConversionStrategy extends NfaToDfaStrategyBase {

    public NfaToDfaWithoutEpsilonConversionStrategy(StateDiagram stateDiagram) {
        super(stateDiagram);
    }

    @Override
    protected List<State> initializeQueue() {
        List<State> queue = new ArrayList<>();

        for (State state : stateDiagram.getStates()) {
            queue.add(new SubSetState(state));
        }

        return queue;
    }

    @Override
    protected State convertToRequiredState(State state) {
        return state;
    }
}
