package tumbler;

import tumbler.internal.*;
import tumbler.internal.domain.*;

/**
 * <h1>Tumbler</h1><br/>
 * <img src="doc-files/tumbler.jpg" align="left" border="0" margin="0"/>
 * <p>
 * <br/>
 * What is the first thing you do when you want to make your favourite drink?
 * Prepare a proper glass. It will lead the process of creating a tasty and
 * good-looking drink by defining amounts of its ingredients and how they should
 * be placed for the drink to look nice.
 * </p>
 * <p>
 * Tumbler is a library which will help you do the same with your code. It will
 * lead the way you think about and build your code, as well as help you
 * remember what should be implemented and when. As a side-effect you'll be able
 * to impress your customer with always up-to-date reports of what has already
 * been implemented (and whether it works or not), and what's still pending.
 * </p>
 * <br/>
 * <h2>Contents</h2> <b><a href="#1">1. Introduction</a><br/>
 * <a href="#2">2. Usage example</a><br/>
 * <a href="#3">3. Generating Java from scenarios</a><br/>
 * <a href="#4">4. Generating reports from Java</a><br/>
 * <a href="#5">5. Integration with JUnit</a><br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#5a">&gt;=4.5</a><br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#5b">4.4</a><br/>
 * <a href="#6">6. Annotating stories and scenarios</a><br/>
 * <a href="#7">7. Parameterised scenarios</a><br/>
 * <a href="#8">8. Integration with Spring</a><br/>
 * </b><br/>
 * <h3 id="1">1. Introduction</h3>
 * <p>
 * Tumbler is a Behaviour-Driven Development library. It supports BDD-like way
 * of thinking about the application you're building. No longer do you write
 * tests and test methods. Now you're thinking about a <b>Story</b>
 * (functionality) in terms of <b>Scenarios</b> of its use/work. All your
 * scenarios start with a magical word <b>should</b> which means, that we're
 * going to say what should happen in a given story.
 * </p>
 * <h3 id="2">2. Usage example</h3>
 * <p>
 * Let's give a real-life example. I have a library. I can lend books and accept
 * them back. Let me specify this functionality in terms of scenarios with the
 * following description:<br/>
 * 
 * <pre>
 *  Story: Library
 *    As a library user
 *    In order to borrow a book
 *    I want librarian to give me that book
 *    So that I can take it home
 * 
 *    Scenario: lend an existing book from the library
 *        Given 'Children from Bullerbyn' book in the library
 *           And a pretty librarian.
 *        When this book is borrowed from the library
 *           And the librarian is blinking at you
 *        Then the library doesn't contain it anymore
 *           And the librarian wants to go out with you
 *           But you're already married, so no way.
 * 
 *    Scenario: not lend a nonexisting book from the library
 *        Given empty library
 *        When we try to borrow 'Children from Bullerbyn' from the library
 *        Then the library doesn't let it to be borrowed.
 * 
 *    Scenario: accept back a book previously borrowed
 *        Given 'Children from Bullerbyn' has been borrowed from the library
 *        When this book is given back
 *        Then the library contains it.
 * </pre>
 * 
 * <br/>
 * You can represent exactly this way of thinking about your functionalities in
 * a form of Tumbler scenarios:<br/>
 * <img src="doc-files/java-examples.gif"/>
 * </p>
 * <p>
 * You can see that we're using a <code>TumblerRunner</code> to run scenarios
 * with JUnit, define a <code>Story</code> with the proper name, and specify all
 * <code>Scenarios</code> as pending - meaning that they have not been
 * implemented yet, so should be ignored during execution.<br/>
 * All scenarios have names that start with the <b>should</b> word, and contain
 * <code>Given</code>, <code>When</code> and <code>Then</code> sections which
 * define: initial state for the scenario, action to be performed and expected
 * output accordingly. The only thing that's lacking is... the scenario
 * implementation.
 * </p>
 * <p>
 * Let me quickly show you how a sample implementation of one of the methods
 * could look like:<br/>
 * <img src="doc-files/java-example.gif"/>
 * </p>
 * <p>
 * See? Not only do you have a documentation of your functionalities, but you
 * just got an acceptance-testing framework!
 * </p>
 * <p>
 * You can see, that I now removed the <code>pending = true</code> from the
 * <code>@Scenario</code> annotation - that's because the scenario has its
 * implementation and the it should be failing/red now, so that you can produce
 * production code that realises this scenario and makes it passing/green.
 * </p>
 * <h3 id="3">3. Generating Java from scenarios</h3>
 * <p>
 * So far so good. But now remember that according to basically any agile method
 * you should be working on requirements / scenarios together with business
 * people. Java code is not really readable for them. So as as helper in your
 * cooperation with your business you are free to write your scenarios in the
 * form given before like this:<br/>
 * 
 * <pre>
 * Story: STORY TITLE
 *     Scenario: SCENARIO TITLE
 *         Given INPUT CONDITIONS
 *            And OTHER CONDITION
 *         When ACTION TO BE PERFORMED
 *            And OTHER ACTION
 *         Then EXPECTATION
 *            And OTHER EXPECTATION
 *            But ANOTHER EXPECTATION
 * </pre>
 * 
 * You can have as many scenarios for a story as you like (obviously they must
 * have unique names.) There can be as many stories as you wish per file.
 * Whitespaces and newlines are not important (so format the document to make it
 * readable). <br/>
 * Now a simple call
 * 
 * <pre>
 * java -cp tumbler.jar tumbler.ScenarioToJavaConverter YOUR_STORY_FILE.scenarios
 * </pre>
 * 
 * will generate a StoryTitleScenarios.java file with java representation of the
 * scenarios.<br/>
 * 
 * You can give your scenarios and stories basically any name, and all
 * characters other than letters, digits and underscore will be skipped in
 * method/class names. Still, the original name will be kept in
 * <code>@Story</code> and <code>@Scenario</code> annotations.
 * 
 * Scenario can also be written in basically any language. Supported languages
 * are: English, German, Polish and Danish. Just add <code>-Dlocale=de</code> to
 * be able to process a story in German.
 * </p>
 * <h3 id="4">4. Generating reports from Java</h3>
 * <p>
 * But, say, now you filled in all the scenarios, implemented the functionality
 * that realises the scenarios and you'd like to show to your business what's
 * already there. It's as easy as running all the tests with
 * <code>-DgenerateReport=scenarios</code>. Now you have .scenarios files for
 * all your scenarios. It is exactly the same file format as you might have used
 * while defining the scenarios together with business people, with additional
 * information about scenarios' statuses ([PENDING,PASSED,FAILED]). Our scenario
 * would then look like this:
 * 
 * <pre>
 *  Story: Library
 *      As a library user, In order to borrow a book, I want librarian to give me that book, So that I can take it home
 *  
 *  Scenario: lend an existing book from the library    [PENDING]
 *      Given 'Children from Bullerbyn' book in the library and a pretty librarian.
 *      When this book is borrowed from the library and the librarian is blinking at you
 *      Then the library doesn't contain it anymore and the librarian wants to go out with you but you're already married, so no way.
 *  
 *  Scenario: not lend a nonexisting book from the library  [PASSED]
 *      Given empty library
 *      When we try to borrow 'Children from Bullerbyn' from the library
 *      Then the library doesn't let it to be borrowed.
 *  
 *  Scenario: accept back a book previously borrowed    [FAILED]
 *      Given 'Children from Bullerbyn' has been borrowed from the library
 *      When this book is given back
 *      Then the library contains it.
 * </pre>
 * 
 * </p>
 * <p>
 * There's also an option to generate a nice HTML report. Just pass
 * <code>-DgenerateReport=html</code> to VM while executing scenarios and you'll
 * get a nice set of HTML files like this one here (this is actually a real
 * report made from one of Tumbler stories):
 * </p>
 * <p align="center">
 * <img src="doc-files/html-example.gif" border="0" margin="0"/>
 * </p>
 * <p>
 * Since HTML report has one page per Story, there's also a Table of Contents
 * for your stories, which shows overall statistics. It will also let you
 * navigate through your stories so that you can see details for each of them.
 * The ToC looks like this:
 * </p>
 * <p align="center">
 * <img src="doc-files/html-toc.gif" border="0" margin="0"/>
 * </p>
 * <p>
 * Tumbler let you also configure the way HTML reports look. All you need to do
 * is pass <code>-Dtemplate=TEMPLATE_FOLDER</code> parameter to the VM while
 * executing scenarios. The template folder must contain 3 things:
 * <ol>
 * <li><code>template.html</code> file with FreeMarker template for your Stories
 * </li>
 * <li><code>toc-template.html</code> file with FreeMarker template for Table of
 * Contents</li>
 * <li><code>resources</code> directory with any resources you might want to use
 * in your templates</li>
 * </ol>
 * The folder with resources cannot have any subfolders - all your resources
 * must be 'flat' in the <code>resources</code> folder.
 * </p>
 * <p>
 * The following objects are available for usage in the templates:<br/>
 * In <code>template.html</code>:
 * <ul>
 * <li><code>story</code> of class {@link StoryModel} which is a current story</li>
 * <li><code>runStatistics</code> of class {@link RunStatistics} and contains
 * statistics for the current story</li>
 * <li><code>scenarios</code> which is a list of {@link ScenarioModel} which are
 * scenarios defined in the current story</li>
 * <li><code>resources</code> which is a String containing path to resources
 * folder in your template</li>
 * <li><code>narrative</code> which is a String containing the 'As a ... I
 * want... So that' phrase</li>
 * <li><code>keywords</code> which contains translations according to current
 * locale - use it like this: keywords.keyword("Given")</li>
 * </ul>
 * </p>
 * In <code>toc-template.html</code>:
 * <ul>
 * <li><code>stories</code> which is a list of {@link StoryModel}</li>
 * <li><code>runStoryStatistics</code> of class {@link RunStatistics} which
 * contains statistics over all executed stories</li>
 * <li><code>runScenarioStatistics</code> of class {@link RunStatistics} which
 * contains statistics over all executed scenarios</li>
 * <li><code>resources</code> which is a String containing path to resources
 * folder in your template</li>
 * </ul>
 * </p>
 * <p>
 * So a sample template may look like this:
 * </p>
 * 
 * <pre>
 * Story: ${story.name()}
 * Run Summary: 
 *     passed: ${runStatistics.passed()}
 *     failed: ${runStatistics.failed()}
 *     pending: ${runStatistics.pending()}
 * &lt;#list scenarios as scenario&gt;Scenario: ${scenario.name()}
 *     Given ${scenario.given()}
 *     When ${scenario.when()}
 *     Then ${scenario.then()}
 * &lt;/#list&gt;
 * </pre>
 * 
 * <h3 id="4a">4a. Generating reports from a Maven build</h3> You can also
 * generate reports automatically in your maven build with this surefire plugin
 * configuration to your pom.xml: <code>
 *  &lt;plugin&gt;
 *     &lt;groupId&gt;org.apache.maven.plugins&lt;/groupId&gt;
 *     &lt;artifactId&gt;maven-surefire-plugin&lt;/artifactId&gt;
 *     &lt;configuration&gt;
 *         &lt;useFile&gt;false&lt;/useFile&gt;
 *         &lt;systemProperties&gt;
 *             &lt;property&gt;
 *                 &lt;name&gt;generateReport&lt;/name&gt;
 *                 &lt;value&gt;html&lt;/value&gt;
 *             &lt;/property&gt;
 *         &lt;/systemProperties&gt;
 *         &lt;includes&gt;
 *             &lt;include&gt;**\/*Scenarios.java&lt;/include&gt;
 *         &lt;/includes&gt;
 *     &lt;/configuration&gt;
 * &lt;/plugin&gt;
 * </code>
 * 
 * <h3 id="5">5. Integration with JUnit</h3>
 * <p>
 * Tumbler can be used both with recent versions of JUnit (currently 4.8) as
 * well as older ones. The oldest I've tested it with is JUnit4.4. You're
 * obviously encouraged to use the most recent one, but still there are some
 * projects out there which require some history. To name just two - current
 * Eclipse has JUnit4.5 built-in, and Spring 2.5.6 works with JUnit4.4 only.
 * Since there have been quite a few changes in JUnit internals which Tumbler
 * relies on, there are two ways of running Tumbler - one with newer JUnits
 * (&gt;=4.5) and one for 4.4
 * </p>
 * <h4 id="5a">&gt;=v4.5</h4>
 * <p>
 * Tubmler is integrated with JUnit version starting with version 4.5. You can
 * use it together with your existing junit code. Just add all the newcoming
 * tests as Tubmler scenarios. The integration is pretty deep - just look at the
 * JUnit tests window in your IDE. You'll see all stories and scenarios as full
 * sentences instead of camel-case'ed names. Here is what I now can see in my
 * eclipse:
 * <p align="center">
 * <img src="doc-files/junit.gif"/>
 * </p>
 * <p>
 * This looks much better than your normal tests, right?<br/>
 * You can see, that the first scenario of <code>Java generator</code> story
 * passed, the second one failed, and the third one was ignored - it's either
 * set to pending or annotated with JUnit's <code>@Ignore</code> attribute.<br/>
 * The only problem with eclipse is that you cannot double-click on scenarios in
 * JUnit window. I mean, you can, but it won't lead you to the scenario method -
 * will only open the proper class. Having spent some time looking into JUnit
 * eclipse plugin I realised it cannot be accomplished, since the plugin is
 * using the same name for showing entries and for finding them in code.<br/>
 * While I'm fine with finding the right method by using Ctrl-O, not all of you
 * may be. So I added the possibility to stick by the old camel-case names (just
 * as normal JUnit tests) so that you can navigate from the plugin. In order to
 * do so, just run your scenarios with <code>-DcamelCase</code> passed as VM
 * argument. The generated report will still have proper full-text names.
 * </p>
 * <p>
 * Each scenario can be in one of four states: passing, failing, pending and
 * ignored. If a test is ignored (<code>@Ignore</code>) it won't generated in
 * the .scenarios file - for Tumbler it means you're not interested in it
 * anymore. If you just want an scenario to be skipped, but to be taken into
 * account in report generation, set it to the pending state.
 * </p>
 * <h4 id="5b">4.4</h4>
 * <p>
 * JUnit4.4 is used by Spring 2.5. Because there are so many Spring 2.5
 * applications out there, I've decided not to exclude all these people.<br/>
 * In order to use Tumbler with JUnit4.4 you need to run your scenarios with a
 * <code>JUnit44TumblerRunner</code>. This runner is functionally equal to the
 * normal one, but is based on JUnit4.4 classes. I strongly discourage you to
 * use such an old JUnit (and so this runner), but if you have no other choice -
 * then voila!
 * </p>
 * <h3 id="6">6. Annotating Stories and Scenarios</h3>
 * <p>
 * You don't need to annotate stories in your Java code. The story name is
 * automatically resolved from the Java class name. Still, you may want to use
 * some special characters, or simply have your code more intention-revealing
 * (that's always a good thing to have!). In such case just annotate your
 * scenarios class with:
 * 
 * <pre>
 * &#064;Story(&quot;Some story name&quot;)
 * </pre>
 * 
 * Scenarios must be annotated - in the end Tumbler must know which methods are
 * scenarios and which serve some other purpose. So each scenario method must be
 * annotated with <code>@Scenario</code>. Scenarios can have full-text names
 * different to method names. Just give the name as the annotation value like
 * this:
 * 
 * <pre>
 * &#064;Scenario(&quot;Some name other than the method name&quot;)
 * </pre>
 * 
 * Scenarios can also be set to <i>pending</i> state, which means that their
 * implementation has not been finished yet. In such cases annotate such
 * scenarios with:
 * 
 * <pre>
 * &#064;Scenario(pending = true)
 * </pre>
 * 
 * This is also the way they always get generated using
 * <code>ScenariosToJavaConverter</code> since obviously all generated scenarios
 * need proper implementation first.
 * </p>
 * 
 * <h3 id="7">7. Parameterised scenarios</h3>
 * <p>
 * Scenarios can be parameterised. If you're using scenario text files, you can
 * pass them in the following form:
 * 
 * <pre>
 *  Scenario: lend an existing book from the library
 *      Given the following book is in the library:
 *          | title | author | quantity |
 *          | Children from Bullerbyn | Astrid Lindgren | 10 |
 *          | Fairy tales | Grimm brothers | 15 |
 *      When a book is borrowed from the library
 *      Then the library doesn't contain it anymore
 * </pre>
 * 
 * If you're only using Java files, you can use more options for parameterising
 * your scenarios. Since Tumbler is using JUnitParams library to handle
 * parameterised scenarios, you can load parameters from a method declared in
 * the Scenarios class, or public static method from external classes.
 * 
 * <pre>
 *  &#064;Scenario
 *  &#064;Parameters(source = BooksProvider.class)
 *  public void shouldBorrowBookFromLibrary(String title, String author, int quantity) {
 *      ...
 *  }
 * </pre>
 * 
 * Look at JUnitParams site for more examples.
 * </p>
 * <h3 id="8">8. Integration with Spring</h3>
 * <p>
 * You can easily use Tumbler together with Spring. The only problem is that
 * Spring's test framework is based on JUnit runners, and JUnit allows only one
 * runner to be run at once. Which would normally mean that you could use only
 * one of Spring or Tumbler. Luckily we can cheat Spring a little by adding this
 * to your test class:
 * 
 * <pre>
 * private TestContextManager testContextManager;
 * 
 * &#064;Before
 * public void init() throws Exception {
 *     this.testContextManager = new TestContextManager(getClass());
 *     this.testContextManager.prepareTestInstance(this);
 * }
 * </pre>
 * 
 * This lets you use in your tests anything that Spring provides in its test
 * framework.
 * </p>
 * 
 * 
 * @author Pawel Lipinski (pawel.lipinski@pragmatists.pl)
 */
public class Tumbler {
    /**
     * 'Given' section of a scenario
     * 
     * @param description
     *            description of what is given
     */
    public static void Given(String description) {
        ScenarioManager.currentScenario().withGiven(description);
    }

    /**
     * 'When' section of a scenario
     * 
     * @param description
     *            description of action being performed
     */
    public static void When(String description) {
        ScenarioManager.currentScenario().withWhen(description);
    }

    /**
     * 'Then' section of a scenario
     * 
     * @param description
     *            description of expected effect / state
     */
    public static void Then(String description) {
        ScenarioManager.currentScenario().withThen(description);
    }

    /**
     * 'Narrative' of a story
     * 
     * @param narrative
     *            the 'As a... I want... So that...' phrase
     */
    public static void Narrative(String narrative) {
        if (ScenarioManager.currentScenario().story() != null)
            ScenarioManager.currentScenario().story().withNarrative(narrative);
    }
}
