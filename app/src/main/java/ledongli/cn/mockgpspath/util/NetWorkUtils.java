
package ledongli.cn.mockgpspath.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

public class NetWorkUtils {
    private static java.net.Proxy mProxy;
    
    public static <T> T createHttpRequest(Context context, String url, boolean isGetRequest) {
        String proxyHost = null;
        int proxyPort = -1;
        boolean useProxy = false;

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivity == null ? null : connectivity.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.getType() != ConnectivityManager.TYPE_WIFI) {
            proxyHost = getHost(context, url);
            proxyPort = getPort(context, url);
            useProxy = (proxyHost != null && proxyPort > 0);
        }

        HttpRequest request = null;

        if (isGetRequest) {
            try {
                request = new HttpGet(new String(url.getBytes(), "UTF-8"));
            } catch (Exception e) {
                Log.w("OnlineUtilites", e);
            }
        } else {
            try {
                request = new HttpPost(new String(url.getBytes(), "UTF-8"));
            } catch (Exception e) {
                Log.w("OnlineUtilites", e);
            }
        }
        if (request == null) {
            return null;
        }
        if (useProxy) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            ConnRouteParams.setDefaultProxy(request.getParams(), proxy);
        }

        return (T) request;
    }

    public static String getHost(Context context, String url) {
        if (Build.VERSION.SDK_INT < 16) {
            return android.net.Proxy.getHost(context);
        } else {
            if (mProxy == null) {
                try {
                    Class<?> proxy = android.net.Proxy.class;
                    Method getProxyMethod = proxy.getDeclaredMethod("getProxy", new Class<?>[] {
                            Context.class, String.class
                    });
                    if (getProxyMethod != null) {
                        mProxy = (java.net.Proxy) getProxyMethod.invoke(null, new Object[] {
                                context, url
                        });
                    }
                } catch (Exception e) {
                }
            }
            if (mProxy != null) {
                if (mProxy == java.net.Proxy.NO_PROXY)
                    return null;
                try {
                    return ((InetSocketAddress) (mProxy.address())).getHostName();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    public static int getPort(Context context, String url) {
        if (Build.VERSION.SDK_INT < 16) {
            return android.net.Proxy.getPort(context);
        } else {
            if (mProxy == null) {
                try {
                    Class<?> proxy = android.net.Proxy.class;
                    Method getProxyMethod = proxy.getDeclaredMethod("getProxy", new Class<?>[] {
                            Context.class, String.class
                    });
                    if (getProxyMethod != null) {
                        mProxy = (java.net.Proxy) getProxyMethod.invoke(null, new Object[] {
                                context, url
                        });
                    }
                } catch (Exception e) {
                }
            }
            if (mProxy != null) {
                if (mProxy == java.net.Proxy.NO_PROXY)
                    return -1;
                try {
                    return ((InetSocketAddress) (mProxy.address())).getPort();
                } catch (Exception e) {
                    return -1;
                }
            }
            return -1;
        }
    }

    public static HttpClient getHttpClient() {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        return httpClient;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo.isConnected();
    }

    /**
     * NET_TYPE_WIFI = 1; NET_TYPE_2G = 2; NET_TYPE_3G = 3;
     * 
     * @param context
     * @return
     */
    public static final int NET_TYPE_WIFI = 1;
    public static final int NET_TYPE_2G = 2;
    public static final int NET_TYPE_3G = 3;

    public static int getNetWorkType(Context context) {
        int status = 0;
        NetworkInfo ni = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (ni == null) {
            // don't know , shall we stop checking.?
            return status;
        }

        if (ni.getType() == ConnectivityManager.TYPE_MOBILE) {
            TelephonyManager teleManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            final boolean bIsNetworkAvailable = isNetworkAvailable(context);
            if (bIsNetworkAvailable) {
                int type = teleManager.getNetworkType();
                switch (type) {
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        status = NET_TYPE_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        status = NET_TYPE_3G;
                        break;
                    default:
                        status = 0;
                }
            }
        } else {
            status = NET_TYPE_WIFI;
        }

        return status;
    }
}
