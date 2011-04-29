package tumbler.internal.domain;

public class RunStatistics {

	private int passed;
	private int failed;
	private int pending;

	private RunStatistics(int passed, int failed, int pending) {
		this.passed = passed;
		this.failed = failed;
		this.pending = pending;
	}

	public int passed() {
		return passed;
	}

	public int failed() {
		return failed;
	}

	public int pending() {
		return pending;
	}

	public static RunStatistics fromArray(int[] stats) {
		return new RunStatistics(stats[0], stats[1], stats[2]);
	}

	public void updateWith(RunStatistics runStatistics) {
		passed += runStatistics.passed;
		failed += runStatistics.failed;
		pending += runStatistics.pending;
	}

	public void updateStoryStatistics(StoryModel story) {
		if (story.runStatistics().failed == 0 && story.runStatistics().pending == 0 && story.runStatistics().passed > 0)
			passed++;
		else if (story.runStatistics().failed != 0 && story.runStatistics().pending == 0)
			failed++;
		else
			pending++;
	}

}
