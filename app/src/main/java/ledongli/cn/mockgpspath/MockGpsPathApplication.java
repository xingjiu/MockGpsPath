package ledongli.cn.mockgpspath;

import android.app.Application;

import ledongli.cn.mockgpspath.common.GlobalConfig;

/**
 * Created by xiangying on 7/25/15.
 * desc:
 */
public class MockGpsPathApplication extends Application {
    private static final String TAG = "MockGpsPathApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalConfig.setAppContext(this);
    }

}
