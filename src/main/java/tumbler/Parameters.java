package tumbler;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Parameters {
    String[] value();
}
