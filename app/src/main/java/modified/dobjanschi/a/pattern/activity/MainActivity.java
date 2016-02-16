package modified.dobjanschi.a.pattern.activity;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import modified.dobjanschi.a.pattern.database.DatabaseObserver;
import modified.dobjanschi.a.pattern.database.tables.RequestsTable;
import modified.dobjanschi.a.pattern.service.MainIntentService;
import modified.dobjanschi.a.pattern.callback.IntentServiceResultReceiver;
import modified.dobjanschi.a.pattern.service.MainService;
import modified.dobjanschi.a.pattern.R;

public class MainActivity extends AppCompatActivity implements IntentServiceResultReceiver.Receiver {

    public IntentServiceResultReceiver receiver;
    private View throbberView;

    private final ContentObserver mMessagesObserver = new DatabaseObserver() {

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            showThrobber(false);
            LoadedContentActivity.start(MainActivity.this);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            showThrobber(false);
            LoadedContentActivity.start(MainActivity.this);
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        throbberView = findViewById(R.id.throbber_layout);
        findViewById(R.id.service_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });
        findViewById(R.id.intent_service_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentService();
            }
        });

        receiver = new IntentServiceResultReceiver(new Handler());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register a receiver for IntentService broadcasts
        receiver.setReceiver(this);

        getContentResolver().registerContentObserver(RequestsTable.URI, true, mMessagesObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (receiver != null) {
            // clear receiver to avoid leaks.
            receiver.setReceiver(null);
        }

        getContentResolver().unregisterContentObserver(mMessagesObserver);
    }

    private void showThrobber(boolean isVisible) {
        throbberView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void startIntentService() {
        MainIntentService.start(this, receiver);
    }

    private void startService() {
        showThrobber(true);
        MainService.start(this);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case MainIntentService.STATUS_RUNNING:
                showThrobber(true);
                break;
            case MainIntentService.STATUS_FINISHED:
                showThrobber(false);
                LoadedImageActivity.start(this);
                break;
            case MainIntentService.STATUS_ERROR:
                showThrobber(false);
                Toast.makeText(this, "Intent service error", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}