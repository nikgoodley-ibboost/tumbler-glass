package tumbler;

import org.junit.runners.model.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.statements.*;

/**
 * This is a Tumbler runner for Spring. If you want to have spring-aware Tumbler
 * scenarios, use this runner instead of the standard TumblerRunner.
 * 
 * You can use the same Spring annotations as with the SpringJUnit4ClassRunner,
 * while being able to take advantage of the whole Tumbler, including
 * parameters.
 * 
 * @author Pawel Lipinski
 * 
 */
public class SpringTumblerRunner extends TumblerRunner {

    private final TestContextManager testContextManager;

    public SpringTumblerRunner(Class<?> klass) throws InitializationError {
        super(klass);
        this.testContextManager = new TestContextManager(klass);
    }

    @Override
    protected Statement withBeforeClasses(Statement statement) {
        Statement junitBeforeClasses = super.withBeforeClasses(statement);
        return new RunBeforeTestClassCallbacks(junitBeforeClasses, testContextManager);
    }

    @Override
    protected Statement withAfterClasses(Statement statement) {
        Statement junitAfterClasses = super.withAfterClasses(statement);
        return new RunAfterTestClassCallbacks(junitAfterClasses, testContextManager);
    }

    @Override
    protected Object createTest() throws Exception {
        Object testInstance = super.createTest();
        testContextManager.prepareTestInstance(testInstance);
        return testInstance;
    }

    @Override
    protected Statement withBefores(FrameworkMethod frameworkMethod, Object testInstance, Statement statement) {
        Statement junitBefores = super.withBefores(frameworkMethod, testInstance, statement);
        return new RunBeforeTestMethodCallbacks(junitBefores, testInstance, frameworkMethod.getMethod(),
                testContextManager);
    }

    @Override
    protected Statement withAfters(FrameworkMethod frameworkMethod, Object testInstance, Statement statement) {
        Statement junitAfters = super.withAfters(frameworkMethod, testInstance, statement);
        return new RunAfterTestMethodCallbacks(junitAfters, testInstance, frameworkMethod.getMethod(),
                testContextManager);
    }
}
