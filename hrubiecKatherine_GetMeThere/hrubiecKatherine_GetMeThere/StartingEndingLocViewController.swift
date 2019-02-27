//
//  StartingEndingLocViewController.swift
//  hrubiecKatherine_GetMeThere
//
//  Created by Katie on 2/21/19.
//  Copyright © 2019 Katie. All rights reserved.
//

import UIKit
import MapKit
import CoreLocation
import Foundation
import GoogleMaps

class StartingEndingLocViewController: UIViewController, CLLocationManagerDelegate, MKMapViewDelegate, GMSMapViewDelegate{
    
    @IBOutlet weak var viewForMap: UIView!
    
    var locationManager = CLLocationManager()
    var newMapInfo: MKMapView?
    var userLoc = CLLocationCoordinate2D()

    var durationInTraffic = CLong()
    var startLoc = String()
    var endLoc = String()
    var gmaps: GMSMapView?
    var startLat = Double()
    var startLng = Double()
    var endLat = Double()
    var endLng = Double()
    var ditString = String()
    
    var sourceLoc = CLLocationCoordinate2D()
    var destinationLoc = CLLocationCoordinate2D()
    
    @IBOutlet weak var map: MKMapView!
    @IBOutlet weak var editButton: UIBarButtonItem!
    
    @IBOutlet weak var nextButton: UIBarButtonItem!
    @IBOutlet weak var startingLoc: UITextField!
    @IBOutlet weak var endingLoc: UITextField!
    //@IBOutlet weak var map: MKMapView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        map.delegate = self
        map.showsScale = true
        if map.overlays.count == 0 {
            nextButton.isEnabled = false
            editButton.isEnabled = false
        }
//        let camera = GMSCameraPosition.camera(withLatitude: 28.7041, longitude: 77.1025, zoom: 10.0)
//        let mapView = GMSMapView.map(withFrame: self.viewForMap.frame, camera: camera)
//        self.view.addSubview(mapView)
    }
    
    @IBAction func editClick(_ sender: Any) {
        nextButton.isEnabled = false
        editButton.isEnabled = false
        startingLoc.isEnabled = true
        endingLoc.isEnabled = true
    }
    
    
    @IBAction func backButton(_ sender: Any) {
        for overlay in map.overlays{
            map.removeOverlay(overlay)
        }
        navigationController?.popViewController(animated: true)
    }
    
    @IBAction func nextButton(_ sender: Any) {
        performSegue(withIdentifier: "toSetTime", sender: self)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        
    }
    
    
    @IBAction func searchButton(_ sender: Any) {
        for overlay in map.overlays{
            map.removeOverlay(overlay)
        }
        
        if startingLoc.text?.trimmingCharacters(in: .whitespaces) != "" && endingLoc.text?.trimmingCharacters(in: .whitespaces) != ""{
            //contruct url
            let url = getUrl(start: startingLoc.text!, end: endingLoc.text!)
            getJSON(url: url)
        }
    }

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let dVC = segue.destination as! DateTimePickerViewController
        dVC.startLoc = startLoc
        dVC.endLoc = endLoc
        dVC.durationInTraffic = durationInTraffic
        dVC.ditString = ditString
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
                                                }else if item.key == "start_address"{
                                                    self.startLoc = item.value as! String
                                                }else if item.key == "end_address"{
                                                    self.endLoc = item.value as! String
                                                }else if item.key == "start_location"{
                                                    if let gettinginfo = item.value as? [String:Any]{
                                                        for tv in gettinginfo{
                                                            if tv.key == "lat"{
                                                                self.startLat = tv.value as! Double
                                                            }else if tv.key == "lng"{
                                                                self.startLng = tv.value as! Double
                                                            }
                                                        }
                                                    }
                                                }else if item.key == "end_location"{
                                                    if let gettinginfo = item.value as? [String:Any]{
                                                        for tv in gettinginfo{
                                                            if tv.key == "lat"{
                                                                self.endLat = tv.value as! Double
                                                                print(tv.value)
                                                            }else if tv.key == "lng"{
                                                                self.endLng = tv.value as! Double
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
                self.startingLoc.text = self.startLoc
                self.endingLoc.text = self.endLoc
                self.startingLoc.isEnabled = false
                self.endingLoc.isEnabled = false
                self.nextButton.isEnabled = true
                self.editButton.isEnabled = true
                self.sourceLoc = CLLocationCoordinate2D(latitude: CLLocationDegrees(self.startLat), longitude: CLLocationDegrees(self.startLng))
                self.destinationLoc = CLLocationCoordinate2D(latitude: CLLocationDegrees(self.endLat), longitude: CLLocationDegrees(self.endLng))
               let startPin = MKPointAnnotation()
                startPin.coordinate = self.sourceLoc
                let endPin = MKPointAnnotation()
                endPin.coordinate = self.destinationLoc
                self.map.addAnnotation(startPin)
                self.map.addAnnotation(endPin)
                
                let sourcePlacemark = MKPlacemark(coordinate: self.sourceLoc)
                let destinationPlacemark = MKPlacemark(coordinate: self.destinationLoc)
                
                let directionsRequest = MKDirections.Request()
                directionsRequest.source = MKMapItem(placemark: sourcePlacemark)
                directionsRequest.destination = MKMapItem(placemark: destinationPlacemark)
                directionsRequest.transportType = .automobile
                
                let directions = MKDirections(request: directionsRequest)
                directions.calculate(completionHandler: { (response, error) in
                    guard let directionResponse = response else {
                        if let error = error{
                            print(error)
                        }
                        return
                    }
                    let route = directionResponse.routes[0]
                    self.map.addOverlay(route.polyline, level: .aboveRoads)
                    let rect = route.polyline.boundingMapRect
                    self.map.setRegion(MKCoordinateRegion(rect), animated: true)
                })
                
            }
        }
        task.resume()
        
    }

    
    func mapView(_ mapView: MKMapView, rendererFor overlay: MKOverlay) -> MKOverlayRenderer {
        let renderer = MKPolylineRenderer(overlay: overlay)
        renderer.strokeColor = UIColor.blue
        renderer.lineWidth = 4.0
        return renderer
    }
    
    //polyline on map
//    func showPath(polyStr :String){
//        let path = GMSPath(fromEncodedPath: polyStr)
//        let polyline = GMSPolyline(path: path)
//        polyline.strokeWidth = 3.0
//        polyline.map = gmaps // Your map view
//    }
    
    //sets user loc info
    func userLocInfo(){
        let uLoc = map.userLocation.coordinate
        userLoc = uLoc

        locationManager.requestWhenInUseAuthorization()

        if CLLocationManager.locationServicesEnabled(){
            locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            locationManager.startUpdatingLocation()
        }else{
            dismiss(animated: true, completion: nil)
        }
    }
    
    func getUrl(start: String, end:String) -> String{
        let str_org = "origin=\(start)"
        let str_dest = "destination=\(end)"
        let sensor = "sensor=false";
        let mode = "mode=driving";
        let trafficModel = "traffic_model=pessimistic";
        let apiKey = "key=AIzaSyACz6AjczEW5-jt3EW6lYsmjfO84U2W3jI";
        let departureTime = "departure_time=now";
        //let param = str_org+"&"+str_dest+"&"+sensor+"&"+mode+"&"+trafficModel+"&"+departureTime+"&"+apiKey
        let param = "\(str_org)&\(str_dest)&\(sensor)&\(mode)&\(trafficModel)&\(departureTime)&\(apiKey)"
        let output = "json"
        let wholeURL = "https://maps.googleapis.com/maps/api/directions/\(output)?\(param)"
        print(wholeURL)
        return wholeURL
    }
    
}
