package modified.dobjanschi.a.pattern.network.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.Serializable;

import droidkit.annotation.SQLiteColumn;
import droidkit.annotation.SQLiteObject;
import droidkit.annotation.SQLitePk;

public class RequestEntry implements Serializable {

    public static final String TYPE = "type";
    public static final String KEY = "key";
    public static final String STATUS = "status";
    public static final String RESPONSE = "response";
    public static final String BODY = "body";

    @SuppressWarnings("unused")
    @SQLitePk
    private long mId;

    @SQLiteColumn(TYPE)
    private String mType;

    @SQLiteColumn(KEY)
    private String mKey;

    @SQLiteColumn(STATUS)
    private String mStatus;

    @SQLiteColumn(RESPONSE)
    private int mResponse;

    @SQLiteColumn(BODY)
    private String mBody;

    @SuppressWarnings("unused")
    public RequestEntry() {

    }

    public static RequestEntry create(@NonNull RequestType type, @Nullable String key) {
        final RequestEntry entry = new RequestEntry();
        entry.mType = type.name();
        entry.mKey = key;
        return entry;
    }

    @NonNull
    public Status getStatus() {
        return Status.valueOfStr(mStatus);
    }

    public void setStatus(@NonNull Status status) {
        mStatus = status.name();
    }

    @NonNull
    public RequestType getType() {
        return RequestType.fromString(mType);
    }

    public void setType(RequestType type) {
        mType = type.name();
    }

    @Nullable
    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public int getResponse() {
        return mResponse;
    }

    public void setResponse(int response) {
        mResponse = response;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }

    @Override
    public String toString() {
        //noinspection StringBufferReplaceableByString
        final StringBuilder sb = new StringBuilder("RequestEntry{");
        sb.append("mId=").append(mId);
        sb.append(", mType='").append(mType).append('\'');
        sb.append(", mKey='").append(mKey).append('\'');
        sb.append(", mStatus='").append(mStatus).append('\'');
        sb.append(", mResponse=").append(mResponse);
        sb.append(", mBody='").append(mBody).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Network request statuses.
     */
    public enum Status {
        UNKNOWN,
        IN_PROGRESS,
        SUCCESS,
        ERROR,
        NETWORK_ERROR,
        SESSION_EXPIRED;

        public static Status valueOfStr(String statusStr) {
            if (!TextUtils.isEmpty(statusStr)) {
                for (Status status : Status.values()) {
                    if (status.name().equalsIgnoreCase(statusStr)) {
                        return status;
                    }
                }
            }

            return Status.UNKNOWN;
        }
    }
}