package apps.dmc.screenshotservice.util.email;

import android.content.Intent;
import android.net.Uri;

import java.lang.Exception;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by DCarmo on 2015-10-28.
 */
public class EmailUtil {

    /**
     * Creates a sharing intent for an image or text
     *
     * @param
     *      subject -> The subject of the email to send
     * @param
     *      body -> The body of the email to send
     * @param
     *      imageUris -> The location of the image(s) to share.
     */
    public static Intent createShareIntent(String subject, String body, Uri ... imageUris) {
        Intent sharingIntent = null;
        try {
            sharingIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            sharingIntent.setType("image/*");

            sharingIntent.setType("text/html");

            if (subject != null && !subject.equals("")) {
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            }

            if (body != null && !body.equals("")) {
                sharingIntent.putExtra(Intent.EXTRA_TEXT, body);
            }

            if (imageUris != null) {
                sharingIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, new ArrayList<Uri>(Arrays.asList(imageUris)));
                sharingIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            return sharingIntent;
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

        return sharingIntent;
    }
}
