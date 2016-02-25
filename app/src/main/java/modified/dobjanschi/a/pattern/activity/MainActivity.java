package modified.dobjanschi.a.pattern.activity;

import android.database.ContentObserver;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import modified.dobjanschi.a.pattern.base.BaseActivity;
import modified.dobjanschi.a.pattern.callback.RequestListener;
import modified.dobjanschi.a.pattern.database.DatabaseObserver;
import modified.dobjanschi.a.pattern.database.tables.RequestsTable;
import modified.dobjanschi.a.pattern.network.MainIntentService;
import modified.dobjanschi.a.pattern.callback.IntentServiceResultReceiver;
import modified.dobjanschi.a.pattern.network.MainService;
import modified.dobjanschi.a.pattern.R;
import modified.dobjanschi.a.pattern.network.NetworkService;
import modified.dobjanschi.a.pattern.network.request.GetWeatherData;
import modified.dobjanschi.a.pattern.network.request.RequestEntry;

public class MainActivity extends BaseActivity implements IntentServiceResultReceiver.Receiver, RequestListener {

    public IntentServiceResultReceiver receiver;
    private View throbberView;

    // Observe database changes for architecture with Service
    private final ContentObserver messagesObserver = new DatabaseObserver() {

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


        // create a receiver for IntentService broadcasts
        receiver = new IntentServiceResultReceiver(new Handler());

        throbberView = findViewById(R.id.throbber_layout);
        findViewById(R.id.intent_service_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainIntentService.start(MainActivity.this, receiver);
            }
        });
        findViewById(R.id.service_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainService doesn't have directly callbacks for progress
                showThrobber(true);

                MainService.start(MainActivity.this);
            }
        });
        findViewById(R.id.service_with_rxjava_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNetworkManager().getWeatherUseCase().loadWeather(new GetWeatherData());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register a receiver for IntentService broadcasts
        receiver.setReceiver(this);

        // register an observer for architecture with Service
        getContentResolver().registerContentObserver(RequestsTable.URI, true, messagesObserver);

        registerRequestListener(this, GET_SETTINGS);
    }

    @Override
    public void onPause() {
        super.onPause();

        // clear receiver to avoid leaks.
        if (receiver != null) {
            receiver.setReceiver(null);
        }

        // unregister an observer for architecture with Service
        getContentResolver().unregisterContentObserver(messagesObserver);
    }

    private void showThrobber(boolean isVisible) {
        throbberView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    // Callbacks from IntentService
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

    @Override
    public void onRequestProgress(RequestEntry requestEntry, boolean inProgress) {

    }

    @Override
    public void onRequestSuccess(RequestEntry requestEntry) {

    }

    @Override
    public void onRequestFailed(RequestEntry requestEntry) {

    }
}