package tumbler.internal.domain;

import java.util.*;

public class TableModel {

    private List<TableRow> rows = new ArrayList<TableRow>();
    private TableRow header;

    public TableModel withHeader(String headerLine) {
        header = TableRow.from(headerLine);
        return this;
    }

    public TableModel withRows(List<String> tableRows) {
        for (String rowLine : tableRows)
            rows.add(TableRow.from(rowLine));
        return this;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        text.append("\" +\n\t\t\t\"");
        text.append(header.toString());
        text.append("\"");
        text.append(" +\n\t\t\t");
        for (int i = 0; i < header.columns().length; i++) {
            text.append("\"|\" + ");
            text.append(header.column(i).trim().replaceAll("\\s", "_"));
            text.append(" + ");
        }
        text.append("\"|");
        return text.toString();
    }

    public TableRow header() {
        return header;
    }

    public List<TableRow> rows() {
        return rows;
    }
}