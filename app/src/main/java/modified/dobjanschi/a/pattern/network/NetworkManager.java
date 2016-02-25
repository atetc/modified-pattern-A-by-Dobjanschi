package modified.dobjanschi.a.pattern.network;

import modified.dobjanschi.a.pattern.network.usecase.WeatherUseCase;

public interface NetworkManager {

    WeatherUseCase getWeatherUseCase();
}