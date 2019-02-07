package com.example.katie.hrubiec_katheirne_getmethere.objects;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Alarm implements Serializable {

    private long departureTime;
    private long arrivalTime;
    private long durationInTraffic;
    private String startingLoc;
    private String endingLoc;
    private long wakeUpBefore;
    private long identifier;
    private String imageuri;
    private String sounduri;
    final String userID;

    public Alarm(long departureTime, long arrivalTime, long durationInTraffic, String startingLoc, String endingLoc, long wakeUpBefore, long identifier,String imageuri, String sounduri, String userID) {
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.durationInTraffic = durationInTraffic;
        this.startingLoc = startingLoc;
        this.endingLoc = endingLoc;
        this.wakeUpBefore = wakeUpBefore;
        this.identifier = identifier;
        this.imageuri = imageuri;
        this.sounduri = sounduri;
        this.userID = userID;

    }

    @Override
    public String toString() {
        return startingLoc + " - " + endingLoc;
    }

    public String getStringDepart(){
        return new SimpleDateFormat("MMM d, yyyy, h:mm a").format(getDepartureTime());
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

// --Commented out by Inspection START (2/6/19, 11:13 PM):
//    public String getUserID() {
//        return userID;
//    }
// --Commented out by Inspection STOP (2/6/19, 11:13 PM)

// --Commented out by Inspection START (2/6/19, 11:13 PM):
//    public void setDepartureTime(long departureTime) {
//        this.departureTime = departureTime;
//    }
// --Commented out by Inspection STOP (2/6/19, 11:13 PM)

// --Commented out by Inspection START (2/6/19, 11:13 PM):
//    public void setArrivalTime(long arrivalTime) {
//        this.arrivalTime = arrivalTime;
//    }
// --Commented out by Inspection STOP (2/6/19, 11:13 PM)

// --Commented out by Inspection START (2/6/19, 11:13 PM):
//    public void setDurationInTraffic(long durationInTraffic) {
//        this.durationInTraffic = durationInTraffic;
//    }
// --Commented out by Inspection STOP (2/6/19, 11:13 PM)

// --Commented out by Inspection START (2/6/19, 11:13 PM):
//    public void setStartingLoc(String startingLoc) {
//        this.startingLoc = startingLoc;
//    }
// --Commented out by Inspection STOP (2/6/19, 11:13 PM)

// --Commented out by Inspection START (2/6/19, 11:13 PM):
//    public void setEndingLoc(String endingLoc) {
//        this.endingLoc = endingLoc;
//    }
// --Commented out by Inspection STOP (2/6/19, 11:13 PM)

// --Commented out by Inspection START (2/6/19, 11:13 PM):
//    public void setWakeUpBefore(long wakeUpBefore) {
//        this.wakeUpBefore = wakeUpBefore;
//    }
// --Commented out by Inspection STOP (2/6/19, 11:13 PM)

// --Commented out by Inspection START (2/6/19, 11:13 PM):
//    public void setIdentifier(long identifier) {
//        this.identifier = identifier;
//    }
// --Commented out by Inspection STOP (2/6/19, 11:13 PM)

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public void setSounduri(String sounduri) {
        this.sounduri = sounduri;
    }

}
