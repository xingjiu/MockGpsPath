package ledongli.cn.mockgpspath.util;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ledongli.cn.mockgpspath.common.GlobalConfig;


public final class ImageUtils {

    /**
     * 利用Canvas和View创建图片
     *
     * @param view
     * @return
     */
    public static Bitmap loadBitmapFromView(View view) {

        if (view == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        return screenshot;
    }

    public static boolean saveBitmapByPath(String path, Bitmap mBitmap) {

        File f = new File(path);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            mBitmap.compress(CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return true;
    }

    public static Bitmap scaleBitmap(Bitmap paramBitmap, int width, int height) {
        if (paramBitmap == null)
            return paramBitmap;

        float i = paramBitmap.getWidth();
        float j = paramBitmap.getHeight();
        float f = Math.min(width / i, height / j);
        if (f < 1.0F)
            paramBitmap = Bitmap.createScaledBitmap(paramBitmap, Math.round(f * i), Math.round(f * j), true);
        return paramBitmap;
    }

    public byte[] toByteArray(Bitmap paramBitmap) {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        paramBitmap.compress(CompressFormat.PNG, 100, localByteArrayOutputStream);
        return localByteArrayOutputStream.toByteArray();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Bitmap createMutableBitmap(Bitmap src) {
        if (src == null) {
            return null;
        }
        return src.copy(Bitmap.Config.ARGB_8888, true);
    }

    // Bitmap → byte[]
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 90, baos);

        int options = 90;
        while (baos.toByteArray().length / 1024 > 60 && options > 30) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            options -= 5;// 每次都减少5
            baos.reset();// 重置baos即清空baos
            bm.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }

        return baos.toByteArray();
    }

    // byte[] → Bitmap
    public static Bitmap bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static Bitmap cropImage(Bitmap bitmap, int left, int top, int width, int height) {
        Bitmap ret = Bitmap.createBitmap(bitmap, left, top, width, height, null, false);
        bitmap.recycle();
        return ret;
    }

    public static void saveBitmapByName(String destName, Bitmap bitmap) {
        try {
            File f = new File(GlobalConfig.getAppContext().getExternalFilesDir(null), destName);
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fOut = new FileOutputStream(f);
            bitmap.compress(CompressFormat.JPEG, 60, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the size in bytes of a bitmap.
     *
     * @param bitmap
     * @return size in bytes
     */
    @SuppressLint("NewApi")
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getBounds().width();
        int height = drawable.getBounds().height();
        if (width <= 0) {
            width = drawable.getIntrinsicWidth();
            height = drawable.getIntrinsicHeight();
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }

        boolean isOpacity = drawable.getOpacity() == PixelFormat.OPAQUE;

        Bitmap bitmap = Bitmap.createBitmap(width, height, isOpacity ? Bitmap.Config.RGB_565
                : Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return bitmap;
    }



    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeBitmapFromResource(int resId, int reqWidth, int reqHeight) {
        Resources res = GlobalConfig.getAppContext().getResources();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeBitmapFromSdCard(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
}
