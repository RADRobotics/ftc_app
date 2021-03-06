package org.firstinspires.ftc.teamcode.ChallengeFile;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.utils.drive_at_angle_psudo;
import org.firstinspires.ftc.teamcode.utils.gyroCompass;
import org.firstinspires.ftc.teamcode.utils.turnTo;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOpTestBot", group = "ChallengeFile")
public class TeleOpTestBot extends OpMode {

    public DcMotor fl;
    public DcMotor fr;
    public DcMotor br;
    public DcMotor bl;

    gyroCompass testGyro;
    drive_at_angle_psudo thing;
    public turnTo turn;


    int column1 = 0;
    int column2 = 0;
    int column3 = 0;
    boolean balanceEnabled;
    boolean offBalance;
    double angle2 = 0;
    boolean omnidrive = false;
    boolean startPressed = false;
    boolean gyroReset = false;
    boolean Driving;
    double i;
    double p;
    double intake;

    //    Servo leftSorter;
//    Servo rightSorter;
    @Override
    public void init() {
        intake = 0;
        fr = hardwareMap.dcMotor.get("fr");
        fl = hardwareMap.dcMotor.get("fl");
        bl = hardwareMap.dcMotor.get("bl");
        br = hardwareMap.dcMotor.get("br");
//        leftSorter = hardwareMap.servo.get("leftSorter");
//       rightSorter = hardwareMap.servo.get("rightSorter");

        testGyro = new gyroCompass(hardwareMap);
        balanceEnabled = true;
        thing = new drive_at_angle_psudo(hardwareMap);
        turn = new turnTo(hardwareMap, testGyro);
    }

    @Override
    public void loop() {

        if (gamepad1.start && !gyroReset && !gamepad1.a && !gamepad1.b) {
            gyroReset = true;
            testGyro.reset();
        } else if (!gamepad1.start) {
            gyroReset = false;
        }
        telemetry.addData("x", gamepad1.left_stick_x);
        telemetry.addData("y", gamepad1.left_stick_y);
        telemetry.addData("x2", gamepad1.right_stick_x);
        if ((Math.abs(gamepad1.left_stick_x) > .05) || (Math.abs(gamepad1.left_stick_y) > .05) || (Math.abs(gamepad1.right_stick_x) > .05)) {
            Driving = true;
            turn.reset();
        } else {
            Driving = false;
        }
        double speed = 0.35;
        double speed2 = 0.7;
        speed = speed + gamepad1.right_trigger * 0.65 - gamepad1.left_trigger * 0.25;
        speed2 = speed2 + gamepad2.right_trigger / 1.65;

        if (gamepad1.y && startPressed == false) {
            startPressed = true;
            omnidrive = !omnidrive;
        } else if (!gamepad1.y) {
            startPressed = false;
        }


        telemetry.addData("Omnidrive", omnidrive);
        telemetry.addData("balanceEnabled", balanceEnabled);
        telemetry.addData("driving", Driving);
        if (gamepad1.a) {
            balanceEnabled = true;

        }
        if (gamepad1.b) {
            balanceEnabled = false;
        }

        //System.out.println(gamepad1.right_bumper);
        if (!omnidrive && Driving) {
            double x2 = gamepad1.left_stick_x;
            double y2 = -1 * gamepad1.left_stick_y;
            if (x2 > 0 && y2 > 0) {
                angle2 = Math.toDegrees(Math.atan(x2 / y2));
            } else if (x2 > 0 && y2 <= 0) {
                angle2 = 90 + -1 * Math.toDegrees(Math.atan(y2 / x2));
            } else if (x2 < 0 && y2 <= 0) {
                angle2 = -90 + -1 * Math.toDegrees(Math.atan(y2 / x2));
            } else if (x2 < 0 && y2 > 0) {
                angle2 = Math.toDegrees(Math.atan(x2 / y2));
            } else if (x2 == 0 && y2 < 0.15) {
                angle2 = 180.0;
            } else if (x2 == 0 && y2 > 0.15) {
                angle2 = 0.0;
            }
            double magnitude = Math.sqrt(Math.pow(x2, 2) + Math.pow(y2, 2));
            telemetry.addData("gyro", testGyro.getHeading());
//        if(((Math.abs(x)>.08)||(Math.abs(y)>.08))){
//                turn.turnT(angle, p, i, 0.0, 1);
//            dontDoIt=true;
//            }

            if (((Math.abs(x2) > .08) || (Math.abs(y2) > .08))) {
                thing.angle(angle2 - (-1 * testGyro.getHeading()), magnitude * speed, -1 * gamepad1.right_stick_x * (speed + .03));
                telemetry.addData("angle", angle2);
                telemetry.addData("target angle", angle2 - (-1 * testGyro.getHeading()));
            } else if (Math.abs(gamepad1.right_stick_x) > .05) {
                fr.setPower(-1 * gamepad1.right_stick_x * speed);
                fl.setPower(-1 * gamepad1.right_stick_x * speed);
                br.setPower(-1 * gamepad1.right_stick_x * speed);
                bl.setPower(-1 * gamepad1.right_stick_x * speed);
            }
        } else if (omnidrive && Driving) {
            double frSpeed = (speed) * (-gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x);
            double flSpeed = (speed) * (+gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x);
            double brSpeed = (speed) * (-gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x);
            double blSpeed = (speed) * (+gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x);
            //if(offBalance&&balanceEnabled) {
            fr.setPower(frSpeed);
            fl.setPower(flSpeed);
            br.setPower(brSpeed);
            bl.setPower(blSpeed);
        } else if (gamepad1.dpad_up) {
            telemetry.addData("i", turn.turnT(0, 0.0052, 0.002, 0.0, 1));
        } else if (gamepad1.dpad_down) {
            telemetry.addData("i", turn.turnT(179.9, 0.0052, 0.002, 0.0, 1));
        } else if (gamepad1.dpad_right) {
            telemetry.addData("i", turn.turnT(-90, 0.0052, 0.002, 0.0, 1));
        } else if (gamepad1.dpad_left) {
            telemetry.addData("i", turn.turnT(90, 0.0052, 0.002, 0.0, 1));
        } else if (!Driving && balanceEnabled) {
            turn.reset();
            double pitch = -1 * testGyro.getPitch();
            double roll = -1 * (testGyro.getRoll());

            double asdf = testGyro.getHeading();
            telemetry.addData("roll", roll);
            telemetry.addData("pitch", pitch);
            telemetry.addData("heading", asdf);

            if ((roll > 2) || (roll < -2) || (pitch > 2) || (pitch < -2)) {

                if (roll < 2 && roll > -2) {
                    roll = 0;
                }
                if (pitch < 2 && pitch > -2) {
                    pitch = 0;
                }

                fr.setPower((speed - .3) * (-roll + pitch));
                fl.setPower((speed - .3) * (+roll + pitch));
                br.setPower((speed - .3) * (-roll - pitch));
                bl.setPower((speed - .3) * (+roll - pitch));
                telemetry.addData("balancing", ".");
            } else {
                fr.setPower(0);
                fl.setPower(0);
                br.setPower(0);
                bl.setPower(0);
                telemetry.addData("Level", "");
                offBalance = false;
            }
        } else if (!Driving) {
            turn.reset();
            fr.setPower(0);
            fl.setPower(0);
            br.setPower(0);
            bl.setPower(0);
        }
    }
}