package ale2;

import ale2.classes.Automaton.Automaton;
import ale2.classes.Automaton.AutomatonFileManager;
import ale2.classes.Automaton.Diagram.NfaConverter;
import ale2.classes.Automaton.Diagram.PushDownAutomata;
import ale2.classes.Automaton.Diagram.StateDiagram;
import ale2.classes.Automaton.Exceptions.FileProcessingException;
import ale2.classes.Automaton.Exceptions.NotConvertibleNfaException;
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
import java.time.LocalDateTime;
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
    private JButton convertToDfaBtn;
    private JLabel convertToDfaErrorLb;
    private JLabel inputFileErrorLb;
    private JLabel reErrorLb;
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
                        clearFields();

                        StateDiagram stateDiagram = parser.parse(regex);

                        automaton = Automaton.fromStateDiagram(stateDiagram);
                        automaton.exportToFile("RE-export");

                        onAutomatonLoaded();
                    } catch (RegularExpressionException ex) {
                        unsetAutomatonAndLockControls();
                        reErrorLb.setText(ex.getMessage());

                        ex.printStackTrace();
                    }
                }
            }
        });
        convertToDfaBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (automaton != null) {
                    // We always write either the original dfa or nfa to file.
                    NfaConverter nfaConverter = null;
                    try {
                        convertToDfaErrorLb.setText("");
                        nfaConverter = new NfaConverter(automaton.getStateDiagram());
                    } catch (NotConvertibleNfaException notConvertibleNfaException) {
                        convertToDfaErrorLb.setText(notConvertibleNfaException.getMessage());

                        notConvertibleNfaException.printStackTrace();
                    }
                    String now = LocalDateTime.now().toString();
                    automaton.exportToFile( now + "_original");

                    // Only if it is not already in dfa we convert it to dfa and write
                    // the test vectors.
                    if (!automaton.isDFA()) {
                        StateDiagram convertedStateDiagram = nfaConverter.convertToDfa();
                        Automaton converted = Automaton.fromStateDiagram(convertedStateDiagram);
                        converted.exportToFile(now + "_dfa");
                    }
                }
            }
        });
    }

    private void unsetAutomatonAndLockControls() {
        automaton = null;

        wordTextField.setEditable(false);
        addWordBtn.setEnabled(false);

        openImageBtn.setEnabled(false);
        convertToDfaBtn.setEnabled(false);
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
        inputFileErrorLb.setText("");
        convertToDfaErrorLb.setText("");

        displayAlphabet();
        createDiagramImage();

        StateDiagram stateDiagram = automaton.getStateDiagram();

        if (stateDiagram.getClass() == StateDiagram.class) {
            displayIfDFA();
            displayLanguageWords();

            convertToDfaBtn.setEnabled(true);
        }

        if (stateDiagram.getClass() == PushDownAutomata.class || automaton.isDFA()) {
            // Disable converting to DFA.
            convertToDfaBtn.setEnabled(false);
        }

        displayWords();

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
                clearFields();
                automaton = Automaton.fromFile(file);
            } catch (FileProcessingException e) {
                unsetAutomatonAndLockControls();

                openedFileLb.setText("Tried to open: " + file.getName());
                inputFileErrorLb.setText(e.getMessage());

                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        if (wordListModel != null) {
            wordListModel.clear();
        }

        if (languageWordListModel != null) {
            languageWordListModel.clear();
        }

        wordTextField.setText("");
        regexTextField.setText("");

        openedFileLb.setText("");
        stateDiagramLb.setIcon(null);
        isDFALb.setText("");
        alphabetLb.setText("");
        infinityLabel.setText("");

        inputFileErrorLb.setText("");
        reErrorLb.setText("");
        convertToDfaErrorLb.setText("");
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
