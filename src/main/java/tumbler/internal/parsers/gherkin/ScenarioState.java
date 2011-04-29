package tumbler.internal.parsers.gherkin;

import static tumbler.internal.parsers.gherkin.Keyword.*;
import tumbler.internal.domain.*;

public class ScenarioState extends ParsingState {
    
    public ScenarioState() {
        stateTransitionMap.put(new PlainLineParser().withToken(keyword("Given")).producingModel(GivenModel.class), new GivenState());
        stateTransitionMap.put(new PlainLineParser().withToken(keyword("When")).producingModel(WhenModel.class), new WhenState());
        stateTransitionMap.put(new PlainLineParser().withToken(keyword("Then")).producingModel(ThenModel.class), new ThenState());
    }

    @Override
    protected void update(Object parent, Object child) {
        if (parent != null)
            ((ScenarioModel) parent).steps().add((StepBasedModel) child);
    }

}
