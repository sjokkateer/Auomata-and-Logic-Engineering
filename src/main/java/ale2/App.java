package ale2;

import ale2.classes.Automaton.Automaton;
import ale2.classes.Automaton.AutomatonFileManager;
import ale2.classes.Automaton.Exceptions.FileProcessingException;
import ale2.classes.Automaton.Regex.Word;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;

public class App extends JFrame {
    private JPanel mainPanel;
    private JButton openFileBtn;
    private JLabel openedFileLb;
    private JLabel stateDiagramLb;
    private JLabel isDFALb;
    private JLabel alphabetLb;
    private JPanel leftSidePanel;
    private JList wordList;
    private DefaultListModel wordListModel;
    private JTextField wordTextField;
    private JButton addWordBtn;
    private JLabel wordLb;
    private JScrollPane wordListScrollPane;
    private JFileChooser fileChooser;

    private Automaton automaton;

    public static final int WIDTH = 650;
    public static final int HEIGHT = 350;

    public App(String title) {
        super(title);

        fileChooser = new JFileChooser(new File(AutomatonFileManager.getResourceFolder()));
        fileChooser.setDialogTitle("Choose Automaton Input File");
    }

    public App() {
        this("Automata and Process Theory App");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();

        openFileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This should preferably be handled smarter.
                // At the moment if an automaton is created, and after that the user pretends
                // to select a new one but cancels, it will again run through the previously selected automaton.
                createAutomatonFromFile();

                if (automaton != null) {
                    displayAlphabet();
                    createDiagramImage();
                    displayIfDFA();
                    displayWords();

                    enableWordFormControls();
                }
            }
        });
        addWordBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String wordContent = wordTextField.getText();

                if (!wordContent.equals("")) {
                    Word word = new Word(wordContent);
                    automaton.getWordValidator().validate(word);
                    wordListModel.addElement(word);
                }
            }
        });
    }

    private void enableWordFormControls() {
        wordTextField.setEditable(true);
        addWordBtn.setEnabled(true);
    }

    private void createAutomatonFromFile() {
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // Sets the file's parent folder as the folder to put the dot file and image to use.
            AutomatonFileManager.setDotBasePath(file.getParent());
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
        String result = "Alphabet: ";

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

    public void displayWords() {
        Set<Word> words = automaton.getWordCollection();
        wordListModel = new DefaultListModel();

        for (Word w: words) {
            wordListModel.addElement(w);
        }

        wordList.setModel(wordListModel);
    }

    public static void main(String[] args) {
        App app = new App();
        app.setSize(App.WIDTH, App.HEIGHT);
        app.setLocationRelativeTo(null);
        app.setVisible(true);
    }
}
