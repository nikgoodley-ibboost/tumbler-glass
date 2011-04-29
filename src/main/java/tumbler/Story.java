package tumbler;

import java.lang.annotation.*;

/**
 * Defines a name of a story to be represented in form of scenarios. This is
 * optional - if not specified, the class name (de-camelised) is used as the
 * story name.
 * 
 * @author Pawel Lipinski
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Story {
	/**
	 * Story name.
	 */
	String value();
}
