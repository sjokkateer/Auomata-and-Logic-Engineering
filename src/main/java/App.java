import classes.Automaton;
import classes.Exceptions.FileProcessingException;
import classes.Exceptions.InvalidTransitionFormatException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class App extends JFrame {
    private JPanel mainPanel;
    private JButton openFileBtn;
    private JLabel openedFileLb;
    private JFileChooser fc;

    private static final String RESOURCE_FOLDER = "/src/main/resources";
    private Automaton automaton;

    public App(String title) {
        super();

        String projectFolder = System.getProperty("user.dir");
        String resourceFolder = projectFolder + RESOURCE_FOLDER;
        // Will make sure the resources folder will be opened when opening a file
        fc = new JFileChooser(new File(resourceFolder));
    }

    public App() {
        this("Automata and Process Theory App");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();

        openFileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
    }

    private void openFile() {
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

    public static void main(String[] args) {
        App app = new App();
        app.setVisible(true);
    }
}
