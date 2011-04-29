package tumbler.internal.parsers.gherkin;

import java.util.*;

public class Keyword {
    private static ResourceBundle keywords;
    
    static {
        if (System.getProperty("locale") == null)
            keywords = ResourceBundle.getBundle("tumbler/internal/parsers/gherkin/gherkin", new Locale("en"));
        else {
            keywords = ResourceBundle.getBundle("tumbler/internal/parsers/gherkin/gherkin", new Locale(System.getProperty("locale")));        
            if (! keywords.getLocale().getLanguage().equals(System.getProperty("locale"))) {
                System.err.println("The specified language (" + System.getProperty("locale") + ") is not supported. I'll be using english instead.");
                keywords = ResourceBundle.getBundle("tumbler/internal/parsers/gherkin/gherkin", new Locale("en"));
            }
        }
    }
    
    public static final String keyword(String key) {
        return keywords.getString(key);
    }
}
