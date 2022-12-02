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
    boolean applyBreak =  true;

    @Override
    public void runOpMode() {
        double rt;
        double lt;

        Thread  driveThread = new DriveThread();

        robot.initTeleOpIMU(hardwareMap, applyBreak);
        //robot.slidemotorleft.resetDeviceConfigurationForOpMode();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        driveThread.start();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            rt = (double) gamepad2.right_stick_y;
            lt = (double) gamepad2.left_stick_y;
            if (rt != 0.0) {
                telemetry.addLine("RT Triggered" );
                telemetry.addData("RT value= %0.2f", rt );
                telemetry.addData("LT value= %0.2f", lt );
                //robot.slidemotorleft.setPower(rt);
                robot.slidemotorright.setPower(-rt);
            } else {
                telemetry.addLine("None Triggered" );
                telemetry.addData("RT value= %0.2f", rt );
                telemetry.addData("LT value= %0.2f", lt );
                //robot.slidemotorleft.setPower(0.0);
                robot.slidemotorright.setPower(0.0);
            }
            telemetry.update();
            idle();
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
