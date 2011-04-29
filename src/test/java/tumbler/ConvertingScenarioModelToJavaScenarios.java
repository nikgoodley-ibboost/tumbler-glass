package tumbler;

import static tumbler.Tumbler.*;
import static tumbler.TumblerTestUtils.*;

import org.junit.runner.*;

@RunWith(TumblerRunner.class)
@Story("Scenario to Java converter")
public class ConvertingScenarioModelToJavaScenarios {
	private ScenarioToJavaConverter converter = new ScenarioToJavaConverter();

	@Scenario("Should create java from scenario")
	public void shouldCreateJavaFileFromScenarioFile() {
		Given("scenario file exists");
		String fileName = "src/test/resources/booklist.scenarios";

		When("converter is executed");
		converter.convert(fileName);

		Then("java file should exist");
		assertFileExistedInTarget("LibraryScenarios.java");
	}
}
