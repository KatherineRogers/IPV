//
//  DateTimePickerViewController.swift
//  hrubiecKatherine_GetMeThere
//
//  Created by Katie on 2/22/19.
//  Copyright © 2019 Katie. All rights reserved.
//

import UIKit
import MapKit


class DateTimePickerViewController: UIViewController, MKMapViewDelegate, UITextFieldDelegate {

    @IBOutlet weak var map: MKMapView!
    @IBOutlet weak var bar: UINavigationItem!
    @IBOutlet weak var arrival: UITextField!
    @IBOutlet weak var departure: UITextField!
    @IBOutlet weak var wakeUp: UITextField!
    
    /*
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
     */
    
    
    
    //need these
    var dtdate = NSDate()//depart
    var senderDate = Date()//arival
    var durationInTraffic = CLong()
    var startLoc = String()
    var endLoc = String()
    var wakeUpBefore = Int()
    
    var ditString = String()
    let picker : UIDatePicker = UIDatePicker()
    var datePickerContainer = UIView()
    var datePicked = String()
    var datePickedDate = Date()
    var departDateString = Date()
    var timePickerContainer = UIView()
    var timePicked = CLong()
    var departTimeDate = Date()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        map.delegate = self
        map.showsScale = true
        
        bar.title = "\(ditString)"
        let calendar = Calendar.current
        let arrivalCal = Calendar.current
        let date = calendar.date(byAdding: .minute, value: 2, to: Date())
        let departDate = arrivalCal.date(byAdding: .second, value: durationInTraffic + 120, to: Date())
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = .medium
        dateFormatter.timeStyle = .short
        wakeUp.delegate = self
        departTimeDate = date!
        arrival.text = dateFormatter.string(from: departDate!)
        departure.text = dateFormatter.string(from: date!)
    }

    
    
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
            let aSet = NSCharacterSet(charactersIn:"0123456789").inverted
            let compSepByCharInSet = string.components(separatedBy: aSet)
            let numberFiltered = compSepByCharInSet.joined(separator: "")
            return string == numberFiltered
    }
    
    @IBAction func arrival(_ sender: Any) {
        
        datePickerContainer.frame = CGRect(x: 0.0, y: self.view.frame.height/2, width: 320.0, height: 320.0)
        datePickerContainer.backgroundColor = UIColor.white
        
        let pickerSize : CGSize = picker.sizeThatFits(CGSize.zero)
        picker.frame = CGRect(x: 0.0, y: 20, width: pickerSize.width, height: 460)
        picker.setDate(NSDate() as Date, animated: true)
        let calendar = Calendar.current
        let departDate = calendar.date(byAdding: .second, value: durationInTraffic, to: Date())
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = .medium
        dateFormatter.timeStyle = .short
        picker.minimumDate = departDate
        //picker
        picker.addTarget(self, action:  #selector(dateChangedInDate(sender:)), for: UIControl.Event.valueChanged)
        datePickerContainer.addSubview(picker)
        
        let doneButton = UIButton()
        doneButton.setTitle("Done", for: UIControl.State.normal)
        doneButton.setTitleColor(UIColor.blue, for: UIControl.State.normal)
        doneButton.addTarget(self, action:   #selector(doneButtonClick(sender:)), for: UIControl.Event.touchUpInside)
        doneButton.frame = CGRect(x:250, y:5.0, width:70, height:37)
        
        datePickerContainer.addSubview(doneButton)
        
        self.view.addSubview(datePickerContainer)
    }

    @objc func dateChangedInDate(sender:UIDatePicker){
        senderDate = sender.date
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = DateFormatter.Style.medium
        dateFormatter.timeStyle = DateFormatter.Style.short
        datePicked = dateFormatter.string(from: sender.date)
    }
    

    
    @objc func doneButtonClick(sender:UIButton)
    {
        getURL()
        arrival.text = datePicked
        datePickerContainer.removeFromSuperview()
    }
    
    func getURL(){
        let url = getUrl(start: startLoc, end: endLoc)
        getJSON(url: url)
    }
    
    func getUrl(start: String, end:String) -> String{
        let timeInSeconds: TimeInterval = senderDate.timeIntervalSince1970
        let tis = NSInteger(timeInSeconds)
        let str_org = "origin=\(start)"
        let str_dest = "destination=\(end)"
        let sensor = "sensor=false";
        let mode = "mode=driving";
        let trafficModel = "traffic_model=pessimistic";
        let apiKey = "key=AIzaSyACz6AjczEW5-jt3EW6lYsmjfO84U2W3jI";
        let departureTime = "departure_time=\(tis)";
        //let param = str_org+"&"+str_dest+"&"+sensor+"&"+mode+"&"+trafficModel+"&"+departureTime+"&"+apiKey
        let param = "\(str_org)&\(str_dest)&\(sensor)&\(mode)&\(trafficModel)&\(departureTime)&\(apiKey)"
        let output = "json"
        let wholeURL = "https://maps.googleapis.com/maps/api/directions/\(output)?\(param)"
        return wholeURL
    }
    
    @IBAction func wakeUp(_ sender: Any) {
        
    }
    
    @IBAction func backButton(_ sender: Any) {
        navigationController?.popViewController(animated: true)
    }
    
    @IBAction func saveDetailsButton(_ sender: Any) {
        if wakeUp.text?.trimmingCharacters(in: .whitespaces) != ""{
            wakeUpBefore = Int(wakeUp.text!)!
            performSegue(withIdentifier: "toImageSound", sender: self)
        }
        
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let dVC = segue.destination as! ImageSoundViewController
        dVC.dtdate = dtdate
        dVC.senderDate = senderDate
        dVC.durationInTraffic = durationInTraffic
        dVC.startLoc = startLoc
        dVC.endLoc = endLoc
        dVC.wakeUpBefore = wakeUpBefore
    }

    func getJSON(url:String){
        //let convertstring = URL(string: url)
        let encodeString = url.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed)
        let request = URLRequest(url: URL(string: encodeString!)!)
        let session = URLSession.shared
        
        let task = session.dataTask(with: request) { (data, response, error) in
            if error != nil{
                print("error=\(String(describing: error))")
                return
            }
            let parsedResult = try? JSONSerialization.jsonObject(with: data!, options: [])
            //var data = [String:Any]()
            if let dict = parsedResult as? [String:Any]{
                if let routes = dict["routes"] as? [Any]{
                    
                    if let legs1 = routes.first as? [String:Any]{
                        for (key, value) in legs1{
                            if key == "legs"{
                                if let legs = [key:value] as? [String:Any]{
                                    if let data1 = legs["legs"] as? [Any]{
                                        if let data2 = data1.first as? [String:Any]{
                                            for item in data2{
                                                if item.key == "duration_in_traffic"{
                                                    if let gettinginfo = item.value as? [String:Any]{
                                                        for tv in gettinginfo{
                                                            if tv.key == "value"{
                                                                self.durationInTraffic = tv.value as! CLong
                                                            }else if tv.key == "text"{
                                                                self.ditString = tv.value as! String
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        
                    }
                }
            }
            DispatchQueue.main.async {
                
                let timeInSeconds: TimeInterval = self.senderDate.timeIntervalSince1970
                let tis = NSInteger(timeInSeconds)
                let dt = tis - self.durationInTraffic
                self.dtdate = NSDate(timeIntervalSince1970: TimeInterval(dt))
                let dateFormatter = DateFormatter()
                dateFormatter.dateStyle = .medium
                dateFormatter.timeStyle = .short
                self.departure.text = dateFormatter.string(from: self.dtdate as Date)
                self.bar.title = "\(self.ditString)"
            }
        }
        task.resume()
        
    }
    
}
