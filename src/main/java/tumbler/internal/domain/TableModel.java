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

    public TableRow header() {        
        return header;
    }

    public List<TableRow> rows() {
        return rows;
    }
}