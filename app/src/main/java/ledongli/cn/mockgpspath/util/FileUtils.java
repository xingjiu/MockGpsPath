package ledongli.cn.mockgpspath.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by wangyida on 15-6-2.
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    public static File getCacheFile(Context context, String fileName) {
        return new File(context.getExternalFilesDir(null), fileName);
    }

    public static <T> T readBeanFromFile(Context context, String fileName, Class<T> classOfT) {
        long start = System.currentTimeMillis();
        File file = getCacheFile(context, fileName);
        FileInputStream fs = null;
        ObjectInputStream os = null;
        T ret = null;
        try {
            fs = new FileInputStream(file);
            os = new ObjectInputStream(fs);
            ret = (T) os.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        IOUtils.closeQuietly(fs);
        IOUtils.closeQuietly(os);
        long end = System.currentTimeMillis();
        LogUtils.i(TAG, " readBean : " + file.getAbsolutePath() + " cost time : " + (end - start));

        return ret;
    }

    public static void writeBeanToFile(Context context, String fileName, Object object) {
        File file = getCacheFile(context, fileName);
        FileOutputStream fs = null;
        ObjectOutputStream os = null;
        try {
            fs = new FileOutputStream(file);
            os = new ObjectOutputStream(fs);
            os.writeObject(object);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        IOUtils.closeQuietly(fs);
        IOUtils.closeQuietly(os);
    }


}
