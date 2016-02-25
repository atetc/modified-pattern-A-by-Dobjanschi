package modified.dobjanschi.a.pattern.network.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

public interface RequestData extends Serializable {

    @NonNull
    RequestType getType();

    /**
     * @return additional key for request. Could be NULL.
     */
    @Nullable
    String getKey();
}
