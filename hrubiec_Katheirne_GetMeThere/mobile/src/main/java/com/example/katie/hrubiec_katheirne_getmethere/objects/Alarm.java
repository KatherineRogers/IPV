package com.example.katie.hrubiec_katheirne_getmethere.objects;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Alarm implements Serializable {

    long departureTime;
    long arrivalTime;
    long durationInTraffic;
    String startingLoc;
    String endingLoc;
    long wakeUpBefore;
    long identifier;
    String imageuri;
    String sounduri;

    public Alarm(long departureTime, long arrivalTime, long durationInTraffic, String startingLoc, String endingLoc, long wakeUpBefore, long identifier,String imageuri, String sounduri) {
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.durationInTraffic = durationInTraffic;
        this.startingLoc = startingLoc;
        this.endingLoc = endingLoc;
        this.wakeUpBefore = wakeUpBefore;
        this.identifier = identifier;
        this.imageuri = imageuri;
        this.sounduri = sounduri;
    }

    @Override
    public String toString() {
        return startingLoc + " - " + endingLoc;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public long getDurationInTraffic() {
        return durationInTraffic;
    }

    public String getStartingLoc() {
        return startingLoc;
    }

    public String getEndingLoc() {
        return endingLoc;
    }

    public long getWakeUpBefore() {
        return wakeUpBefore;
    }

    public long getIdentifier() {
        return identifier;
    }

    public String getImageuri() {
        return imageuri;
    }

    public String getSounduri() {
        return sounduri;
    }


    public void setDepartureTime(long departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setDurationInTraffic(long durationInTraffic) {
        this.durationInTraffic = durationInTraffic;
    }

    public void setStartingLoc(String startingLoc) {
        this.startingLoc = startingLoc;
    }

    public void setEndingLoc(String endingLoc) {
        this.endingLoc = endingLoc;
    }

    public void setWakeUpBefore(long wakeUpBefore) {
        this.wakeUpBefore = wakeUpBefore;
    }

    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public void setSounduri(String sounduri) {
        this.sounduri = sounduri;
    }
}
