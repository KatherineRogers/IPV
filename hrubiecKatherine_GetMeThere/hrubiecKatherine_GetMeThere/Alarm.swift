//
//  Alarm.swift
//  hrubiecKatherine_GetMeThere
//
//  Created by Katie on 2/20/19.
//  Copyright © 2019 Katie. All rights reserved.
//

import Foundation

class Alarm{
    
    let departureTime : CLong
    let arrivalTime : CLong
    let durationInTraffic : CLong
    let startingLoc : String
    let endingLoc : String
    let wakeUpBefore : CLong
    let identifier : CLong
    let imageuri : String
    let sounduri : String
    let userID : String
    
    init(departureTime : CLong,arrivalTime : CLong,durationInTraffic : CLong,startingLoc : String,
         endingLoc : String,wakeUpBefore : CLong,identifier : CLong,imageuri : String,sounduri : String,userID : String) {
        self.departureTime = departureTime
        self.arrivalTime = arrivalTime
        self.durationInTraffic = durationInTraffic
        self.startingLoc = startingLoc
        self.endingLoc = endingLoc
        self.wakeUpBefore = wakeUpBefore
        self.identifier = identifier
        self.imageuri = imageuri
        self.sounduri = sounduri
        self.userID = userID
    }
    
    
}
