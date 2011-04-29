package tumbler.internal.parsers.gherkin;

import java.io.*;

public class LineReader {

    private BufferedReader reader;

    public LineReader(String text) {
        reader = new BufferedReader(new StringReader(text));
    }

    public String readLine() throws EOFException {        
        String line = null;
        try {
            reader.mark(1000);
            line  = reader.readLine();            
        } catch (IOException e) {
            throw new RuntimeException("Reading failed", e);
        }
        
        if (line == null)
            throw new EOFException();
        
        line = skipComment(line);
        line = replaceDoubleToSingleQuotation(line);
        
        return line;
    }

    private String replaceDoubleToSingleQuotation(String line) {
        return line.replace('"', '\'');
    }

    private String skipComment(String line) {
        return line.replaceFirst("--.*", "");
    }

    public void lineBack() {
        try {
            reader.reset();
        } catch (IOException e) {
            throw new RuntimeException("Lineback failed", e);
        }
    }

}
