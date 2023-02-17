package org.firstinspires.ftc.teamcode.autoprog;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.DMHardwareTest;

@TeleOp(name="firstTest", group="prototypes")
public class firstTest extends LinearOpMode {
    public DMHardwareTest robot = new DMHardwareTest(true);
    boolean applyBreak =  false;

    @Override
    public void runOpMode() {
        double y;
        double x;
        double rx;

        y = gamepad1.right_stick_x;
        x = gamepad1.left_stick_x;
        rx = gamepad1.left_stick_y;

                    // Denominator is the largest motor power (absolute value) or 1
                    // This ensures all the powers maintain the same ratio, but only when
                    // at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (-y - x + rx) / denominator;
        double backLeftPower = (-y + x + rx) / denominator;
        double frontRightPower = (-y - x - rx) / denominator;
        double backRightPower = (-y + x - rx) / denominator;

        robot.frontLeft.setPower(frontLeftPower * 0.70);
        robot.backLeft.setPower(backLeftPower * 0.70);
        robot.frontRight.setPower(frontRightPower * 0.70);
        robot.backRight.setPower(backRightPower * 0.70);

        idle();
    }
    }

