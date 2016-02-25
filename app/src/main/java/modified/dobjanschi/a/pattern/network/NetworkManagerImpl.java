package modified.dobjanschi.a.pattern.network;

import android.content.Context;

import modified.dobjanschi.a.pattern.network.usecase.WeatherUseCase;
import ru.ok.okgifts.network.server.usecase.OkPhotosUseCaseImpl;
import ru.ok.okgifts.network.server.usecase.OkPresentsUseCaseImplOk;
import ru.ok.okgifts.network.server.usecase.OkSettingsUseCaseImpl;
import ru.ok.okgifts.network.server.usecase.OkTranslationUseCaseImpl;
import ru.ok.okgifts.network.server.usecase.OkWebSearchUseCaseImpl;
import ru.ok.okgifts.network.server.usecase.ProfileUseCaseImpl;
import ru.ok.okgifts.network.usecase.OkGiftsUseCase;
import ru.ok.okgifts.network.usecase.OkPhotosUseCase;
import ru.ok.okgifts.network.usecase.OkProfileUseCase;
import ru.ok.okgifts.network.usecase.OkSettingsUseCase;
import ru.ok.okgifts.network.usecase.OkTranslationUseCase;
import ru.ok.okgifts.network.usecase.OkWebSearchUseCase;

public class NetworkManagerImpl implements NetworkManager {

    private Context mAppContext;


    public NetworkManagerImpl(Context appContext) {
        mAppContext = appContext;
    }

    @Override
    public WeatherUseCase getWeatherUseCase() {
        return new WeatherUseCaseImpl(mAppContext);
    }
}