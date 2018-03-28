package jjpartnership.hub.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by Jonathan on 3/28/2018.
 */

public class CommunicationsUtil {

    public static void launchDirectionsIntent(String address, Context applicationContext) {
        if(address != null && !address.isEmpty()) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + address);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            applicationContext.startActivity(mapIntent);
        }else{
            Toast.makeText(applicationContext, "The company's address is unavailable.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void launchPhoneCallIntent(String phoneNumber, Context applicationContext){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
        applicationContext.startActivity(intent);
    }

    public static void launchEmailIntent(String email, Context applicationContext){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto: " + email));
        applicationContext.startActivity(Intent.createChooser(emailIntent, "Send e-mail"));
    }
}
