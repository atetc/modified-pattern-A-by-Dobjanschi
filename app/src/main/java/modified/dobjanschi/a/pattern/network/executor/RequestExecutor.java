package modified.dobjanschi.a.pattern.network.executor;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;

import modified.dobjanschi.a.pattern.network.request.RequestData;
import modified.dobjanschi.a.pattern.network.responce.Response;
import rx.Observable;
import rx.functions.Func1;


public class RequestExecutor<T extends RequestData> implements Func1<Context, Observable<? extends Response>>,
        Serializable {

    /**
     * The data for request processing.
     */
    @NonNull
    private T mData;

    public RequestExecutor(@NonNull T data) {
        mData = data;
    }

    @NonNull
    public T getData() {
        return mData;
    }

    /**
     * Generates new observable passing all needed params straight to it.
     *
     * @param context Context that might be used during request. Should be "unboxed" to application
     *                context by calling {@link Context#getApplicationContext()}. Reason is that
     *                request can take a while to execute and activity context might not live long
     *                enough.
     * @return Observable with setup request.
     */
    @Override
    @NonNull
    public Observable<? extends Response> call(@NonNull Context context) {
        return Observable.empty();
    }

    /**
     * Helper enum for request methods.
     */
    public enum MethodType {
        GET, POST
    }
}
