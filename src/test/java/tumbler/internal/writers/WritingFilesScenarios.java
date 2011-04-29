package tumbler.internal.writers;

import static tumbler.Tumbler.*;
import static tumbler.TumblerTestUtils.*;

import java.io.*;
import java.util.*;

import org.junit.runner.*;

import tumbler.*;
import tumbler.internal.domain.*;

@RunWith(TumblerRunner.class)
public class WritingFilesScenarios {

	@Scenario
	public void shouldWriteFileInDefinedOutputFolder() {
		Given("existing story and StoryFileWriter writing to the 'WritingTest' folder");
		StoryModel story = new StoryModel("test");
		StoryFileWriter writer = new StoryFileWriter(){
			{
				this.fileExtension = ".test";
			}
			@Override
			String textFor(StoryModel story) {
				return "";
			}

			@Override
			protected String tocFor(List<StoryModel> storiesList) {
				return "";
			}
			
		};
		writer.setOutputFolder("target" + File.separator + "WritingTest");

		When("story is written to a file");
		writer.write(story);

		Then("file with proper name exists");
		assertFileExistedInTarget("WritingTest" + File.separator + "Tumbler-reports" + File.separator + "Test.test");
		deleteDirectoryRecursively(new File("target" + File.separator + "WritingTest"));
	}
}
