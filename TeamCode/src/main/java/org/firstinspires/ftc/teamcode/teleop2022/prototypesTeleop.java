package org.firstinspires.ftc.teamcode.teleop2022;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.DMHardwareTest;

@TeleOp(name="prototypesTeleop", group="prototypes")
public class prototypesTeleop extends LinearOpMode {
    public DMHardwareTest robot = new DMHardwareTest(true);
    boolean applyBreak =  true;

    @Override
    public void runOpMode() {
        boolean linear_slide_up;
        boolean linear_slide_down;
        boolean linear_slide_down_left;
        boolean linear_slide_up_left;
        boolean linear_slide_down_right;
        boolean linear_slide_up_right;

        int curposLeft;
        int curposRight;
        int newposLeft = 0;
        int newposRight =0;
        int ticksPerRev = 120;  // 1/2 revolution
        double power = 0.9;     // Adjust the power of the slide of the power
        int MAXSLIDEPOS = 1500;  // Adjust this value
        int rev = 3;

        boolean ClawOn=false;
        boolean protect = false;
        double clawOffsetL = 1;
        double clawOffsetR = 1;
        double servo_power = 0.5;
        double CLAW_SPEED = 0.05;                 // sets rate to move servo
        double  MIN_POSITION = 0.2;
        double MIN_POSITIONR=0.1;
        double MAX_POSITION = 0.5;
        double MAX_POSITIONL = 0.7;

        Thread  driveThread = new DriveThread();

        robot.initTeleOpIMU(hardwareMap, applyBreak);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        robot.slidemotorleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.slidemotorright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        driveThread.start();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            linear_slide_up = gamepad2.dpad_up;
            linear_slide_down = gamepad2.dpad_down;

            linear_slide_up_left = gamepad2.x;
            linear_slide_down_left = gamepad2.a;
            linear_slide_up_right = gamepad2.y;
            linear_slide_down_right = gamepad2.b;
            protect = gamepad2.start;

            //////////////////////////////////////////////////////////////////////////////////////
            // Slide motor Code
            // ////////////////////////////////////////////////////////////////////////////////////
            curposLeft = robot.slidemotorleft.getCurrentPosition();
            curposRight = robot.slidemotorright.getCurrentPosition();

            //telemetry.addLine(String.format("\nIn SlideControl motor encoder position left = %d right = %d", curposLeft, curposRight));
            //telemetry.update();
            // We need to move one motor from 0 to MAX and the other from 0 to -MAX

            if (linear_slide_up) {
                newposLeft = Math.min((curposLeft + (ticksPerRev * rev)),MAXSLIDEPOS);
                newposRight = Math.min((curposRight + (ticksPerRev * rev)),MAXSLIDEPOS);

                robot.slidemotorleft.setTargetPosition(newposLeft);
                robot.slidemotorright.setTargetPosition(newposRight);
                robot.slidemotorleft.setPower(power);
                robot.slidemotorright.setPower(power);
                robot.slidemotorleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.slidemotorright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            else if (linear_slide_down) {
                if(protect) {
                    newposLeft = Math.max((curposLeft - (ticksPerRev * rev)), 0);
                    newposRight = Math.max((curposRight- (ticksPerRev * rev)),0);
                } else {
                    newposLeft = (curposLeft - (ticksPerRev * rev));
                    newposRight = (curposRight- (ticksPerRev * rev));
                }

                // left motor is going from 0 to MAX and from MAX to 0. When going down , we never want to go below 0.
                // So we choose max of current position and zero. If the motor undeshoots and goes negative, it will still
                // saturate to zero
                // The right motor is going the opposite direction. From 0 to -MAX when going up and -MAX to 0 when going down.
                // If it overshoots, the value is positive. We compare with zero and choose the min value to saturate
                //newposRight =Math.min((curposRight + (ticksPerRev * rev)),0);

                robot.slidemotorleft.setTargetPosition(newposLeft);
                robot.slidemotorright.setTargetPosition(newposRight);
                robot.slidemotorleft.setPower(power);
                robot.slidemotorright.setPower(power);
                robot.slidemotorleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.slidemotorright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }  else if ( linear_slide_up_left) {
                newposLeft = (curposLeft + (ticksPerRev * 1));
                robot.slidemotorleft.setTargetPosition(newposLeft);
                robot.slidemotorleft.setPower(0.5);
                robot.slidemotorleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            } else if (linear_slide_up_right) {
                newposRight = (curposRight + (ticksPerRev * 1));
                robot.slidemotorright.setTargetPosition(newposRight);
                robot.slidemotorright.setPower(0.5);
                robot.slidemotorright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            } else if (linear_slide_down_left) {
                newposLeft = (curposLeft - (ticksPerRev * 1));
                robot.slidemotorleft.setTargetPosition(newposLeft);
                robot.slidemotorleft.setPower(0.5);
            } else if (linear_slide_down_right) {
                newposRight =(curposRight - (ticksPerRev * 1));
                robot.slidemotorright.setTargetPosition(newposRight);
                robot.slidemotorright.setPower(0.5);
                robot.slidemotorright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }


            while (robot.slidemotorleft.isBusy() || robot.slidemotorright.isBusy()){
                curposLeft = robot.slidemotorleft.getCurrentPosition();
                curposRight = robot.slidemotorright.getCurrentPosition();
                telemetry.addLine(String.format("\n slide motor encoder Left position = %d , Right position = %d", curposLeft, curposRight));
                telemetry.update();
            }
            // Use gamepad2 left & right Bumpers to open and close the claw
            if (gamepad2.right_bumper && clawOffsetL>=MIN_POSITION && clawOffsetR<=MAX_POSITION)
            {
                clawOffsetL -= CLAW_SPEED;
                clawOffsetR += CLAW_SPEED;
            }
            if (gamepad2.left_bumper && clawOffsetL<=MAX_POSITIONL && clawOffsetR>=MIN_POSITIONR)
            {
                clawOffsetL += CLAW_SPEED;
                clawOffsetR -= CLAW_SPEED;
            }
            clawOffsetL = Range.clip(clawOffsetL, 0.2, 0.7);
            clawOffsetR = Range.clip(clawOffsetR, 0.1, 0.4);
            robot.LeftClaw.setPosition(clawOffsetL);
            robot.RightClaw.setPosition(clawOffsetR);


            // Send telemetry message to signify robot running;

            telemetry.addData("Motor Encoder Left = %d", curposLeft);
            telemetry.addData("Motor Encoder right = %d", curposRight);
            telemetry.addData("left_clawoffset",  "Offset = %.2f", clawOffsetL);
            telemetry.addData("left_position","offset = %.6f", robot.LeftClaw.getPosition());
            telemetry.addData("right_clawoffset",  "Offset = %.3f", clawOffsetR);
            telemetry.addData("right_position","offset = %.7f", robot.RightClaw.getPosition());
            telemetry.update();
        }
        driveThread.interrupt();
    }

    private class DriveThread extends Thread {
        double y;
        double x;
        double rx;

        public DriveThread() {
            this.setName("DriveThread");
        }

        // called when tread.start is called. thread stays in loop to do what it does until exit is
        // signaled by main code calling thread.interrupt.
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
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

                    robot.frontLeft.setPower(frontLeftPower/2.0);
                    robot.backLeft.setPower(backLeftPower/2.0);
                    robot.frontRight.setPower(frontRightPower/2.0);
                    robot.backRight.setPower(backRightPower/2.0);

                    idle();
                }
            } catch (Exception e) {
                telemetry.addData("%s exception", this.getName());
            }

        }
    }
}
