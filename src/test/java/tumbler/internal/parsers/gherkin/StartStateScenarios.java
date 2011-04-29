package tumbler.internal.parsers.gherkin;

import static org.junit.Assert.*;
import static tumbler.Tumbler.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;

import tumbler.*;
import tumbler.internal.domain.*;
import tumbler.internal.parsers.*;

@RunWith(TumblerRunner.class)
public class StartStateScenarios {
	private ParsingState startState;
	private StoryState storyState;
	private List<StoryModel> emptyList = new ArrayList<StoryModel>();
	
	@Before
	public void initStates() {
	    startState = new StartState();	   
	    storyState = new StoryState();
	}

	@Scenario	
	public void shouldTransitionToStoryStateIfStoryParsed() {
		Given("text 'Story: test'");
		String text = "Story: test";
		startState.setReader(new LineReader(text));
		
		When("text is parsed");
        startState.parse(null);

		Then("current state is 'Story'");
		assertEquals(storyState, startState.getNextState());
	}	
	

	@Scenario
	public void shouldNotTransitionToStoryStateIfStoryNotParsed() {
		Given("text 'blabla'");
		String text = "blabla";
		startState.setReader(new LineReader(text));

		When("text is parsed");
		try {
		    startState.parse(emptyList);
			fail("should not parse - improper text for state " + startState);
		} catch (ParseException e) {			
		}

		Then("current state is still 'Start'");
		assertEquals(startState, startState.getNextState());
	}
	
    @Scenario
    public void shouldCreateTwoStoryModelsForTextWithTwoStories() {
        Given("text with 2 stories: test1 and test2");
        String text = "Story: test1\nStory: test2";
        startState.setReader(new LineReader(text));
        
        When("text is parsed");
        startState.parse(emptyList);

        Then("two StoryModels with proper names are created");
        assertEquals(2, emptyList.size());
        assertEquals("test1", emptyList.get(0).name());
        assertEquals("test2", emptyList.get(1).name());
    }   
    
    @Scenario   
    public void shouldTransitionToScenarioStateForScenarioWithinStoryInText() {
        Given("text with story with one scenario");
        String text = "Story: test story\nScenario: test scenario";
        startState.setReader(new LineReader(text));
        
        When("text is parsed");
        startState.parse(emptyList);

        Then("story with scenario has been created");
        assertEquals("test story", emptyList.get(0).name());
        assertEquals("test scenario", emptyList.get(0).scenarios().get(0).name());
    }
}
