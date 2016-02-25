package modified.dobjanschi.a.pattern;

import android.app.Application;

import modified.dobjanschi.a.pattern.network.NetworkManager;
import modified.dobjanschi.a.pattern.network.NetworkManagerImpl;

/**
 * @author Rustem
 */
public class AppDelegate extends Application {

    /**
     * The network manager for data loading.
     */
    private NetworkManager networkManager;

    public NetworkManager getNetworkManager() {
        if (networkManager == null) {
            networkManager = new NetworkManagerImpl(getApplicationContext());
        }
        return networkManager;
    }
}