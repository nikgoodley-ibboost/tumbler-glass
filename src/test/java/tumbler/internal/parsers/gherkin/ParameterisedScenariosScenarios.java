package tumbler.internal.parsers.gherkin;

import static org.junit.Assert.*;
import static tumbler.Tumbler.*;

import org.junit.runner.*;

import tumbler.*;
import tumbler.internal.domain.*;

@RunWith(TumblerRunner.class)
public class ParameterisedScenariosScenarios {
    private ScenarioState scenarioState = new ScenarioState();
    
    @Scenario
    public void shouldReadPramsHeaderAndValue() {
        Given("'Given' step ending with a colon and");
        String text = "Given the following:\n|header|\n|value|";
        scenarioState.setReader(new LineReader(text));
        ScenarioModel scenario = new ScenarioModel("");

        When("text is loaded");
        scenarioState.parse(scenario);

        Then("table in GivenState has a header and a value");
        assertEquals("header", ((GivenModel)scenario.steps().get(0)).table().header().column(0));
        assertEquals("value", ((GivenModel)scenario.steps().get(0)).table().rows().get(0).column(0));
    }
    
    @Scenario
    @Parameters({"|val1|1|2|3|1.1|2.2|true|a|0|", "|val1|1|2|3|2.1|2.2|true|a|0|", "|val1|1|2|3|1.2|2.2|true|a|0|"})
    public void shouldHandleAllPrimitiveTypes(String string, int integer, short schort, long loong, float real1, double real2, boolean bool, char character, byte bite) {       
        assertEquals("val1", string);
        assertEquals(1, integer);
        assertEquals(2, schort);
        assertEquals(3, loong);
        assertEquals(1.1, real1, 1);
        assertEquals(2.2, real2, 0.1);
        assertTrue(bool);
        assertEquals('a', character);
        assertEquals(0, bite);
    }
    
    @Scenario(pending = true)
    public void shouldReadPramsHeaderAndValueForPending() {
        Given("'Given' step ending with a colon and");
        String text = "Given the following:\n|header|\n|value|";
        scenarioState.setReader(new LineReader(text));
        ScenarioModel scenario = new ScenarioModel("");
        
        When("text is loaded");
        scenarioState.parse(scenario);
        
        Then("table in GivenState has a header and a value");
        assertEquals("header", ((GivenModel)scenario.steps().get(0)).table().header().column(0));
        assertEquals("value", ((GivenModel)scenario.steps().get(0)).table().rows().get(0).column(0));
    }
    
    @Scenario(pending = true)
    @Parameters({"|val1|1|2|3|1.1|2.2|true|a|0|", "|val1|1|2|3|2.1|2.2|true|a|0|", "|val1|1|2|3|1.2|2.2|true|a|0|"})
    public void shouldHandleAllPrimitiveTypesForPending(String string, int integer, short schort, long loong, float real1, double real2, boolean bool, char character, byte bite) {       
        assertEquals("val1", string);
        assertEquals(1, integer);
        assertEquals(2, schort);
        assertEquals(3, loong);
        assertEquals(1.1, real1, 1);
        assertEquals(2.2, real2, 0.1);
        assertTrue(bool);
        assertEquals('a', character);
        assertEquals(0, bite);
    }
}
