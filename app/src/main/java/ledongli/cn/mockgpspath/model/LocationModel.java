package ledongli.cn.mockgpspath.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangying on 7/25/15.
 * desc:
 */
public class LocationModel  implements Serializable {
    private static final String TAG = "LocationModel";

    private ArrayList<LDLLocation> locations = new ArrayList<>();

    public List<LDLLocation> getLocations() {
        return locations;
    }

}
