package DateTimeRegex;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class FormattedDateMatcher {

    public boolean Pattern(String patern){
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            format.parse(patern);
            return true;
        }catch(ParseException e){
            System.out.println("Incorrect date");
        }
        return false;
    }

}