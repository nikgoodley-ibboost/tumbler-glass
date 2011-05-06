package tumbler.internal.domain;

public class GivenModel extends StepBasedModel {

    public GivenModel(String text) {
        super(text);
    }

    public GivenModel() {
    }

    public TableModel table() {
        for (Object step : steps)
            if (step instanceof TableModel)
                return (TableModel) step;

        return null;
    }

    public String text() {
        StringBuilder text = new StringBuilder();
        for (Object step : steps)
            text.append(step.toString() + " ");
        return text.toString().trim();
    }
}
