package tumbler.internal.parsers.gherkin;

import java.io.*;
import java.util.*;

import tumbler.internal.domain.*;
import tumbler.internal.parsers.*;

public class TableParser extends TokenParser {

    private final GivenState givenState;

    public TableParser(GivenState givenState) {
        this.givenState = givenState;
    }

    @Override
    public String token() {
        return "|";
    }

    @Override
    public Object result() {        
        TableModel table = new TableModel()
            .withHeader(parsedLine)
            .withRows(readTableRows());        
        return table;
    }

    @Override
    protected String parseLine(String line) {        
        checkTableLine(line);        
        return line;
    }

    private List<String> readTableRows() {
        LineReader reader = givenState.reader;
        List<String> rows = new ArrayList<String>();
        while(true) {
            String line;
            try {
                line = reader.readLine().trim();
            } catch (EOFException e) {
                break;
            }
            
            try {
                checkTableLine(line);
                rows.add(line);
            } catch (ParseException e) {
                reader.lineBack();
                break;
            }
        }
        
        return rows;
    }

    private void checkTableLine(String line) {
        if (! line.startsWith("|") && ! line.endsWith("|"))
            throw new ParseException("Proper table line should start and end with a |. Fix it. Now it says: \n\t" + line);
    }

}
