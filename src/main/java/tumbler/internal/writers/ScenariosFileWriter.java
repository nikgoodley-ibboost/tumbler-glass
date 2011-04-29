package tumbler.internal.writers;

import java.util.*;

import tumbler.internal.domain.*;

public class ScenariosFileWriter extends StoryFileWriter {

	public static final String FILE_EXTENSION = "scenarios";

	public ScenariosFileWriter() {
		fileExtension = "." + FILE_EXTENSION;
		tocFile = "Stories.toc";
	}

	@Override
	String textFor(StoryModel story) {
		String result = appendStory(story);		
		result = appendNarrative(story, result);		
		result = appendScenarios(story, result);		
		return result;
	}

    private String appendStory(StoryModel story) {
        return "Story: " + story.name() + "\n";
    }

    private String appendScenarios(StoryModel story, String result) {
        for (ScenarioModel scenario : story.scenarios())
			result += textForScenario(scenario) + "\n";
        return result;
    }

    private String appendNarrative(StoryModel story, String result) {
        if (story.hasNarrative())
		    result += "\t" + story.narrative() + "\n\n";
        return result;
    }

	private String textForScenario(ScenarioModel scenario) {
		String scenarioStatus = getStatusAsString(scenario);
		String result = "Scenario: " + scenario.name() + scenarioStatus + "\n";
		if (scenario.given() != null)
			result += "\tGiven " + scenario.given() + "\n";
		if (scenario.when() != null)
			result += "\tWhen " + scenario.when() + "\n";
		if (scenario.then() != null)
			result += "\tThen " + scenario.then() + "\n";
		return result;
	}

	private String getStatusAsString(ScenarioModel scenario) {
		return (scenario.status() != null) ? " \t[" + scenario.status() + "]" : "";
	}

	@Override
	public String tocFor(List<StoryModel> storiesList) {
		StringBuilder sb = new StringBuilder();
		appendStories(storiesList, sb);
		return sb.toString();
	}

	private void appendStories(List<StoryModel> storiesList, StringBuilder sb) {
		for (StoryModel story : storiesList)
			appendScenarios(sb, story);
	}

	private void appendScenarios(StringBuilder sb, StoryModel story) {
		sb.append(story.name() + " should:\n");
		for (ScenarioModel scenario : story.scenarios())
			sb.append("\t" + scenario.name() + "\n");
		sb.append("\n");
	}
}
