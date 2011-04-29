package tumbler.internal.parsers.gherkin;


import static tumbler.internal.parsers.gherkin.Keyword.*;

import java.util.*;

import tumbler.internal.domain.*;


public class StartState extends ParsingState {
	public StartState() {
		stateTransitionMap.put(new PlainLineParser().withToken(keyword("Story")).producingModel(StoryModel.class), new StoryState());
		stateTransitionMap.put(new PlainLineParser().withToken(keyword("Feature")).producingModel(StoryModel.class), new StoryState());
	}
	
	@Override
	protected void update(Object parent, Object child) {
	    if (parent == null)
	        return;
	    List<StoryModel> stories = (List<StoryModel>) parent;
	    stories.add((StoryModel) child);
	}
}
