package tumbler.internal;

import static tumbler.internal.TumblerUtils.*;

import java.util.*;

import junitparams.*;

import org.junit.runner.*;
import org.junit.runner.notification.*;

import tumbler.*;
import tumbler.internal.domain.*;
import tumbler.internal.domain.ScenarioModel.ScenarioStatus;
import tumbler.internal.writers.*;

public class ScenarioListener extends RunListener {
    private static final String GENERATE_FILES = "generateReport";
    private StoryModel story;
    private static List<StoryModel> storiesList = new LinkedList<StoryModel>();
    private StoryFileWriter fileWriter;

    @Override
    public void testRunStarted(Description description) throws Exception {
        createStory(description);
    }

    private void createStory(Description description) {
        if (description.getAnnotation(Story.class) != null)
            createStoryFromAnnotation(description.getAnnotation(Story.class));
        else {
            createStoryFromDescription(description.getDisplayName());
        }
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        setScenariosStatuses(result);
        updateStoriesList();
        generateReport();
    }

    private void updateStoriesList() {
        storiesList.add(getStory());
    }

    private void generateReport() {
        fileWriter = null;
        if ("scenarios".equals(shouldGenerateFile()))
            fileWriter = new ScenariosFileWriter();
        else if ("html".equals(shouldGenerateFile()))
            fileWriter = new FreemarkerFileWriter();

        if (fileWriter != null) {
            fileWriter.setOutputFolder(System.getProperty("outputFolder"));
            fileWriter.write(story);
            fileWriter.writeToc(storiesList);
        }
    }

    private void setScenariosStatuses(Result result) {
        setAllFailedScenariosToStatusFailed(result);
        setAllPassedAndPendingScenariosToProperStatus();
    }

    private void setAllPassedAndPendingScenariosToProperStatus() {
        for (ScenarioModel scenario : story.scenarios()) {
            setStatusToPassedIfNotFailed(scenario);
            setStatusToPendingIfAnnotatedAsPending(scenario);
        }
    }

    private void setAllFailedScenariosToStatusFailed(Result result) {
        for (Failure failure : result.getFailures()) {
            story.scenarioDescribedBy(failure.getDescription()).withStatus(ScenarioStatus.FAILED.withDetails(failure.getException()));
        }
    }

    private void setStatusToPendingIfAnnotatedAsPending(ScenarioModel scenario) {
        Scenario scenarioAnnotation = scenario.description().getAnnotation(Scenario.class);
        if (scenarioAnnotation != null && scenarioAnnotation.pending())
            scenario.withStatus(ScenarioStatus.PENDING);
    }

    private void setStatusToPassedIfNotFailed(ScenarioModel scenario) {
        if (scenario.status() == null)
            scenario.withStatus(ScenarioStatus.PASSED);
    }

    private String shouldGenerateFile() {
        return System.getProperty(GENERATE_FILES);
    }

    @Override
    public void testStarted(Description description) throws Exception {
        if (isItSingleScenario(description)) {
            ScenarioManager.startScenario();
            ScenarioManager.currentScenario().withStory(story);
        }
    }

    private boolean isItSingleScenario(Description description) {
        try {
            return description.getMethodName() != null;
        } catch (NoSuchMethodError e) { // junit 4.5
            return description.getDisplayName() != null;
        }
    }

    @Override
    public void testFinished(Description description) throws Exception {
        if (isItSingleScenario(description)) {
            ScenarioModel namedScenario = namedScenario(description);
            story.addScenario(namedScenario);
        }
    }

    private ScenarioModel namedScenario(Description description) {
        Scenario scenarioAnnotation = description.getAnnotation(Scenario.class);
        Parameters parametersAnnotation = description.getAnnotation(Parameters.class);
        String name = null;

        if (scenarioHasDefinedName(scenarioAnnotation))
            name = scenarioAnnotation.value();
        else
            name = createScenarioNameFromTestMethodName(description, parametersAnnotation);

        if (parametersAnnotation != null)
            name = name.substring(name.indexOf('(') + 1, name.indexOf(')')) + " ("
                    + description.getMethodName().substring(0, description.getMethodName().indexOf('(') - 1) + ")";

        return ScenarioManager.currentScenario()
                .withName(name)
                .withDescription(description);
    }

    private String createScenarioNameFromTestMethodName(Description description, Parameters parametersAnnotation) {
        try {
            return decamelise(description.getMethodName());
        } catch (NoSuchMethodError e) { // junit 4.5
            return decamelise(description.getDisplayName());
        }
    }

    private boolean scenarioHasDefinedName(Scenario scenarioAnnotation) {
        return scenarioAnnotation != null && scenarioAnnotation.value() != null && !scenarioAnnotation.value().equals("");
    };

    void createStoryFromAnnotation(Story behaviours) {
        story = new StoryModel(behaviours.value());
    }

    void createStoryFromDescription(String className) {
        if (className.contains(" should")) {
            className = className.substring(0, className.indexOf(" should"));
        } else {
            className = removeSuffixFromClassName(className, "Test");
            className = removeSuffixFromClassName(className, "Scenarios");
            className = removePackage(className);
        }
        story = new StoryModel(TumblerUtils.decamelise(className));
    }

    private String removePackage(String className) {
        if (className.contains("."))
            return className.substring(className.lastIndexOf('.') + 1);
        return className;
    }

    private String removeSuffixFromClassName(String className, String suffix) {
        if (className.endsWith(suffix))
            className = className.substring(0, className.indexOf(suffix));
        return className;
    };

    public StoryModel getStory() {
        return story;
    }

    public List<StoryModel> storiesList() {
        return storiesList;
    }

    public StoryFileWriter getWriter() {
        return fileWriter;
    }
}