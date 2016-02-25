package modified.dobjanschi.a.pattern.base;

import android.support.v7.app.AppCompatActivity;

import modified.dobjanschi.a.pattern.AppDelegate;
import modified.dobjanschi.a.pattern.network.NetworkManager;

/**
 * @author Rustem
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * @return manager for network requests.
     */
    public NetworkManager getNetworkManager() {
        // Implementation depends on cases (Network/Demo)
        return ((AppDelegate) getApplicationContext()).getNetworkManager();
    }
}