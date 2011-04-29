package tumbler.internal.parsers.gherkin;

import java.util.*;

import tumbler.internal.domain.*;

public class GherkinLoader {
    private StartState startState = new StartState();    
    private List<StoryModel> stories = new ArrayList<StoryModel>();

    public void loadFrom(String text) {
        startState.setReader(new LineReader(text));
        startState.parse(stories);
        if (stories.isEmpty())
            throw new IllegalArgumentException("No stories in file.");
    }

    public List<StoryModel> stories() {
        if (stories == null || stories.isEmpty())
            throw new IllegalStateException("No stories loaded. Did you try to load some file?");
        return stories;
    }
}
