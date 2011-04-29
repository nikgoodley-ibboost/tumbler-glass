package tumbler.internal.parsers.gherkin;

import tumbler.internal.domain.*;

public class PlainLineParser extends TokenParser {
    protected String token;
    protected Class<? extends WithText> clazz;

    @Override
    protected String parseLine(String line) {
        return line.substring(token().length()).trim();
    }

    @Override
    public Object result() {
        try {
            return clazz.newInstance().withText(parsedLine).withToken(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String token() {
        return token;
    }

    public PlainLineParser withToken(String token) {
        this.token = token;
        return this;
    }

    public PlainLineParser producingModel(Class<? extends WithText> clazz) {        
        this.clazz = clazz;
        return this;
    }
}