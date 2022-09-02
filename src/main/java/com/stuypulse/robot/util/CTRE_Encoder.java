package com.stuypulse.robot.util;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;

public class CTRE_Encoder {
    
    private final BaseMotorController motor;

    public CTRE_Encoder(BaseMotorController motor, FeedbackDevice feedbackDevice) { 
        this.motor = motor;
        
        motor.configSelectedFeedbackSensor(feedbackDevice);
    }

}
