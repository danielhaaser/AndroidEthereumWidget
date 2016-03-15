package layout;

import android.app.Application;
import android.content.Context;

/**
 * Created by daniel on 3/15/16.
 */
public class MyApp extends android.app.Application {

    private static MyApp instance;

    public MyApp() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }
}
