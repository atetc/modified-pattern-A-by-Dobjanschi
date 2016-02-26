package modified.dobjanschi.a.pattern.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;

import modified.dobjanschi.a.pattern.R;
import modified.dobjanschi.a.pattern.database.tables.RequestsTable;
import modified.dobjanschi.a.pattern.service.model.RequestItem;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Rustem
 */
public class ServiceWithRxJava extends Service {

    public static final String YOUR_URL = "http://catalog.data.gov/harvest/object/5decbb23-fff9-4fcd-b37b-7f5a52df0583";

    private Subscription subscribe;

    public static void start(@NonNull Context context) {
        // start a new server request.
        context.startService(new Intent(context, ServiceWithRxJava.class));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(ServiceWithRxJava.this, "Service started", Toast.LENGTH_SHORT).show();

        subscribe = Observable.create(new Observable.OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Response response = client.newCall(new Request.Builder().url(YOUR_URL).build()).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext(response);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new Exception("error"));
                    }
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<Response>() {
                    @Override
                    public void onCompleted() {
                        getContentResolver().notifyChange(RequestsTable.URI, null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Do anything
                    }

                    @Override
                    public void onNext(Response response) {
                        // Do anything
                        RequestItem requestItem = new RequestItem("request1");
                        requestItem.setResponse(response.toString());
                        RequestsTable.save(ServiceWithRxJava.this, requestItem);
                    }
                });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }

        Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
    }
}