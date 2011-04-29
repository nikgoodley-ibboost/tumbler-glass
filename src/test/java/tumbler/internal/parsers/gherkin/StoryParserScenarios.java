package tumbler.internal.parsers.gherkin;

import static org.junit.Assert.*;
import static tumbler.Tumbler.*;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;

import tumbler.*;
import tumbler.internal.domain.*;
import tumbler.internal.parsers.*;

@RunWith(TumblerRunner.class)
public class StoryParserScenarios {
	@Rule
	public ExpectedException throwing = ExpectedException.none();
	private TokenParser parser = new ScenarioParser();
	
	@Scenario
	public void shouldNotCreateScenarioIfTextIsEmpty() {
		Given("empty text");
		String text = "";
		throwing.expect(ParseException.class);

		When("text is parsed");
		Then("no scenario is created");
		parser.parse(text);
	}

	@Scenario
	public void shouldNotCreateScenarioIfTextHasNoScenarioToken() {
		Given("text without 'Scenario' token");
		
		String text = "blahblah";
		throwing.expect(ParseException.class);
		
		When("text is parsed");
		Then("no scenario is created");
		parser.parse(text);
	}
	
	@Scenario
	public void shouldCreateScenarioWhenLineStartsWithScenario() {
		Given("text line 'Scenario: test'");
		When("text is parsed");
		parser.parse(" Scenario: test");
		
		Then("scenario 'test' is created");
		assertEquals("test", ((ScenarioModel)parser.result()).name());
	}
}
