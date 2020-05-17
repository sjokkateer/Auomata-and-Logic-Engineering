package ale2.classes.Automaton.Diagram.Thompson;

import ale2.classes.Automaton.Diagram.State;
import ale2.classes.Automaton.Diagram.StateDiagram;
import ale2.classes.Automaton.Diagram.Transition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

abstract public class AbstractThompsonConstructionStrategy implements IThompsonConstructionStrategy {
    abstract public StateDiagram create();

    public static State getSink(StateDiagram stateDiagram) {
        for (State state: stateDiagram.getStates()) {
            // First sink but in the context of thompson construction the
            // passed state diagrams only have 1 source and 1 sink each.
            if (stateDiagram.getTransitions(state).size() == 0) return state;
        }

        // Might be better to throw an exception since it is impossible to continue without a sink. (same for source)
        return null;
    }

    public static State getSource(StateDiagram stateDiagram) {
        // source is a state with only outgoing transitions (thus never used as a destination in a transition).
        // New fresh set otherwise we cannot get the set of states later on when we merge the statediagrams.
        Set<State> states = new HashSet<>(stateDiagram.getStates());
        Set<State> destinations = new HashSet<>();

        for (Transition transition: stateDiagram.getAllTransitions()) {
            destinations.add(transition.getDestination());
        }
        // Now the set difference between states and destinations should hold the one
        // state that is only a destination and has no destinations.
        // Maybe throw an exception if the size is not exactly one since we would have two sources which would be incorrect
        states.removeAll(destinations);

        State source = null;

        for (State state: states) {
            source = state;
            // Even though by logic it should always result in a 1 element set.
            break;
        }

        return source;
    }
}
