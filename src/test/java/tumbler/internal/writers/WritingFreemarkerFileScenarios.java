package tumbler.internal.writers;

import static org.junit.Assert.*;
import static tumbler.Tumbler.*;
import static tumbler.TumblerTestUtils.*;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;

import tumbler.*;
import tumbler.internal.domain.*;
import tumbler.internal.domain.ScenarioModel.ScenarioStatus;

@RunWith(TumblerRunner.class)
public class WritingFreemarkerFileScenarios {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private StoryFileWriter writer = new FreemarkerFileWriter();

	@Scenario
	public void shouldNotCreateHtmlWhenNoStory() {
		Given("no story");
		thrown.expect(IllegalArgumentException.class);

		When("generating html file");
		Then("exception should be thrown");
		writer.textFor(null);
	}

	@Scenario
	public void shouldCreateHtmlWithStoryInTitleAndH1() {
		Given("there is story named 'test'");
		StoryModel story = new StoryModel("test");

		When("generating html file");
		String text = writer.textFor(story);

		Then("is 'test' in title and h1");
		assertTrue(text.contains("<title>Tumbler - Story: test</title>"));
		assertTrue(text.contains("<th>test should:</th>"));
	}
	
	@Scenario
	public void shouldCreateHtmlWithNarrative() {
	    Given("there is story narrated as 'As me, I want to test, So that it's tested'");
	    StoryModel story = new StoryModel("test");
	    story.withNarrative("As me, I want to test, So that it's tested");
	    
	    When("generating html file");
	    String text = writer.textFor(story);
	    
	    Then("the narrative is under the title ");
	    assertTrue(text.contains("As me, I want to test, So that it's tested"));
	}

	@Scenario
	public void shouldCreateHtmlWithScenariosAndGiven() {
		Given("there is story and two scenarios");
		StoryModel story = new StoryModel("Sample story");
		story.addScenario(
				new ScenarioModel("have scenario 1")
				.withGiven("setup1")
				.withStatus(ScenarioStatus.PASSED)
				.withStory(story)
				);
		story.addScenario(
				new ScenarioModel("have scenario 2")
				.withGiven("setup2")
				.withStatus(ScenarioStatus.PASSED)
				.withStory(story)
				);

		When("generating html file");
		String text = writer.textFor(story);

		Then("is 'test' in title and h1");
		assertTrue(text.contains("have scenario 1"));
		assertTrue(text.contains("Given setup1"));
		assertTrue(text.contains("have scenario 2"));
		assertTrue(text.contains("Given setup2"));
	}

	@Scenario
	public void shouldShowPassingScenariosAsPassed() {
		shouldShowScenarioWithProperStatus(ScenarioStatus.PASSED);
	}

	@Scenario
	public void shouldShowFailingScenariosAsFailed() {
		shouldShowScenarioWithProperStatus(ScenarioStatus.FAILED);
	}

	@Scenario
	public void shouldShowPendingScenariosAsPending() {
		shouldShowScenarioWithProperStatus(ScenarioStatus.PENDING);
	}

	private void shouldShowScenarioWithProperStatus(ScenarioStatus scenarioStatus) {
		Given("there is a story and a " + scenarioStatus + " scenario");
		StoryModel story = new StoryModel("Sample story");
		story.addScenario(
				new ScenarioModel("have scenario")
				.withStatus(scenarioStatus)
				);

		When("generating html file");
		String text = writer.textFor(story);

		Then("scenario should have image with name " + scenarioStatus);
		String imageFile = "img src=\"resources" + File.separator + scenarioStatus + ".png\"";
		assertTrue(text.lastIndexOf(imageFile) != text.indexOf(imageFile));
	}

	@Scenario
	public void shouldCreateFolderWithResources() {
		Given("a flag to create files is set");
		String reportsFolder = writer.outputFolder() + writer.reportFolder();
		deleteReportFolder();

		When("tests are run");
		writer.write(new StoryModel("test"));

		Then("resources folder with html resources should exist");
		assertTrue(new File(reportsFolder).exists());
		String resources = reportsFolder + File.separator + "resources";
		assertTrue(new File(resources).exists());
		assertTrue(new File(resources + File.separator + "tumbler.jpg").exists());
		deleteReportFolder();
	}

	@Scenario
	public void shouldCreateReportsInProperPackageFolders() {
		Given("a story 'some story' in a test.me package");
		StoryModel story = new StoryModel("some story").withPackage("test.me");

		When("a report is created");
		writer.write(story);

		Then("the report is in the test/me folder");
		assertReportExisted("test" + File.separator + "me" + File.separator + "SomeStoryScenarios.html");
		deleteReportFolder();
	}

	@Scenario
	public void shouldCreateReportsWithCorrectLinksToHtmlResources() {
		Given("a story 'some story' in a test.me package");
		StoryModel story = new StoryModel("some story").withPackage("test.me");

		When("a report is created");
		String text = writer.textFor(story);

		Then("the report contains correct links to html resources");
		assertTrue(text.contains("src=\"../../resources/PASSED.png\""));
	}

	@Scenario
	public void shouldGenerateTableOfContentsWithLinksToStories() {
		Given("two stories: 'one' and 'two'");
		List<StoryModel> stories = Arrays.asList(new StoryModel("one"), new StoryModel("two"));

		When("toc is generated");
		String toc = writer.tocFor(stories);

		Then("toc should contain links to story reports");
		assertTrue(toc.contains("href=\"OneScenarios.html\""));
	}

	@Scenario
	public void shouldGenerateTableOfContentsWithStoryStatistics() {
		Given("two stories: 'passing' with one passing scenario and 'pending' with one failing and one pending scenario");
		StoryModel passingStory = new StoryModel("one");
		passingStory.addScenario(new ScenarioModel("pass").withStatus(ScenarioStatus.PASSED));
		StoryModel pendingStory = new StoryModel("two");
		pendingStory.addScenario(new ScenarioModel("fail").withStatus(ScenarioStatus.FAILED));
		pendingStory.addScenario(new ScenarioModel("pend").withStatus(ScenarioStatus.PENDING));
		List<StoryModel> stories = Arrays.asList(passingStory, pendingStory);

		When("toc is generated");
		String toc = writer.tocFor(stories);

		Then("toc should contain one passing, zero failing, and one pending story and proper scenario stats");
		assertTrue(toc.contains("id=\"stories passed\">1<"));
		assertTrue(toc.contains("id=\"stories failed\">0<"));
		assertTrue(toc.contains("id=\"stories pending\">1<"));
		assertTrue(toc.contains("id=\"scenarios passed\">1<"));
		assertTrue(toc.contains("id=\"scenarios failed\">1<"));
		assertTrue(toc.contains("id=\"scenarios pending\">1<"));
	}

	@Scenario
	public void shouldCopyResourcesIfCustomTemplateUsed() {
		Given("system property -Dtemplate=folder and some story");
		System.setProperty("template", "src" + File.separator + "test" + File.separator + "resources" + File.separator + "template");
		StoryModel story = new StoryModel("test");

		When("files are generated");
		writer.write(story);

		Then("given template is used and resources copied");
		assertReportExisted("TestScenarios.html");
		assertReportExisted("resources" + File.separator + "resource.css");
		deleteReportFolder();
		System.clearProperty("template");
	}

	@Scenario
	public void shouldUseCustomTemplateIfUsed() {
		Given("system property -Dtemplate=folder and two stories");
		System.setProperty("template", "src" + File.separator + "test" + File.separator + "resources" + File.separator + "template");
		writer = new FreemarkerFileWriter();
		StoryModel story1 = new StoryModel("story1");
		StoryModel story2 = new StoryModel("story2");

		When("text and toc contents for stories are generated");
		String text = writer.textFor(story1);
		String toc = writer.tocFor(Arrays.asList(story1, story2));

		Then("text contents contains 'story1' and toc contains 'story1 story2'");
		assertEquals("story1", text);
		assertEquals("story1 story2 ", toc);
		System.clearProperty("template");
	}

}
