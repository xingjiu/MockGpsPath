package ledongli.cn.mockgpspath.controller;

import android.location.Location;

import java.util.List;

import ledongli.cn.mockgpspath.common.GlobalConfig;
import ledongli.cn.mockgpspath.common.request.RequestLoader;
import ledongli.cn.mockgpspath.event.LocationFetchEvent;
import ledongli.cn.mockgpspath.model.LDLLocation;
import ledongli.cn.mockgpspath.model.LocationModel;
import ledongli.cn.mockgpspath.thread.ThreadPool;
import ledongli.cn.mockgpspath.util.FileUtils;

/**
 * Created by xiangying on 7/25/15.
 * desc:
 */
public class LocationsProvider {
    private static final String TAG = "LocationsProvider";

    private static final String LOCATION_CACHE = "location_cache";

    private static LocationsProvider instance;
    private List<LDLLocation> mLicationList;

    private LocationsProvider() {

    }

    public static LocationsProvider getInstance() {
        if (null == instance) {
            synchronized (LocationsProvider.class) {
                if (null == instance) {
                    instance = new LocationsProvider();
                }
            }
        }
        return instance;
    }

    public List<LDLLocation> getLocations() {
        return mLicationList;
    }

    public void getLocationFromUrl(String url) {
        RequestLoader.loadData(url, LocationModel.class, new RequestLoader.ICallback<LocationModel>() {
            @Override
            public void onSuccess(final LocationModel result) {
                ThreadPool.runOnPool(new Runnable() {
                    @Override
                    public void run() {
                        FileUtils.writeBeanToFile(GlobalConfig.getAppContext(), LOCATION_CACHE, result);
                    }
                });
                mLicationList = result.getLocations();
                GlobalConfig.getBus().post(new LocationFetchEvent(result.getLocations()));
            }

            @Override
            public void onFailed(String errMsg) {
                GlobalConfig.getBus().post(new LocationFetchEvent(null));
            }
        });
    }

}
