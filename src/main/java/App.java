import classes.Automaton;
import classes.AutomatonFileManager;
import classes.Exceptions.FileProcessingException;
import classes.Exceptions.InvalidTransitionFormatException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class App extends JFrame {
    private JPanel mainPanel;
    private JButton openFileBtn;
    private JLabel openedFileLb;
    private JLabel stateDiagramLb;
    private JLabel isDnfLb;
    private JFileChooser fc;

    private Automaton automaton;

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
                AutomatonFileManager.createDotFile(automaton);
                BufferedImage stateDiagramImage = AutomatonFileManager.createDotFileImage();

                stateDiagramLb.setIcon(new ImageIcon(stateDiagramImage));
            } catch (FileProcessingException | FileNotFoundException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        App app = new App();

        app.setExtendedState(JFrame.MAXIMIZED_BOTH);
        app.setVisible(true);
    }
}
