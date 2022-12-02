package org.firstinspires.ftc.teamcode.teleop2022;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
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

    ElapsedTime timer = new ElapsedTime();
    private double lastError = 0;

    public double PIDcontrol(double reference, double state) {
        double error = reference - state;
        integralSum += error + timer.seconds();
        double derivative = (error - lastError) / timer.seconds();
        lastError = error;

        timer.reset();

        double output = (error + Kp) + (derivative + Kp) + (integralSum +Ki);
        return output;
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
        boolean down;

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
        double clawOffsetL = 0.2;
        double clawOffsetR = 0.7;
        double servo_power = 0.5;
        double CLAW_SPEED = 0.09;                 // sets rate to move servo
        double  MIN_POSITION = 0.2;
        double MIN_POSITIONR=0.2;
        double MAX_POSITION = 0.7;
        double MAX_POSITIONL = 0.7;

        Thread  driveThread = new DriveThread();

        robot.initTeleOpIMU(hardwareMap, applyBreak);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        driveThread.start();

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
            down = gamepad2.b;


            //////////////////////////////////////////////////////////////////////////////////////
            // Slide motor Code
            // ////////////////////////////////////////////////////////////////////////////////////
            curposRight = robot.slidemotorright.getCurrentPosition();
            int SlidePos = 0;
            int[] SlideLocation = {1600,2700,3800};

            //telemetry.addLine(String.format("\nIn SlideControl motor encoder position left = %d right = %d", curposLeft, curposRight));
            //telemetry.update();
            // We need to move one motor from 0 to MAX and the other from 0 to -MAX
            while (robot.slidemotorright.isBusy()){
                curposRight = robot.slidemotorright.getCurrentPosition();
                telemetry.addLine(String.format("\n slide motor encoder Left position = %d , Right position = %d",curposRight));
            }
            if (lt > 0){
                telemetry.addLine("LT is being pressed");
                telemetry.addData("LT is being pressed", lt);

                if (SlidePos < 3) {
                    robot.slidemotorright.setTargetPosition(SlideLocation[SlidePos]);
                    robot.slidemotorright.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                    SlidePos++;
                    while (robot.slidemotorright.isBusy()) {
                    }
                }

            }
            else if (rt > 0){
                telemetry.addLine("RT is being pressed");
                telemetry.addData("LT is being pressed", rt);
                //robot.slidemotorleft.setPower(-0.5 * rt);
                //robot.slidemotorright.setPower(-0.5 * rt);
                while (robot.slidemotorright.getCurrentPosition() >= 50) {
                    robot.slidemotorright.setPower(PIDcontrol(-curposRight,robot.slidemotorright.getCurrentPosition()));
                }
            }
            else {
                telemetry.addLine("nothing is being pressed");
                robot.slidemotorright.setPower(0.0);
            }
            // Use gamepad2 left & right Bumpers to open and close the claw

            if (gamepad2.right_bumper && clawOffsetL>=MIN_POSITION && clawOffsetR<=MAX_POSITION)
            {
                //clawOffsetL -= CLAW_SPEED;
               // clawOffsetR += CLAW_SPEED;
                clawOffsetL = 0.7;//0.2 to open and 0.7 to close left claw
                clawOffsetR = 0.2;//0.2 to close and 0.7 to open
            }
            if (gamepad2.left_bumper && clawOffsetL<=MAX_POSITIONL && clawOffsetR>=MIN_POSITIONR)
            {
               // clawOffsetL += CLAW_SPEED;
               // clawOffsetR -= CLAW_SPEED;
                clawOffsetL = 0.2; //0.2 to open and 0.7 to close left claw
                clawOffsetR = 0.7; // 0.2 to close and 0.7 to open right claw
            }
            clawOffsetL = Range.clip(clawOffsetL, 0.2, 0.7);
            clawOffsetR = Range.clip(clawOffsetR, 0.2, 0.7);
            robot.LeftClaw.setPosition(clawOffsetL);
            robot.RightClaw.setPosition(clawOffsetR);


            // Send telemetry message to signify robot running;

            telemetry.addData("Motor Encoder right = %d", curposRight);
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
