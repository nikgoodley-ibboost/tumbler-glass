package tumbler;

import static org.junit.Assert.*;

import java.io.*;

import tumbler.internal.writers.*;

public class TumblerTestUtils {
	private static StoryFileWriter writer = new ScenariosFileWriter();
	
	public static void assertReportExisted(String fileName) {
		assertReportExisted(fileName, writer);
	}
	
	public static void assertReportExisted(String fileName, StoryFileWriter storyWriter) {
		fileName = storyWriter.outputFolder() + storyWriter.reportFolder()
			+ File.separator + fileName;
		
		assertTrue(new File(fileName).delete());
	}

	public static void assertFileExistedInTarget(String fileName) {
		fileName = "target" + File.separator + fileName;

		assertTrue(new File(fileName).delete());
	}

	public static boolean deleteDirectoryRecursively(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory())
					deleteDirectoryRecursively(files[i]);
				else
					files[i].delete();
			}
		}
		return path.delete();
	}

	public static void deleteReportFolder() {
		deleteReportDirectory(writer);
	}

	public static void deleteReportDirectory(StoryFileWriter storyWriter) {
		if (System.getProperty("generateReport") == null)
			deleteDirectoryRecursively(new File(storyWriter.outputFolder() + writer.reportFolder()));
	}
}
