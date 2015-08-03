# Introduction #

The whole idea of Tumbler is to help you think about the software you're writing. It has an extremely simple interface, so it won't take you more than 5 seconds to learn it, but it can completely change the way you develop code.

But before I show you how to use it, see how to install it.

# Installation #
There are two ways you can use Tumbler - either by manually adding it your project's classpath, or by using maven.

## Manual installation ##
There's not much you need to do to start using Tumbler. Just grab a jar file from the Downloads section of the right tab and place it on your project's classpath. That's all, at least for the beginning. You can now import statically `tumbler.Tumbler` class' methods like this:
`import static tumbler.Tumbler.*;`

## Maven installation ##
This is trivial. Just add to the `dependencies` section of the pom.xml file for your project:
```
<dependency>
	<groupId>pl.pragmatists.tumbler</groupId>
	<artifactId>tumbler</artifactId>
	<version>0.4.1</version>
 </dependency>
```
and you're done. Both new JUnit (4.10) and proper FreeMarker are automatically downloaded, so you don't need to worry about them!

# Usage #
So, first you don't use word _test_ anymore - now you're writing Stories and their Scenarios. So annotate your _Test_ class with `@Story`. You'll also need to add `@RunWith(TumblerRunner.class)` annotation to your class to let JUnit use Tumbler. If you're forced to use JUnit4.4, use `@RunWith(JUnit44TumblerRunner.class)` instead.
Now, each test in your Story must have `@Scenario` annotation, so that Tumbler knows which methods are scenarios and which are just helpers.
Within your scenarios use `Given()`, `When()` and `Then()` methods to specify the scenario - i.e. define what you plan to do there. It's a good idea to first define scenarios, so that you know exactly how you're going to develop your code. If you do that, you'll see that during development you'll be much quicker, since you'll have a good mental model of your application and it will be easier for you to lead your code in the right direction.

Basicaly what you need to do is write your tests in the following manner:
```
import static tumbler.Tumbler.*;

@RunWith(TumblerRunner.class}
@Story("Story name here")
public class SomeScenarios {

    @Scenario("Scenario name here")
    public void shouldDoSomething() {
        Given("input conditions description");
        Something sth = new Something();
        sth.setSomeField(someValue);

        When("operation description");
        Result result = sth.doSomething();

        Then("expected result description");
        assertThat(result, matchesExpectation);
    }
 
}
```

That's basically nearly all API. For more usage information and details see [documentation](http://tumbler-glass.googlecode.com/svn/tags/tumbler-0.3.0/apidocs/tumbler/Tumbler.html)

# Generating Java classes from scenario files #
Tumbler lets you generate java classes (JUnit tests) directly from scenario files. Just run
```
java -classpath Tumbler.jar tumbler.ScenarioToJavaConverter YOUR_STORY.scenario
```
This will generate a `*`Scenarios.java file in the current directory. Just place it in the right package and start working with it!

You can use your mother tongue in the scenario files if you wish. Just add -Dlocale=de if you want German, da for Danish, pl for Polish, etc. If your language is not supported, send me the translation, and I'll add it.

# HTML generation #
Generation of HTML reports is done using [FreeMarker](http://freemarker.sourceforge.net) library. In order to be able to use it, place freemarker jar on your classpath and you're done.
Now you can pass `-DgenerateReport=html` and `-DoutputFolder=target` to the VM while executing tests, and you'll find execution report in the `target/Tumbler-reports` directory.

You can also generate reports automatically in your maven build with this surefire plugin configuration to your pom.xml:
```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <useFile>false</useFile>
        <systemProperties>
            <property>
                <name>generateReport</name>
                <value>html</value>
            </property>
        </systemProperties>
        <includes>
            <include>**/*Scenarios.java</include>
        </includes>
    </configuration>
</plugin>
```
# Running Tumbler stories together with Spring #
Spring uses its own runner to execute its tests. But it's pretty simple to cheat it. Instead of `@RunWith(SpringJUnit4ClassRunner.class)` do this in your Tumbler class:
```
    private TestContextManager testContextManager;

    @Before
    public void init() throws Exception {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
    }
```

It will start Spring context with every Tumbler scenario run. You can still inherit after Spring-specific `Abstract*Test` classes and use their specific functionalities.