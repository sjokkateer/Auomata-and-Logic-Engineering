package ale2.classes.Automaton.Diagram;

public class Initial extends State {
    private State state;

    public Initial(State state) {
        this.state = state;
    }

    public String getSymbol() {
        return state.getSymbol();
    }

    @Override
    public boolean isInitial() {
        return true;
    }

    @Override
    public boolean isAccepting() {
        return state.isAccepting();
    }
}
