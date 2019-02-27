//
//  DetailViewController.swift
//  hrubiecKatherine_GetMeThere
//
//  Created by Katie on 2/20/19.
//  Copyright © 2019 Katie. All rights reserved.
//

import UIKit

class DetailViewController: UIViewController, UINavigationControllerDelegate {

    var alarmSelected: Alarm?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    @IBAction func backButton(_ sender: Any) {
        navigationController?.popViewController(animated: true)
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
