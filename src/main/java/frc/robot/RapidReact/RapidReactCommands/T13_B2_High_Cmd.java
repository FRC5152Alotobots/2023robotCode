// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.RapidReact.RapidReactCommands;

// import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
// import edu.wpi.first.wpilibj.smartdashboard.Field2d;
// import edu.wpi.first.wpilibj2.command.InstantCommand;
// import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Components.DriveSubSys.DriveSubSys_Old;
// import frc.robot.Components.DriveSubSys.SwerveDrives.AM_SwerveSubSys.drive_cmds.DriveSubSys_Drive4Time_Cmd;
// import frc.robot.Components.DriveSubSys.SwerveDrives.AM_SwerveSubSys.drive_cmds.DriveSubSys_DriveDistanceTrajectory_Cmd;
// import frc.robot.Components.DriveSubSys.SwerveDrives.AM_SwerveSubSys.drive_cmds.DriveSubSys_DriveVariableTrajectory_Cmd;
import frc.robot.Components.DriveSubSys.SwerveDrives.AM_SwerveSubSys.drive_cmds.DriveSubSys_ResetOdometry_Cmd;
// import frc.robot.Components.DriveSubSys.SwerveDrives.AM_SwerveSubSys.drive_cmds.DriveSubSys_Rotate2Heading;
import frc.robot.Constants.Field;
// import frc.robot.Constants.RobotSettings;
import frc.robot.Constants.Field.StartingPos;
import frc.robot.RapidReact.Intake.IntakeArm.IntakeArmSubSys;
// import frc.robot.RapidReact.Intake.IntakeArm.IntakeArmCmds.IntakeArm_Pos_Cmd;
// import frc.robot.RapidReact.Intake.IntakeArm.IntakeArmCmds.IntakeArm_Spd_Cmd;
import frc.robot.RapidReact.Intake.IntakeInNOut.IntakeInNOutSubSys;
// import frc.robot.RapidReact.Intake.IntakeInNOut.IntakeInNOutCmds.IntakeInNOut_Spd_Cmd;
import frc.robot.RapidReact.Intake.IntakeTriggers.IntakeTriggersSubSys;
// import frc.robot.RapidReact.Intake.IntakeTriggers.IntakeTriggersCmds.IntakeTriggers_Trigger_Cmd;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class T13_B2_High_Cmd extends SequentialCommandGroup {
  /** Creates a new BasicAuto_Cmd. */
  public T13_B2_High_Cmd(
    DriveSubSys_Old driveSubSys,
    IntakeArmSubSys intakeArmSubSys,
    IntakeInNOutSubSys intakeInNOutSubSys,
    IntakeTriggersSubSys intakeTriggersSubSys) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    // T13 Corner S
    Translation2d startOffset = new Translation2d(20,0);
    Pose2d initialPose = new Pose2d(
      Field.Tarmac.kT13.plus(startOffset),
      new Rotation2d(Units.degreesToRadians(30)));
    
    // T13 Shooting Pose
    Pose2d shot1Pose = new Pose2d(
      Field.Tarmac.kT13,
      new Rotation2d(Units.degreesToRadians(33)));

    // B2 Intake Pose
    Pose2d b2IntakePose = new Pose2d(
      Field.Balls.kB2,
      new Rotation2d(Units.degreesToRadians(-135)));

    addCommands(
      // Initialize
      new DriveSubSys_ResetOdometry_Cmd(
        driveSubSys,
        StartingPos.kT13_Start)
      /*
      // Move to Shot1Pose
      new DriveSubSys_DriveVariableTrajectory_Cmd(
        driveSubSys,
        StartingPos.kT13_Start,
        List.of(
          initialPose.getTranslation(),
          shot1Pose.getTranslation()
        ),
        shot1Pose,
        false)
          .withTimeout(4),

      // Prep for shot
      new ParallelCommandGroup(
        new IntakeArm_Pos_Cmd(intakeArmSubSys, RobotSettings.kArmHighGoalAngle-5)
          .withTimeout(1.5),

        new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys, RobotSettings.kShootHighGoalVel, RobotSettings.kShootLwrHighGoalVel)
          .withTimeout(1.5)),
           
      // Shoot
      new ParallelCommandGroup(
        new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys, RobotSettings.kShootHighGoalVel, RobotSettings.kShootLwrHighGoalVel)
          .withTimeout(1.5),

        new IntakeTriggers_Trigger_Cmd(intakeTriggersSubSys, true, true)
          .withTimeout(2)
      ),
     
      // Rotate and Prep for Intake
      new ParallelCommandGroup(
        new DriveSubSys_Rotate2Heading(-135, driveSubSys)
          .withTimeout(3),
        
        new IntakeArm_Pos_Cmd(intakeArmSubSys, RobotSettings.kArmIntakeAngle)
          .withTimeout(3)),

      // Drive to B2 and Intake
      new ParallelCommandGroup(

        new DriveSubSys_DriveVariableTrajectory_Cmd(
          driveSubSys,
          shot1Pose,
          List.of(
            b2IntakePose.getTranslation()
          ),
          b2IntakePose,
          true)
            .withTimeout(4),

        new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys, RobotSettings.kIntakeVel, RobotSettings.kIntakeLwrVel)
          .withTimeout(4)
      ),

      // Lift Intake Arm
      new IntakeArm_Pos_Cmd(intakeArmSubSys, RobotSettings.kArmHighGoalAngle)
      .withTimeout(1.5),

      // Move Back to shot1Pose
      new DriveSubSys_DriveVariableTrajectory_Cmd(
        driveSubSys,
        b2IntakePose,
        List.of(
          shot1Pose.getTranslation()
        ),
        shot1Pose,
        true)
          .withTimeout(4),

      // Prep to Shoot
      new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys, RobotSettings.kShootHighGoalVel, RobotSettings.kShootLwrHighGoalVel)
      .withTimeout(1),

      // Shoot
      new ParallelCommandGroup(
        
        new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys, RobotSettings.kShootHighGoalVel, RobotSettings.kShootLwrHighGoalVel)
        .withTimeout(1),

        new IntakeTriggers_Trigger_Cmd(intakeTriggersSubSys, true, true)
        .withTimeout(2)
      )
      */
    );     
  }
}
