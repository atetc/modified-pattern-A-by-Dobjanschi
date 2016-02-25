package modified.dobjanschi.a.pattern.network;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * @author Rustem
 */
public class MainService extends Service {

    public static void start(@NonNull Context context) {
        // start a new server request.
        context.startService(new Intent(context, MainService.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(this, "Service created", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(MainService.this, "Service start command", Toast.LENGTH_SHORT).show();

        new RequestAsyncTask(this).execute();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
    }
}