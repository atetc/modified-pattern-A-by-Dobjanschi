package modified.dobjanschi.a.pattern;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements BgProcessingResultReceiver.Receiver {

    public static final String IMAGE_URL = "http://interfacelift.com/wallpaper/D96e1f66/03490_serenesunset_4096x2160.jpg";

    public BgProcessingResultReceiver receiver;
    private View throbberView;
    private ImageView imageView;
    private View errorView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Picasso.with(this).invalidate(IMAGE_URL);

        throbberView = findViewById(R.id.throbber_layout);
        errorView = findViewById(R.id.error_layout);
        imageView = (ImageView) findViewById(R.id.image_view);
        findViewById(R.id.retry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoading();
            }
        });

        startLoading();
    }

    private void startLoading() {
        // register a receiver for IntentService broadcasts
        receiver = new BgProcessingResultReceiver(new Handler());
        receiver.setReceiver(this);
        // start IntentService
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, this, BgProcessingIntentService.class);
        // optional: send Extra to IntentService
        // intent.putExtra("name", "value");
        intent.putExtra("receiver", receiver);
        intent.putExtra("command", "query");
        startService(intent);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case BgProcessingIntentService.STATUS_RUNNING:
                throbberView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                //show progress
                Toast.makeText(this, "Running", Toast.LENGTH_SHORT).show();
                break;
            case BgProcessingIntentService.STATUS_FINISHED:
                throbberView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);

                Picasso.with(this).load(IMAGE_URL).into(imageView);
                // hide progress & analyze bundle
                Toast.makeText(this, "Loaded", Toast.LENGTH_SHORT).show();
                break;
            case BgProcessingIntentService.STATUS_ERROR:
                throbberView.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
                // handle the error;
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (receiver != null) {
            receiver.setReceiver(null); // clear receiver to avoid leaks.
        }
    }
}