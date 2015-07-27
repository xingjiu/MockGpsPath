package ledongli.cn.mockgpspath.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by wangyida on 15-7-22.
 */
public class ZipUtils {

    public static final byte[] compress(byte[] value) {
        byte[] compressedData = null;
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(value.length);
            try {
                GZIPOutputStream zipStream;
                zipStream = new GZIPOutputStream(byteStream);
                try {
                    zipStream.write(value);
                } finally {
                    zipStream.close();
                }
            } finally {
                byteStream.close();
            }

            compressedData = byteStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compressedData;
    }

    public static final byte[] decompress(byte[] value) {
        if (value == null)
            return null;
        ByteArrayOutputStream outputStream = null;
        ByteArrayInputStream inputStream = null;
        GZIPInputStream zipInputStream = null;
        byte[] ret;
        try {
            outputStream = new ByteArrayOutputStream();
            inputStream = new ByteArrayInputStream(value);
            zipInputStream = new GZIPInputStream(inputStream);
            byte[] arrayOfByte = new byte[1024];
            int i = -1;
            while ((i = zipInputStream.read(arrayOfByte)) != -1)
                outputStream.write(arrayOfByte, 0, i);
            ret = outputStream.toByteArray();
        } catch (IOException localIOException7) {
            ret = null;
            localIOException7.printStackTrace();
        } finally {
            IOUtils.closeQuietly(zipInputStream);
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
        return ret;
    }
}
