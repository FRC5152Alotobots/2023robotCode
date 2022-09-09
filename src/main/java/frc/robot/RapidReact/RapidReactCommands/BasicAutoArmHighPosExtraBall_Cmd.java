// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.RapidReact.RapidReactCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Components.DriveSubSys.DriveSubSys_Old;
import frc.robot.Components.DriveSubSys.SwerveDrives.AM_SwerveSubSys.drive_cmds.DriveSubSys_Drive4Time_Cmd;
import frc.robot.Components.DriveSubSys.SwerveDrives.AM_SwerveSubSys.drive_cmds.DriveSubSys_DriveDistanceTrajectory_Cmd;
import frc.robot.Components.DriveSubSys.SwerveDrives.AM_SwerveSubSys.drive_cmds.DriveSubSys_ResetOdometry_Cmd;
import frc.robot.Components.DriveSubSys.SwerveDrives.AM_SwerveSubSys.drive_cmds.DriveSubSys_RotateDegreesProfiled_Cmd;
import frc.robot.Constants.RobotSettings;
import frc.robot.RapidReact.Intake.IntakeArm.IntakeArmSubSys;
import frc.robot.RapidReact.Intake.IntakeArm.IntakeArmCmds.IntakeArm_Pos_Cmd;
import frc.robot.RapidReact.Intake.IntakeArm.IntakeArmCmds.IntakeArm_Spd_Cmd;
import frc.robot.RapidReact.Intake.IntakeInNOut.IntakeInNOutSubSys;
import frc.robot.RapidReact.Intake.IntakeInNOut.IntakeInNOutCmds.IntakeInNOut_Spd_Cmd;
import frc.robot.RapidReact.Intake.IntakeTriggers.IntakeTriggersSubSys;
import frc.robot.RapidReact.Intake.IntakeTriggers.IntakeTriggersCmds.IntakeTriggers_Trigger_Cmd;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class BasicAutoArmHighPosExtraBall_Cmd extends SequentialCommandGroup {
  /** Creates a new BasicAuto_Cmd. */
  public BasicAutoArmHighPosExtraBall_Cmd(
    DriveSubSys_Old driveSubSys,
    IntakeArmSubSys intakeArmSubSys,
    IntakeInNOutSubSys intakeInNOutSubSys,
    IntakeTriggersSubSys intakeTriggersSubSys) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    addCommands(
      //new DriveSubSys_ResetOdometry_Cmd(driveSubSys),

      //inital backwards movement
      new ParallelCommandGroup(
      
        new DriveSubSys_Drive4Time_Cmd(driveSubSys, -0.4, 0, 0, 2.0, false),
        //new DriveSubSys_Drive4Time_Cmd(driveSubSys, 0, 0, 0, 1.0, false),

        new IntakeArm_Pos_Cmd(intakeArmSubSys, RobotSettings.Shooting.kArmHighGoalAngle-5)
          .withTimeout(1.0)),
      
      new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys,
          //RobotSettings.kShootHighGoalVel,
          -0.65,
          //RobotSettings.kShootLwrHighGoalVel)
          -1.0)
          .withTimeout(2.0),

      new ParallelCommandGroup(

        new IntakeArm_Pos_Cmd(intakeArmSubSys, RobotSettings.Shooting.kArmHighGoalAngle-5)
        .withTimeout(1.0),  
      
        new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys,
          //RobotSettings.kShootHighGoalVel,
          -0.65,
          //RobotSettings.kShootLwrHighGoalVel)
          -1.0)
        .withTimeout(2.0),
        
        new IntakeTriggers_Trigger_Cmd(intakeTriggersSubSys, true, true)
        .withTimeout(1)
      ),

      // Rotate 180 degrees
      new DriveSubSys_RotateDegreesProfiled_Cmd(-180, driveSubSys),
      
      // Reset Odometry for Drive Commands
      new DriveSubSys_ResetOdometry_Cmd(driveSubSys,
        new Pose2d(
        new Translation2d(0,0),
        new Rotation2d(0)
      )),

      // Drive to 2nd Ball and Intake
      new ParallelCommandGroup(
        new IntakeArm_Pos_Cmd(intakeArmSubSys, RobotSettings.Intake.kArmIntakeAngle)
          .withTimeout(4.0),

        new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys,
          RobotSettings.Intake.kIntakeVel,
          RobotSettings.Intake.kIntakeLwrVel)
            .withTimeout(4.0),  

        new DriveSubSys_DriveDistanceTrajectory_Cmd(driveSubSys, Units.inchesToMeters(72))
          .withTimeout(4.0)
      ),

      // Lift and Intake
      new ParallelCommandGroup(
        
        new IntakeArm_Pos_Cmd(intakeArmSubSys, RobotSettings.Shooting.kArmHighGoalAngle)
          .withTimeout(2),
          
        new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys,
          RobotSettings.Intake.kIntakeVel,
          RobotSettings.Intake.kIntakeLwrVel)
            .withTimeout(2)  
      ),

      // Rotate 180 degrees
      new DriveSubSys_RotateDegreesProfiled_Cmd(180, driveSubSys),

      // Reset Odometry for Drive Commands
      new DriveSubSys_ResetOdometry_Cmd(driveSubSys,
        new Pose2d(
        new Translation2d(0,0),
        new Rotation2d(0)
      )),

      // Drive Forward and Prep for shot
      new ParallelCommandGroup(
        new DriveSubSys_DriveDistanceTrajectory_Cmd(driveSubSys, Units.inchesToMeters(72))
          .withTimeout(4.0),
        
        new IntakeArm_Pos_Cmd(intakeArmSubSys, RobotSettings.Shooting.kArmHighGoalAngle)
          .withTimeout(4.0)
      ),

      // Spin up Shooter
      new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys,
        RobotSettings.Shooting.kShootHighGoalVel,
        RobotSettings.Shooting.kShootLwrHighGoalVel)
          .withTimeout(1.0),

      new ParallelCommandGroup(
        new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys,
          RobotSettings.Shooting.kShootHighGoalVel,
          RobotSettings.Shooting.kShootLwrHighGoalVel)
            .withTimeout(2.0),
        
        new IntakeTriggers_Trigger_Cmd(intakeTriggersSubSys, true, true)
        .withTimeout(1)
      )
      
      /*
      //new DriveSubSys_ResetOdometry_Cmd(driveSubSys),

      //inital backwards movement
      new ParallelCommandGroup(
      
        new DriveSubSys_Drive4Time_Cmd(driveSubSys, -0.4, 0, 0, 2.0, false),
        //new DriveSubSys_Drive4Time_Cmd(driveSubSys, 0, 0, 0, 1.0, false),

        new IntakeArm_Pos_Cmd(intakeArmSubSys, RobotSettings.kArmHighGoalAngle-5)
          .withTimeout(1.0)),
      
      new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys,
          //RobotSettings.kShootHighGoalVel,
          -0.65,
          //RobotSettings.kShootLwrHighGoalVel)
          -1.0)
          .withTimeout(2.0),

      new ParallelCommandGroup(

        new IntakeArm_Pos_Cmd(intakeArmSubSys, RobotSettings.kArmHighGoalAngle-5)
        .withTimeout(1.0),  
      
        new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys,
          //RobotSettings.kShootHighGoalVel,
          -0.65,
          //RobotSettings.kShootLwrHighGoalVel)
          -1.0)
        .withTimeout(2.0),
        
        new IntakeTriggers_Trigger_Cmd(intakeTriggersSubSys, true, true)
        .withTimeout(1)
      ),

      new DriveSubSys_Drive4Time_Cmd(driveSubSys, -0.6, 0, 0, 3.0, false)
      //new DriveSubSys_Drive4Time_Cmd(driveSubSys, 0.0, 0, 0, 3.0, false)
    
      //new DriveSubSys_ResetOdometry_Cmd(driveSubSys),

      //inital backwards movement
      new ParallelCommandGroup(
      
        new DriveSubSys_DriveDistanceTrajectory_Cmd(driveSubSys, Units.inchesToMeters(48))
          .withTimeout(3),
        new IntakeArm_Pos_Cmd(intakeArmSubSys, RobotSettings.kArmHighGoalAngle-5)
          .withTimeout(3)
      ),
      
      new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys,
          RobotSettings.kShootHighGoalVel,
          RobotSettings.kShootLwrHighGoalVel)
            .withTimeout(1.0),

      new ParallelCommandGroup(
        new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys,
          RobotSettings.kShootHighGoalVel,
          RobotSettings.kShootLwrHighGoalVel)
            .withTimeout(2.0),
        
        new IntakeTriggers_Trigger_Cmd(intakeTriggersSubSys, true, true)
        .withTimeout(1)
      ),

      // Rotate 180 degrees
      new DriveSubSys_RotateDegreesProfiled_Cmd(-180, driveSubSys),
      
      // Drive to 2nd Ball and Intake
      new ParallelCommandGroup(
        new IntakeArm_Pos_Cmd(intakeArmSubSys, RobotSettings.kArmIntakeAngle)
          .withTimeout(4.0),

        new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys,
          RobotSettings.kIntakeVel,
          RobotSettings.kIntakeLwrVel)
            .withTimeout(4.0),  

        new DriveSubSys_DriveDistanceTrajectory_Cmd(driveSubSys, Units.inchesToMeters(72))
          .withTimeout(4.0)
      ),

      // Lift and Intake
      new ParallelCommandGroup(
        
        new IntakeArm_Pos_Cmd(intakeArmSubSys, RobotSettings.kArmHighGoalAngle)
          .withTimeout(2),
          
        new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys,
          RobotSettings.kIntakeVel,
          RobotSettings.kIntakeLwrVel)
            .withTimeout(2)  
      ),

      // Rotate 180 degrees
      new DriveSubSys_RotateDegreesProfiled_Cmd(180, driveSubSys),

      // Drive Forward and Prep for shot
      new ParallelCommandGroup(
        new DriveSubSys_DriveDistanceTrajectory_Cmd(driveSubSys, Units.inchesToMeters(72))
          .withTimeout(4.0),
        
        new IntakeArm_Pos_Cmd(intakeArmSubSys, RobotSettings.kArmHighGoalAngle)
          .withTimeout(4.0)
      ),

      // Spin up Shooter
      new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys,
        RobotSettings.kShootHighGoalVel,
        RobotSettings.kShootLwrHighGoalVel)
          .withTimeout(1.0),

      new ParallelCommandGroup(
        new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys,
          RobotSettings.kShootHighGoalVel,
          RobotSettings.kShootLwrHighGoalVel)
            .withTimeout(2.0),
        
        new IntakeTriggers_Trigger_Cmd(intakeTriggersSubSys, true, true)
        .withTimeout(1)
      )
    */
    );
  }
}
