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
        double rt;
        double lt;

        robot.initTeleOpIMU(hardwareMap, applyBreak);
        //robot.slidemotorleft.resetDeviceConfigurationForOpMode();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();


        // run until the end of the match (driver presses STOP)
        //robot.slidemotorright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        while (opModeIsActive()) {
            rt =  gamepad2.right_stick_y;
            int curpos = robot.slidemotorright.getCurrentPosition();
            if (rt != 0) {
                telemetry.addLine("RT Triggered" );
                telemetry.addData("RT value= %0.2f", rt );
                robot.slidemotorright.setPower(rt/2.0);
                curpos = robot.slidemotorright.getCurrentPosition();
                telemetry.addData("Current encoder position: ", curpos);
            } else {
                telemetry.addLine("None Triggered" );
                telemetry.addData("RT value= %0.2f", rt );
                telemetry.addData("Current encoder position: ", curpos);
                //robot.slidemotorleft.setPower(0.0);
                robot.slidemotorright.setPower(0.0);
            }
            telemetry.update();
            idle();
        }
    }
}
