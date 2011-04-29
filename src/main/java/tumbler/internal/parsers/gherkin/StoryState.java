package tumbler.internal.parsers.gherkin;

import static tumbler.internal.parsers.gherkin.Keyword.*;

import java.util.*;

import tumbler.internal.domain.*;

public class StoryState extends ParsingState {

    public StoryState() {
        stateTransitionMap.put(new ScenarioParser(), new ScenarioState());
        stateTransitionMap.put(new PlainLineParser().withToken(keyword("In_order")).producingModel(StepBasedModel.class), new ParsingState());
        stateTransitionMap.put(new PlainLineParser().withToken(keyword("As")).producingModel(StepBasedModel.class), new ParsingState());
        stateTransitionMap.put(new PlainLineParser().withToken(keyword("I_want")).producingModel(StepBasedModel.class), new ParsingState());
        stateTransitionMap.put(new PlainLineParser().withToken(keyword("So_that")).producingModel(StepBasedModel.class), new ParsingState());
    }

    @Override
    protected void update(Object parent, Object child) {
        if (parent instanceof List) {
            List scenarios = (List) parent;
            if (child instanceof ScenarioModel) {
                ScenarioModel scenario = (ScenarioModel) child;
                if (scenarios.contains(scenario))
                    throw new IllegalStateException("Two elements with the same name: " + child.toString());
                else
                    scenarios.add(scenario);
            }
        } else {
            StoryModel story = (StoryModel) parent;
            if (child instanceof ScenarioModel) {
                ScenarioModel scenario = (ScenarioModel) child;
                scenario.withStory(story);
                if (story != null) {
                    if (story.scenarios().contains(scenario))
                        throw new IllegalStateException("Two elements with the same name: " + child.toString());
                    else
                        story.scenarios().add(scenario);
                }
            } else if (child instanceof StepBasedModel) {
                StepBasedModel step = (StepBasedModel) child;
                if (story != null)
                    story.withNarrativeStep(step);
            }
        }
    }
}
