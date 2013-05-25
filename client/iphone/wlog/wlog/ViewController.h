//
//  ViewController.h
//  wlog
//
//  Created by TN on 2013/05/11.
//  Copyright (c) 2013年 wlog. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ViewController : UIViewController <UITextFieldDelegate>
@property (weak, nonatomic) IBOutlet UITextField *value1;
@property (weak, nonatomic) IBOutlet UILabel *myLable;
- (IBAction)tapBtn:(id)sender;


@end
