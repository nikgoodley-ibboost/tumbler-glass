package tumbler.internal.parsers.gherkin;

import static org.junit.Assert.*;
import static tumbler.Tumbler.*;

import org.junit.runner.*;

import tumbler.*;

@RunWith(TumblerRunner.class)
public class GherkinLoaderScenarios {
    private GherkinLoader loader = new GherkinLoader();
    
    @Scenario
    public void shouldCreateStoryForProperText() {
        Given("text 'Story: test'");
        String text = "Story: test";

        When("text is loaded");
        loader.loadFrom(text);

        Then("story is created");
        assertEquals("test", loader.stories().get(0).name());
    }
    
    @Scenario
    public void shouldCreateGivenStep() {
        Given("text 'Story: test\nScenario: test\nGiven one'");
        String text = "Story: test\nScenario: test\nGiven one";
        
        When("text is loaded");
        loader.loadFrom(text);
        
        Then("given step is created");
        assertEquals("one", loader.stories().get(0).scenarios().get(0).steps().get(0).text());
    }
    
    @Scenario
    public void shouldCreateGivenAndStep() {
        Given("text 'Story: test\nScenario: test\nGiven one\nAnd two'");
        String text = "Story: test\nScenario: test\nGiven one\nAnd two";
        
        When("text is loaded");
        loader.loadFrom(text);
        
        Then("given step is created with embedded and");
        assertEquals("one and two", loader.stories().get(0).scenarios().get(0).steps().get(0).text());
    }
    
    @Scenario
    public void shouldCreateWhenAndStep() {
        Given("text 'Story: test\nScenario: test\nWhen one\nAnd two'");
        String text = "Story: test\nScenario: test\nWhen one\nAnd two";
        
        When("text is loaded");
        loader.loadFrom(text);
        
        Then("when step is created with embedded and");
        assertEquals("one and two", loader.stories().get(0).scenarios().get(0).steps().get(0).text());
    }
    
    @Scenario
    public void shouldCreateThenAndButStep() {
        Given("text 'Story: test\nScenario: test\nThen one\nAnd two\nBut three'");
        String text = "Story: test\nScenario: test\nThen one\nAnd two\nBut three";
        
        When("text is loaded");
        loader.loadFrom(text);
        
        Then("then step is created with embedded 'and' and 'but' ");
        assertEquals("one and two but three", loader.stories().get(0).scenarios().get(0).steps().get(0).text());
    }
    
    @Scenario
    public void shouldDescribeScenarioWithAsNarrative() {
        Given("text 'Story: test\nAs a tester");
        String text = "Story: test\nAs a tester";
        
        When("text is loaded");
        loader.loadFrom(text);
        
        Then("story is annotated with 'as a tester' narrative");
        assertEquals("a tester", loader.stories().get(0).as());
    }
    
    @Scenario
    public void shouldDescribeScenarioWithInOrderNarrative() {
        Given("text 'Story: test\nIn order to do something");
        String text = "Story: test\nIn order to do something";
        
        When("text is loaded");
        loader.loadFrom(text);
        
        Then("story is annotated with 'In order to do something' narrative");
        assertEquals("to do something", loader.stories().get(0).inOrder());
    }
    
    @Scenario
    public void shouldDescribeScenarioWithIWantNarrative() {
        Given("text 'Story: test\nI want to do something");
        String text = "Story: test\nI want to do something";
        
        When("text is loaded");
        loader.loadFrom(text);
        
        Then("story is annotated with 'I want to do something' narrative");
        assertEquals("to do something", loader.stories().get(0).iWant());
    }
    
    @Scenario
    public void shouldDescribeScenarioWithSoThatNarrative() {
        Given("text 'Story: test\nSo that something");
        String text = "Story: test\nSo that something";
        
        When("text is loaded");
        loader.loadFrom(text);
        
        Then("story is annotated with 'So that something' narrative");
        assertEquals("something", loader.stories().get(0).soThat());
    }
}
