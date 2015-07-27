package ledongli.cn.mockgpspath.common.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by wangyida on 15-7-18.
 */
public class XMPostRequest extends StringRequest {

    private Map<String, String> mParams;

    public XMPostRequest(String url, Map params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);
        mParams = params;
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

}
