package tumbler;

import static tumbler.internal.TumblerUtils.*;

import java.lang.reflect.*;
import java.util.*;

import org.junit.internal.runners.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;

import tumbler.internal.*;

/**
 * JUnit4.4 runner. Should be used EXCLUSIVELY with JUnit 4.4. 
 * It's functionally equivalent to {@link TumblerRunner}, so look there for description.
 * 
 * @author Pawel Lipinski
 */
@SuppressWarnings("deprecation")
public class JUnit44TumblerRunner extends JUnit4ClassRunner {

	private ScenarioListener scenarioListener;
	private Result result;
	private RunListener runListener;

	public JUnit44TumblerRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}
	
	protected void validate() throws InitializationError {
		MethodValidator methodValidator= new MethodValidator(getTestClass()) {
			public List<Throwable> validateMethodsForDefaultRunner() {
				validateNoArgConstructor();
				validateStaticMethods();				
				return null;
			}
		};
		methodValidator.validateMethodsForDefaultRunner();
		methodValidator.assertValid();
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
	protected void invokeTestMethod(Method method, RunNotifier notifier) {
		super.invokeTestMethod(method, notifier);
		if (method.getAnnotation(Scenario.class).pending())
			notifier.fireTestIgnored(describeChild(method));
	};

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
	protected java.util.List<Method> getTestMethods() {
		List<Method> testMethods = super.getTestMethods();
		if (! testMethods.isEmpty())
			throw new RuntimeException("Class " + super.getName() + " is a Tumbler story. It should not use @Test annotation (but it did!) \n Remove @Test annotation from: " + testMethods);
		
		return getTestClass().getAnnotatedMethods(Scenario.class);
	};

	protected Description describeChild(Method method) {
		return Description.createTestDescription(getTestClass().getJavaClass(),
				testName(method), method.getAnnotations());
	}
	
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
	protected String testName(Method method) {
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
		return sentence.substring(sentence.indexOf(" "));
	}
}
