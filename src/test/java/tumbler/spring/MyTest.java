package tumbler.spring;

import static org.junit.Assert.*;
import junitparams.*;

import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.annotation.*;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.*;

import tumbler.*;

@RunWith(SpringTumblerRunner.class)
@ContextConfiguration(loader = TestContextLoader.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class MyTest {

    @Autowired
    Config.MyComponent myComponent;

    @Scenario
    public void test1() throws Exception {
        assertNotNull(myComponent);
    }

    @Scenario
    @Parameters("2")
    public void test2(int a) throws Exception {
        assertNotNull(myComponent);
        assertEquals(2, a);
    }
}
