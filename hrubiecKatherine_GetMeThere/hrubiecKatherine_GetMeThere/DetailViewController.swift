//
//  DetailViewController.swift
//  hrubiecKatherine_GetMeThere
//
//  Created by Katie on 2/20/19.
//  Copyright © 2019 Katie. All rights reserved.
//

import UIKit

class DetailViewController: UIViewController, UINavigationControllerDelegate, URLSessionDelegate {
    
    var alarmSelected: Alarm?
    @IBOutlet weak var starting: UILabel!
    @IBOutlet weak var ending: UILabel!
    @IBOutlet weak var arrival: UILabel!
    @IBOutlet weak var departure: UILabel!
    @IBOutlet weak var timetogetthereavg: UILabel!
    @IBOutlet weak var wakeup: UILabel!
    @IBOutlet weak var image: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        if alarmSelected != nil{
            
            let ti = alarmSelected?.durationInTraffic
            let minutes = (ti! / 60) % 60
            let hours = (ti! / 3600)
            
            let ms = NSString(format: "%0.1d hour %0.2d mins",hours,minutes)
            
            starting.text = alarmSelected?.startingLoc
            ending.text = alarmSelected?.endingLoc
            arrival.text = getStringTime(timeL: (alarmSelected?.arrivalTime)!)
            departure.text = getStringTime(timeL: (alarmSelected?.departureTime)!)
            wakeup.text = getStringTime(timeL: (alarmSelected?.departureTime)! - ((alarmSelected?.wakeUpBefore)!*60))
            timetogetthereavg.text = "\(ms)"
            print("alarm img path - \(alarmSelected?.imageUrl)")
            
            if let profileImageUrl = alarmSelected?.imageUrl{
                let imgurl = URL(string: profileImageUrl)
                URLSession.shared.dataTask(with: imgurl!) { (data, response, error) in
                    if error != nil{
                        print(error)
                    }else{
                        print("setting image")
                        self.image.contentMode = .scaleAspectFit
                        self.image.image = UIImage(data: data!)
                    }
                    
                }
            }

        }
    }
    
    func getStringTime(timeL:CLong) -> String{
        
        let timeInt = NSInteger(timeL)
        let dateFromLong = NSDate(timeIntervalSince1970: TimeInterval(timeInt))
        let dateFormatter = DateFormatter();
        dateFormatter.dateStyle = .medium
        dateFormatter.timeStyle = .short
    
        return dateFormatter.string(from: dateFromLong as Date)
    }
    
    @IBAction func backButton(_ sender: Any) {
        navigationController?.popViewController(animated: true)
    }
    
    @IBAction func startDirections(_ sender: Any) {
        let start = alarmSelected?.startingLoc.replacingOccurrences(of: " ", with: "")
        let end = alarmSelected?.endingLoc.replacingOccurrences(of: " ", with: "")
        let gmapsstart = "comgooglemaps://?saddr="
        let gmapsmid = "&daddr="
        let gmapsend = "&directionsmode=driving"
        let stringURL = gmapsstart + start! + gmapsmid + end! + gmapsend
        print("string URL - \(stringURL) - \(start) - \(end)")
        if (UIApplication.shared.canOpenURL(URL(string:"comgooglemaps://")!)){
            
            if let asdf = URL(string: stringURL){
                UIApplication.shared.open(asdf, options: [:], completionHandler: nil)
            }else{
                print("cant make url from string")
            }
        
        } else
        {
            NSLog("Can't use com.google.maps://");
        }
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
