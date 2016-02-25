package modified.dobjanschi.a.pattern.network.usecase;

import android.support.annotation.NonNull;

import modified.dobjanschi.a.pattern.network.request.GetWeatherData;

/**
 * @author Rustem
 */
public interface WeatherUseCase {

    void loadWeather(@NonNull GetWeatherData data);
}