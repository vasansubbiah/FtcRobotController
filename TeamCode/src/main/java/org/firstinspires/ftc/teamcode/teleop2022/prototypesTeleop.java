package org.firstinspires.ftc.teamcode.teleop2022;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.DMHardwareTest;

@TeleOp(name="prototypesTeleop", group="prototypes")
public class prototypesTeleop extends LinearOpMode {
    public DMHardwareTest robot = new DMHardwareTest(true);
    boolean applyBreak =  true;

    double integralSum = 0;
    double Kp = 0;
    double Ki = 0;
    double Kd = 0;
    int SlidePos = 0;
    int loopCount = 0;
    ElapsedTime timer = new ElapsedTime();
    private double lastError = 0;

    public double PIDcontrol(double reference, double state) {
        double error = reference - state;
        integralSum += error + timer.seconds();
        double derivative = (error - lastError) / timer.seconds();
        lastError = error;

        timer.reset();

        double output = (error + Kp) + (derivative + Kd) + (integralSum +Ki);
        return output;
    }

    public void SlideDown(int ticks, double speed) {
        robot.slidemotorright.setTargetPosition(ticks);
        robot.slidemotorright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.slidemotorright.setPower(-speed);
        while (robot.slidemotorright.isBusy()) {
        }
        robot.slidemotorright.setPower(0.0);
    }

    @Override
    public void runOpMode() {
        boolean linear_slide_up;
        boolean linear_slide_down;
        boolean linear_slide_down_left;
        boolean linear_slide_up_left;
        boolean linear_slide_down_right;
        boolean linear_slide_up_right;
        double lt;
        double rt;
        boolean up;

        int curposLeft;
        int curposRight;
        int newposLeft = 0;
        int newposRight =0;
        int ticksPerRev = 80;  // 1/2 revolution
        double power = 0.9;     // Adjust the power of the slide of the power
        int MAXSLIDEPOS = 3000;  // Adjust this value
        int rev = 3;

        boolean ClawOn=false;
        boolean protect = false;
        double clawOffsetL = 0.5;
        double clawOffsetR = 0.2;
        double servo_power = 0.5;
        double CLAW_SPEED = 0.09;                 // sets rate to move servo
        double  MIN_POSITION = 0.2;
        double MIN_POSITIONR=0.2;
        double MAX_POSITION = 0.5;
        double MAX_POSITIONL = 0.5;

        Thread  driveThread = new DriveThread();

        robot.initTeleOpIMU(hardwareMap, applyBreak);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        driveThread.start();

        robot.slidemotorright.setDirection(DcMotor.Direction.REVERSE);
        robot.slidemotorright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            /*linear_slide_up = gamepad2.dpad_up;
            linear_slide_down = gamepad2.dpad_down;

            linear_slide_up_left = gamepad2.x;
            linear_slide_down_left = gamepad2.a;
            linear_slide_up_right = gamepad2.y;
            linear_slide_down_right = gamepad2.b;
            protect = gamepad2.start;*/
            lt = (double) gamepad2.left_trigger;
            rt = (double) gamepad2.right_trigger;
            up = gamepad2.a;


            //////////////////////////////////////////////////////////////////////////////////////
            // Slide motor Code
            // ////////////////////////////////////////////////////////////////////////////////////
            curposRight = robot.slidemotorright.getCurrentPosition();

            int[] SlideLocation ={1650,2810,4000} ;

            //telemetry.addLine(String.format("\nIn SlideControl motor encoder position left = %d right = %d", curposLeft, curposRight));
            //telemetry.update();
            // We need to move one motor from 0 to MAX and the other from 0 to -MAX

            if (gamepad2.left_trigger > 0){
                telemetry.addData("LT is being pressed **", SlidePos);
                telemetry.addData("LT is being pressed", lt);
                telemetry.addData("loop count: ", loopCount);
                loopCount++;

                if (SlidePos < 3) {
                    telemetry.addData("SlidePos is: ", SlidePos);
                    robot.slidemotorright.setTargetPosition(SlideLocation[SlidePos]);
                    robot.slidemotorright.setPower(0.9);
                    robot.slidemotorright.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                    while (robot.slidemotorright.isBusy()) {
                    }
                    //robot.slidemotorright.setPower(0.0);
                    SlidePos++;
                }

            } else if (gamepad2.right_trigger > 0){
                telemetry.addLine("RT is being pressed");
                telemetry.addData("LT is being pressed", rt);
                SlidePos = 0;
                robot.slidemotorright.setTargetPosition(0);
                robot.slidemotorright.setPower(0.9);
                robot.slidemotorright.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                while (robot.slidemotorright.isBusy()) {
                }
                robot.slidemotorright.setPower(0.0);
            }
            else {
                telemetry.addLine("nothing is being pressed");
                //robot.slidemotorright.setPower(0.0);
            }
            // Use gamepad2 left & right Bumpers to open and close the claw

            if (gamepad2.right_bumper && clawOffsetL>=MIN_POSITION && clawOffsetR<=MAX_POSITION)
            {
                //clawOffsetL -= CLAW_SPEED;
               // clawOffsetR += CLAW_SPEED;
                clawOffsetL = 0.5;//0.5 to close left claw
                clawOffsetR = 0.2;//0.2 to close right claw
            }
            if (gamepad2.left_bumper && clawOffsetL<=MAX_POSITIONL && clawOffsetR>=MIN_POSITIONR)
            {
               // clawOffsetL += CLAW_SPEED;
               // clawOffsetR -= CLAW_SPEED;
                if (SlidePos == 1) {
                    SlideDown(1450, 0.5);
                    while (robot.slidemotorright.isBusy()) {
                    }
                } else if (SlidePos == 2) {
                    SlideDown(2610, 0.5);
                    while (robot.slidemotorright.isBusy()) {
                    }
                } else if (SlidePos == 3) {
                    SlideDown(3800, 0.5);
                    while (robot.slidemotorright.isBusy()) {
                    }
                }
                clawOffsetL = 0.3; //0.3 to open left claw
                clawOffsetR = 0.4; // 0.4 to close  right claw
            }
            clawOffsetL = Range.clip(clawOffsetL, 0.2, 0.5);
            clawOffsetR = Range.clip(clawOffsetR, 0.2, 0.5);
            robot.LeftClaw.setPosition(clawOffsetL);
            robot.RightClaw.setPosition(clawOffsetR);


            // Send telemetry message to signify robot running;

            telemetry.addData("Motor Encoder right = %d", robot.slidemotorright.getCurrentPosition());
            //telemetry.addData("initital position = %d", xyz);
            //telemetry.addData("left_clawoffset",  "Offset = %.2f", clawOffsetL);
            //telemetry.addData("left_position","offset = %.6f", robot.LeftClaw.getPosition());
            //telemetry.addData("right_clawoffset",  "Offset = %.3f", clawOffsetR);
            //telemetry.addData("right_position","offset = %.7f", robot.RightClaw.getPosition());
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

                    robot.frontLeft.setPower(frontLeftPower * 0.65);
                    robot.backLeft.setPower(backLeftPower * 0.65);
                    robot.frontRight.setPower(frontRightPower * 0.65);
                    robot.backRight.setPower(backRightPower * 0.65 );

                    idle();
                }
            } catch (Exception e) {
                telemetry.addData("%s exception", this.getName());
            }

        }
    }
}
