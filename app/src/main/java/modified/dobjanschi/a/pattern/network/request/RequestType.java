package modified.dobjanschi.a.pattern.network.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public enum RequestType {
    GET_SETTINGS,
    GET_USER_PROFILE,
    GET_WEATHER,
    GET_IMAGES_FROM_WEB,
    UNKNOWN;

    @NonNull
    public static RequestType fromString(@Nullable String type) {
        if (!TextUtils.isEmpty(type)) {
            for (RequestType requestType : values()) {
                if (requestType.name().equalsIgnoreCase(type)) {
                    return requestType;
                }
            }
        }

        return UNKNOWN;
    }
}