package modified.dobjanschi.a.pattern.network.usecase;

import android.content.Context;
import android.support.annotation.NonNull;

import modified.dobjanschi.a.pattern.network.NetworkService;
import modified.dobjanschi.a.pattern.network.executor.RequestExecutor;
import modified.dobjanschi.a.pattern.network.request.RequestData;

public abstract class BaseUseCase {

    /**
     * Application context.
     */
    @NonNull
    private Context mContext;

    public BaseUseCase(@NonNull Context context) {
        mContext = context;
    }

    /**
     * Called to make server request.
     *
     * @param executor request executor.
     */
    protected void makeRequest(@NonNull RequestExecutor<? extends RequestData> executor) {
        NetworkService.executeRequest(mContext, executor);
    }
}
