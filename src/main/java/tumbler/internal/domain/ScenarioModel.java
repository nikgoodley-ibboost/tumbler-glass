package tumbler.internal.domain;

import java.util.*;

import org.junit.runner.*;

public class ScenarioModel {

    public enum ScenarioStatus {
        PASSED("Scenario passed"), FAILED(new Exception("")), PENDING("Not implemented yet");

        private Object details;

        ScenarioStatus(Object details) {
            this.details = details;
        }

        public Object details() {
            return details;
        }

        public ScenarioStatus withDetails(Object details) {
            this.details = details;
            return this;
        }
    }

    private String name;
    private List<StepBasedModel> steps = new ArrayList<StepBasedModel>();
    private StoryModel story;
    private ScenarioStatus status;
    private Description description;

    public ScenarioModel(String name) {
        this.name = name.trim();
    }

    public String name() {
        return name;
    }

    public String given() {
        if (givenStep() == null)
            return "";
        return processStory(givenStep().text());
    }

    public GivenModel givenStep() {
        return stepOfType(GivenModel.class);
    }

    public String when() {
        if (whenStep() == null)
            return "";
        return processStory(whenStep().text());
    }

    private StepBasedModel whenStep() {
        return stepOfType(WhenModel.class);
    }

    public String then() {
        if (thenStep() == null)
            return "";
        return processStory(thenStep().text());
    }

    private StepBasedModel thenStep() {
        return stepOfType(ThenModel.class);
    }

    @SuppressWarnings("unchecked")
    private <T extends StepBasedModel> T stepOfType(Class<T> stepClass) {
        for (StepBasedModel step : steps)
            if (step.getClass().equals(stepClass))
                return (T) step;
        return null;
    }

    private String processStory(String text) {
        if (text == null)
            return "";

        return text;
    }

    public ScenarioModel withName(String name) {
        this.name = name.trim();
        return this;
    }

    public ScenarioModel withGiven(String given) {
        if (givenStep() == null)
            steps.add(new GivenModel(given));
        return this;
    }

    public ScenarioModel withWhen(String when) {
        if (whenStep() == null)
            steps.add(new WhenModel(when));
        return this;
    }

    public ScenarioModel withThen(String then) {
        if (thenStep() == null)
            steps.add(new ThenModel(then));
        return this;
    }

    public ScenarioModel withStatus(ScenarioStatus status) {
        this.status = status;
        return this;
    }

    public ScenarioStatus status() {
        return status;
    }

    @Override
    public boolean equals(Object obj) {
        ScenarioModel other = (ScenarioModel) obj;
        if (other == null)
            return false;
        if ((other.name == null && name != null) || (name == null && other.name != null))
            return false;
        if (other.name.toLowerCase().equals(name.toLowerCase()))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    public ScenarioModel withDescription(Description description) {
        this.description = description;
        return this;
    }

    public Description description() {
        return description;
    }

    public List<StepBasedModel> steps() {
        return steps;
    }

    public StoryModel story() {
        return story;
    }

    public ScenarioModel withStory(StoryModel story) {
        this.story = story;
        return this;
    }
}
