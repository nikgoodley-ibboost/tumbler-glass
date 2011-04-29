package tumbler.internal.writers;

import static org.junit.Assert.*;
import static tumbler.Tumbler.*;
import static tumbler.TumblerTestUtils.*;

import java.io.*;

import org.junit.*;
import org.junit.runner.*;

import tumbler.*;
import tumbler.internal.domain.*;
import tumbler.internal.domain.ScenarioModel.ScenarioStatus;
import tumbler.internal.parsers.gherkin.*;

@RunWith(TumblerRunner.class)
public class WritingScenariosFileScenarios {
	private ScenariosFileWriter writer = new ScenariosFileWriter();
	private StoryModel story;

	@Before
	public void setupStory() {
		story = new StoryModel("sample story");
		story.addScenario(
				new ScenarioModel("first")				
				.withGiven("some1 given")
				.withWhen("some1 when")
				.withThen("some1 then")
				.withStory(story));
		story.addScenario(
				new ScenarioModel("second")
				.withGiven("some2 given")
				.withWhen("some2 when")
				.withThen("some2 then")
				.withStory(story));
	}

	@Scenario("Should produce correct scenarios file contents (checked as a roundtrip)")
	public void shouldProduceCorrectScenariosFileContents() {
		Given("story with some name and scenarios");
		GherkinLoader loader = new GherkinLoader();

		When("scenarios file's content is generated and re-parsed to produce story");
		String text = writer.textFor(story);
		loader.loadFrom(text);

		Then("initial story and parsed story should be equal");
		assertEquals(story, loader.stories().get(0));
	}

	@Scenario
	public void shouldCreateScenariosFileFromStory() {
		Given("story with some name and scenarios");

		When("scenario writer is called");
		writer.write(story);

		Then("scenarios file should exist");
		assertReportExisted("SampleStory.scenarios");
		deleteReportFolder();
	}

	@Scenario
	public void shouldStoreInfoAboutPassingScenariosInGeneratedFile() {
		shouldStoreInfoAboutStatusOfScenarioInGeneratedFile("passed", ScenarioStatus.PASSED);
	}

	@Scenario
	public void shouldStoreInfoAboutFailingScenariosInGeneratedFile() {
		shouldStoreInfoAboutStatusOfScenarioInGeneratedFile("failed", ScenarioStatus.FAILED);
	}

	@Scenario
	public void shouldStoreInfoAboutPendingScenariosInGeneratedFile() {
		shouldStoreInfoAboutStatusOfScenarioInGeneratedFile("is pending", ScenarioStatus.PENDING);
	}

	private void shouldStoreInfoAboutStatusOfScenarioInGeneratedFile(String statusName, ScenarioStatus status) {
		Given("story with one scenario which " + statusName);
		story = new StoryModel();
		story.addScenario(new ScenarioModel("sample").withStatus(status));

		When("scenario is processed");
		String text = writer.textFor(story);

		Then("file contents should contain [" + status + "]");
		assertTrue(text.contains("[" + status + "]"));
	}
	

    @Scenario
    public void shouldWriteFileInDefinedOutputFolder() {
        Given("existing story and JavaFileWriter writing to the 'WritingTest' folder");
        StoryModel story = new StoryModel("test");
        JavaFileWriter writer = new JavaFileWriter();
        writer.setOutputFolder("target" + File.separator + "WritingTest");

        When("story is written to a file");
        writer.write(story);

        Then("file with proper name exists");
        assertFileExistedInTarget("WritingTest" + File.separator + "TestScenarios.java");
        deleteDirectoryRecursively(new File("target" + File.separator + "WritingTest"));
    }

}
