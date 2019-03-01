//
//  ListViewController.swift
//  hrubiecKatherine_GetMeThere
//
//  Created by Katie on 2/20/19.
//  Copyright © 2019 Katie. All rights reserved.
//

import UIKit
import Firebase
import FirebaseAuth
import FirebaseDatabase
import FirebaseCore
import AVFoundation
import UserNotifications

class ListViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, UIGestureRecognizerDelegate {
    
    @IBOutlet weak var tablev: UITableView!
    @IBOutlet weak var titleInCell: UILabel!
    var alarms = [Alarm]()
    var chosenAlarm : Alarm?
    
    
    var timer:Timer!
    var countdown:Int = 5
    var player:AVAudioPlayer!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        loadAlarms()
        //setTimer()
    }
    
    
    func setTimer(){
        
        var bgTask:UIBackgroundTaskIdentifier!
        bgTask = UIApplication.shared.beginBackgroundTask(expirationHandler: {
            UIApplication.shared.endBackgroundTask(bgTask)
        })
        
        timer =  Timer.scheduledTimer(timeInterval: 1, target: self, selector: #selector(countdownAction), userInfo: nil, repeats: true)
        RunLoop.current.add(timer, forMode: RunLoop.Mode.default)
    }
    
    @objc func countdownAction(){
        countdown -= 1
        
        if countdown == 0 {
            timer.invalidate()
            print("counter done")
            
            let goingOff = self.storyboard?.instantiateViewController(withIdentifier: "goingOff") as! AlarmGoingOffViewController
            
           self.present(goingOff, animated: true, completion: nil)
            
//            let center = UNUserNotificationCenter.current()
//            let content = UNMutableNotificationContent()
//            content.title = "Jurrasic Park"
//            content.subtitle = "lunch"
//            content.body = "body text"
//            content.sound = UNNotificationSound.defaultCritical
//            content.threadIdentifier = "local-notifications temp"
//
//            let date = Date(timeIntervalSinceNow: 10)
//            let dateComponenets = Calendar.current.dateComponents([.year,.month,.day,.hour,.minute,.second], from: date)
//
//            let trigget = UNCalendarNotificationTrigger(dateMatching: dateComponenets, repeats: true)
//           // let trigget = UNTimeIntervalNotificationTrigger(timeInterval: 1, repeats: true)
//            let request = UNNotificationRequest(identifier: "content", content: content, trigger: trigget)
//
//            center.add(request) { (error) in
//                if error != nil{
//                    print(error)
//                }
//            }
            
            
        }else{
            
        }
    }
    
    
    @IBAction func addNewButton(_ sender: Any) {
        self.performSegue(withIdentifier: "addAlarm", sender: self)
    }
    
    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldRecognizeSimultaneouslyWith otherGestureRecognizer: UIGestureRecognizer) -> Bool {
        return true
    }
    
    override func viewDidAppear(_ animated: Bool) {
        loadAlarms()
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return alarms.count
    }
    
    
    func numberOfSections(in tableView: UITableView) -> Int {
        if alarms.count > 0 {
            tablev.backgroundView = nil
            return 1
        } else {
            
            let rect = CGRect(origin: CGPoint(x: 0,y :0), size: CGSize(width: self.view.bounds.size.width, height: self.view.bounds.size.height))
            let messageLabel = UILabel(frame: rect)
            messageLabel.text = "No alarms set. Please add a new alarm to get started."
            messageLabel.textColor = UIColor.black
            messageLabel.numberOfLines = 0;
            messageLabel.textAlignment = .center;
            messageLabel.sizeToFit()
            
            tablev.backgroundView = messageLabel
            tablev.separatorStyle = .none
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cells", for:indexPath)
        let alarm = alarms[indexPath.row]
        cell.textLabel?.text = alarm.startingLoc + " - " + alarm.endingLoc
        let longPressRecognizer = UILongPressGestureRecognizer(target: self, action: #selector(ListViewController.longPress(sender:)))
        cell.addGestureRecognizer(longPressRecognizer)
        return cell
    }
    
    @objc func longPress(sender: UILongPressGestureRecognizer) {
        let touchpoint = sender.location(in: self.tablev)
        let ip = self.tablev.indexPathForRow(at: touchpoint)
        if sender.state == UIGestureRecognizer.State.began {
            let alertController = UIAlertController(title: "Delete Alarm", message: "Are you sure you wish to delete this alarm?", preferredStyle: .alert)
            let defaultAction = UIAlertAction(title: "No", style: .cancel, handler: nil)
            let handler = UIAlertAction(title: "Yes", style: .destructive) { (UIAlertAction) in
                //var fbalarmid = String()
                let chosen = self.alarms[ip!.row].identifier
                let database = Database.database().reference()
                let user = Auth.auth().currentUser!.uid
                let usersList = database.child("users").child(user)
                let ref = usersList.child("alarms")
                ref.observeSingleEvent(of: .value , with: { (snapshot) in
                    for item in snapshot.children.allObjects as! [DataSnapshot]{
                        let alarmID = item.key
                        if let getData = item.value as? [String:Any]{
                             let fbalarmid = getData["identifier"] as! CLong
                            if fbalarmid == chosen {
                                ref.child(alarmID).removeValue()
                            }
                        }
                    }
                    self.alarms.removeAll()
                    self.loadAlarms()
                })
            }
            alertController.addAction(defaultAction)
            alertController.addAction(handler)
            self.present(alertController, animated: true, completion: nil)
        }
    }

  
    func loadAlarms(){
        alarms.removeAll()
        let users = Database.database().reference().child("users")
        let currentUser : String = (Auth.auth().currentUser?.uid)!
        let ualarms = users.child(currentUser).child("alarms")
        ualarms.observeSingleEvent(of: .value) { (snapshot) in
            
            for a in snapshot.children.allObjects as! [DataSnapshot]{
                if let getData = a.value as? [String:Any]{
                    
                    let arrivalTime = getData["arrivalTime"] as! CLong
                    let departTime = getData["departureTime"] as! CLong
                    let dit = getData["durationInTraffic"] as! CLong
                    let startLoc = getData["startingLoc"] as! String
                    let endLoc = getData["endingLoc"] as! String
                    let ident = getData["identifier"] as! CLong
                    let wub = getData["wakeUpBefore"] as! CLong
                    let imgurl = getData["imageURL"] as! String
                    
                    let alarm = Alarm(departureTime: departTime, arrivalTime: arrivalTime, durationInTraffic: dit, startingLoc: startLoc, endingLoc: endLoc, wakeUpBefore: wub, identifier: ident,  userID: currentUser, imageUrl: imgurl)
                    
                    self.alarms.append(alarm)
                    self.tablev.reloadData()
                }
            }
        }
    }
    
    
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        chosenAlarm = alarms[indexPath.row]
        performSegue(withIdentifier: "ShowDetailVC", sender: Any?.self)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "ShowDetailVC"{
            let dVC = segue.destination as! DetailViewController
            dVC.alarmSelected = chosenAlarm
        }
    }
    
    
    @IBAction func signOut(_ sender: Any) {
        do{
            try Auth.auth().signOut()
        }catch let err{
            print(err)
        }
        alarms.removeAll()
        let storyBoard = UIStoryboard(name: "Main", bundle: nil)
        let destinationVC = storyBoard.instantiateViewController(withIdentifier: "LoginVC") as? ViewController
        self.present(destinationVC!, animated: true, completion: nil)
    }
}
