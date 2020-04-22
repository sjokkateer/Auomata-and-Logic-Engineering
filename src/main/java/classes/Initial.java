package classes;

public class Initial extends State {
    private State state;

    public Initial(State state) {
        this.state = state;
    }

    public String getSymbol() {
        return state.getSymbol();
    }
}
