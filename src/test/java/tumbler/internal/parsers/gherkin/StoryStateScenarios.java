package tumbler.internal.parsers.gherkin;

import static org.junit.Assert.*;
import static tumbler.Tumbler.*;

import java.util.*;

import org.junit.runner.*;

import tumbler.*;
import tumbler.internal.domain.*;
import tumbler.internal.parsers.*;

@RunWith(TumblerRunner.class)
public class StoryStateScenarios {
	private StoryState storyState = new StoryState();
	private ParsingState scenarioState = new ScenarioState();

	@Scenario
	public void shouldTransitionToScenarioStateIfScenarioParsed() {
		Given("current state 'Story' and text 'Scenario: test'");
		String text = "Scenario: test";
		storyState.setReader(new LineReader(text));

		When("text is parsed");
		storyState.parse(null);

		Then("current state is 'Scenario'");
		assertEquals(scenarioState, storyState.getNextState());
	}

	@Scenario
	public void shouldTransitionToAsState() {
	    Given("current state 'Story' and text 'As a role'");
	    String text = "As a role";
	    storyState.setReader(new LineReader(text));
	    
	    When("text is parsed");
	    storyState.parse(null);
	    
	    Then("current state is 'As a'");
	    assertEquals(new ParsingState(), storyState.getNextState());
	}
	
	@Scenario
	public void shouldTransitionToInOrderState() {
	    Given("current state 'Story' and text 'In order to do something'");
	    String text = "In order to do something";
	    storyState.setReader(new LineReader(text));
	    
	    When("text is parsed");
	    storyState.parse(null);
	    
	    Then("current state is 'In order to'");
	    assertEquals(new ParsingState(), storyState.getNextState());
	}
	
	@Scenario
	public void shouldTransitionToSoThatState() {
	    Given("current state 'Story' and text 'So that something happens'");
	    String text = "So that something happens";
	    storyState.setReader(new LineReader(text));
	    
	    When("text is parsed");
	    storyState.parse(null);
	    
	    Then("current state is 'In order to'");
	    assertEquals(new ParsingState(), storyState.getNextState());
	}
	
	@Scenario
	public void shouldTransitionToIWantState() {
	    Given("current state 'Story' and text 'I want to do something'");
	    String text = "I want to do something";
	    storyState.setReader(new LineReader(text));
	    
	    When("text is parsed");
	    storyState.parse(null);
	    
	    Then("current state is 'I want to'");
	    assertEquals(new ParsingState(), storyState.getNextState());
	}
	
	@Scenario
	public void shouldNotTransitionToScenarioStateIfStoryNotParsed() {
		Given("current state 'Story' and text 'blabla'");
		String text = "blabla";
		storyState.setReader(new LineReader(text));

		When("text is parsed");
		try {
		    storyState.parse(null);
			fail("should not parse - improper text for state " + storyState);
		} catch (ParseException e) {			
		}

		Then("current state is still 'Story'");
		assertEquals(storyState, storyState.getNextState());
	}
	   
    @Scenario
    public void shouldCreateTwoScenarioModelsForTextWithTwoScenarios() {
        Given("text with 2 scenarios: test1 and test2");
        String text = "Scenario: test1\nScenario: test2";
        storyState.setReader(new LineReader(text));
        List<ScenarioModel> scenarios = new ArrayList<ScenarioModel>();
        
        When("text is parsed");
        storyState.parse(scenarios);

        Then("two ScenarioModels with proper names are created");
        assertEquals(2, scenarios.size());
        assertEquals("test1", scenarios.get(0).name());
        assertEquals("test2", scenarios.get(1).name());
    }   

}
