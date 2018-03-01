package jjpartnership.hub.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jbrannen on 5/30/17.
 */

public class StringValidationUtil {
    public static boolean isValidEmailAddress(String email) {
        String regex = "^(?!.*\\.{2})(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        short duplicateCharCount = 0;
        for(int i = 0; i < email.length(); i++){
            char c = email.charAt(i);
            if(c == '@')
                duplicateCharCount++;
        }
        if(duplicateCharCount > 1)
            return false;
        return matcher.matches();
    }

    public static boolean isValidPassword(String password){
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        //validate phone numbers of format "1234567890"
        if (phoneNumber.matches("\\d{10}")) return true;
            //validating phone number with -, . or spaces
        else if(phoneNumber.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
        else if(phoneNumber.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if(phoneNumber.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;
    }
}
