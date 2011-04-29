package tumbler.internal.parsers.gherkin;

import static org.junit.Assert.*;
import static tumbler.Tumbler.*;

import org.junit.runner.*;

import tumbler.*;

@RunWith(TumblerRunner.class)
public class ScenarioStateScenarios {
    private ParsingState givenState = new GivenState();
    private ParsingState whenState = new WhenState();
    private ParsingState thenState = new ThenState();
	private ParsingState scenarioState = new ScenarioState();

	@Scenario
	public void shouldTransitionToGivenStateIfGivenParsed() {
		Given("current state 'Scenario' and text 'Given: test'");
		String text = "Given: test";
		scenarioState.setReader(new LineReader(text));

		When("text is parsed");
		scenarioState.parse(null);

		Then("current state is 'Given'");
		assertEquals(givenState, scenarioState.getNextState());
	}
	
	@Scenario
	public void shouldTransitionToWhenStateIfWhenParsed() {
	    Given("current state 'Scenario' and text 'When: test'");
	    String text = "When: test";
	    scenarioState.setReader(new LineReader(text));
	    
	    When("text is parsed");
	    scenarioState.parse(null);
	    
	    Then("current state is 'When'");
	    assertEquals(whenState, scenarioState.getNextState());
	}
	
	@Scenario
	public void shouldTransitionToThenStateIfThenParsed() {
	    Given("current state 'Scenario' and text 'Then: test'");
	    String text = "Then: test";
	    scenarioState.setReader(new LineReader(text));
	    
	    When("text is parsed");
	    scenarioState.parse(null);
	    
	    Then("current state is 'Then'");
	    assertEquals(thenState, scenarioState.getNextState());
	}
}
