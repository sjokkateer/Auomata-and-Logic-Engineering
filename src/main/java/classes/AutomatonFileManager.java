package classes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class AutomatonFileManager {
    private static final String RESOURCE_FOLDER = "/src/main/resources";

    public static void createDotFile(Automaton automaton) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(getDotFilePath());
        out.print(automaton.getDotFileString());
        out.close();
    }

    public static BufferedImage createDotFileImage() {
        String[] cmd = { "dot", "-Tpng", "-o" + getDotFileImagePath(), getDotFilePath()};
        BufferedImage img = null;

        try {
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            img = ImageIO.read(new File(getDotFileImagePath()));
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return img;
    }

    public static String getResourceFolder() {
        return System.getProperty("user.dir") + RESOURCE_FOLDER;
    }

    public static String getDotFilePath() {
        return getDotBasePath() + ".dot";
    }

    private static String getDotBasePath() {
        return getResourceFolder() + "/" + Automaton.NAME;
    }

    public static String getDotFileImagePath() {
        return getDotBasePath() + ".png";
    }
}
