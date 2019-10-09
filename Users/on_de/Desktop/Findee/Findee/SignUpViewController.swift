//
//  SignUpViewController.swift
//  Findee
//
//  Created by михаил on 08/10/2019.
//  Copyright © 2019 михаил. All rights reserved.
//

import UIKit

class SignUpViewController: UIViewController {

    @IBOutlet weak var changeBtnOutlet: UISegmentedControl!
    @IBOutlet weak var professionLabel: UILabel!

    
    @IBOutlet weak var professionTextBox: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        professionLabel.isHidden = true
        professionTextBox.isHidden = true
    }
    
    @IBAction func changeBtn(_ sender: Any) {
       if changeBtnOutlet?.rightAnchor != nil
        {
            professionLabel.isHidden = false
        professionTextBox.isHidden = false
        }
        else
       {
        professionLabel.isHidden = true
        professionTextBox.isHidden = true
        }
    }
    
  

}
