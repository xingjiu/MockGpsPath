package ledongli.cn.mockgpspath.util;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by xiangying on 7/24/15.
 * desc:
 */
public class FileWriterUtil {
    private static final String TAG = "FileWriterUtil";

    private String mFileName;
    private String mFilePath;
    private String mFileWithFullPath;

    public FileWriterUtil(String fileName) {
        this.mFileName = fileName;
        this.mFilePath = Environment.getExternalStorageDirectory() + "/ledongli";
        this.mFileWithFullPath = this.mFilePath + "/" + fileName;
        LogUtils.d(TAG, "filePath="+mFileWithFullPath);
        ensureFileExist();
    }

    public void writeToEnd(String str) {
        String sdStatus = Environment.getExternalStorageState();
        if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        try {

            FileWriter writer = new FileWriter(mFileWithFullPath);
            writer.write(str);
            writer.close();


        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void ensureFileExist() {
        File path = new File(mFilePath);
        File file = new File(mFileWithFullPath);
        if (!path.exists()) {
            path.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
