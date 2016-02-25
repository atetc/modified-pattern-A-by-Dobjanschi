package modified.dobjanschi.a.pattern.network;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import modified.dobjanschi.a.pattern.network.exception.SessionExpiredException;
import modified.dobjanschi.a.pattern.network.request.RequestEntry;
import rx.Observer;
import rx.Subscription;
import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;

import modified.dobjanschi.a.pattern.network.executor.RequestExecutor;
import modified.dobjanschi.a.pattern.network.request.RequestData;
import modified.dobjanschi.a.pattern.network.responce.Response;


/**
 * @author Rustem
 */
public class NetworkService extends Service {

    private static final String ARG_EXECUTOR = "ARG_EXECUTOR";

    /**
     * The looper for downloading.
     */
    private volatile Looper mServiceLooper;

    /**
     * The one by one download handler.
     */
    private volatile Handler mRequestHandler;

    /**
     * The subscriptions map to make un-subscribe logic. Used in different threads.
     */
    @NonNull
    private Map<RequestKey, RequestWrapper> mSubscriptionMap = new ConcurrentHashMap<>();

    /**
     * Used to make request.
     *
     * @param context  the application context.
     * @param executor the request executor with request data.
     * @param <T>      interface of request.
     */
    public static <T extends RequestData> void executeRequest(Context context,
                                                              @NonNull RequestExecutor<T> executor) {
        // start a new server request.
        context.startService(
                new Intent(context, NetworkService.class)
                        .putExtra(ARG_EXECUTOR, executor)
        );
    }

    /**
     * Helper method to build query to find request entry.
     *
     * @param requestKey instance for search.
     * @return the query instance of search result.
     */
    private static RequestEntry findRequestEntry(RequestKey requestKey) {
        // prepare request to remove previous requests.
        SQLiteQuery<RequestEntry> result = SQLite
                .where(RequestEntry.class)
                .equalTo(RequestEntry.TYPE, requestKey.mType);

        // add key restriction to request.
        if (!TextUtils.isEmpty(requestKey.mKey)) {
            result.and().equalTo(RequestEntry.KEY, requestKey.mKey);
        }

        return result;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // get intent parameters.
        RequestExecutor<?> requestExecutor = (RequestExecutor<?>)
                (intent == null ? null : intent.getSerializableExtra(ARG_EXECUTOR));
        if (requestExecutor == null) {
            Toast.makeText(this, "Service with RxJava: received empty request", Toast.LENGTH_SHORT).show();
            return 0;
        }

        // proceed request message in a handler.
        mRequestHandler.obtainMessage(0, new RequestKey(requestExecutor)).sendToTarget();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "Network service: stopped", Toast.LENGTH_SHORT).show();
    }

    /**
     * The type/key container.
     */
    @SuppressWarnings("SimplifiableIfStatement")
    private static class RequestKey implements Serializable {
        /**
         * The instance of executor.
         */
        private RequestExecutor<?> mRequestExecutor;
        /**
         * The request type.
         */
        private String mType;
        /**
         * The request Key.
         */
        private String mKey;

        public RequestKey(RequestExecutor<?> executor) {
            mRequestExecutor = executor;
            mType = executor.getData().getType().name();
            mKey = executor.getData().getKey();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RequestKey that = (RequestKey) o;

            if (mType != null ? !mType.equals(that.mType) : that.mType != null)
                return false;
            return !(mKey != null ? !mKey.equals(that.mKey) : that.mKey != null);

        }

        @Override
        public int hashCode() {
            int result = mType != null ? mType.hashCode() : 0;
            result = 31 * result + (mKey != null ? mKey.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return mType + ":" + mKey;
        }
    }

    private class RequestWrapper {
        private Subscription mSubscription;
        private NetworkObserver mObserver;

        public RequestWrapper(Subscription subscription, NetworkObserver observer) {
            mSubscription = subscription;
            mObserver = observer;
        }
    }

    /**
     * Helper observer for proceed requests result.
     */
    private static class NetworkObserver implements Observer<Response> {

        private final NetworkService mService;

        private final RequestKey mRequestKey;

        private boolean mIsCancelled;

        public NetworkObserver(RequestKey requestKey, NetworkService service) {
            mRequestKey = requestKey;
            mService = service;
        }

        public void setIsCancelled(boolean isCancelled) {
            mIsCancelled = isCancelled;
        }

        @Override
        public void onCompleted() {
            if (mIsCancelled) {
                return;
            }

            RequestEntry requestEntry = findRequestEntry(mRequestKey).one();
            if (requestEntry != null) {
                SQLite.beginTransaction();
                requestEntry.setResponse(200);
                requestEntry.setStatus(RequestEntry.Status.SUCCESS);
                SQLite.update(requestEntry);
                SQLite.endTransaction();
            }

            // remove subscription from map.
            mService.mSubscriptionMap.remove(mRequestKey);

            Toast.makeText(mService, "NETWORK_SERVICE: Request completed: " + mRequestKey, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(final Throwable throwable) {
            if (mIsCancelled) {
                return;
            }
            RequestEntry requestEntry = findRequestEntry(mRequestKey).one();
            if (requestEntry != null) {
                RequestEntry.Status errorStatus = RequestEntry.Status.ERROR;

                if (throwable instanceof RetrofitError) {
                    RetrofitError e = (RetrofitError) throwable;
                    // ffs retrofit
                    if (e.getResponse() != null) {
                        if (e.getResponse().getBody() != null) {
                            requestEntry.setBody(new String(((TypedByteArray) e.getResponse().getBody()).getBytes()));
                        }
                        requestEntry.setResponse(e.getResponse().getStatus());
                    }
                    if (RetrofitError.Kind.NETWORK.equals(e.getKind())) {
                        errorStatus = RequestEntry.Status.NETWORK_ERROR;
                    }
                } else if (throwable instanceof SessionExpiredException) {
                    errorStatus = RequestEntry.Status.SESSION_EXPIRED;
                } else if (throwable instanceof UnknownHostException) {
                    errorStatus = RequestEntry.Status.NETWORK_ERROR;
                }

                requestEntry.setStatus(errorStatus);
                SQLite.update(requestEntry);
            }

            // remove subscription from map.
            mService.mSubscriptionMap.remove(mRequestKey);

            Toast.makeText(mService, "NETWORK_SERVICE: Request error: " + mRequestKey, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(final Response response) {
            if (mIsCancelled) {
                return;
            }

            Toast.makeText(mService, "NETWORK_SERVICE: Request success: " + mRequestKey, Toast.LENGTH_SHORT).show();
            if (!(response instanceof Response.Savable)) {
                return;
            }

            // will add a new response body to DB.
            RequestEntry requestEntry = findRequestEntry(mRequestKey).one();
            if (requestEntry != null) {
                requestEntry.setBody(((Response.Savable) response).getBody());
                SQLite.update(requestEntry);
            }
        }
    }
}