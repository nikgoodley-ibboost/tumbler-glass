package tumbler.internal.writers;

import java.util.*;

import tumbler.internal.domain.*;

public class JavaFileWriter extends StoryFileWriter {
	private JavaGenerator generator = new JavaGenerator();

	public JavaFileWriter() {
		fileExtension = "Scenarios.java";
	}

	@Override
	protected String textFor(StoryModel story) {
		generator.generateClassFor(story);
		return generator.javaText();
	}

	@Override
	public void writeToc(List<StoryModel> storiesList) {
	}

	@Override
	protected String tocFor(List<StoryModel> storiesList) {
		return null;
	}
	
	@Override
	public String reportFolder() {
		return "";
	}
}
