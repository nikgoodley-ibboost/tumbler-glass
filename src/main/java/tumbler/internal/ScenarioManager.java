package tumbler.internal;

import tumbler.internal.domain.*;

public class ScenarioManager {

	public static ThreadLocal<ScenarioModel> threadSafeScenario = new ThreadLocal<ScenarioModel>();

	public static void startScenario() {
		threadSafeScenario.set(new ScenarioModel(""));
	}

	public static void cleanScenario() {
		threadSafeScenario.remove();
	}

	public static ScenarioModel currentScenario() {
		ScenarioModel scenario = threadSafeScenario.get();
		if (scenario == null) {
			startScenario();
			scenario = currentScenario();
		}
		return scenario;
	}
}
