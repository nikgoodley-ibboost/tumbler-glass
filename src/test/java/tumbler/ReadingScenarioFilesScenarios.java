package tumbler;

import static org.junit.Assert.*;
import static tumbler.Tumbler.*;

import org.junit.runner.*;

import tumbler.internal.domain.ScenarioModel.ScenarioStatus;

@RunWith(TumblerRunner.class)
public class ReadingScenarioFilesScenarios {

	@Scenario
	public void shouldReadFileWithScenarios() {
		Given("existing file name");
		String file = "src/test/resources/booklist.scenarios";
		ScenariosReader reader = new ScenariosReader();

		When("file is read in");
		reader.load(file);

		Then("generator generates story class");
		assertEquals(3, reader.stories().get(0).scenarios().size());
		assertEquals(ScenarioStatus.PENDING, reader.stories().get(0).scenarios().get(0).status());
		assertEquals(ScenarioStatus.PASSED, reader.stories().get(0).scenarios().get(1).status());
		assertEquals(ScenarioStatus.FAILED, reader.stories().get(0).scenarios().get(2).status());
	}
}
