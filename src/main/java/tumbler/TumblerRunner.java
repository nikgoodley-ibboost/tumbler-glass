package tumbler;

import static tumbler.internal.TumblerUtils.*;

import java.util.*;

import junitparams.*;

import org.junit.*;
import org.junit.internal.runners.model.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;
import org.junit.runners.*;
import org.junit.runners.model.*;

import tumbler.internal.*;

/**
 * JUnit runner. Represents stories and scenarios as sentences in JUnit GUIs.
 * May also generate scenarios' statuses report in .scenarios or html format if
 * <code>-DgenerateReport=[scenario|html]</code> is passed while running
 * scenarios. The output folder for the report can be configured by setting
 * <code>-DoutputFolder</code> property.
 * 
 * If -DcamelCase is passed, IDEs will show class and method names instead of
 * scenario and story names.
 * 
 * @author Pawel Lipinski
 */
public class TumblerRunner extends BlockJUnit4ClassRunner {

    private ScenarioListener scenarioListener;
    private Result result;
    private RunListener runListener;
    private ParameterisedTestClassRunner parameterisedRunner = new ParameterisedTestClassRunner();

    public TumblerRunner(Class<?> klass) throws InitializationError {
        super(klass);
        computeTestMethods();
    }

    protected void collectInitializationErrors(List<Throwable> errors) {
    }

    @Override
    public void run(RunNotifier notifier) {
        setupScenariosListener(notifier);
        if (getTestClass().getJavaClass().getPackage() != null)
            scenarioListener.getStory().withPackage(getTestClass().getJavaClass().getPackage().getName());

        super.run(notifier);
        removeScenariosListener(notifier);
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        if (parameterisedRunner.isParameterised(method)) {
            if (method.getAnnotation(Ignore.class) != null) {
                Description ignoredMethod = parameterisedRunner.describeParameterisedMethod(method);
                for (Description child : ignoredMethod.getChildren()) {
                    notifier.fireTestIgnored(child);
                }
                return;
            }
            parameterisedRunner.runParameterisedTest(method, methodBlock(method), notifier);
        } else
            runScenario(isPending(method), method, notifier, methodBlock(method));
    }

    private boolean isPending(FrameworkMethod method) {
        return method.getAnnotation(Scenario.class).pending();
    }

    private void runScenario(boolean pending, FrameworkMethod method, RunNotifier notifier, Statement methodInvoker) {
        Description description = describeChild(method);
        runMethodInvoker(pending, notifier, description, methodInvoker, description);
    }

    private void runMethodInvoker(boolean pending, RunNotifier notifier, Description description, Statement methodInvoker,
            Description methodWithParams) {
        EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
        try {
            if (pending)
                scenarioListener.testStarted(description);
            else
                eachNotifier.fireTestStarted();

            methodInvoker.evaluate();
        } catch (Throwable e) {
            if (!pending)
                notifier.fireTestFailure(new Failure(methodWithParams, e));
        } finally {
            eachNotifier.fireTestFinished();
        }
        if (pending)
            eachNotifier.fireTestIgnored();
    };

    @Override
    public Description getDescription() {
        Description description = Description.createSuiteDescription(getName(), getTestClass().getAnnotations());

        List<FrameworkMethod> resultMethods = new ArrayList<FrameworkMethod>();
        resultMethods.addAll(parameterisedRunner.computeTestMethods(getTestClass().getAnnotatedMethods(Scenario.class), true));

        for (FrameworkMethod child : resultMethods) {
            Description describeMethod = describeMethod(child);
            description.addChild(describeMethod);
        }

        return description;
    }

    private Description describeMethod(FrameworkMethod method) {
        Description child = parameterisedRunner.describeParameterisedMethod(method);

        if (child == null)
            child = describeChild(method);

        return child;
    }

    private void removeScenariosListener(RunNotifier notifier) {
        notifier.fireTestRunFinished(result);

        notifier.removeListener(scenarioListener);
        notifier.removeListener(runListener);
    }

    private void setupScenariosListener(RunNotifier notifier) {
        result = new Result();
        runListener = result.createListener();
        notifier.addFirstListener(runListener);

        scenarioListener = new ScenarioListener();
        notifier.addListener(scenarioListener);
        notifier.fireTestRunStarted(getDescription());
    };

    @Override
    protected java.util.List<FrameworkMethod> computeTestMethods() {
        ensureNothingAnnotatedWithTest(super.computeTestMethods());
        List<FrameworkMethod> resultMethods = new ArrayList<FrameworkMethod>();

        resultMethods.addAll(parameterisedRunner.computeTestMethods(getTestClass().getAnnotatedMethods(Scenario.class), false));

        for (FrameworkMethod child : resultMethods) {
            Description describeMethod = describeMethod(child);
        }

        return resultMethods;
    }

    private void ensureNothingAnnotatedWithTest(List<FrameworkMethod> testMethods) {
        if (!testMethods.isEmpty())
            throw new RuntimeException("Class " + super.getName()
                    + " is a Tumbler story. It should not use @Test annotation (but it did!) \n Remove @Test annotation from: "
                    + testMethods);
    };

    @Override
    protected String getName() {
        if (shouldSkipNameResolution())
            return super.getName();

        String name = super.getName();
        name = name.substring(name.lastIndexOf('.') + 1);
        if (name.endsWith("Test"))
            name = name.substring(0, name.lastIndexOf("Test"));
        if (name.endsWith("Scenarios"))
            name = name.substring(0, name.lastIndexOf("Scenarios"));
        return decamelise(name) + " should";
    };

    @Override
    protected String testName(FrameworkMethod method) {
        if (shouldSkipNameResolution())
            return super.testName(method);

        String scenarioName = decamelise(super.testName(method));
        String lowerCaseScenario = scenarioName.toLowerCase();
        if (lowerCaseScenario.startsWith("should") || lowerCaseScenario.startsWith("test"))
            scenarioName = skipFirstWord(scenarioName);

        return scenarioName;
    }

    private boolean shouldSkipNameResolution() {
        return System.getProperty("camelCase") != null;
    }

    private String skipFirstWord(String sentence) {
        int indexOfFirstWord = sentence.indexOf(" ");

        if (indexOfFirstWord > -1)
            return sentence.substring(indexOfFirstWord);
        else
            return sentence;
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        Statement methodInvoker = parameterisedRunner.parameterisedMethodInvoker(method, test);
        if (methodInvoker == null)
            methodInvoker = super.methodInvoker(method, test);

        return methodInvoker;
    }

    public static Object[] $(Object... params) {
        return params;
    }
}
