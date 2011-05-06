package tumbler;

import static tumbler.internal.TumblerUtils.*;

import java.lang.reflect.*;
import java.util.*;

import javax.lang.model.type.*;

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
    private HashMap<FrameworkMethod, Integer> paramSetIndexMap;

    public TumblerRunner(Class<?> klass) throws InitializationError {
        super(klass);
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
        Statement methodInvoker = methodBlock(method);

        if (isParametrised(method))
            runParameterisedScenario(isPending(method), method, notifier, methodInvoker);
        else
            runScenario(isPending(method), method, notifier, methodInvoker);
    }

    private boolean isPending(FrameworkMethod method) {
        return method.getAnnotation(Scenario.class).pending();
    }

    private void runScenario(boolean pending, FrameworkMethod method, RunNotifier notifier, Statement methodInvoker) {
        Description description = describeChild(method);
        runMethodInvoker(pending, notifier, description, methodInvoker, description);
    }

    private void runParameterisedScenario(boolean pending, FrameworkMethod method, RunNotifier notifier, Statement methodInvoker) {
        String[] params = paramsFromAnnotation(method);

        Description methodDescription = Description.createSuiteDescription(testName(method));
        for (String paramSet : params)
            methodDescription.addChild(Description.createTestDescription(getTestClass().getJavaClass(),
                    paramSet + " (" + testName(method) + ")", method.getAnnotations()));

        Description methodWithParams = findChildForParams(methodInvoker, methodDescription);

        if (!pending)
            notifier.fireTestStarted(methodWithParams);

        runMethodInvoker(pending, notifier, methodDescription, methodInvoker, methodWithParams);

        if (pending)
            notifier.fireTestIgnored(methodWithParams);
        else
            notifier.fireTestFinished(methodWithParams);
    }

    private String[] paramsFromAnnotation(FrameworkMethod method) {
        Parameters parametersAnnotation = method.getAnnotation(Parameters.class);
        String[] params = parametersAnnotation.value();
        if (params.length == 0 && !(parametersAnnotation.source().isAssignableFrom(NullType.class)))
            params = paramsFromProvider(parametersAnnotation.source());
        return params;
    }

    private String[] paramsFromProvider(Class sourceClass) {
        ArrayList<String> result = new ArrayList<String>();
        Method[] methods = sourceClass.getDeclaredMethods();

        for (Method method : methods) {
            if (method.getName().startsWith("provide")) {
                if (!Modifier.isStatic(method.getModifiers()))
                    throw new RuntimeException("Parameters source method " +
                            method.getName() +
                            " is not declared as static. Modify it to a static method.");
                try {
                    result.addAll(Arrays.asList((String[]) method.invoke(null)));
                } catch (Exception e) {
                    throw new RuntimeException("Cannot invoke parameters source method: " + method.getName(), e);
                }
            }
        }

        if (result.isEmpty())
            throw new RuntimeException("No methods starting with provide or they return no result in the parameters source class: "
                    + sourceClass.getName());
        return result.toArray(new String[] {});
    }

    private Description findChildForParams(Statement methodInvoker, Description methodDescription) {
        for (Description child : methodDescription.getChildren()) {
            InvokeParameterisedMethod parameterisedInvoker = findParameterisedMethodInvokerInChain(methodInvoker);

            if (child.getMethodName().startsWith(parameterisedInvoker.getParamsAsString()))
                return child;
        }
        return null;
    }

    private InvokeParameterisedMethod findParameterisedMethodInvokerInChain(Statement methodInvoker) {
        while (methodInvoker != null && !(methodInvoker instanceof InvokeParameterisedMethod))
            methodInvoker = nextChainedInvoker(methodInvoker);

        if (methodInvoker == null)
            throw new RuntimeException("Cannot find invoker for the parameterised method. Using wrong JUnit version?");

        return (InvokeParameterisedMethod) methodInvoker;
    }

    private Statement nextChainedInvoker(Statement methodInvoker) {
        try {
            Field methodInvokerField = methodInvoker.getClass().getDeclaredField("fNext");
            methodInvokerField.setAccessible(true);
            return (Statement) methodInvokerField.get(methodInvoker);
        } catch (Exception e) {
            return null;
        }
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
        Description description = Description.createSuiteDescription(getName(),
                getTestClass().getAnnotations());

        List<FrameworkMethod> resultMethods = new ArrayList<FrameworkMethod>();
        addScenarioMethods(getTestClass().getAnnotatedMethods(Scenario.class), resultMethods, true);

        for (FrameworkMethod child : resultMethods) {
            if (isParametrised(child)) {
                Description parametrised = Description.createSuiteDescription(testName(child));

                String[] params = paramsFromAnnotation(child);

                for (String paramSet : params)
                    parametrised.addChild(Description.createTestDescription(getTestClass().getJavaClass(),
                            paramSet + " (" + testName(child) + ")", child.getAnnotations()));

                description.addChild(parametrised);
            } else {
                description.addChild(describeChild(child));
            }
        }
        return description;
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

        addScenarioMethods(getTestClass().getAnnotatedMethods(Scenario.class), resultMethods, false);

        return resultMethods;
    }

    private void addScenarioMethods(List<FrameworkMethod> scenarios, List<FrameworkMethod> resultMethods, boolean flat) {
        paramSetIndexMap = new HashMap<FrameworkMethod, Integer>();
        for (FrameworkMethod scenarioMethod : scenarios) {
            if (isParametrised(scenarioMethod) && !flat)
                addScenarioForEachParamSet(resultMethods, scenarioMethod);
            else
                addScenarioOnce(resultMethods, scenarioMethod);
        }
    }

    private boolean isParametrised(FrameworkMethod method) {
        return method.getMethod().isAnnotationPresent(Parameters.class);
    }

    private void addScenarioForEachParamSet(List<FrameworkMethod> resultMethods, FrameworkMethod scenarioMethod) {
        String[] paramSets = paramsFromAnnotation(scenarioMethod);

        for (int i = 0; i < paramSets.length; i++)
            addScenarioOnce(resultMethods, scenarioMethod);

        paramSetIndexMap.put(scenarioMethod, 0);
    }

    private void addScenarioOnce(List<FrameworkMethod> resultMethods, FrameworkMethod scenarioMethod) {
        resultMethods.add(scenarioMethod);
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
        if (isParametrised(method)) {
            Integer counter = paramSetIndexMap.get(method);
            paramSetIndexMap.put(method, counter + 1);
            return new InvokeParameterisedMethod(method, test, paramsFromAnnotation(method)[counter]);
        } else {
            return super.methodInvoker(method, test);
        }
    }
}
