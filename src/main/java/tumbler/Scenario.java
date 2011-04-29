package tumbler;

import java.lang.annotation.*;

/**
 * Denotes scenario for a {@link Story}. Methods annotated this way are
 * executed as JUnit tests.
 * 
 * @author Pawel Lipinski
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Scenario {
	/**
	 * Scenario name. This is optional - if not used, the method name
	 * (de-camelised) will be used.
	 */
	String value() default "";

	/**
	 * True if this scenario has only definition ({@link Tumbler#Given(String)
	 * given} / {@link Tumbler#When(String) when} / {@link Tumbler#Then(String)
	 * then} methods) <br/>
	 * Should be used only if scenario is in a 'think' phase, so until the
	 * scenario is fully defined.
	 */
	boolean pending() default false;
}
