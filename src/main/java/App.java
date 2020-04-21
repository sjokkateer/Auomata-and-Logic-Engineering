import javax.swing.*;

public class App extends JFrame {
    private JPanel mainPanel;

    public App(String title) {
        super();
    }

    public App() {
        this("Automata and Process Theory App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
    }

    public static void main(String[] args) {
        App app = new App();
        app.setVisible(true);
    }
}
