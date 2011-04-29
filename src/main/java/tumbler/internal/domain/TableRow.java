package tumbler.internal.domain;

import java.util.*;

public class TableRow {

    private List<String> columns = new ArrayList<String>();

    public static TableRow from(String headerLine) {
        TableRow header = new TableRow();
        header.parse(headerLine);
        return header;
    }
    
    private void parse(String headerLine) {
        headerLine = headerLine.substring(1);
        headerLine = headerLine.substring(0, headerLine.lastIndexOf('|'));
        StringTokenizer tokenizer = new StringTokenizer(headerLine, "|");
        while (tokenizer.hasMoreTokens())
            columns.add(tokenizer.nextToken());        
    }

    public String column(int i) {
        return columns.get(i);
    }

    public String[] columns() {
        return columns.toArray(new String[]{});
    }
}