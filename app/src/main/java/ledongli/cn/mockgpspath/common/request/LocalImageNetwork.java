package ledongli.cn.mockgpspath.common.request;

import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import ledongli.cn.mockgpspath.util.IOUtils;


/**
 * Create a fake network request to load a local image file
 * Created by wangyida on 15-6-1.
 */
public class LocalImageNetwork implements Network {

    private static final int STATUS_SUCESS_CODE = 200;

    @Override
    public NetworkResponse performRequest(Request<?> request) throws VolleyError {
        byte[] data = null;
        try {
            InputStream inputStream = new FileInputStream(request.getUrl());
            data = IOUtils.toByteArray(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new NetworkResponse(STATUS_SUCESS_CODE, data, request.getHeaders(), false,
                1);
    }
}
