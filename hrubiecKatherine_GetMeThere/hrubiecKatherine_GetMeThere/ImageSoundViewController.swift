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
import Firebase
import FirebaseAuth
import FirebaseDatabase
import FirebaseCore
import FirebaseStorage

class ImageSoundViewController: UIViewController,UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    var dtdate = NSDate()//depart
    var senderDate = Date()//arival
    var durationInTraffic = CLong()
    var startLoc = String()
    var endLoc = String()
    var wakeUpBefore = Int()
    var setForTime = Date()
    var imageChosen = UIImage()
    var timer:Timer!
    //var countdown:Int = 5
    var countdown = Int()
    var imagePicker = UIImagePickerController()

    @IBOutlet weak var imageV: UIImageView!
    @IBOutlet weak var addImageButton: UIButton!
    @IBOutlet weak var spinner: UIActivityIndicatorView!
    @IBOutlet weak var saveButton: UIButton!
    
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
        self.title = dateFormatter.string(from: setForTime)
        spinner.isHidden = true
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
        }
    }
    
    @IBAction func saveAlarm(_ sender: Any) {
        spinner.isHidden = false
        spinner.startAnimating()
        saveButton.isEnabled = false
        saveToFirebase()
    }
    
    @IBAction func imageButton(_ sender: Any) {
        
        if UIImagePickerController.isSourceTypeAvailable(.savedPhotosAlbum){

            
            imagePicker.delegate = self
            imagePicker.sourceType = .savedPhotosAlbum;
            imagePicker.allowsEditing = false
            
            self.present(imagePicker, animated: true, completion: nil)
        }
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        
        if let pickedImage = info[UIImagePickerController.InfoKey.originalImage] as? UIImage{
            self.imageV.contentMode = .scaleAspectFit
            self.imageV.image = pickedImage
            self.imageChosen = pickedImage
            self.addImageButton.titleLabel?.layer.opacity = 0.0
        }
        dismiss(animated: true, completion: nil)
    }
    
    func saveToFirebase(){
        var urlImage = String()
        let imageName = NSUUID().uuidString
        let storageRef = Storage.storage().reference().child(imageName)
        let database = Database.database().reference()
        let user = Auth.auth().currentUser!.uid
        let usersList = database.child("users").child(user)
        let timeInSecondsArrive: TimeInterval = senderDate.timeIntervalSince1970
        let tisdepart: TimeInterval = dtdate.timeIntervalSince1970
        let tisidentity: TimeInterval = Date().timeIntervalSince1970
        let tisi = NSInteger(tisidentity)
        let tisd = NSInteger(tisdepart)
        let tisa = NSInteger(timeInSecondsArrive)
        
        let tiDepartWakeUp = tisd + (wakeUpBefore*60)
        let now:TimeInterval = Date().timeIntervalSince1970
        let nowInt = NSInteger(now)
        countdown = tiDepartWakeUp - nowInt
        
        if let uploadData = imageChosen.pngData(){
            storageRef.putData(uploadData, metadata: nil) { (metadata, error) in
                if error != nil{
                    print(error)
                    return
                }
                storageRef.downloadURL(completion: { (url, error) in
                    if error != nil{
                        print("error getting download url")
                    }else{
                        urlImage = "\(String(describing: url!))"
                        let alarmData: [String:Any] = [
                            "arrivalTime" : tisa,
                            "departureTime" : tisd,
                            "durationInTraffic" : self.durationInTraffic,
                            "endingLoc" : self.endLoc,
                            "identifier" : tisi,
                            "startingLoc" : self.startLoc,
                            "wakeUpBefore" : self.wakeUpBefore,
                            "imageURL" : urlImage
                        ]
                        
                        let ref = usersList.child("alarms").childByAutoId()
                        ref.setValue(alarmData)
                        self.spinner.stopAnimating()
                        self.spinner.isHidden = true
                        self.setTimer()
                        self.navigationController?.popToRootViewController(animated: true)
                    }
                })
            }
        }
    }
    
    
    
//    func imagePickerController(picker: UIImagePickerController!, didFinishPickingImage image: UIImage!, editingInfo: NSDictionary!){
//        self.dismiss(animated: true, completion: { () -> Void in
//            self.imageV.image = image
//            self.addImageButton.layer.opacity = 0.0
//        })
//
//
//    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
