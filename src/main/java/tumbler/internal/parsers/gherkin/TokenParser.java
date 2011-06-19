package tumbler.internal.parsers.gherkin;

import tumbler.internal.parsers.*;

public abstract class TokenParser {
    
    protected String parsedLine;
    
    public abstract String token();
    public abstract Object result();    
    protected abstract String parseLine(String line);
        
    public final void parse(String text) {
        verifyTextNotEmpty(text);
        
        String textLine = text.trim();
        verifyTextStartsWithProperToken(textLine);
        
        parsedLine = parseLine(textLine);
    }
    
	protected void verifyTextStartsWithProperToken(String storyLine) {
		if (! storyLine.startsWith(token()))
			throw new ParseException("Wrong line definition - did you start it with '" + token() + "'?");
	}

	protected void verifyTextNotEmpty(String text) {
		if (text == null || "".equals(text))
			throw new ParseException("Empty text");
	}
}