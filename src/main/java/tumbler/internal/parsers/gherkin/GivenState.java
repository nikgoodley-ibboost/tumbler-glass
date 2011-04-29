package tumbler.internal.parsers.gherkin;

import static tumbler.internal.parsers.gherkin.Keyword.*;
import tumbler.internal.domain.*;

public class GivenState extends ParsingState {

    public GivenState() {
        stateTransitionMap.put(new PrefixedParser().withToken(keyword("And")).withPrefix(keyword("and")), new ParsingState());
        stateTransitionMap.put(new TableParser(this), new ParsingState());
    }

    @Override
    protected void update(Object parent, Object child) {
        if (parent != null)
            ((GivenModel) parent).steps().add(child);
    }

}
