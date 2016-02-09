package modified.dobjanschi.a.pattern;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * @author Rustem
 */
public class BgProcessingIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    public BgProcessingIntentService() {
        super(BgProcessingIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(BgProcessingIntentService.class.getName(), "service started...");
        // sendBroadcast();

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String command = intent.getStringExtra("command");
        final Bundle bundle = new Bundle();
        if (command.equals("query")) {
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            Picasso.with(this.getApplicationContext())
                    .load(MainActivity.IMAGE_URL)
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
        Log.d(BgProcessingIntentService.class.getName(), "service stopping...");
        this.stopSelf();
    }
}