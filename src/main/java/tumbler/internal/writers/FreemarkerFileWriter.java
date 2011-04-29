package tumbler.internal.writers;

import java.io.*;
import java.util.*;

import tumbler.internal.domain.*;
import tumbler.internal.parsers.gherkin.*;
import freemarker.template.*;

public class FreemarkerFileWriter extends StoryFileWriter {

	private Template template;
	private Template tocTemplate;

	public FreemarkerFileWriter() {
		fileExtension = "Scenarios.html";
		tocFile = "index.html";
		setupFreeMarkerCongifuration();
	}

	private void setupFreeMarkerCongifuration() {
		try {
			Configuration cfg = new Configuration();
			cfg.setObjectWrapper(new DefaultObjectWrapper());

			if (isCustomTemplateUsed()) {
				cfg.setDirectoryForTemplateLoading(new File(System.getProperty("template")));
			} else {
				cfg.setClassForTemplateLoading(this.getClass(), "html");
			}

			template = cfg.getTemplate("template.html");
			tocTemplate = cfg.getTemplate("toc-template.html");
		} catch (IOException e) {
			throw new RuntimeException("Error getting HTML template - wrong template folder or no template.html in given folder.", e);
		}
	}

	@Override
	String textFor(StoryModel story) {
		if (story == null)
			throw new IllegalArgumentException("story cannot be null");

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("keywords", new Keyword());
		paramMap.put("story", story);
		paramMap.put("scenarios", story.scenarios());
		paramMap.put("narrative", story.narrative());
		paramMap.put("runStatistics", story.runStatistics());
		paramMap.put("resources", resourcesLocationRelativeTo(story));

		StringWriter writer = new StringWriter();
		try {
			template.process(paramMap, writer);
		} catch (Exception e) {
			throw new RuntimeException("Error while processing HTML template", e);
		}
		return writer.toString();
	}

	protected String tocFor(java.util.List<StoryModel> storiesList) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("stories", storiesList);
		paramMap.put("runStoryStatistics", runStoryStatisticsFor(storiesList));
		paramMap.put("runScenarioStatistics", runScenarioStatisticsFor(storiesList));
		paramMap.put("resources", "resources");

		StringWriter writer = new StringWriter();
		try {
			tocTemplate.process(paramMap, writer);
		} catch (Exception e) {
			throw new RuntimeException("Error while processing HTML toc template", e);
		}
		return writer.toString();
	}

	private Object runStoryStatisticsFor(List<StoryModel> stories) {
		RunStatistics stats = RunStatistics.fromArray(new int[] { 0, 0, 0 });
		for (StoryModel story : stories)
			stats.updateStoryStatistics(story);
		return stats;
	}

	private Object runScenarioStatisticsFor(List<StoryModel> stories) {
		RunStatistics stats = RunStatistics.fromArray(new int[] { 0, 0, 0 });
		for (StoryModel story : stories)
			stats.updateWith(story.runStatistics());
		return stats;
	}

	private String resourcesLocationRelativeTo(StoryModel story) {
		String packageName = story.packageName();
		if (!"".equals(packageName)) {
			packageName += ".";
			packageName = packageName.replaceAll("[^\\.]*\\.", "../");
		}

		return packageName + "resources";
	}

	@Override
	protected void writeTextToFile(StoryModel story, String text) throws IOException {
		super.writeTextToFile(story, text);
		copyAdditionalFiles();
	}

	private void copyAdditionalFiles() throws IOException {
		String resources = createResourcesFolder();
		if (isCustomTemplateUsed())
			copyCustomTemplateFiles(resources);
		else
			copyDefaultTemplateFiles(resources);
	}

	private boolean isCustomTemplateUsed() {
		return System.getProperty("template") != null;
	}

	private void copyCustomTemplateFiles(String resources) throws IOException {
		File resourcesDir = new File(System.getProperty("template") + File.separator + "resources");
		for (File resource : resourcesDir.listFiles(new OnlyFilesFileFilter())) {
			File dst = new File(resources + File.separator + resource.getName());
			copy(new FileInputStream(resource), dst);
		}
	}

	private void copyDefaultTemplateFiles(String resources) throws IOException {
		String[] files = { "FAILED.png", "PASSED.png", "PENDING.png", "report.css", "tumbler.jpg" };
		for (String file : files) {
			File dst = new File(resources + File.separator + file);
			if (!dst.exists())
				copy(getClass().getResourceAsStream("html/" + file), dst);
		}
	}

	private String createResourcesFolder() {
		File resources = new File(outputFolder + reportFolder() + File.separator + "resources");
		if (!resources.exists())
			resources.mkdirs();
		return resources.getAbsolutePath();
	}

	private final class OnlyFilesFileFilter implements FileFilter {
		public boolean accept(File file) {
			return !file.isDirectory();
		}
	}
}
