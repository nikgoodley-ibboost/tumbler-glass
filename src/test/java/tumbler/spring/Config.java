package tumbler.spring;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

@Configuration
public class Config {
    @Bean
    public MyComponent myComponent() {
        return new MyComponent();
    }

    @Component
    public class MyComponent {

    }
}