package modified.dobjanschi.a.pattern.network.responce;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Response {

    public static final int NO_ERROR = 0;

    public static final int PARAM_SESSION_EXPIRED = 102;

    /**
     * The error code from server.
     */
    @SuppressWarnings("unused")
    @SerializedName("error_code")
    @Nullable
    private Integer mErrorCode;

    /**
     * The error message from server.
     */
    @SuppressWarnings("unused")
    @SerializedName("error_msg")
    @Nullable
    private String mErrorMsg;

    /**
     * The raw response.
     */
    @Nullable
    transient private String mRaw;

    public int getErrorCode() {
        return mErrorCode == null ? 0 : mErrorCode;
    }

    @Nullable
    public String getErrorMsg() {
        return mErrorMsg;
    }

    /**
     * @return the origin response data.
     */
    @Nullable
    public String getRaw() {
        return mRaw;
    }

    /**
     * Method to setup Raw response.
     *
     * @param raw the server response.
     */
    public void setRaw(@Nullable String raw) {
        mRaw = raw;
    }

    /**
     * The savable interface that used to save data to request entry.
     */
    public interface Savable {
        /**
         * @return data that will be saved to request body.
         */
        String getBody();
    }
}
