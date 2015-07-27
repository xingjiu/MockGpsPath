package ledongli.cn.mockgpspath.util;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by wangyida on 15-7-18.
 */
public class UserInfo {

    public static String getIMEI(Context context){
        try {
            final TelephonyManager telephonyMgr =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyMgr != null)
                return telephonyMgr.getDeviceId();
            else
                return "0";
        } catch (Exception e) {
            return "0";
        }
    }
}
