package com.example.katie.hrubiec_katheirne_getmethere.objects;

import java.io.Serializable;

public class Alarm implements Serializable {

    private final long departureTime;
    private final long arrivalTime;
    private final long durationInTraffic;
    private final String startingLoc;
    private final String endingLoc;
    private final long wakeUpBefore;
    private final long identifier;


// --Commented out by Inspection START (2/6/19, 11:13 PM):
//    public Alarm(long departureTime, long arrivalTime, long durationInTraffic, String startingLoc, String endingLoc, long wakeUpBefore,long identifier) {
//        this.departureTime = departureTime;
//        this.arrivalTime = arrivalTime;
//        this.durationInTraffic = durationInTraffic;
//        this.startingLoc = startingLoc;
//        this.endingLoc = endingLoc;
//        this.wakeUpBefore = wakeUpBefore;
//        this.identifier = identifier;
//    }
// --Commented out by Inspection STOP (2/6/19, 11:13 PM)

    @Override
    public String toString() {
        return startingLoc + " - " + endingLoc;
    }

// --Commented out by Inspection START (2/6/19, 11:13 PM):
//    public long getDepartureTime() {
//        return departureTime;
//    }
// --Commented out by Inspection STOP (2/6/19, 11:13 PM)

// --Commented out by Inspection START (2/6/19, 11:13 PM):
//    public long getArrivalTime() {
//        return arrivalTime;
//    }
// --Commented out by Inspection STOP (2/6/19, 11:13 PM)

// --Commented out by Inspection START (2/6/19, 11:13 PM):
//    public long getDurationInTraffic() {
//        return durationInTraffic;
//    }
// --Commented out by Inspection STOP (2/6/19, 11:13 PM)

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
