package ledongli.cn.mockgpspath.model;

import java.io.Serializable;

/**
 * Created by xiangying on 7/25/15.
 * desc:
 */
public class LDLLocation  implements Serializable {
    private static final String TAG = "LDLLocation";

    double accuracy;
    double course;
    double lat;
    double lon;
    double speed;
    long timeInterval;

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getCourse() {
        return course;
    }

    public void setCourse(double course) {
        this.course = course;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }
}
