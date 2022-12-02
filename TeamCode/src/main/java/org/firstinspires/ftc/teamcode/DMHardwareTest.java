package org.firstinspires.ftc.teamcode;

// Importing all of the packages
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
public class DMHardwareTest {

    // Instantiating the hardware classes
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;
    public DcMotorEx slidemotorleft;
    public DcMotorEx slidemotorright;
    public Servo   LeftClaw;
    public Servo   RightClaw;
    double servo_power = 0.2;

    public boolean runThisWithEncoders;


    public HardwareMap hwMap;
    public ElapsedTime timer = new ElapsedTime();

    // Methods...
    public DMHardwareTest(boolean runThisWithEncoders) {
        this.runThisWithEncoders = runThisWithEncoders;
    }

    public void initTeleOpIMU(HardwareMap hwMap, boolean bk) {
        this.hwMap = hwMap;
        timer.reset();
        frontLeft = hwMap.dcMotor.get("frntLF");
        frontRight = hwMap.dcMotor.get("frntRT");
        backLeft = hwMap.dcMotor.get("bckLF");
        backRight = hwMap.dcMotor.get("bckRT");
        //slidemotorleft = hwMap.get(DcMotorEx.class, "slidemotorleft");
        slidemotorright = hwMap.get(DcMotorEx.class,"slidemotorright");
        LeftClaw  = hwMap.get(Servo.class,"left_hand");
     //   LeftClaw.setPosition(servo_power);
        RightClaw  = hwMap.get(Servo.class, "right_hand");
       // RightClaw.setPosition(servo_power);

        if (bk) {
            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            slidemotorright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //slidemotorleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    // Method to set the power of all of the motors...

    public void setPowerOfAllMotorsTo(double speed) {
        //We only have two motors...
        frontLeft.setPower(speed);
        frontRight.setPower(speed);
        backLeft.setPower(speed);
        backRight.setPower(speed);
    }

    // Method to get current time...
    public double getTime() {
        return timer.time();
    }

}
