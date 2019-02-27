//
//  FirstScreenViewController.swift
//  hrubiecKatherine_GetMeThere
//
//  Created by Katie on 2/20/19.
//  Copyright © 2019 Katie. All rights reserved.
//

import UIKit
import Firebase
import FirebaseAuth

class FirstScreenViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
//        do{
//            try Auth.auth().signOut()
//        }catch let err{
//            print(err)
//        }
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 1, execute: {
            if Auth.auth().currentUser != nil{
                let storyBoard = UIStoryboard(name: "Main", bundle: nil)
                let destinationVC = storyBoard.instantiateViewController(withIdentifier: "ListVC") as? ListViewController
                self.present(destinationVC!, animated: true, completion: nil)
            }else{
                self.performSegue(withIdentifier: "leadingToLogin", sender: self)
            }
        })
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
