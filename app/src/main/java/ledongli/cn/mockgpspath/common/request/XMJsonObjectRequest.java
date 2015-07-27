package ledongli.cn.mockgpspath.common.request;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by wangyida on 15-5-4.
 */
public class XMJsonObjectRequest extends JsonObjectRequest {

    protected boolean enforceLoadFromCache = false;

    public XMJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        enforceLoadFromCache = false;
    }

    public XMJsonObjectRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
        enforceLoadFromCache = false;
    }

    public void setEnforceLoadFromCache(boolean isEnforce) {
        enforceLoadFromCache = isEnforce;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(new JSONObject(jsonString),
                    XMRequestHelper.enforceClientCaching(HttpHeaderParser.parseCacheHeaders(response), response, enforceLoadFromCache));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
