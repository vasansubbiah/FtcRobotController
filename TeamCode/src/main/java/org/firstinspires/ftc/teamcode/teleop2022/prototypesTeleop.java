package org.firstinspires.ftc.teamcode.teleop2022;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DMHardwareTest;

@TeleOp(name="prototypesTeleop", group="prototypes")
public class prototypesTeleop extends LinearOpMode {
    public DMHardwareTest robot = new DMHardwareTest(false);
    boolean bk =  true;

    @Override
    public void runOpMode() {
        double y;
        double x;
        double rx;

        robot.initTeleOpIMU(hardwareMap, bk);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            y = gamepad1.right_stick_x;
            x = gamepad1.right_stick_y;
            rx = gamepad1.left_stick_y;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (-y + x + rx) / denominator;
            double backLeftPower = (-y - x + rx) / denominator;
            double frontRightPower = (-y - x - rx) / denominator;
            double backRightPower = (-y + x - rx) / denominator;

            robot.frontLeft.setPower(frontLeftPower/3.0);
            robot.backLeft.setPower(backLeftPower/3.0);
            robot.frontRight.setPower(frontRightPower/3.0);
            robot.backRight.setPower(backRightPower/3.0);
        }
    }
}
