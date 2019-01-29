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

    public Alarm(long departureTime, long arrivalTime, long durationInTraffic, String startingLoc, String endingLoc, long wakeUpBefore,long identifier) {
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.durationInTraffic = durationInTraffic;
        this.startingLoc = startingLoc;
        this.endingLoc = endingLoc;
        this.wakeUpBefore = wakeUpBefore;
        this.identifier = identifier;
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
}
