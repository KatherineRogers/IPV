//
//  ImageSoundViewController.swift
//  hrubiecKatherine_GetMeThere
//
//  Created by Katie on 2/26/19.
//  Copyright © 2019 Katie. All rights reserved.
//

import UIKit
import AVFoundation
import AudioToolbox

class ImageSoundViewController: UIViewController {
    
    var dtdate = NSDate()//depart
    var senderDate = Date()//arival
    var durationInTraffic = CLong()
    var startLoc = String()
    var endLoc = String()
    var wakeUpBefore = Int()
    var setForTime = Date()

    @IBOutlet weak var setFor: UILabel!
    @IBOutlet weak var soundLabel: UILabel!
    @IBOutlet weak var image: UIImageView!
    @IBOutlet weak var imageClick: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        let timeInSeconds: TimeInterval = dtdate.timeIntervalSince1970
        let tis = NSInteger(timeInSeconds)
        let sft = tis-(wakeUpBefore*60)
        setForTime = Date(timeIntervalSince1970: TimeInterval(sft))
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = DateFormatter.Style.medium
        dateFormatter.timeStyle = DateFormatter.Style.short
        setFor.text = dateFormatter.string(from: setForTime)
    }
    
    @IBAction func saveAlarm(_ sender: Any) {
    }
    
    @IBAction func sounfButton(_ sender: Any) {
        
        AudioServicesPlaySystemSound(SystemSoundID(1000))
    }
    
    @IBAction func imageButton(_ sender: Any) {
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
