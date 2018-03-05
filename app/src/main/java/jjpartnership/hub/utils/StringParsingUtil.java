package jjpartnership.hub.utils;

/**
 * Created by jbrannen on 3/2/18.
 */

public class StringParsingUtil {
    public static String parseEmailDomain(String email){
        String emailDomain = null;
        for(int i = 0; i < email.length(); i++){
            if(email.charAt(i) == '@'){
                emailDomain = email.substring(i+1, email.length());
            }
        }
        return emailDomain;
    }
}
