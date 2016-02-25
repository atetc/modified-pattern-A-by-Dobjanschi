package modified.dobjanschi.a.pattern.database;

import android.provider.BaseColumns;

import modified.dobjanschi.a.pattern.network.model.RequestItem;

/**
 * @author Artur Vasilov
 */
public class RequestItemContract {

    public interface Columns extends BaseColumns {
        String REQUEST = "request";
        String RESPONSE = "response";
    }

    public interface DatabaseRequests {

        String TABLE_NAME = RequestItem.class.getSimpleName();

        String CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + Columns._ID + " integer primary key autoincrement, "
                + Columns.REQUEST + " TEXT, "
                + Columns.RESPONSE + " TEXT"
                + ");";

        String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}