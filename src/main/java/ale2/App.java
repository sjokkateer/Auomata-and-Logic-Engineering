package ale2;

import ale2.classes.Automaton.Automaton;
import ale2.classes.Automaton.AutomatonFileManager;
import ale2.classes.Automaton.Diagram.StateDiagram;
import ale2.classes.Automaton.Exceptions.FileProcessingException;
import ale2.classes.Automaton.Exceptions.RegularExpressionException;
import ale2.classes.Automaton.LanguageChecker;
import ale2.classes.Automaton.Regex.Parser;
import ale2.classes.Automaton.Regex.Word;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.List;

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
    private JButton openImageBtn;
    private JTextField regexTextField;
    private JButton parseRegexBtn;

    private JList languageWordList;
    private DefaultListModel languageWordListModel;

    private JScrollPane languageScrollPane;
    private JPanel languagePanel;
    private JLabel infinityLabel;
    private JLabel wordsLabel;
    private JLabel languageLabel;
    private JFileChooser fileChooser;

    private Automaton automaton;
    private LanguageChecker languageChecker;
    private Desktop desktop;

    public static final int WIDTH = 650;
    public static final int HEIGHT = 350;

    private Parser parser;

    public App(String title) {
        super(title);

        languageChecker = new LanguageChecker();

        desktop = Desktop.getDesktop();

        // Maybe come up with something smarter later on.
        AutomatonFileManager.setDotBasePath(AutomatonFileManager.getResourceFolder());

        fileChooser = new JFileChooser(new File(AutomatonFileManager.getResourceFolder()));
        fileChooser.setDialogTitle("Choose Automaton Input File");

        parser = new Parser();
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
                    onAutomatonLoaded();
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
        openImageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    desktop.open(new File(AutomatonFileManager.getDotFileImagePath()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        parseRegexBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String regex = regexTextField.getText();

                if (!regex.equals("")) {
                    try {
                        StateDiagram stateDiagram = parser.parse(regex);

                        automaton = Automaton.fromStateDiagram(stateDiagram);
                        automaton.exportToFile();

                        onAutomatonLoaded();
                    } catch (RegularExpressionException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void displayLanguageWords() {
        infinityLabel.setText("");
        languageWordListModel = new DefaultListModel();

        languageChecker.setStateDiagram(automaton.getStateDiagram());
        List<Word> languageWords = languageChecker.getWords();

        String infiniteOrFinite= "Infinite";

        if (languageWords.size() > 0) {
            for (Word w: languageWords) {
                languageWordListModel.addElement(w);
            }

            infiniteOrFinite = "Finite";
        }

        languageWordList.setModel(languageWordListModel);
        infinityLabel.setText(infiniteOrFinite);
    }

    private void onAutomatonLoaded() {
        displayAlphabet();
        createDiagramImage();
        displayIfDFA();
        displayWords();

        displayLanguageWords();

        enableWordFormControls();
        openImageBtn.setEnabled(true);
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
