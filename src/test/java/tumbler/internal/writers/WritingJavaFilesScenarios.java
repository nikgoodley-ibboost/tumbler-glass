package tumbler.internal.writers;

import static tumbler.Tumbler.*;
import static tumbler.TumblerTestUtils.*;

import org.junit.runner.*;

import tumbler.*;
import tumbler.internal.domain.*;

@RunWith(TumblerRunner.class)
public class WritingJavaFilesScenarios {
	private String storyName = "some story";

	@Scenario
	public void shouldWriteJavaFileForStory() {
		Given("existing story");
		StoryModel story = new StoryModel(storyName);
		StoryFileWriter writer = new JavaFileWriter();

		When("story is written to a file");
		writer.write(story);

		Then("java file with proper name exists");
		assertFileExistedInTarget("SomeStoryScenarios.java");
	}
}
