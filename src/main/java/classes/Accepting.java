package classes;

public class Accepting extends State {
    private State state;

    public Accepting(State state) {
        this.state = state;
    }

    public String getSymbol() {
        return state.getSymbol();
    }
}
