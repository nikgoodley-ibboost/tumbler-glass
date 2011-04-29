package tumbler.internal.parsers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static tumbler.Tumbler.*;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;

import tumbler.*;
import tumbler.internal.parsers.gherkin.*;

@RunWith(TumblerRunner.class)
public class ParsingStoriesScenarios {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private GherkinLoader loader = new GherkinLoader();
	private String text;

	@Scenario
	public void shouldFindStoryIfStartsWithStory() {
		Given("text with story for scenarios, line starting with Story");
		String story = "one";
		text = "Story:" + story;

		When("text is loadFromd");
		loader.loadFrom(text);

		Then("it should return object with story");
		assertThat(nameOfFirstStory(), is(story));
	}
	
	@Scenario
	public void shouldFindStoryIfStartsWithFeature() {
		Given("text with story for scenarios, line starting with Feature");
		String expectedStoryName = "one";
		text = "Feature:" + expectedStoryName;
		
		When("text is loadFromd");
		loader.loadFrom(text);
		
		Then("it should return object with story");
		assertThat(nameOfFirstStory(), is(expectedStoryName));
	}

    private String nameOfFirstStory() {
        return loader.stories().get(0).name();
    }

	@Scenario
	public void shouldThrowExceptionIfThereIsNoStoryInText() {
		Given("text without story in it");
		text = "";

		When("text is loadFromd");
		Then("IllegalArgumentException should be thrown");
		thrown.expect(IllegalArgumentException.class);
		loader.loadFrom(text);
	}

	@Scenario
	public void shouldThrowExceptionIfStoryRetrievedWithoutParsing() {
		When("story is retrieved without parsing");
		Then("IllegalStateException should be thrown");
		thrown.expect(IllegalStateException.class);
		loader.stories();
	}
}
