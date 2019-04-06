package team1.fabflixmobile;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;

class NetworkManager {
    private static NetworkManager instance = null;
    RequestQueue queue;

    private NetworkManager() {
        NukeSSLCerts.nuke();  // disable ssl cert self-sign check
    }

    static NetworkManager sharedManager (Context ctx) {
        if (instance == null) {
            instance = new NetworkManager();
            instance.queue = Volley.newRequestQueue(ctx.getApplicationContext());
            // Create cookie
            CookieHandler.setDefault(new CookieManager());
        }
        return instance;
    }
}
