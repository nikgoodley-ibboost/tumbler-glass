package tumbler;

import java.io.*;
import java.util.*;

import tumbler.internal.domain.*;
import tumbler.internal.parsers.gherkin.*;
import tumbler.internal.writers.*;

/**
 * Reads in .scenarios file and produces a Java class with scenarios to be
 * implemented.
 * 
 * @author Pawel Lipinski
 */
public class ScenariosReader {
	private GherkinLoader loader = new GherkinLoader();

	/**
	 * Loads and parses given file.
	 * 
	 * @param fileName
	 *            Path to the file to be read. In the form to be accepted by
	 *            {@link java.io.FileReader}
	 * @throws RuntimeException
	 *             if something went wrong with reading the file
	 */
	public void load(String fileName) {
		String text = "";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null)
				text += line + "\n";			
		} catch (Exception e) {
			throw new RuntimeException("Could not load scenarios file.", e);
		}		
		loader.loadFrom(text);
	}

	/**
	 * Accessor to {@link StoryModel} representing the loaded scenarios file.
	 * 
	 * @return Parsed StoryModel (with scenarios, etc.)
	 */
	public List<StoryModel> stories() {
		return loader.stories();
	}

	/**
	 * Prints out a Java file contents for a given .scenarios file.
	 * 
	 * @param args
	 *            with .scenarios file name as first argument
	 */
	public static void main(String[] args) {
		ScenariosReader reader = new ScenariosReader();
		reader.load(args[0]);
		for (StoryModel story : reader.stories()) {
			JavaGenerator generator = new JavaGenerator();
			generator.generateClassFor(story);
			System.out.println(generator.javaText());
			System.out.println("\n");
		}
	}

}
