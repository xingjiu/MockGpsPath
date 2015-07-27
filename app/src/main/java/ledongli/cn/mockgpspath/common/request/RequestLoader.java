package ledongli.cn.mockgpspath.common.request;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;

import ledongli.cn.mockgpspath.common.GlobalConfig;
import ledongli.cn.mockgpspath.util.LogUtils;
import ledongli.cn.mockgpspath.util.NetWorkUtils;


/**
 * Created by wangyida on 15-3-24.
 */
public class RequestLoader {

    private static final boolean DEBUG = true;

    private static String TAG = RequestLoader.class.getSimpleName();

    public static interface ICallback<T> {
        void onSuccess(T result);

        void onFailed(String errMsg);
    }

    public static <Params, Progress, Result> void executeAsyncTask(
            AsyncTask<Params, Progress, Result> task, Params... params) {
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

    public static void executeRequest(Request request) {
        RequestManager.addNetworkRequest(request, RequestLoader.class.getSimpleName());
    }

    //无网络情况下强制从缓存中读取数据
    public static <T> void loadData(final String url, final Class<T> classOfT, final ICallback<T> callback) {
        boolean enforceLoadLocal = !NetWorkUtils.isNetworkAvailable(GlobalConfig.getAppContext());
        loadDataFromServer(url, enforceLoadLocal, classOfT, callback);
    }

    private static <T> void loadDataFromServer(final String url, final boolean loadFromLocal, final Class<T> classOfT, final ICallback<T> callback) {
        if (DEBUG) {
            Log.i(TAG, "loadDataFromServer : " + url);
        }
        XMJsonObjectRequest req = new XMJsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                executeAsyncTask(new AsyncTask<Object, Object, T>() {
                    @Override
                    protected T doInBackground(Object... params) {
                        if (DEBUG) {
                            Log.i(TAG, response.toString());
                        }

                        Gson gson = new Gson();
                        T ret = null;
                        try {
                            ret = gson.fromJson(response.toString(), classOfT);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return ret;
                    }


                    @Override
                    protected void onPostExecute(T result) {
                        super.onPostExecute(result);
                        if (callback != null) {
                            callback.onSuccess(result);
                        }
                    }
                });

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (callback != null) {
                    callback.onFailed(error.getMessage());
                }

            }
        });

        req.setEnforceLoadFromCache(loadFromLocal);
        executeRequest(req);
    }

    public static void postData(String url, final Map<String, String> params, final ICallback<String> callback) {
        XMPostRequest request = new XMPostRequest(url, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(callback != null) {
                    callback.onSuccess(s);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(callback != null) {
                    callback.onFailed(volleyError.getMessage());
                }
            }
        });
        executeRequest(request);
    }

    public static void postMultiRequest(String url, MultiRequestParam param, final ICallback<String> callback) {
        MultipartRequest request = new MultipartRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(callback != null) {
                    callback.onSuccess(s);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(callback != null) {
                    callback.onFailed(volleyError.getMessage());
                }
            }
        });
        for(String key : param.getStringPart().keySet()) {
            request.getMultiPartEntity().addStringPart(key, param.getStringPart().get(key));
        }
        for(String datakey : param.getDataPart().keySet()) {
            request.getMultiPartEntity().addBinaryPart(datakey, param.getDataPart().get(datakey));
        }

        for(String fileKey : param.getFileMap().keySet()) {
            request.getMultiPartEntity().addFilePart(fileKey, param.getFileMap().get(fileKey));
        }
        executeRequest(request);
    }

    public static <T> void loadDynamicBg(final String url, final Class<T> classOfT, final ICallback<T> callback) {
        Request request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtils.i("Dozen", " loadDynamicBg : " + s);
                Gson gson = new Gson();
                T ret = null;
                try {
                    ret = gson.fromJson(s, classOfT);
                    if (callback != null) {
                        callback.onSuccess(ret);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(callback != null) {
                    callback.onFailed(volleyError.getMessage());
                }
            }
        });
        executeRequest(request);
    }

}
