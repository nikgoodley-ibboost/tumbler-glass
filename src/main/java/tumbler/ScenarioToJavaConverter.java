package tumbler;

import tumbler.internal.domain.*;
import tumbler.internal.writers.*;

/**
 * Converts .scenarios files to a Java class.
 * 
 * @author Pawel Lipinski
 */
public class ScenarioToJavaConverter {
	StoryFileWriter writer = new JavaFileWriter();

	/**
	 * Converts a file with the given name to a Java class using a
	 * {@link JavaFileWriter}.
	 * 
	 * @param fileName
	 *            name of a file to be read by {@link ScenariosReader}
	 */
	public void convert(String fileName) {
	    setOutputFolder();
		ScenariosReader scenariosReader = new ScenariosReader();
		scenariosReader.load(fileName);
		for (StoryModel story : scenariosReader.stories())
			writer.write(story);
	}

    private void setOutputFolder() {
        if (System.getProperty("outputFolder") != null)
            writer.setOutputFolder(System.getProperty("outputFolder"));
    }

	/**
	 * Generates a Java file for a given .scenarios file.
	 * 
	 * @param args
	 *            with .scenarios file name as first argument
	 */
	public static void main(String[] args) {
		handleNoArgs(args);
		new ScenarioToJavaConverter().convert(args[0]);
	}

	private static void handleNoArgs(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java -cp Tumbler.jar tumbler.ScenarioToJavaConverter <scenarios file>");
			System.exit(0);
		}
	}

	public StoryFileWriter getWriter() {
		return writer;
	}
}
