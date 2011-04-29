package tumbler.internal.parsers.gherkin;

import static org.junit.Assert.*;
import static tumbler.Tumbler.*;

import org.junit.runner.*;

import tumbler.*;

@RunWith(TumblerRunner.class)
public class GivenWhenThenStateScenarios {
    private ParsingState scenarioState = new ScenarioState();
    private ParsingState givenState = new GivenState();
    private ParsingState whenState = new WhenState();
    private ParsingState thenState = new ThenState();
    private ParsingState andState = new ParsingState();
    private ParsingState butState = new ParsingState();

	@Scenario
	public void shouldTransitionFromGivenToAndState() {
		Given("current state 'Given' and text 'And test'");
		String text = "And test";
		givenState.setReader(new LineReader(text));

		When("text is parsed");
		givenState.parse(null);

		Then("current state is 'And'");
		assertEquals(andState, givenState.getNextState());
	}	
	
	@Scenario
	public void shouldTransitionFromWhenToAndState() {
	    Given("current state 'When' and text 'And test'");
	    String text = "And test";
	    whenState.setReader(new LineReader(text));
	    
	    When("text is parsed");
	    whenState.parse(null);
	    
	    Then("current state is 'And'");
	    assertEquals(andState, whenState.getNextState());
	}	
	
	@Scenario
	public void shouldTransitionFromThenToAndState() {
	    Given("current state 'Then' and text 'And test'");
	    String text = "And test";
	    thenState.setReader(new LineReader(text));
	    
	    When("text is parsed");
	    thenState.parse(null);
	    
	    Then("current state is 'And'");
	    assertEquals(andState, thenState.getNextState());
	}	
	
	@Scenario
	public void shouldTransitionFromThenToButState() {
	    Given("current state 'Then' and text 'But test'");
	    String text = "But test";
	    thenState.setReader(new LineReader(text));
	    
	    When("text is parsed");
	    thenState.parse(null);
	    
	    Then("current state is 'But'");
	    assertEquals(butState, thenState.getNextState());
	}	
	
	@Scenario
	public void shouldTransitionToAndStateFromScenarioViaGiven() {
	    Given("current state 'Scenario' and text 'Given test1\nAnd test2'");
	    String text = "Given test1\nAnd test2";
	    scenarioState.setReader(new LineReader(text));
	    
	    When("text is parsed");
	    scenarioState.parse(null);
	    
	    Then("current state is 'And'");
	    assertEquals(andState, scenarioState.getNextState().getNextState());
	}	
	
	@Scenario
	public void shouldTransitionToAndStateFromScenarioViaWhen() {
	    Given("current state 'Scenario' and text 'When test1\nAnd test2'");
	    String text = "When test1\nAnd test2";
	    scenarioState.setReader(new LineReader(text));
	    
	    When("text is parsed");
	    scenarioState.parse(null);
	    
	    Then("current state is 'And'");
	    assertEquals(andState, scenarioState.getNextState().getNextState());
	}	
	
	@Scenario
	public void shouldTransitionToAndStateFromScenarioViaThen() {
	    Given("current state 'Scenario' and text 'Then test1\nAnd test2'");
	    String text = "Then test1\nAnd test2";
	    scenarioState.setReader(new LineReader(text));
	    
	    When("text is parsed");
	    scenarioState.parse(null);
	    
	    Then("current state is 'And'");
	    assertEquals(andState, scenarioState.getNextState().getNextState());
	}	
	
	@Scenario
	public void shouldTransitionToButStateFromScenarioViaThen() {
	    Given("current state 'Scenario' and text 'Then test1\nBut test2'");
	    String text = "Then test1\nBut test2";
	    scenarioState.setReader(new LineReader(text));
	    
	    When("text is parsed");
	    scenarioState.parse(null);
	    
	    Then("current state is 'But'");
	    assertEquals(butState, scenarioState.getNextState().getNextState());
	}	
}
