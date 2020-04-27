package ale2.classes.Automaton.Diagram;

public class Accepting extends State {
    private State state;

    public Accepting(State state) {
        this.state = state;
    }

    public String getSymbol() {
        return state.getSymbol();
    }

    @Override
    protected String getShape() {
        return "double" + super.getShape();
    }

    @Override
    public boolean isInitial() {
        return state.isInitial();
    }
}
