package pl.edu.pw.ee.overseer.utilities;

import android.content.Context;
import android.widget.Toast;

public class ToastUtility {
    public static void makeError(Context context, String error) {
        int id = context.getResources().getIdentifier(error, "string", context.getPackageName());
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
    }

    public static void makeText(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
