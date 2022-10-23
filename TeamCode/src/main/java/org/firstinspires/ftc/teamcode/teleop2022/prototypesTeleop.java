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
        double y = gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        robot.initTeleOpIMU(hardwareMap, bk);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            robot.frontLeft.setPower(y + x + rx);
            robot.backLeft.setPower(y - x + rx);
            robot.frontRight.setPower(y - x - rx);
            robot.backRight.setPower(y + x - rx);
        }
    }
}
