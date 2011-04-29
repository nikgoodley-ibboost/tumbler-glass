package tumbler.internal.parsers.gherkin;

import tumbler.internal.domain.*;

public class PrefixedParser extends PlainLineParser {
    private String prefix;


    public PrefixedParser() {
        clazz = StepBasedModel.class;
    }
    
    @Override
    protected String parseLine(String line) {        
        return prefix + " " + super.parseLine(line);
    }
    

    public TokenParser withPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public PrefixedParser withToken(String token) {
        return (PrefixedParser) super.withToken(token);
    }
}
