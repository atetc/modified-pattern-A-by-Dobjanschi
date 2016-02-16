package modified.dobjanschi.a.pattern.database;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * @author Artur Vasilov
 */
public class DatabaseUtils {

    public static boolean isUriEmpty(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(
                uri,
                new String[] {"count(*) AS count"},
                null,
                null,
                null);
        if (cursor == null || cursor.getCount() == 0) {
            return true;
        }
        if (!cursor.moveToFirst()) {
            return true;
        }
        try {
            return cursor.getInt(0) == 0;
        }
        finally {
            cursor.close();
        }
    }

    public static void safeCloseCursor(@Nullable Cursor cursor) {
        try {
            if (cursor == null || cursor.isClosed()) {
                return;
            }
            cursor.close();
        } catch (Exception ignored) {
        }
    }


}
