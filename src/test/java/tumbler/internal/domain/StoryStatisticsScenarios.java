package tumbler.internal.domain;

import static org.junit.Assert.*;
import static tumbler.Tumbler.*;

import org.junit.runner.*;

import tumbler.*;
import tumbler.internal.domain.ScenarioModel.ScenarioStatus;

@RunWith(TumblerRunner.class)
public class StoryStatisticsScenarios {
	@Scenario
	public void shouldCountNumberOfPassedFailedAndPendingScenarios() {
		Given("Story with three scenarios: one passing, one failing, and one pending");
		StoryModel story = new StoryModel();
		story.addScenario(new ScenarioModel("passing").withStatus(ScenarioStatus.PASSED));
		story.addScenario(new ScenarioModel("failing").withStatus(ScenarioStatus.FAILED));
		story.addScenario(new ScenarioModel("pending").withStatus(ScenarioStatus.PENDING));

		When("statistics are counted");
		RunStatistics stats = story.runStatistics();

		Then("there is one scenario for each status");
		assertEquals(1, stats.passed());
		assertEquals(1, stats.failed());
		assertEquals(1, stats.pending());
	}

}
