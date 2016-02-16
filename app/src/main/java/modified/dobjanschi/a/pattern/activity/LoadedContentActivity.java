package modified.dobjanschi.a.pattern.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import modified.dobjanschi.a.pattern.R;
import modified.dobjanschi.a.pattern.database.RequestItemContract;
import modified.dobjanschi.a.pattern.database.tables.RequestsTable;

/**
 * @author Rustem
 */
public class LoadedContentActivity extends AppCompatActivity {

    private TextView textView;

    public static void start(Activity activity) {
        ActivityCompat.startActivity(activity, new Intent(activity, LoadedContentActivity.class), null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loaded_content);

        textView = (TextView) findViewById(R.id.text_view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Cursor cursor = getContentResolver().query(RequestsTable.URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String string = cursor.getString(cursor.getColumnIndex(RequestItemContract.Columns.RESPONSE));
            textView.setText(string);
        }
    }
}