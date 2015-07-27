package ledongli.cn.mockgpspath.util;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.List;

import ledongli.cn.mockgpspath.common.GlobalConfig;


/**
 * Created by wangyida on 15-4-15.
 */
public class ApkUtils {

    public static void installApkByPath(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static File getApkFile(String apkTitle) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Download", apkTitle);
        return file;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void installApkById(Context context, long downloadId) {
        if (downloadId == -1) {
            return;
        }
        Intent install = new Intent(Intent.ACTION_VIEW);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadFileUri = downloadManager.getUriForDownloadedFile(downloadId);
        install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
    }

    public static void installApkByName(Context context, String appName) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + getDownloadedApkPath(appName)), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    private static String getDownloadedApkPath(String appName) {
        File file = new File(SDCardUtils.getExternalFileDir(GlobalConfig.getAppContext()), appName);
        return file.getAbsolutePath();
    }

    public static boolean isApkDownloaded(String appName) {
        if(TextUtils.isEmpty(appName)) {
            return false;
        }
        File downloadDir = new File(SDCardUtils.getExternalFileDir(GlobalConfig.getAppContext()));
        if(downloadDir.exists() && downloadDir.isDirectory()) {
            for(File downloadFile : downloadDir.listFiles()) {
                if(downloadFile.getName().contains(appName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void uninstallAppbyPackageName(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static int checkApkVersion(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return 0;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            if (info == null) {
                return 0;
            }

            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_ACTIVITIES);
            if (info == null) {
                return false;
            }
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getAppTitleByPkgName(String pkgName) {
        final PackageManager pm = GlobalConfig.getAppContext().getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
        for (ResolveInfo info : list) {
            if (pkgName.equals(info.activityInfo.packageName)) {
                return String.valueOf(info.activityInfo.loadLabel(pm));
            }
        }
        return null;
    }

}
