package ledongli.cn.mockgpspath.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.List;

import ledongli.cn.mockgpspath.R;
import ledongli.cn.mockgpspath.controller.LocationsProvider;
import ledongli.cn.mockgpspath.model.LDLLocation;
import ledongli.cn.mockgpspath.ui.activity.MainActivity;
import ledongli.cn.mockgpspath.util.ListUtil;
import ledongli.cn.mockgpspath.util.LogUtils;

public class MockGpsService extends Service {

    private static final String TAG = "MockGpsService";

    public static final String START_MOCK_CMD = "start_mock_cmd";
    public static final String STOP_MOCK_CMD = "stop_mock_cmd";

    public static MockGpsService instance = null;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private static final int MOCKGPS_NOTI_ID = 1001;

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
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        startForeground(MOCKGPS_NOTI_ID, initNotification(0));
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

    public void setSpeed(double speed) {
        if (mockThread != null && mockThread.isAlive()) {
            mockThread.setSpeed(speed);
        }
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

    private Notification initNotification(float speed) {
        mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_mock_small);
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent contextIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contextIntent);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.mock_notification_layout);
        remoteViews.setTextViewText(R.id.tv_noti_speed, speed + "m/s");
        mBuilder.setContent(remoteViews);
        return mBuilder.build();
    }

    private void updateNotification(float speed, long timestamp) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.mock_notification_layout);
        remoteViews.setTextViewText(R.id.tv_noti_speed, speed + "");
        remoteViews.setTextViewText(R.id.tv_noti_timeinterval, timestamp + "s");
        if (mBuilder != null) {
            mBuilder.setContent(remoteViews);
            mNotificationManager.notify(MOCKGPS_NOTI_ID, mBuilder.build());
        }
    }

    private void updateNotification(String left, String right) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.mock_notification_layout);
        remoteViews.setTextViewText(R.id.tv_noti_speed, left);
        remoteViews.setTextViewText(R.id.tv_noti_timeinterval, right);
        if (mBuilder != null) {
            mBuilder.setContent(remoteViews);
            mNotificationManager.notify(MOCKGPS_NOTI_ID, mBuilder.build());
        }
    }

    class UpdateGPSThread extends Thread {
        List<Location> mLocationList = null;
        List<LDLLocation> mLDLocationList = null;

        boolean stoped  = false;
        double speed    = 3.0;     // m/s

        public UpdateGPSThread(List<LDLLocation> locationList) {
            this.mLDLocationList = locationList;
        }

        Location currentLocation = null;

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        @Override
        public void run() {

            this.mLocationList = ListUtil.trasList(mLDLocationList, new ListUtil.Transferable<Location, LDLLocation>() {
                @Override
                public Location transfer(LDLLocation location) {
                    Location loc = new Location("gps");
                    loc.setTime(location.getTimeInterval());
                    loc.setLatitude(location.getLat());
                    loc.setLongitude(location.getLon());
                    loc.setBearing((float) location.getCourse());
                    loc.setSpeed((float) location.getSpeed());
                    loc.setAccuracy((float) location.getAccuracy());
                    return loc;
                }
            });

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.addTestProvider("gps", false, false, false, false, false, true, true, 1, 1);
            locationManager.setTestProviderEnabled("gps", true);

            long timeInterval = 0;
            for (int i=0;!stoped; i++) {

                i = i%mLocationList.size();

                double random = Math.random() * 0.00002;

                currentLocation = copyLocation(mLocationList.get(i));
                currentLocation.setLongitude(currentLocation.getLongitude() + random);
                currentLocation.setLatitude(currentLocation.getLatitude() + random);
                currentLocation.setTime(System.currentTimeMillis());

                if(Build.VERSION.SDK_INT > 16){
                    currentLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                }

                locationManager.setTestProviderLocation("gps", currentLocation);

                Location next = mLocationList.get((i + 1) % mLocationList.size());

                float distance = currentLocation.distanceTo(next);

//                timeInterval = Math.abs(mLocationList.get(i).getTime() - next.getTime());  //(long)(distance*1000/speed);
//                timeInterval = Math.min(timeInterval, 20000);

                timeInterval = (int)(distance * 1000 / speed);

                updateNotification(distance*1000/timeInterval + "m/s", i+"");

                try {
                    Thread.sleep(timeInterval);
                } catch (Exception e) {
                }
            }

            updateNotification("0.0m/s", "stoped");

            locationManager.setTestProviderEnabled("gps", false);
            locationManager.removeTestProvider("gps");
        }

        private Location copyLocation(Location location) {
            Location loc = new Location("gps");
            loc.setTime(System.currentTimeMillis());
            loc.setLatitude(location.getLatitude());
            loc.setLongitude(location.getLongitude());
            loc.setBearing(location.getBearing());
            loc.setSpeed(location.getSpeed());
            loc.setAccuracy(location.getAccuracy());

            return loc;
        }

        private List<Location> genLocations(List<Location> locationLis) {


            return null;
        }
    }

    class UpdateGPSThread1 extends Thread {
        List<LDLLocation> mLocationList = null;

        boolean stoped = false;
        double speed = 10;

        public UpdateGPSThread1(List<LDLLocation> locationList) {
            this.mLocationList = locationList;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        @Override
        public void run() {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.addTestProvider("gps", false, false, false, false, false, true, true, 1, 1);
            locationManager.setTestProviderEnabled("gps", true);

            long timeInterval = 500;

            double lat10meter = -0.00009;

            double initLon = 116.337618;
            double initLat = 39.992888;
            double incrLon = 0.0;
            double incrLat = -0.00009;

            Location preLoc = null;
            Location currentLoc = null;

            while (!stoped) {

                incrLat = lat10meter*speed/20;

                initLon+=incrLon;
                initLat+=incrLat;

                Location loc = new Location("gps");
                loc.setTime(System.currentTimeMillis());
                loc.setLatitude(initLat);
                loc.setLongitude(initLon);
                loc.setBearing((float) 270.0);
                loc.setSpeed(1.0f);
                loc.setAccuracy(10.0f);
                if(Build.VERSION.SDK_INT > 16){
                    loc.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                }

                if (preLoc == null) {
                    preLoc = loc;
                }

                currentLoc = loc;

                float distance = preLoc.distanceTo(currentLoc);

                locationManager.setTestProviderLocation("gps", loc);

                updateNotification((distance*1000/timeInterval) + "", initLat + "");

                try {
                    Thread.sleep(timeInterval);
                } catch (Exception e) {
                }

                preLoc = loc;
            }

            updateNotification("0", "0");

            locationManager.setTestProviderEnabled("gps", false);
            locationManager.removeTestProvider("gps");
        }
    }
}
