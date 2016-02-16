package modified.dobjanschi.a.pattern.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import modified.dobjanschi.a.pattern.database.DatabaseUtils;
import modified.dobjanschi.a.pattern.database.RequestItemContract;
import modified.dobjanschi.a.pattern.database.SqliteHelper;
import modified.dobjanschi.a.pattern.service.model.RequestItem;

/**
 * @author Rustem
 */
public class RequestsTable {

    public static final Uri URI = SqliteHelper.BASE_CONTENT_URI
            .buildUpon()
            .appendPath(RequestItemContract.DatabaseRequests.TABLE_NAME)
            .build();

    public static void save(Context context, @NonNull RequestItem requestItem) {
        context.getContentResolver().insert(URI, toContentValues(requestItem));
    }

    @NonNull
    public static ContentValues toContentValues(@NonNull RequestItem message) {
        ContentValues values = new ContentValues();
        values.put(RequestItemContract.Columns.REQUEST, message.getRequest());
        values.put(RequestItemContract.Columns.RESPONSE, message.getResponse());
        return values;
    }

    @NonNull
    public static RequestItem fromCursor(@NonNull Cursor cursor) {
        String message = cursor.getString(cursor.getColumnIndex(RequestItemContract.Columns.REQUEST));
        return new RequestItem(message);
    }

    @NonNull
    public static List<RequestItem> listFromCursor(@NonNull Cursor cursor) {
        List<RequestItem> requestItems = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            return requestItems;
        }
        try {
            do {
                requestItems.add(fromCursor(cursor));
            } while (cursor.moveToNext());
            return requestItems;
        } finally {
            DatabaseUtils.safeCloseCursor(cursor);
        }
    }

    public static void clear(Context context) {
        context.getContentResolver().delete(URI, null, null);
    }
}
