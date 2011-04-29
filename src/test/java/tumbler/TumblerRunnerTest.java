package tumbler;

import static org.junit.Assert.*;

import org.junit.*;

import tumbler.internal.*;

public class TumblerRunnerTest {
	@Test
	public void shouldCreateNewScenarioWhenNoScenarioExists() {
		ScenarioManager.cleanScenario();
		assertNotNull(ScenarioManager.currentScenario());
	}
	
	@Test(expected = RuntimeException.class)
	public void shouldNotLetTestAnnotationsInTumblerStories() throws Exception {
		//given tumblerrunner with test method with @Test annotation
		TumblerRunner tumblerRunner = new TumblerRunner(new Object (){
			@SuppressWarnings("unused")
			@Test
			public void shouldThrowException () {				
			}
		}.getClass());
		
		//when computing test methods
		//then RuntimeException is thrown
		tumblerRunner.computeTestMethods();		
	}
}
