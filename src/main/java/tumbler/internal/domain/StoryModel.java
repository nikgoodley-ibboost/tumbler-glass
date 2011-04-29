package tumbler.internal.domain;

import java.io.*;
import java.util.*;

import org.junit.runner.*;

import tumbler.internal.*;
import tumbler.internal.domain.ScenarioModel.ScenarioStatus;
import tumbler.internal.parsers.*;

public class StoryModel implements WithText {
    private String SO_THAT = "So that";
    private String I_WANT = "I want";
    private String IN_ORDER = "In order";
    private String AS = "As";
    private List<String> phrases = Arrays.asList(AS, IN_ORDER, I_WANT, SO_THAT);

    private String name;
    private List<ScenarioModel> scenarios = new ArrayList<ScenarioModel>();
    private String packageName = "";
    private Map<String, String> steps = new HashMap<String, String>();

    public StoryModel() {
    }

    public StoryModel(String name) {
        this.name = name.trim();
    }

    public String name() {
        return name;
    }

    public StoryModel withPackage(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public List<ScenarioModel> scenarios() {
        return scenarios;
    }

    public String asClassPath() {
        if (!"".equals(packageName))
            return packageName.replace('.', '/') + "/" + camelisedName();
        else
            return camelisedName();
    }

    public void addScenario(ScenarioModel scenario) {
        if (scenarios.contains(scenario))
            throw new ParseException("Two scenarios with the same name: " + scenario.name());        

        scenarios.add(scenario);
    }

    public ScenarioStatus status() {
        for (ScenarioModel scenario : scenarios) {
            if (scenario.status() != ScenarioStatus.PASSED)
                return scenario.status();
        }
        return ScenarioStatus.PASSED;
    }

    public static StoryModel createFrom(String firstLine) throws IOException {
        StoryModel story = new StoryModel();
        story.parseNameFrom(firstLine);
        return story;
    }

    private void parseNameFrom(String firstLine) throws IOException {
        checkIfLineContainsStory(firstLine);
        String storyName = retrieveStoryFromLine(firstLine);
        checkIfStoryNotEmpty(storyName);
        name = storyName;
    }

    private void checkIfStoryNotEmpty(String storyName) {
        if (storyName.isEmpty())
            throw new IllegalArgumentException("Story is empty");
    }

    private String retrieveStoryFromLine(String firstLine) {
        return firstLine.trim().substring(firstLine.indexOf(':') + 1).trim();
    }

    private void checkIfLineContainsStory(String firstLine) {
        if (firstLine == null
                || firstLine.trim().isEmpty()
                || !(
                    firstLine.trim().startsWith("Story:") || firstLine.trim().startsWith("Feature:")
                ))
            throw new IllegalArgumentException("Scenarios have no story");
    }

    public String camelisedName() {
        String result = TumblerUtils.removeSpecialCharacters(name);
        return TumblerUtils.camelise(result);
    }

    @Override
    public boolean equals(Object obj) {
        StoryModel other = (StoryModel) obj;
        if (namesNotSame(other))
            return false;
        return (other.scenarios.containsAll(scenarios) && scenarios.containsAll(other.scenarios));
    }

    private boolean namesNotSame(StoryModel other) {
        return !((name == null && other.name == null) || name.equals(other.name));
    }

    @Override
    public String toString() {
        return "Story: " + name + "; Scenarios:" + scenarios;
    }

    public ScenarioModel scenarioDescribedBy(Description description) {
        for (ScenarioModel scenario : scenarios)
            if (scenario.description() == description)
                return scenario;
        return null;
    }

    public RunStatistics runStatistics() {
        int[] stats = new int[] { 0, 0, 0 };
        for (ScenarioModel scenario : scenarios)
            if (scenario.status() != null)
                stats[scenario.status().ordinal()]++;

        return RunStatistics.fromArray(stats);
    }

    public String packageName() {
        return packageName;
    }

    @Override
    public StoryModel withText(String text) {
        this.name = text;
        return this;
    }

    @Override
    public StoryModel withToken(String token) {
        return this;
    }

    public void withNarrativeStep(StepBasedModel step) {
        steps.put(step.token(), step.text());
    }

    public void withNarrative(String narrative) {
        String firstStep = findStepMatchingStartOf(narrative);                               
        int nextStepIndex = findNextStepIndex(narrative, firstStep);            

        setNarrativeStep(narrative, firstStep, firstStep.length(), nextStepIndex);
        
        if (nextStepIndex > -1)
            withNarrative(narrative.substring(nextStepIndex));
    }

    private String findStepMatchingStartOf(String narrative) {
        for (String phrase : phrases) {
            if (narrative.length() > phrase.length() && phrase.equals(narrative.substring(0, phrase.length()))) {
                return phrase;
            }
        }
        
        throwNarrativeFormatException();
        return null;
    }

    private int findNextStepIndex(String narrative, String currentStep) {
        narrative = narrative.substring(currentStep.length());
        for (int currentIndex = 0; currentIndex < narrative.length(); currentIndex++)
            for (String phrase : phrases)
                if (currentIndex + phrase.length() < narrative.length()
                        && phrase.equals(narrative.substring(currentIndex, currentIndex + phrase.length())))
                    return currentIndex + currentStep.length();

        return -1;
    }

    private void setNarrativeStep(String narrative, String currentStep, int start, int end) {
        withNarrativeStep(new StepBasedModel()
                        .withText(stepText(narrative, start, end))
                        .withToken(currentStep));       
    }

    private String stepText(String narrative, int start, int end) {
        String stepText;
        if (end > -1)
            stepText = narrative.substring(start, end).trim();
        else
            stepText = narrative.substring(start).trim();
        
        if (stepText.endsWith(".") || stepText.endsWith(","))
            stepText = stepText.substring(0, stepText.length() - 1);
        
        return stepText;
    }

    private void throwNarrativeFormatException() {
        throw new ParseException("\nThe narrative format is wrong.\n" +
                "It should be a composision of at least one of the following phrases, starting with a capital letter:\n" +
                "    - " + AS + " ...\n" +
                "    - " + IN_ORDER + " ...\n" +
                "    - " + I_WANT + " ...\n" +
                "    - " + SO_THAT + " ...\n" +
                "The phrases CAN be comma-separated, but each MUST start with a capital letter");
    }

    public String as() {
        return steps.get(AS);
    }

    public String inOrder() {
        return steps.get(IN_ORDER);
    }

    public String iWant() {
        return steps.get(I_WANT);
    }

    public String soThat() {
        return steps.get(SO_THAT);
    }

    public String narrative() {
        String narrative = "";
        if (as() != null)
            narrative += AS + " " + as();
        if (inOrder() != null) {
            narrative = addCommaTo(narrative);
            narrative += IN_ORDER + " " + inOrder();
        }
        if (iWant() != null) {
            narrative = addCommaTo(narrative);
            narrative += I_WANT + " " + iWant();
        }
        if (soThat() != null) {
            narrative = addCommaTo(narrative);
            narrative += SO_THAT + " " + soThat();
        }
        return narrative;
    }

    private String addCommaTo(String narrative) {
        if (!"".equals(narrative))
            narrative += ", ";
        return narrative;
    }

    public boolean hasNarrative() {        
        return ! "".equals(narrative());
    }
}
