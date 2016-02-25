package modified.dobjanschi.a.pattern.network.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Rustem
 */
public class GetWeatherData implements RequestData {

    @NonNull
    @Override
    public RequestType getType() {
        return RequestType.GET_WEATHER;
    }

    @Nullable
    @Override
    public String getKey() {
        return null;
    }
}