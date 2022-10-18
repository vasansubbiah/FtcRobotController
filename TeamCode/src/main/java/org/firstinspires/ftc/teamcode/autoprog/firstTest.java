package org.firstinspires.ftc.teamcode.autoprog;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.DMHardwareTest;

@Autonomous(name = "firsttest", group = "prototypes")
@Disabled
public class firstTest  extends LinearOpMode {
    public DMHardwareTest robot = new DMHardwareTest(false);

    @Override
    public void runOpMode() {
        boolean bk = true;
        robot.initTeleOpIMU(hardwareMap, bk);
        goForward();
        robot.timer.reset();
        while (robot.getTime() <= 1) {
        }
        goBackward();
    }
    public void goForward() {
        robot.timer.reset();
        robot.setPowerOfAllMotorsTo(0.3);
        while (robot.getTime() <= 5) {
        }
        robot.setPowerOfAllMotorsTo(0);
    }

    public void goBackward() {
        robot.timer.reset();
        robot.setPowerOfAllMotorsTo(-0.3);
        while (robot.getTime() <= 5) {
        }
        robot.setPowerOfAllMotorsTo(0);
    }
}
