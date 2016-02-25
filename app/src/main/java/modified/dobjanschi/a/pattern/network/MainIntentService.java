package modified.dobjanschi.a.pattern.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import modified.dobjanschi.a.pattern.activity.LoadedImageActivity;
import modified.dobjanschi.a.pattern.callback.IntentServiceResultReceiver;

/**
 * @author Rustem
 */
public class MainIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    public static void start(Context context, IntentServiceResultReceiver receiver) {
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, context, MainIntentService.class);
        // optional: send Extra to IntentService
        // intent.putExtra("name", "value");
        intent.putExtra("receiver", receiver);
        intent.putExtra("command", "query");
        context.startService(intent);
    }

    public MainIntentService() {
        super(MainIntentService.class.getName());
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Toast.makeText(this, "Intent service started", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Toast.makeText(this, "Intent service: onHandle", Toast.LENGTH_SHORT).show();

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String command = intent.getStringExtra("command");
        if (command.equals("query")) {
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            // TODO: remove sleep
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final Bundle bundle = new Bundle();
            Picasso.with(this.getApplicationContext())
                    .load(LoadedImageActivity.IMAGE_URL)
                    .fetch(new Callback() {
                        @Override
                        public void onSuccess() {
                            receiver.send(STATUS_FINISHED, bundle);
                        }


                        @Override
                        public void onError() {
                            receiver.send(STATUS_ERROR, bundle);
                        }
                    });
        }

        Toast.makeText(this, "Intent service service stopping...", Toast.LENGTH_SHORT).show();
        this.stopSelf();
    }
}