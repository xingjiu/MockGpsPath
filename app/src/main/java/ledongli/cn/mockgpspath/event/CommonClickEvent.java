package ledongli.cn.mockgpspath.event;

/**
 * Created by xiangying on 7/25/15.
 * desc:
 */
public class CommonClickEvent {
    private static final String TAG = "CommonClickEvent";

    // Main view
    public static final int EVENT_REQUESTLOCATIONS  = 1000;
    public static final int EVENT_STOPMOCKGPS       = 1001;
    public static final int EVENT_STARTMOCKGPS      = 1001;

    private int mType;

    public CommonClickEvent(int type) {
        this.mType = type;
    }

    public int getType() {
        return mType;
    }
}
