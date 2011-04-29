package tumbler.internal.parsers.gherkin;

import static tumbler.internal.parsers.gherkin.Keyword.*;
import tumbler.internal.domain.*;

public class ThenState extends ParsingState {
    
    public ThenState() {
        stateTransitionMap.put(new PrefixedParser().withToken(keyword("And")).withPrefix(keyword("and")), new ParsingState());
        stateTransitionMap.put(new PrefixedParser().withToken(keyword("But")).withPrefix(keyword("but")), new ParsingState());
    }

    @Override
    protected void update(Object parent, Object child) {
        if (parent != null)
            ((StepBasedModel) parent).steps().add(child);
    }

}
