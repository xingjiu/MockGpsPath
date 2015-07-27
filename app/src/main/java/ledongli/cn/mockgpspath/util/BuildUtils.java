package ledongli.cn.mockgpspath.util;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import ledongli.cn.mockgpspath.common.GlobalConfig;


/**
 * Created by wangyida on 15-5-18.
 */

public class BuildUtils {

    private final static boolean JELLY_BEAN_PLUS = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static long getMemSize() {
        if (JELLY_BEAN_PLUS) {
            ActivityManager actManager = ((ActivityManager) GlobalConfig.getAppContext()
                    .getSystemService(Context.ACTIVITY_SERVICE));
            ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
            actManager.getMemoryInfo(memInfo);
            long totalMemory = memInfo.totalMem;
            return totalMemory;
        } else {
            String str1 = "/proc/meminfo";
            String str2;
            String[] arrayOfString;
            long initial_memory = 0;
            try {
                FileReader localFileReader = new FileReader(str1);
                BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
                str2 = localBufferedReader.readLine();

                arrayOfString = str2.split("\\s+");

                initial_memory = Integer.valueOf(arrayOfString[1]).intValue();
                localBufferedReader.close();

            } catch (IOException e) {
            }
            return initial_memory;
        }

    }

    /**
     * 内存超过1G为高性能手机
     *
     * @return
     */
    public static boolean isHightPerformanceMobile() {
        return getMemSize() > 1024 * 1024 * 1024;
    }
}
