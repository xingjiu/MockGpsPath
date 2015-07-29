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
import android.widget.RemoteViews;
import android.support.v4.app.NotificationCompat;

import java.util.List;

import ledongli.cn.mockgpspath.R;
import ledongli.cn.mockgpspath.controller.LocationsProvider;
import ledongli.cn.mockgpspath.model.LDLLocation;
import ledongli.cn.mockgpspath.ui.activity.MainActivity;
import ledongli.cn.mockgpspath.util.LogUtils;

public class MockGpsService extends Service {

    private static final String TAG = "MockGpsService";

    public static final String START_MOCK_CMD = "start_mock_cmd";
    public static final String STOP_MOCK_CMD = "stop_mock_cmd";

    public static MockGpsService instance = null;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private static final int MOCKGPS_NOTI_ID = 1001;
    private long lastTimeStamp = 0;

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
        remoteViews.setTextViewText(R.id.tv_noti_timeinterval, (timestamp - lastTimeStamp) / 1000 + "s");
        lastTimeStamp = timestamp;
        if (mBuilder != null) {
            mBuilder.setContent(remoteViews);
            mNotificationManager.notify(MOCKGPS_NOTI_ID, mBuilder.build());
        }
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

                updateNotification(loc.getSpeed(), loc.getTime());

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
