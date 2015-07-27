package ledongli.cn.mockgpspath.util;

import android.content.Context;
import android.os.Environment;

/** 
 * 
 */
public class SDCardUtils {

    public static boolean sdcardWriteable() {
        String state = Environment.getExternalStorageState();
        if(!Environment.MEDIA_MOUNTED.equals(state))
            return false;
        if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
            return false;
        return true;
    }

    public static String getExternalFileDir(Context context) {
        final String fileDir = "/Android/data/" + context.getPackageName() + "/files/";
        return Environment.getExternalStorageDirectory().getPath() + fileDir;
    }

    public static String getExternalJsonDir(Context context) {
        final String fileDir = "/Android/data/" + context.getPackageName() + "/json/";
        return Environment.getExternalStorageDirectory().getPath() + fileDir;
    }


}
