
package ledongli.cn.mockgpspath.common.request;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

import ledongli.cn.mockgpspath.common.GlobalConfig;
import ledongli.cn.mockgpspath.common.cache.BitmapLruCache;
import ledongli.cn.mockgpspath.util.CacheUtils;


public class RequestManager {
    public static RequestQueue mNetworkRequestQueue = newNetworkLoaderQueue();

    public static RequestQueue mLocalRequestQueue = newLocalLoaderQueue();

    public static final int MEM_CACHE_SIZE = 1024 * 1024 * ((ActivityManager) GlobalConfig.getAppContext()
            .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() / 4;

    public static BitmapLruCache mBitmapCache = new BitmapLruCache(MEM_CACHE_SIZE);

    private static ImageLoader mNetworkImageLoader = new XMImageLoader(mNetworkRequestQueue, mBitmapCache);

    private static ImageLoader mLocalImageLoader = new XMImageLoader(mLocalRequestQueue, mBitmapCache);

    private RequestManager() {
    }

    private static Cache openCache() {
        return new DiskBasedCache(CacheUtils.getExternalCacheDir(GlobalConfig.getAppContext()),
                10 * 1024 * 1024);
    }

    private static RequestQueue newNetworkLoaderQueue() {
        RequestQueue requestQueue = new RequestQueue(openCache(), new BasicNetwork(new HurlStack()));
        requestQueue.start();
        return requestQueue;
    }

    private static RequestQueue newLocalLoaderQueue() {
        RequestQueue requestQueue = new RequestQueue(openCache(), new LocalImageNetwork());
        requestQueue.start();
        return requestQueue;
    }

    public static void addNetworkRequest(Request request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mNetworkRequestQueue.add(request);
    }

    public static void cancelAllNetworkRequests(Object tag) {
        if (mNetworkRequestQueue != null) {
            mNetworkRequestQueue.cancelAll(tag);
        }
    }


    private static ImageLoader.ImageContainer loadLocalImage(String filePath, ImageLoader.ImageListener imageListener, int maxWidth, int maxHeight) {
        return mLocalImageLoader.get(filePath, imageListener, maxWidth, maxHeight, ImageView.ScaleType.CENTER);
    }

    private static ImageLoader.ImageContainer loadNetworkImage(String requestUrl,
                                                               ImageLoader.ImageListener imageListener, int maxWidth, int maxHeight) {
        return mNetworkImageLoader.get(requestUrl, imageListener, maxWidth, maxHeight, ImageView.ScaleType.CENTER);
    }

    public static ImageLoader.ImageListener getImageListener(final ImageView view,
                                                             final Drawable defaultImageDrawable, final Drawable errorImageDrawable) {
        return new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageDrawable != null) {
                    view.setImageDrawable(errorImageDrawable);
                }
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
//                if (response.getBitmap() != null) {
//                    if (!isImmediate && defaultImageDrawable != null) {
//                        TransitionDrawable transitionDrawable = new TransitionDrawable(
//                                new Drawable[]{
//                                        defaultImageDrawable,
//                                        new BitmapDrawable(GlobalConfig.getAppContext().getResources(),
//                                                response.getBitmap())
//                                });
//                        transitionDrawable.setCrossFadeEnabled(true);
//                        view.setImageDrawable(transitionDrawable);
//                        transitionDrawable.startTransition(100);
//                    } else {
//                        view.setImageBitmap(response.getBitmap());
//                    }
//                } else if (defaultImageDrawable != null) {
//                    view.setImageDrawable(defaultImageDrawable);
//                }
                  view.setImageBitmap(response.getBitmap());

            }
        };
    }

    public static ImageLoader.ImageContainer loadImage(String requestUrl, ImageLoader.ImageListener imageListener) {
        ImageLoader.ImageContainer ret = null;
        if (requestUrl.startsWith("http")) {
            ret = loadNetworkImage(requestUrl, imageListener, 0, 0);
        } else {
            ret = loadLocalImage(requestUrl, imageListener, 0, 0);
        }
        return ret;
    }

}
