package ledongli.cn.mockgpspath.common;

import android.content.Context;

import de.greenrobot.event.EventBus;

/**
 * Created by wangyida on 15-1-18.
 */
public class GlobalConfig {

    private static Context sContext;

    public static void setAppContext(Context context) {
        sContext = context;
    }

    public static Context getAppContext() {
        return sContext;
    }

    public static EventBus getBus() {
        return EventBus.getDefault();
    }
}

