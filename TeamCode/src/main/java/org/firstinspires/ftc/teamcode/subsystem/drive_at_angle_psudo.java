package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Andrew on 12/23/2017.
 */

public class drive_at_angle_psudo {
    private DcMotor fl;
    private DcMotor fr;
    private DcMotor br;
    private DcMotor bl;


    public drive_at_angle_psudo(HardwareMap hardwareMap) {

            fr = hardwareMap.dcMotor.get("fr");
            fl = hardwareMap.dcMotor.get("fl");
            bl = hardwareMap.dcMotor.get("bl");
            br = hardwareMap.dcMotor.get("br");
        }
    public void angle (double angle1, double speed) {

        double rad = 3.14159*angle1/180;
        double x = Math.sin(rad);
        double y = -1*Math.cos(rad);
        x=Math.round(1000*x)/1000;
        y=Math.round(1000*y)/1000;
//        double frSpeed = (speed) * (-Math.sin(angle1) + Math.cos(angle1));
//        double flSpeed = (speed) * (-Math.sin(angle1) - Math.cos(angle1));
//        double brSpeed = (speed) * (+Math.sin(angle1) + Math.cos(angle1));
//        double blSpeed = (speed) * (+Math.sin(angle1) - Math.cos(angle1));
        double frSpeed = (speed)*(-y -x );
        double flSpeed = (speed)*(+y -x );
        double brSpeed = (speed)*(-y +x);
        double blSpeed = (speed)*(+y +x);
        String output = "fr: " + frSpeed + "fl: " + flSpeed + "bl " +blSpeed + "br " + brSpeed;
        fr.setPower(frSpeed);
        fl.setPower(flSpeed);
        br.setPower(brSpeed);
        bl.setPower(blSpeed);

    }}






