package tumbler.internal.writers;

import static org.junit.Assert.*;
import static tumbler.Tumbler.*;

import org.junit.runner.*;

import tumbler.*;
import tumbler.internal.domain.*;
import tumbler.internal.parsers.gherkin.*;

@RunWith(TumblerRunner.class)
public class JavaGeneratorScenarios {
    private static final String EMPTY_CLASS_START = JavaGenerator.IMPORTS
            + "\n\n@RunWith(TumblerRunner.class)\n@Story(\"some test\")\npublic class SomeTestScenarios {\n";
    private static final String EMPTY_CLASS_END = "}";
    private GherkinLoader loader = new GherkinLoader();
    private JavaGenerator generator = new JavaGenerator();

    @Scenario
    public void shouldGenerateOnlyClassWithProperNameWhenNoScenarios() {
        Given("text with story, without scenarios");
        String text = "Story: some test";
        loader.loadFrom(text);

        When("class is generated");
        generator.generateClassFor(loader.stories().get(0));

        Then("class should have no methods");
        assertEquals(EMPTY_CLASS_START + EMPTY_CLASS_END, generator.javaText());
    }

    @Scenario
    public void shouldGenerateClassWithTestWithProperNameAndStepsForSingleScenario() {
        Given("text with story and scenario with name 'perform something' and given when then steps");
        String text = "Story: some test\nScenario: perform something\nGiven something\nWhen something happens\nThen check something else";
        loader.loadFrom(text);

        When("class is generated");
        generator.generateClassFor(loader.stories().get(0));

        Then("class should have one method of name 'shouldPerformSomething' and proper steps");
        assertEquals(EMPTY_CLASS_START + method("PerformSomething") + EMPTY_CLASS_END, generator.javaText());
    }

    @Scenario
    public void shouldGenerateClassWithTestWithProperNameAndStepsForManyDifferentScenarios() {
        Given("text with story with two scenarios with different names and given when then steps");
        String text = "Story: some test\n" +
                "Scenario: perform first\nGiven something\nWhen something happens\nThen check something else\n" +
                "Scenario: perform second\nGiven something\nWhen something happens\nThen check something else";
        loader.loadFrom(text);

        When("class is generated");
        generator.generateClassFor(loader.stories().get(0));

        Then("class should have method of names 'shouldPerformFirst' 'shouldPerformSecond' and proper steps");
        assertEquals(EMPTY_CLASS_START + method("PerformFirst") + method("PerformSecond") + EMPTY_CLASS_END, generator.javaText());
    }

    @Scenario
    public void shouldGenerateGivenAndForAndInGiven() {
        Given("text with story with a scenario with Given and And steps");
        String text = "Story: some test\n" +
                "Scenario: perform first\nGiven something\nAnd something else";
        loader.loadFrom(text);

        When("class is generated");
        generator.generateClassFor(loader.stories().get(0));

        Then("method 'shouldPerformFirst' should have step Given(\"something and something else\")");
        assertTrue(generator.javaText().contains("Given(\"something and something else\");"));
    }

    private String method(String name) {
        String method = "\n\t@Scenario(pending = true)\n\tpublic void should" + name
                + "() {\n\t\tGiven(\"something\");\n\n\t\tWhen(\"something happens\");\n\n\t\tThen(\"check something else\");\n\t}\n";
        return method;
    }

    @Scenario
    public void shouldGenerateConstructorIfNarrativeIsPresent() {
        Given("text with story with a narrative");
        String text = "Story: some test\nAs a tester\nI want to test\nSo that the application is tested";
        loader.loadFrom(text);

        When("class is generated");
        generator.generateClassFor(loader.stories().get(0));

        Then("the class should contain a constructor with the Narrative call");
        assertEquals(
                EMPTY_CLASS_START
                        + "\tpublic SomeTestScenarios() {\n\t\tNarrative(\"As a tester, I want to test, So that the application is tested\");\n\t}\n"
                        + EMPTY_CLASS_END, generator.javaText());
    }

    @Scenario
    public void shouldSkipSpecialCharactersInStoryNamesWhileGeneratingJava() {
        Given("story with special characters");
        StoryModel story = new StoryModel("a .file_ '123'");

        When("java is generated");
        generator.generateClassFor(story);

        Then("story class has no special chars");
        assertTrue(generator.javaText().contains(" AFile_123S"));
    }

    @Scenario
    public void shouldSkipSpecialCharactersInScenarioNamesWhileGeneratingJava() {
        Given("Scenario with special characters");
        StoryModel story = new StoryModel("test");
        story.addScenario(new ScenarioModel("a .file_ '123'"));

        When("java is generated");
        generator.generateClassFor(story);

        Then("scenario method has no special chars");
        assertTrue(generator.javaText().contains("shouldAFile_123("));
    }

    @Scenario
    public void shouldGenerateValueInScenarioAnnotationWhenSpecialCharsSkipped() {
        Given("scenario with special characters");
        StoryModel story = new StoryModel("test");
        story.addScenario(new ScenarioModel("a .file_ '123'"));

        When("java is generated");
        generator.generateClassFor(story);

        Then("scenario method @Scenario annotation with original scenario name in value");
        assertTrue(generator.javaText().contains("@Scenario(value = \"a .file_ '123'\", pending = true)"));
    }

    @Scenario
    public void shouldHaveEmptyGivenWhenThenIfNotDefinedInScenariosFile() {
        Given("scenario with empty given when then");
        StoryModel story = new StoryModel("test");
        story.addScenario(new ScenarioModel("test"));

        When("java is generated");
        generator.generateClassFor(story);

        Then("there are empty given when then methods");
        assertTrue(generator.javaText().contains("Given(\"\")"));
    }

    @Scenario
    public void shouldGenerateMethodForParameterisedScenarioWithManyParamsets() {
        Given("text with story and scenario with parameters");
        String text = "Story: some test\nScenario: perform something\nGiven something:\n|A|B|\n|00|01|\n|10|11|\nWhen something happens\nThen check something else";
        loader.loadFrom(text);

        When("class is generated");
        generator.generateClassFor(loader.stories().get(0));

        Then("class should have a method with @Parameters annotation and method params");

        String method = "\n\t@Scenario(pending = true)\n" +
                "\t@Parameters({\n" +
                "\t\t\"|00|01|\",\n" +
                "\t\t\"|10|11|\"\n" +
                "\t})\n" +
                "\tpublic void shouldPerformSomething(String A, String B) {\n" +
                "\t\tGiven(\"something: \" +\n" +
                "\t\t\t\"|A|B|\" +\n" +
                "\t\t\t\"|\" + A + \"|\" + B + \"|\");\n\n" +
                "\t\tWhen(\"something happens\");\n\n" +
                "\t\tThen(\"check something else\");\n" +
                "\t}\n";
        assertEquals(EMPTY_CLASS_START + method + EMPTY_CLASS_END, generator.javaText());
    }

    @Scenario
    public void shouldGenerateMethodForParameterisedScenarioWithSingleParamset() {
        Given("text with story and scenario with parameters");
        String text = "Story: some test\nScenario: perform something\nGiven something:\n|col a|col b|\n|00|01|\nWhen something happens\nThen check something else";
        loader.loadFrom(text);

        When("class is generated");
        generator.generateClassFor(loader.stories().get(0));

        Then("class should have a method with @Parameters annotation and method params");

        String method = "\n\t@Scenario(pending = true)\n" +
                "\t@Parameters({\"|00|01|\"})\n" +
                "\tpublic void shouldPerformSomething(String col_a, String col_b) {\n" +
                "\t\tGiven(\"something: \" +\n" +
                "\t\t\t\"|col_a|col_b|\" +\n" +
                "\t\t\t\"|\" + col_a + \"|\" + col_b + \"|\");\n\n" +
                "\t\tWhen(\"something happens\");\n\n" +
                "\t\tThen(\"check something else\");\n" +
                "\t}\n";
        assertEquals(EMPTY_CLASS_START + method + EMPTY_CLASS_END, generator.javaText());
    }
}
