package tumbler.spring;

import org.springframework.context.*;
import org.springframework.context.annotation.*;
import org.springframework.test.context.*;

import tumbler.spring.MyTest.*;

public class TestContextLoader implements ContextLoader {
    public String[] processLocations(Class<?> clazz, String... locations) {
        return new String[] {};
    }

    public ApplicationContext loadContext(String... locations) throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(Config.class);
        ctx.refresh();
        return ctx;
    }
}