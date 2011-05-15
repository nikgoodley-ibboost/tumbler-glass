package tumbler.internal.domain;

import java.util.*;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class StepBasedModel implements WithText {

    protected List steps = new LinkedList();
    private String token;

    public StepBasedModel() {
    }

    public StepBasedModel(String text) {
        steps.add(text);
    }

    public String text() {
        StringBuilder text = new StringBuilder();
        for (Object step : steps) {
            text.append(step.toString() + " ");
        }
        return text.toString().trim();
    }

    public List steps() {
        return steps;
    }

    @Override
    public StepBasedModel withText(String text) {
        steps.add(text);
        return this;
    }

    @Override
    public String toString() {
        return text();
    }

    @Override
    public StepBasedModel withToken(String token) {
        this.token = token;
        return this;
    }

    public String token() {
        return token;
    }
}