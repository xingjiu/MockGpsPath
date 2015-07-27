package ledongli.cn.mockgpspath.event;

import java.util.List;

import ledongli.cn.mockgpspath.model.LDLLocation;
import ledongli.cn.mockgpspath.model.LocationModel;

/**
 * Created by xiangying on 7/25/15.
 * desc:
 */
public class LocationFetchEvent {
    private static final String TAG = "LocationFetchEvent";
    List<LDLLocation> mLocationList;

    public LocationFetchEvent(List<LDLLocation> locations) {
        this.mLocationList = locations;
    }

    public List<LDLLocation> getmLocationList() {
        return this.mLocationList;
    }
}
