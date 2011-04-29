package tumbler.internal.domain;

import static org.junit.Assert.*;
import static tumbler.Tumbler.*;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;

import tumbler.*;
import tumbler.internal.parsers.*;

@RunWith(TumblerRunner.class)
public class StoryModelScenarios {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Scenario
    public void shouldThrowParseExceptionWhenNarrativeFormatWrong() {
        String narrative = "something";
        
        Given("narrative: " + narrative);
        StoryModel story = new StoryModel();
        
        When("the narrative is set on a story");
        Then("ParseException is thrown");
        thrown.expect(ParseException.class);
        story.withNarrative(narrative);
    }
    
	@Scenario
	public void shouldCreateNarrativeStepsBasedOnNarrativeString() {
	    String as = "a tester";
	    String inOrder = "to have the application tested";
	    String iWant = "to test";
	    String soThat = "the application is tested";
	    String narrative = "As " + as + ", In order " + inOrder + ", I want " + iWant + ", So that " + soThat;

	    Given("narrative: " + narrative);
		StoryModel story = new StoryModel();

		When("the narrative is set on a story");
		story.withNarrative(narrative);

		Then("the story has 4 narrative steps, one for each narrative phrase (As a, In order to, I want, So that) and narrative produced by the story is equal to the given one");
		assertEquals(as, story.as());
        assertEquals(inOrder, story.inOrder());
        assertEquals(iWant, story.iWant());
        assertEquals(soThat, story.soThat());
        assertEquals(narrative, story.narrative());        
	}

}
