package tumbler.internal.writers;

import static tumbler.internal.TumblerStringUtils.*;

import java.util.*;

import tumbler.internal.domain.*;

public class JavaGenerator {

    public static final String IMPORTS =
            "import org.junit.*;\n" +
                    "import org.junit.runner.*;\n" +
                    "import tumbler.*;\n" +
                    "import static org.junit.Assert.*;\n" +
                    "import static tumbler.Tumbler.*;";

    private String javaText;

    public void generateClassFor(StoryModel story) {
        javaText = IMPORTS + "\n\n@RunWith(TumblerRunner.class)\n@Story(\"" + story.name() + "\")\npublic class ";
        String className = story.camelisedName() + "Scenarios";
        javaText += className + " {\n";
        javaText += generateConstructor(story, className);
        for (ScenarioModel scenario : story.scenarios()) {
            javaText += generateMethodFor(scenario);
        }
        javaText += "}";
    }

    private String generateConstructor(StoryModel story, String className) {
        if (story.as() == null && story.inOrder() == null && story.iWant() == null && story.soThat() == null)
            return "";

        String constructor = "\tpublic " + className + "() {\n\t\tNarrative(\"" + story.narrative() + "\");\n\t}\n";

        return constructor;
    }

    private String generateMethodFor(ScenarioModel scenario) {
        String name = scenario.name();
        name = removeSpecialCharacters(name);
        StringBuilder start = new StringBuilder("\n\t@Scenario(");
        if (!name.equals(scenario.name()))
            start.append("value = \"" + scenario.name() + "\", ");
        start.append("pending = true)");
        generateParametersAnnotation(scenario.givenStep(), start);
        start.append("\n\tpublic void should" + camelise(name) + "(");
        generateMethodParameters(scenario.givenStep(), start);
        start.append(") {\n");
        start.append(
                "\t\tGiven(\"" + scenario.given() + "\");\n\n" +
                        "\t\tWhen(\"" + scenario.when() + "\");\n\n" +
                        "\t\tThen(\"" + scenario.then() + "\");\n"
                );
        start.append("\t}\n");
        return start.toString();
    }

    private void generateMethodParameters(GivenModel givenStep, StringBuilder start) {
        if (givenStep != null && givenStep.table() != null) {
            String[] columns = givenStep.table().header().columns();
            for (int i = 0; i < columns.length - 1; i++) {
                start.append("String " + columns[i].trim().replaceAll("\\s", "_"));
                start.append(", ");
            }
            start.append("String " + columns[columns.length - 1].trim().replaceAll("\\s", "_"));
        }
    }

    private void generateParametersAnnotation(GivenModel givenStep, StringBuilder start) {
        if (givenStep != null && givenStep.table() != null) {
            start.append("\n\t@Parameters({");
            List<TableRow> rows = givenStep.table().rows();
            if (rows.size() == 1) {
                TableRow row = rows.get(0);
                addRow(start, row);
            } else {
                for (int i = 0; i < rows.size() - 1; i++) {
                    start.append("\n\t\t");
                    addRow(start, rows.get(i));
                    start.append(",");
                }
                start.append("\n\t\t");
                addRow(start, rows.get(rows.size() - 1));
                start.append("\n\t");
            }
            start.append("})");
        }
    }

    private void addRow(StringBuilder start, TableRow row) {
        start.append("\"");
        for (String column : row.columns())
            start.append("|" + column);
        if (row.columns().length > 0)
            start.append("|");
        start.append("\"");
    }

    public String javaText() {
        return javaText;
    }

}
