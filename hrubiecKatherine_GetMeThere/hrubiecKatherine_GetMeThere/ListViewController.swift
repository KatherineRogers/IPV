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

class ListViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var tablev: UITableView!
    @IBOutlet weak var titleInCell: UILabel!
    var alarms = [Alarm]()
    var chosenAlarm : Alarm?

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        loadAlarms()
    }
    
    @IBAction func addNewButton(_ sender: Any) {
        self.performSegue(withIdentifier: "addAlarm", sender: self)
    }
    
    
    override func viewDidAppear(_ animated: Bool) {
        print("alarms - \(alarms.count)")
        loadAlarms()
        
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return alarms.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cells", for:indexPath)
        let alarm = alarms[indexPath.row]
        cell.textLabel?.text = alarm.startingLoc + " - " + alarm.endingLoc
        return cell
    }
    
    func loadAlarms(){
        if !alarms.isEmpty{
            alarms.removeAll()
        }
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
                    
                    let alarm = Alarm(departureTime: departTime, arrivalTime: arrivalTime, durationInTraffic: dit, startingLoc: startLoc, endingLoc: endLoc, wakeUpBefore: wub, identifier: ident,  userID: currentUser)
                    
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
        
//        if segue.identifier == "newAlarm"{
//            
//        }else
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
