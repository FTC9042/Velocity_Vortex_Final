package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

//Back left wheel is on the second crack away from the corner vortex on driver side
//Flush against wall

@Autonomous(name="Blue Pos2: BEACON(S) Single Push!", group="Blue Position 2")
//@Disabled
public class BluePosTwoBeaconsSinglePush extends LinearOpMode{

    Robot robot   = new Robot();
    private ElapsedTime     runtime = new ElapsedTime();
    private ElapsedTime     elapsed;

    //encoder targets
    private int rightTarget,
            leftTarget;

    //ENCODER CONSTANTS
    private final double CIRCUMFERENCE_INCHES = 4 * Math.PI,
            TICKS_PER_ROTATION = 1200 / 0.8522,
            TICKS_PER_INCH = TICKS_PER_ROTATION / CIRCUMFERENCE_INCHES,
            TOLERANCE = 40,
            ROBOT_WIDTH = 14.5;

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);
        robot.color.enableLed(false);

        robot.setDirection();
        robot.resetEncoders();
        if (robot.gyro.getHeading() != 0) {
            robot.gyro.calibrate();
            while (robot.gyro.isCalibrating() && !opModeIsActive()) {
                telemetry.addData("Status", "Gyro is Resetting. Currently at " + robot.gyro.getHeading());
                telemetry.update();

                idle();
            }
            telemetry.addData("Status", "Gyro is done Calibrating. Heading: "+robot.gyro.getHeading());
            telemetry.update();
        }
        else{
            telemetry.addData("Status", "Gyro is already Calibrated. Heading: "+robot.gyro.getHeading());
            telemetry.update();
        }

        waitForStart();
        elapsed = new ElapsedTime();

        runStraight(19, 10, .7);
        turnRight(45, 10);
        runStraight(60, 10, .7);
        turnRight(45, 3);
        turnTowards(90, 5);
        runStraight(10 , 4, .6);
        if (!isColorRed()){
            turnTowards(94, 3);
        }
        else{
            turnTowards(86, 3);
        }
        runStraight(4, 1, .3);
        runStraight(-3, 1, .3);
        turnTowards(90, 2);
        if (isColorRed()) {
            sleep(4900);
            runStraight(3, 1, .6);
        }
        runStraight(-10, 3, .6);

        if (elapsed.seconds() < 17) {
            turnLeft(85, 5);
            turnTowards(0, 2);
            runStraight(47, 5, .6);
            turnRight(95, 5);
            turnTowards(90, 5);
            if (elapsed.seconds() < 25) {
                runStraight(11, 2, .4);
                if (!isColorRed()){
                    turnTowards(94, 3);
                }
                else{
                    turnTowards(86, 3);
                }
                runStraight(3, 1, .3);
                runStraight(-2, 1, 1);
                if (isColorRed()) {
                    sleep(4900);
                    runStraight(3, 1, .3);
                }
                turnLeftSUPERFAST(30, 3);
                robot.setToCoast();
                runStraight(-80, 6, 1);
            }
        }

        else {
            turnLeft(45, 5);
            runStraight(-24, 4, .6);
            turnRight(90, 3);
            turnTowards(135, 5);
            runStraight(29, 4, .6);
            robot.roller.setPower(1);
            runStraight(4, 5, .6);
            rollout(10);
        }
    }
    //ENCODER BASED MOVEMENT
    public void runStraight(double distance_in_inches, int timeoutS, double speed) throws InterruptedException{
        if (opModeIsActive()){
            leftTarget = (int) (distance_in_inches * TICKS_PER_INCH);
            rightTarget = leftTarget;
            robot.setToEncoderMode();
            setTargetValueMotor();
            runtime.reset();
            robot.setMotorPower(speed,speed);
            while (opModeIsActive() && (runtime.seconds() < timeoutS) && !hasReached()) {
                robot.checkPower(speed, speed);
                basicTel();
                idle();
            }
            robot.setMotorPower(0,0);
            robot.resetEncoders();
            sleep(250);
        }
    }

    public boolean isColorRed(){
        if (robot.color.red()>robot.color.blue() && robot.color.red()>=1){
            telemetry.addData("Colors","Red is %d and Blue is %d", robot.color.red(), robot.color.blue());
            telemetry.addData("Course","The color detected was red");
            telemetry.update();
            return true;
        }
        telemetry.addData("Colors","Red is %d and Blue is %d", robot.color.red(), robot.color.blue());
        telemetry.addData("Course","The color detected was blue");
        telemetry.update();
        return false;
    }

    //Turning With Gyro's
    public void turnRight(int angle, int timeoutS) throws InterruptedException{
        if (opModeIsActive()){
            robot.setToWOEncoderMode();
            runtime.reset();
            robot.setMotorPower(.13,-.13);
            int targetAngle = robot.gyro.getHeading()+angle;
            if (targetAngle>=360){
                targetAngle-=360;
            }
            while (opModeIsActive() && (runtime.seconds() < timeoutS) && Math.abs(robot.gyro.getHeading()-targetAngle)>=4) {
                robot.checkPower(.13, -.13);
                basicTel();
                idle();
            }
            robot.setMotorPower(0,0);
            robot.resetEncoders();
        }
    }

    //Turning With Gyro's
    public void turnLeftSUPERFAST(int angle, int timeoutS) throws InterruptedException{
        if (opModeIsActive()){
            robot.setToWOEncoderMode();
            runtime.reset();
            robot.setMotorPower(-.4,.4);
            int targetAngle = robot.gyro.getHeading()-angle;
            if (targetAngle<0){
                targetAngle += 360;
            }
            while (opModeIsActive() && (runtime.seconds() < timeoutS) && Math.abs(robot.gyro.getHeading()-targetAngle)>=4) {
                robot.checkPower(-.4, .4);
                basicTel();
                idle();
            }
            robot.setMotorPower(0,0);
            robot.resetEncoders();
        }
    }

    //Turning With Gyro's
    public void turnLeft(int angle, int timeoutS) throws InterruptedException{
        if (opModeIsActive()){
            robot.setToWOEncoderMode();
            runtime.reset();
            robot.setMotorPower(-.13,.13);
            int targetAngle = robot.gyro.getHeading()-angle;
            if (targetAngle<0){
                targetAngle += 360;
            }
            while (opModeIsActive() && (runtime.seconds() < timeoutS) && Math.abs(robot.gyro.getHeading()-targetAngle)>=4) {
                robot.checkPower(-.13, .13);
                basicTel();
                idle();
            }
            robot.setMotorPower(0,0);
            robot.resetEncoders();
        }
    }

    public void rollout(int timeoutS){
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < timeoutS){
            robot.roller.setPower(1);
            idle();
        }
    }

    public void turnTowards(int angle, int timeoutS) throws InterruptedException{
        if (opModeIsActive()){
            runtime.reset();
            robot.setToWOEncoderMode();
            if (robot.gyro.getHeading()>angle){
                robot.setMotorPower(-.1,.1);
                while (opModeIsActive() && (runtime.seconds() < timeoutS) && Math.abs(robot.gyro.getHeading()-angle)>=3) {
                    robot.checkPower(-.1, .1);
                    basicTel();
                    idle();
                }
            }
            else{
                robot.setMotorPower(.1, -.1);
                while (opModeIsActive() && (runtime.seconds() < timeoutS) && Math.abs(robot.gyro.getHeading()-angle)>=3) {
                    robot.checkPower(.1, -.1);
                    basicTel();
                    idle();
                }
            }
            robot.setMotorPower(0,0);
            robot.resetEncoders();
        }
    }

    public void setTargetValueMotor() {
        robot.frontLeft.setTargetPosition(leftTarget);
        robot.backLeft.setTargetPosition(leftTarget);

        robot.frontRight.setTargetPosition(rightTarget);
        robot.backRight.setTargetPosition(rightTarget);
    }

    public boolean hasReached() {
        return (Math.abs(robot.frontLeft.getCurrentPosition() - leftTarget) <= TOLERANCE &&
                Math.abs(robot.backLeft.getCurrentPosition() - leftTarget) <= TOLERANCE &&
                Math.abs(robot.frontRight.getCurrentPosition() - rightTarget) <= TOLERANCE &&
                Math.abs(robot.backRight.getCurrentPosition() - rightTarget) <= TOLERANCE);
    }

    public void basicTel(){
        telemetry.addData("Back Right Motor", "Target %7d: Current Pos %7d", robot.backRight.getTargetPosition(), robot.backRight.getCurrentPosition());
        telemetry.addData("Front Right Motor", "Target %7d: Current Pos %7d", robot.frontRight.getTargetPosition(), robot.frontRight.getCurrentPosition());
        telemetry.addData("Back Left Motor", "Target %7d: Current Pos %7d", robot.backLeft.getTargetPosition(), robot.backLeft.getCurrentPosition());
        telemetry.addData("Front Left Motor", "Target %7d: Current Pos %7d", robot.frontLeft.getTargetPosition(), robot.frontLeft.getCurrentPosition());
        telemetry.addData("Gyro", "Robot is facing %d",robot.gyro.getHeading());
        telemetry.addData("Colors","Red is %d and Blue is %d", robot.color.red(), robot.color.blue());
        telemetry.addData("Time", "Total Elapsed time is %.2f", elapsed.seconds());
        telemetry.update();
    }
}