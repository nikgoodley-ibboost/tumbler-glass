package tumbler.internal.parsers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static tumbler.Tumbler.*;

import java.util.*;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;

import tumbler.*;
import tumbler.internal.domain.*;
import tumbler.internal.domain.ScenarioModel.ScenarioStatus;
import tumbler.internal.parsers.gherkin.*;

@RunWith(TumblerRunner.class)
public class ParsingScenariosScenarios {
	private static final String STORY = "Story:sample\n";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private GherkinLoader loader = new GherkinLoader();

	private String text;

	@Scenario
	public void shouldFindAllScenarioNamesInText() {
		Given("text with some scenarios in it");
		String name = "one";
		text = STORY + "Scenario:" + name + "\n" + "Scenario:2" + name;

		When("text is parsed");
		loader.loadFrom(text);

		Then("$it should return all given scenarios with proper names");
		assertThat(scenarios().get(0).name(), is(name));
		assertThat(scenarios().get(1).name(), is("2" + name));
		assertThat(scenarios().size(), is(2));
	}

	@Scenario
	public void shouldThrowExceptionIfScenarioDoesNotStartWithScenario() {
		Given("text not starting with Scenario");
		text = STORY + "Something blablabla";

		When("text is parsed");
		Then("ParseException should be thrown");
		thrown.expect(ParseException.class);
		loader.loadFrom(text);
	}

	@Scenario
	public void shouldSkipEmptyLinesBetweenScenarios() {
		Given("text with some scenarios in it");
		String one = "one";
		String two = "two";
		text = STORY + "Scenario: " + one + "\n\n\nScenario: " + two;

		When("text is parsed");
		loader.loadFrom(text);

		Then("$it should return all given scenarios with proper names");
		assertThat(scenarios().get(0).name(), is(one));
		assertThat(scenarios().get(1).name(), is(two));
		assertThat(scenarios().size(), is(2));
	}

	@Scenario
	public void shouldParseStepGiven() {
		Given("text with some scenarios with given");
		String one = "something";
		String two = "something else";
		text = STORY + "Scenario: one\nGiven " + one + "\nScenario: two\nGiven " + two;

		When("text is parsed");
		loader.loadFrom(text);

		Then("$it should return all given scenarios with proper given steps");
		assertThat(scenarios().get(0).given(), is(one));
		assertThat(scenarios().get(1).given(), is(two));
	}

	@Scenario
	public void shouldParseStepWhen() {
		Given("text with some scenarios with when");
		String one = "something";
		String two = "something else";
		text = STORY + "Scenario: one\nWhen " + one + "\nScenario: two\nWhen " + two;

		When("text is parsed");
		loader.loadFrom(text);

		Then("$it should return all when scenarios with proper when steps");
		assertThat(scenarios().get(0).when(), is(one));
		assertThat(scenarios().get(1).when(), is(two));
	}

	@Scenario
	public void shouldParseStepThen() {
		Given("text with some scenarios with then");
		String one = "something";
		String two = "something else";
		text = STORY + "Scenario: one\nThen " + one + "\nScenario: two\nThen " + two;

		When("text is parsed");
		loader.loadFrom(text);

		Then("$it should return all when scenarios with proper then steps");
		assertThat(scenarios().get(0).then(), is(one));
		assertThat(scenarios().get(1).then(), is(two));
	}

	@Scenario
	public void shouldParseScenarioWithGivenWhenThen() {
		Given("text with an scenario with given when then");
		String scenario = "story";
		String given = "something";
		String when = "something happens";
		String then = "verification";
		text = STORY + "Scenario: " + scenario + "\nGiven " + given + "\nWhen " + when + "\nThen " + then;

		When("text is parsed");
		loader.loadFrom(text);

		Then("$it should return the scenario with proper values in all steps");
		ScenarioModel result = scenarios().get(0);
		assertThat(result.name(), is(scenario));
		assertThat(result.given(), is(given));
		assertThat(result.when(), is(when));
		assertThat(result.then(), is(then));
	}

	@Scenario
	public void shouldNotGenerateClassWhenHasScenariosWithIdenticalNames() {
		Given("text with story and two scenarios with identical names");
		String text = "Story: some test\n" +
				"Scenario: do something\n" +
				"Scenario: do something";

		When("class is generated");
		Then("IllegalStateException should be thrown");
		thrown.expect(IllegalStateException.class);
		loader.loadFrom(text);
	}

	@Scenario
	public void shouldProperlyParsePendingScenario() {
		shouldProperlyParseScenarioWithStatus(ScenarioStatus.PENDING);
	}

	@Scenario
	public void shouldProperlyParsePassingScenario() {
		shouldProperlyParseScenarioWithStatus(ScenarioStatus.PASSED);
	}

	@Scenario
	public void shouldProperlyParseFailingScenario() {
		shouldProperlyParseScenarioWithStatus(ScenarioStatus.FAILED);
	}

	private void shouldProperlyParseScenarioWithStatus(ScenarioStatus status) {
		Given("text for pending scenario");
		String text = "Story: test\n Scenario: test\t [" + status + "]";

		When("text is parsed");
		loader.loadFrom(text);

		Then("resulting scenario object has status: " + status);
		assertEquals(status, scenarios().get(0).status());
	}

	@Scenario
	public void shouldParseScenarioWithoutStatusAsPending() {
		Given("text for pending scenario");
		String text = "Story: test\n Scenario: test";

		When("text is parsed");
		loader.loadFrom(text);

		Then("resulting scenario object has status: " + ScenarioStatus.PENDING);
		assertEquals(ScenarioStatus.PENDING, scenarios().get(0).status());
	}

	private List<ScenarioModel> scenarios() {
		return loader.stories().get(0).scenarios();
	}

	@Scenario
	public void shouldParseManyStoriesFromOneFile() {
		Given("scenarios file contents with two stories");
		String text = "Story: a\nStory: b";

		When("text is parsed");
		loader.loadFrom(text);

		Then("parser generated two stories");
		assertEquals(2, loader.stories().size());
	}

	@Scenario("should skip 'should' if is the first word in scenario name")
	public void shouldSkipShouldIfIsTheFirstWordInScenarioName() {
		Given("scenario named 'should do something'");
		String text = "Story: test\n Scenario: should do something";

		When("text is parsed");
		loader.loadFrom(text);

		Then("resulting scenario has name 'do something'");
		assertEquals("do something", scenarios().get(0).name());
	}

	@Scenario("should skip 'does' if is the first word in scenario name")
	public void shouldSkipDoesIfIsTheFirstWordInScenarioName() {
		Given("scenario named 'does not do something'");
		String text = "Story: test\n Scenario: does not do something";

		When("text is parsed");
		loader.loadFrom(text);

		Then("resulting scenario has name 'not do something'");
		assertEquals("not do something", scenarios().get(0).name());
	}

	@Scenario("should skip 'do' if is the first word in scenario name")
	public void shouldSkipDoIfIsTheFirstWordInScenarioName() {
		Given("scenario named 'do not do something'");
		String text = "Story: test\n Scenario: do not do something";

		When("text is parsed");
		loader.loadFrom(text);

		Then("resulting scenario has name 'not do something'");
		assertEquals("not do something", scenarios().get(0).name());
	}

	@Scenario("change 'doesn't' to 'not' if is the first in scenario name")
	public void shouldChangeDoesntToNotIfIsTheFirstInScenarioName() {
		Given("scenario named 'doesn't do something'");
		String text = "Story: test\n Scenario: doesn't do something";

		When("text is parsed");
		loader.loadFrom(text);

		Then("resulting scenario has name 'not do something'");
		assertEquals("not do something", scenarios().get(0).name());
	}

	@Scenario("skip comments defined as '--'")
	public void shouldSkipCommentsDefinedAsDashes() {
		Given("a text with comment");
		String text = "Story: no comment -- comment";

		When("text is parsed");
		loader.loadFrom(text);

		Then("comment is skipped");
		assertEquals("no comment", loader.stories().get(0).name());
	}
	
	@Scenario
	public void shouldChangeDoubleToSingleQuotationMarks() {
		Given("a story text with a a double quotation mark");
		String text = "Story: \"";
		
		When("text is parsed");
		loader.loadFrom(text);
		
		Then("story name has a single quotation mark");
		assertEquals("'", loader.stories().get(0).name());
	}

}
