//
//  SignUpViewController.swift
//  hrubiecKatherine_GetMeThere
//
//  Created by Katie on 2/20/19.
//  Copyright © 2019 Katie. All rights reserved.
//

import UIKit
import Firebase
import FirebaseAuth
import FirebaseDatabase

class SignUpViewController: UIViewController {
    
    @IBOutlet weak var first: UITextField!
    @IBOutlet weak var last: UITextField!
    @IBOutlet weak var email: UITextField!
    @IBOutlet weak var password: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
    }
    @IBAction func signUpButton(_ sender: UIButton) {
        
        if email.text?.trimmingCharacters(in: .whitespaces) != "" && password.text?.trimmingCharacters(in: .whitespaces) != "" &&  first.text?.trimmingCharacters(in: .whitespaces) != "" &&
            last.text?.trimmingCharacters(in: .whitespaces) != ""{
            //checkEmailPassword(userEmail: email.text!)
            
            let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
            let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
            let emailValid = emailTest.evaluate(with: email.text)
            if emailValid == true{
                //email is vaild
                //firebase signup verification
                Auth.auth().createUser(withEmail: email.text!, password: password.text!) { (authData, error) in
                    if error == nil{
                        let userData = ["email": self.email.text,
                                        "firstname": self.first.text,
                                        "lastname": self.last.text,
                                        "password": self.password.text]
                        let ref = Database.database().reference()
                        ref.child("users").child((authData?.user.uid)!).setValue(userData)
                        if Auth.auth().currentUser != nil{
                            //self.performSegue(withIdentifier: "showList", sender: self)
                            let storyBoard = UIStoryboard(name: "Main", bundle: nil)
                            let destinationVC = storyBoard.instantiateViewController(withIdentifier: "ListVC") as? ListViewController
                            self.present(destinationVC!, animated: true, completion: nil)
                        }
                        
                    }else{
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
            let alert = UIAlertController(title: "Alert", message: "Please enter a valid first name, last name, email, and password", preferredStyle: UIAlertController.Style.alert)
            alert.addAction(UIAlertAction(title: "Ok", style: UIAlertAction.Style.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
            self.email.text = ""
            self.password.text = ""
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
