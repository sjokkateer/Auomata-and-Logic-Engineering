package ale2.classes.Automaton;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class AutomatonFileManager {
    private static final String RESOURCE_FOLDER = "/src/main/resources";
    private static String dotBasePath;

    public static void createDotFile(Automaton automaton) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(getDotFilePath());
        out.print(automaton.getDotFileString());
        out.close();
    }

    public static BufferedImage createDotFileImage() {
        String pathToImage = getDotFileImagePath();
        // It alsmost seems as if the command can not be run through the jar file.
        // if it is not run from the command line? Maybe that has to do with permissions
        // or w/e since otherwise java programs could all execute commands?
        BufferedImage img = null;

        try {
            Process p = new ProcessBuilder("dot", "-Tpng", "-o" + pathToImage, getDotFilePath()).start();
            p.waitFor();
            img = ImageIO.read(new File(pathToImage));
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
        return dotBasePath + "/" + Automaton.NAME;
    }

    public static void setDotBasePath(String path) {
        dotBasePath = path;
    }

    public static String getDotFileImagePath() {
        return getDotBasePath() + ".png";
    }
}
