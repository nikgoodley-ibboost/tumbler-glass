package tumbler.internal.parsers.gherkin;

import static tumbler.internal.parsers.gherkin.Keyword.*;
import tumbler.internal.domain.*;

public class WhenState extends ParsingState {
    
    public WhenState() {
        stateTransitionMap.put(new PrefixedParser().withToken(keyword("And")).withPrefix(keyword("and")), new ParsingState());
    }

    @Override
    protected void update(Object parent, Object child) {
        if (parent != null)
            ((StepBasedModel) parent).steps().add(child);
    }

}
