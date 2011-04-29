package tumbler.internal.writers;

import tumbler.internal.*;
import tumbler.internal.domain.*;

public class JavaGenerator {

	public static final String IMPORTS =
			"import org.junit.*;\n" +
			"import org.junit.runner.*;\n" +
			"import tumbler.*;\n" +
			"import static org.junit.Assert.*;\n" +
			"import static tumbler.Tumbler.*;";

	private String javaText;

	public void generateClassFor(StoryModel story) {
		javaText = IMPORTS + "\n\n@RunWith(TumblerRunner.class)\n@Story(\"" + story.name() + "\")\npublic class ";
		String className = story.camelisedName() + "Scenarios";
		javaText += className + " {\n";
		javaText += generateConstructor(story, className);
		for (ScenarioModel scenario : story.scenarios()) {
			javaText += generateMethodFor(scenario);
		}
		javaText += "}";
	}

	private String generateConstructor(StoryModel story, String className) {
        if (story.as() == null && story.inOrder() == null && story.iWant() == null && story.soThat() == null)
            return "";
        
        String constructor = "\tpublic " + className + "() {\n\t\tNarrative(\"" + story.narrative() + "\");\n\t}\n";
        
        return constructor;
    }

    private String generateMethodFor(ScenarioModel scenario) {
		String name = scenario.name();
		name = TumblerUtils.removeSpecialCharacters(name);
		String start = "\n\t@Scenario(";
		if (!name.equals(scenario.name()))
			start += "value = \"" + scenario.name() + "\", ";
		start += "pending = true)\n\tpublic void should" + TumblerUtils.camelise(name) + "() {\n";
		String ending = "\t}\n";
		String content = "\t\tGiven(\"" + scenario.given() + "\");\n\n\t\tWhen(\"" + scenario.when()
				+ "\");\n\n\t\tThen(\"" + scenario.then() + "\");\n";
		return start + content + ending;
	}

	public String javaText() {
		return javaText;
	}

}
