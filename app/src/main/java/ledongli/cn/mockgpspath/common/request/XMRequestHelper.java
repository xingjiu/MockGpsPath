package ledongli.cn.mockgpspath.common.request;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;


/**
 * Created by wangyida on 15-5-4.
 */
public class XMRequestHelper {

    //time
    public static final long ONE_HOUR = 60 * 60 * 1000;
    public static final long ONE_DAY = 24 * ONE_HOUR;

    public static Cache.Entry enforceClientCaching(Cache.Entry entry, NetworkResponse response, boolean enforceLoadFromCache) {
        if (getClientCacheExpiry() < 0) return entry;

        long now = System.currentTimeMillis();

        if (entry == null) {
            entry = new Cache.Entry();
            entry.data = response.data;
            entry.etag = response.headers.get("ETag");
            entry.softTtl = now + getClientCacheExpiry();
            entry.ttl = entry.softTtl;
            entry.serverDate = now;
            entry.responseHeaders = response.headers;
        } else if (entry.isExpired()) {
            entry.softTtl = enforceLoadFromCache ? now + System.currentTimeMillis() : now + getClientCacheExpiry();
            entry.ttl = entry.softTtl;
        }

        return entry;
    }

    private static long getClientCacheExpiry() {
        return ONE_DAY;
    }

}
