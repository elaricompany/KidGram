package org.telegram.elari.elariapi.pojo.location;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Location {

    @Expose
    private String altitude;
    @Expose
    private Double battery;
    @Expose
    private String course;
    @Expose
    private String haccuracy;
    @Expose
    private Double latitude;
    @Expose
    private String locationdate;
    @Expose
    private Double longitude;
    @Expose
    private String speed;

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public Double getBattery() {
        return battery;
    }

    public void setBattery(Double battery) {
        this.battery = battery;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getHaccuracy() {
        return haccuracy;
    }

    public void setHaccuracy(String haccuracy) {
        this.haccuracy = haccuracy;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getLocationdate() {
        return locationdate;
    }

    public void setLocationdate(String locationdate) {
        this.locationdate = locationdate;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

}
