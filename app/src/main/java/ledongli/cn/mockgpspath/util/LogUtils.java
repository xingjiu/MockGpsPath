package ledongli.cn.mockgpspath.util;

import android.util.Log;

/**
 * Created by wangyida on 15-5-25.
 */
public class LogUtils {

    private static boolean sGlobalDebug = true;

    public static void i(String tag, String msg) {
        if(sGlobalDebug) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if(sGlobalDebug) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if(sGlobalDebug) {
            Log.e(tag, msg);
        }
    }
}
