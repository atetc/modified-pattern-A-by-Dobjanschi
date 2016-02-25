package modified.dobjanschi.a.pattern.callback;

import modified.dobjanschi.a.pattern.network.request.RequestEntry;

/**
 * Created by: Alex
 * Date creation: 06.08.15.
 */
public interface RequestListener {

    /**
     * Called when request in progress.
     *
     * @param requestEntry the request data.
     * @param inProgress   TRUE - the progress started. Otherwise finished.
     */
    void onRequestProgress(RequestEntry requestEntry, boolean inProgress);

    /**
     * The request status is Success.
     *
     * @param requestEntry the request data.
     */
    void onRequestSuccess(RequestEntry requestEntry);

    /**
     * The request status is Failed.
     *
     * @param requestEntry the request data.
     */
    void onRequestFailed(RequestEntry requestEntry);
}
