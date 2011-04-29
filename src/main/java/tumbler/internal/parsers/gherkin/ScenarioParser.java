package tumbler.internal.parsers.gherkin;

import static tumbler.internal.parsers.gherkin.Keyword.*;
import tumbler.internal.domain.*;
import tumbler.internal.domain.ScenarioModel.ScenarioStatus;

public class ScenarioParser extends TokenParser {
	private ScenarioStatus status;

    @Override
	public String token() {
		return keyword("Scenario");
	}

    @Override
    public Object result() {
        return new ScenarioModel(parsedLine).withStatus(status);
    }

    @Override
    protected String parseLine(String line) {
        String name = line.substring(token().length()).trim();
        
        status = statusFromName(name);
        if (status != null)
            name = cutStatusFromName(name);
        else
            status = ScenarioStatus.PENDING;

        name = skipSpecialWords(name);
        
        return name;
    }
    

    private String skipSpecialWords(String name) {
        if (name.toLowerCase().startsWith("should "))
            name = name.replaceFirst("should ", "");
        else if (name.toLowerCase().startsWith("does "))
            name = name.replaceFirst("does ", "");
        else if (name.toLowerCase().startsWith("do "))
            name = name.replaceFirst("do ", "");
        else if (name.toLowerCase().startsWith("doesn't "))
            name = "not " + name.replaceFirst("doesn't ", "");
        return name;
    }

    private ScenarioStatus statusFromName(String name) {
        for (ScenarioStatus status : ScenarioStatus.values())
            if (name.contains("[" + status + "]"))
                return status;
        return null;
    }

    private String cutStatusFromName(String name) {
        return name.substring(0, name.lastIndexOf('[')).trim();
    }

}
