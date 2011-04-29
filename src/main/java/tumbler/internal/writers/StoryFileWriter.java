package tumbler.internal.writers;

import java.io.*;
import java.util.*;

import tumbler.internal.domain.*;

public abstract class StoryFileWriter {

	public static final String REPORT_DIR = "Tumbler-reports";

	protected String fileExtension;
	protected String tocFile;
	protected String outputFolder = "target";

	private File writtenFile;

	public void write(StoryModel story) {
		String text = textFor(story);
		try {
			writeTextToFile(story, text);
		} catch (Exception e) {
			throw new RuntimeException("File could not be generated.", e);
		}
	}

	abstract String textFor(StoryModel story);

	public void writeToc(List<StoryModel> storiesList) {
		String text = tocFor(storiesList);
		try {
			writeTocToFile(text);
		} catch (Exception e) {
			throw new RuntimeException("File could not be generated.", e);
		}
	}

	protected void writeTextToFile(StoryModel story, String text) throws IOException {
		createOutputFolder();
		String fullDir = createPackageFolders(story.packageName());
		writtenFile = new File(fullDir + File.separator + story.camelisedName() + fileExtension);		
		Writer writer = new OutputStreamWriter(new FileOutputStream(getWrittenFile(), false), "UTF-8");
		writer.append(text);
		writer.flush();
		writer.close();
	}

	protected void writeTocToFile(String text) throws IOException {
		FileWriter writer = new FileWriter(new File(buildFullPath(tocFile)), false);
		writer.append(text);
		writer.flush();
		writer.close();
	}

	protected abstract String tocFor(List<StoryModel> storiesList);

	private String createPackageFolders(String packageName) {
		packageName = packageName.replace('.', File.separatorChar);
		String fullDir = buildFullPath(packageName);
		if (! new File(fullDir).exists())
			new File(fullDir).mkdirs();
		return fullDir;
	}

	private String buildFullPath(String file) {
		return outputFolder + reportFolder() + File.separator + file;
	}

	public String reportFolder() {
		return File.separator + REPORT_DIR;
	}

	private void createOutputFolder() {
		if (!new File(buildFullPath("")).exists())
			new File(buildFullPath("")).mkdirs();
	}

	protected void copy(InputStream in, File dst) throws IOException {
		OutputStream out = new FileOutputStream(dst);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}

		in.close();
		out.close();
	}

	public File getWrittenFile() {
		return writtenFile;
	}

	public void setOutputFolder(String outputFolder) {
		if (outputFolder == null)
			this.outputFolder = "target";
		else
			this.outputFolder = outputFolder;		
	}
	
	public String outputFolder() {
		return outputFolder;
	}
}