package ledongli.cn.mockgpspath.common.request;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageRequest;

/**
 * Created by wangyida on 15-5-4.
 */
public class XMImageRequest extends ImageRequest {

    public XMImageRequest(String url, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, Response.ErrorListener errorListener) {
        super(url, listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
    }

    @Override
    protected Response<Bitmap> getBitmapResponse(Bitmap bitmap, NetworkResponse response) {
        return Response.success(bitmap, XMRequestHelper.enforceClientCaching(HttpHeaderParser.parseCacheHeaders(response), response, true));
    }
}
