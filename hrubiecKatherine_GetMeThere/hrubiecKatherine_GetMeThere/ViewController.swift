//
//  ViewController.swift
//  hrubiecKatherine_GetMeThere
//
//  Created by Katie on 2/10/19.
//  Copyright © 2019 Katie. All rights reserved.
//

import UIKit
import Firebase
import FirebaseAuth

class ViewController: UIViewController {

    @IBOutlet weak var email: UITextField!
    @IBOutlet weak var password: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
//        do{
//            try Auth.auth().signOut()
//        }catch let err{
//            print(err)
//        }
        
        // Do any additional setup after loading the view, typically from a nib.
        if Auth.auth().currentUser != nil{
            performSegue(withIdentifier: "showList", sender: Any?.self)
        }
    }
    
    @IBAction func signUp(_ sender: UIButton) {
        performSegue(withIdentifier: "showSignUp", sender: Any?.self)
    }
    
    @IBAction func login(_ sender: Any) {
        
        if email.text?.trimmingCharacters(in: .whitespaces) != "" && password.text?.trimmingCharacters(in: .whitespaces) != "" {
            //checkEmailPassword(userEmail: email.text!)
            
            let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
            let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
            let emailValid = emailTest.evaluate(with: email.text)
            if emailValid == true{
                //email is vaild
                //firebase login verification
                Auth.auth().signIn(withEmail: self.email.text!, password: self.password.text!) { (user, error) in
                    if error == nil{
                        print("You have successfully logged in")
                        self.showList()
                    }else{
                        //Tells the user that there is an error and then gets firebase to tell them the error
                        let alertController = UIAlertController(title: "Error", message: error?.localizedDescription, preferredStyle: .alert)
                        let defaultAction = UIAlertAction(title: "OK", style: .cancel, handler: nil)
                        alertController.addAction(defaultAction)
                        self.present(alertController, animated: true, completion: nil)
                    }
                }
            }else if emailValid == false{
                //not a vailid email
                let alert = UIAlertController(title: "Alert", message: "Please enter a valid email", preferredStyle: UIAlertController.Style.alert)
                alert.addAction(UIAlertAction(title: "Ok", style: UIAlertAction.Style.default, handler: nil))
                self.present(alert, animated: true, completion: nil)
                self.email.text = ""
                self.password.text = ""
            }
        }else{
            let alert = UIAlertController(title: "Alert", message: "Please enter a valid email and password", preferredStyle: UIAlertController.Style.alert)
            alert.addAction(UIAlertAction(title: "Ok", style: UIAlertAction.Style.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
            self.email.text = ""
            self.password.text = ""
        }
        //login to firebase
        //performSegue(withIdentifier: "showDetail", sender: Any?.self)
    }
    
    func showList(){
        performSegue(withIdentifier: "showList", sender: Any?.self)
    }
    

}

