package org.firstinspires.ftc.teamcode.teleop2022;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.DMHardwareTest;

@TeleOp(name="prototypesTeleop", group="prototypes")
public class prototypesTeleop extends LinearOpMode {
    public DMHardwareTest robot = new DMHardwareTest(true);
    boolean bk =  true;

    @Override
    public void runOpMode() {
        double y;
        double x;
        double rx;
        boolean linear_slide_up;
        boolean linear_slide_down;
        int curposLeft;
        int curposRight;
        int newposLeft = 0;
        int newposRight =0;
        int ticksPerRev = 240;  // 1 revolution
        double power = 1.0;     // Adjust the power of the slide of the power
        int rev = 1;

        robot.initTeleOpIMU(hardwareMap, bk);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        robot.slidemotorleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.slidemotorright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            y = gamepad1.right_stick_x;
            x = gamepad1.left_stick_x;
            rx = gamepad1.left_stick_y;
            linear_slide_up = gamepad1.dpad_up;
            linear_slide_down = gamepad1.dpad_down;



            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (-y - x + rx) / denominator;
            double backLeftPower = (-y + x + rx) / denominator;
            double frontRightPower = (-y - x - rx) / denominator;
            double backRightPower = (-y + x - rx) / denominator;


            robot.frontLeft.setPower(frontLeftPower/2.0);
            robot.backLeft.setPower(backLeftPower/2.0);
            robot.frontRight.setPower(frontRightPower/2.0);
            robot.backRight.setPower(backRightPower/2.0);


            //////////////////////////////////////////////////////////////////////////////////////
            // Slide motor Code
            // ////////////////////////////////////////////////////////////////////////////////////
            curposLeft = robot.slidemotorleft.getCurrentPosition();
            curposRight = robot.slidemotorright.getCurrentPosition();

            telemetry.addLine(String.format("\nIn SlideControl motor encoder position left = %d right = %d", curposLeft, curposRight));
            telemetry.update();

            if (linear_slide_up) {
                newposLeft = curposLeft + (ticksPerRev * rev);
                newposRight = curposRight - (ticksPerRev * rev);

                robot.slidemotorleft.setTargetPosition(newposLeft);
                robot.slidemotorright.setTargetPosition(newposRight);
                robot.slidemotorleft.setPower(power);
                robot.slidemotorright.setPower(power);
                robot.slidemotorleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.slidemotorright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            else if (linear_slide_down) {
                newposLeft = curposLeft - (ticksPerRev * rev);
                newposRight = curposRight - (ticksPerRev * rev);

                robot.slidemotorleft.setTargetPosition(newposLeft);
                robot.slidemotorright.setTargetPosition(newposRight);
                robot.slidemotorleft.setPower(power);
                robot.slidemotorright.setPower(power);
                robot.slidemotorleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.slidemotorright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }


            while (robot.slidemotorleft.isBusy() || robot.slidemotorleft.isBusy() ){
                curposLeft = robot.slidemotorleft.getCurrentPosition();
                curposRight = robot.slidemotorright.getCurrentPosition();
                telemetry.addLine(String.format("\n slide modtor encoder Left position = %d , Right position = %d", curposLeft, curposRight));
                telemetry.update();
            }

        }
    }
}
