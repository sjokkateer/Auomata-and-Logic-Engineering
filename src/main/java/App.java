import classes.Automaton;
import classes.AutomatonFileManager;
import classes.Exceptions.FileProcessingException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

public class App extends JFrame {
    private JPanel mainPanel;
    private JButton openFileBtn;
    private JLabel openedFileLb;
    private JLabel stateDiagramLb;
    private JLabel isDFALb;
    private JLabel alphabetLb;
    private JFileChooser fc;

    private Automaton automaton;

    public static final int WIDTH = 650;
    public static final int HEIGHT = 350;

    public App(String title) {
        super(title);

        fc = new JFileChooser(new File(AutomatonFileManager.getResourceFolder()));
    }

    public App() {
        this("Automata and Process Theory App");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();

        openFileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAutomatonFromFile();

                if (automaton != null) {
                    displayAlphabet();
                    createDiagramImage();
                    displayIfDFA();
                }
            }
        });
    }

    private void createAutomatonFromFile() {
        int returnValue = fc.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            openedFileLb.setText("Currently opened: " + file.getName());

            try {
                automaton = Automaton.fromFile(file);
            } catch (FileProcessingException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void displayAlphabet() {
        String result = "<html>Alphabet: <br/>";

        for(char letter: automaton.getAlphabet()) {
            result += letter + ", ";
        }

        alphabetLb.setText(removeTrailingComma(result));
    }

    private String removeTrailingComma(String result) {
        int end = result.length() - 2;

        return result.substring(0, end);
    }

    private void createDiagramImage() {
        try {
            AutomatonFileManager.createDotFile(automaton);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedImage stateDiagramImage = AutomatonFileManager.createDotFileImage();
        stateDiagramLb.setIcon(new ImageIcon(stateDiagramImage));
    }

    private void displayIfDFA() {
        boolean isDFA = automaton.isDFA();
        String result = "This automaton is";

        if (!isDFA) {
            result += " NOT";
        }

        result += " a DFA";

        isDFALb.setText(result);
    }

    public static void main(String[] args) {
        App app = new App();
        app.setSize(App.WIDTH, App.HEIGHT);
        app.setLocationRelativeTo(null);
        app.setVisible(true);
    }
}
