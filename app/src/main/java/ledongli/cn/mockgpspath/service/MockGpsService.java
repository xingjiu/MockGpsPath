package ledongli.cn.mockgpspath.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import java.util.List;

import ledongli.cn.mockgpspath.controller.LocationsProvider;
import ledongli.cn.mockgpspath.model.LDLLocation;
import ledongli.cn.mockgpspath.util.LogUtils;

public class MockGpsService extends Service {

    private static final String TAG = "MockGpsService";

    public static final String START_MOCK_CMD = "start_mock_cmd";
    public static final String STOP_MOCK_CMD = "stop_mock_cmd";

    public static MockGpsService instance = null;

    UpdateGPSThread mockThread = null;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            return START_STICKY;
        }
        if (null == intent.getAction()) {
            return START_STICKY;
        }

        if (intent.getAction().equals(START_MOCK_CMD)) {
            LogUtils.d(TAG, "start commond");
            List<LDLLocation> list = LocationsProvider.getInstance().getLocations();
            if (null != list && list.size() > 0) {
                startMock(list);
            }
        } else if (intent.getAction().equals(STOP_MOCK_CMD)) {
            LogUtils.d(TAG, "stop commond");
            stopMock();
        }

        return START_STICKY;
    }

    public boolean isMocking() {
        return mockThread!=null && mockThread.isAlive();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (instance == this) {
            instance = null;
        }
    }

    private void startMock(List<LDLLocation> locationList) {
        stopMock();
        mockThread = new UpdateGPSThread(locationList);
        mockThread.start();
    }

    private void stopMock() {
        if (null != mockThread && mockThread.isAlive()) {
            mockThread.stoped = true;
            mockThread.interrupt();
        }

        mockThread = null;
    }

    class UpdateGPSThread extends Thread {
        List<LDLLocation> mLocationList = null;

        boolean stoped = false;

        public UpdateGPSThread(List<LDLLocation> locationList) {
            this.mLocationList = locationList;
        }

        @Override
        public void run() {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.addTestProvider("gps", false, false, false, false, false, true, true, 1, 1);
            locationManager.setTestProviderEnabled("gps", true);

            long timeInterval = 0;
            for (int i=0; i<mLocationList.size() && !stoped; i++) {
                LDLLocation lc = mLocationList.get(i);

                LogUtils.i(TAG, "mock gps : " + lc.getLon() + "," + lc.getLat());

                Location loc = new Location("gps");
                loc.setTime(System.currentTimeMillis());
                loc.setLatitude(lc.getLat());
                loc.setLongitude(lc.getLon());
                loc.setBearing((float) lc.getCourse());
                loc.setSpeed((float) lc.getSpeed());
                loc.setAccuracy((float) lc.getAccuracy());
                if(Build.VERSION.SDK_INT > 16){
                    loc.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                }

                locationManager.setTestProviderLocation("gps", loc);

                if (i < mLocationList.size() - 1) {
                    LDLLocation next = mLocationList.get(i + 1);
                    timeInterval = (long) ((next.getTimeInterval() - lc.getTimeInterval()) * 1000);
                } else {
                    timeInterval = 1000;
                }

                try {
                    Thread.sleep(timeInterval);
                } catch (Exception e) {
                }
            }

            locationManager.setTestProviderEnabled("gps", false);
            locationManager.removeTestProvider("gps");
        }
    }
}
