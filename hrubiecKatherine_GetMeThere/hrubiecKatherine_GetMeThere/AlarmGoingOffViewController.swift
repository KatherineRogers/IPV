//
//  AlarmGoingOffViewController.swift
//  hrubiecKatherine_GetMeThere
//
//  Created by Katie on 2/28/19.
//  Copyright © 2019 Katie. All rights reserved.
//

import UIKit
import AVFoundation

class AlarmGoingOffViewController: UIViewController {
    
    
    @IBOutlet weak var topText: UILabel!
    @IBOutlet weak var image: UIImageView!
    @IBOutlet weak var bottomText: UILabel!
    @IBOutlet var vc: UIView!
    
    var timer:Timer!
    var countdown:Int = 5//*60
    var player:AVAudioPlayer!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        guard let path = Bundle.main.path(forResource: "service-bell_daniel_simion", ofType: "wav") else {
            return
        }
        vc.isHidden = false
        let soundURL = URL(fileURLWithPath: path)
        player = try? AVAudioPlayer(contentsOf: soundURL)
        player.prepareToPlay()
        player.numberOfLoops = -1
        player.play()
        
    }
    
    @IBAction func dismiss(_ sender: Any) {
        performSegue(withIdentifier: "showAlarmDismissDeatils", sender: self)
    }
    
    @IBAction func snooze(_ sender: Any) {
        player.stop()
        setTimer()
        
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
        }else{
            
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
