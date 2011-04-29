package tumbler.internal.parsers.gherkin;

import java.io.*;
import java.util.*;

import tumbler.internal.parsers.*;

public class ParsingState {
    protected Map<TokenParser, ParsingState> stateTransitionMap = new HashMap<TokenParser, ParsingState>();
    private ParsingState nextState = this;
    LineReader reader;
    
    public void parse(Object parent) {       
        while (true) {
            String line = skipEmptyLines();
            if (line == null)
                return;
            
            Object child = tryParsingWithAvailableParsers(line);

            update(parent, child);
        }
    }

    protected void update(Object parent, Object child) {
    }

    private String skipEmptyLines() {
        String line;
        do {
            try {
                line = reader.readLine();
            } catch (EOFException e) {
                return null;
            }
        } while ("".equals(line.trim()));
        return line;
    }

    private Object tryParsingWithAvailableParsers(String line) {
        for (TokenParser parser : stateTransitionMap.keySet()) {
            boolean parsed = hasParsedWith(line, parser);
            if (!parsed)
                continue;

            Object result = parser.result();
            parseChildren(parser, result);

            return result;
        }        
        throw new ParseException("Could not parse file. " + parseErrorReason(line));
    }

    private void parseChildren(TokenParser parser, Object result) {
        nextState = stateTransitionMap.get(parser);
        if (nextState != null) {
            nextState.setReader(reader);
            try {
                nextState.parse(result);
            } catch (ParseException e) {
                reader.lineBack();
            }
        }
    }

    private boolean hasParsedWith(String line, TokenParser parser) {
        boolean parsed = true;
        try {
            parser.parse(line);               
        } catch (ParseException e) {
            // will try another parser
            parsed = false;
        }
        return parsed;
    }

    private String parseErrorReason(String line) {
        StringBuilder str = new StringBuilder("\nState: " + this.getClass().getSimpleName());
        str.append("\nLine does not start with ");
        if (stateTransitionMap.size() > 1)
            str.append("one of: ");

        TokenParser[] parsers = stateTransitionMap.keySet().toArray(new TokenParser[] {});
        for (int i = 0; i < parsers.length; i++) {
            TokenParser parser = parsers[i];
            str.append(parser.token());
            if (i < parsers.length - 1)
                str.append(", ");
        }
        
        str.append("\n but is instead: ");
        str.append(line);
        
        return str.toString();
    }

    public ParsingState getNextState() {
        return nextState;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        return this.getClass().equals(obj.getClass());
    }

    public void setReader(LineReader lineReader) {
        this.reader = lineReader;        
    }
}
