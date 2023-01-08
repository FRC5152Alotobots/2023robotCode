// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.RapidReact.RapidReactCommands.AutonomousCmds;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Components.DriveSubSys.DriveSubSys_Old;
// import frc.robot.Components.DriveSubSys.SwerveDrives.AM_SwerveSubSys.drive_cmds.DriveSubSys_Drive4Time_Cmd;
// import frc.robot.Components.DriveSubSys.SwerveDrives.AM_SwerveSubSys.drive_cmds.DriveSubSys_ResetOdometry_Cmd;
// import frc.robot.Components.DriveSubSys.SwerveDrives.AM_SwerveSubSys.drive_cmds.pathplanner_cmds.DriveSubSys_PP_B2toShoot_Cmd;
import frc.robot.Components.DriveSubSys.SwerveDrives.AM_SwerveSubSys.drive_cmds.pathplanner_cmds.DriveSubSys_PP_T13CnrtoT13_Cmd;
import frc.robot.Components.DriveSubSys.SwerveDrives.AM_SwerveSubSys.drive_cmds.pathplanner_cmds.DriveSubSys_PP_T13toB2_Cmd;
import frc.robot.Components.Vision.LimeLightSubSys;
import frc.robot.Constants.RobotSettings;
import frc.robot.RapidReact.Intake.IntakeArm.IntakeArmSubSys;
import frc.robot.RapidReact.Intake.IntakeArm.IntakeArmCmds.IntakeArm_PosHold_Cmd;
// import frc.robot.RapidReact.Intake.IntakeArm.IntakeArmCmds.IntakeArm_Pos_Cmd;
// import frc.robot.RapidReact.Intake.IntakeArm.IntakeArmCmds.IntakeArm_Spd_Cmd;
import frc.robot.RapidReact.Intake.IntakeInNOut.IntakeInNOutSubSys;
import frc.robot.RapidReact.Intake.IntakeInNOut.IntakeInNOutCmds.IntakeInNOut_Spd_Cmd;
import frc.robot.RapidReact.Intake.IntakeTriggers.IntakeTriggersSubSys;
import frc.robot.RapidReact.Intake.IntakeTriggers.IntakeTriggersCmds.IntakeTriggers_Trigger_Cmd;
import frc.robot.RapidReact.RapidReactCommands.AutoShoot_Cmd;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class Auto_AS_T13CnrtoT13HighGoaltoB2toShoot_Cmd extends SequentialCommandGroup {
  /** Creates a new BasicAuto_Cmd. */
  public Auto_AS_T13CnrtoT13HighGoaltoB2toShoot_Cmd(
    DriveSubSys_Old driveSubSys,
    IntakeArmSubSys intakeArmSubSys,
    IntakeInNOutSubSys intakeInNOutSubSys,
    IntakeTriggersSubSys intakeTriggersSubSys,
    LimeLightSubSys limeLightSubSys) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    addCommands(
      //new DriveSubSys_ResetOdometry_Cmd(driveSubSys),

      //inital backwards movement
      new ParallelDeadlineGroup(
        new DriveSubSys_PP_T13CnrtoT13_Cmd(driveSubSys),
        
        new IntakeArm_PosHold_Cmd(
          intakeArmSubSys,
          RobotSettings.Shooting.kArmHighGoalAngle,
          ()-> 0)
          .withTimeout(2.0)),
       
      new AutoShoot_Cmd(
        driveSubSys,
        intakeArmSubSys,
        intakeInNOutSubSys,
        limeLightSubSys,
        ()-> 0,
        ()-> 0)
        .withTimeout(2.0),

      new ParallelCommandGroup(

        new AutoShoot_Cmd(
          driveSubSys,
          intakeArmSubSys,
          intakeInNOutSubSys,
          limeLightSubSys,
          ()-> 0,
          ()-> 0)
            .withTimeout(1.0),
        
        new IntakeTriggers_Trigger_Cmd(
          intakeTriggersSubSys,
          true,
          true)
            .withTimeout(1)),

        new ParallelDeadlineGroup(
          new DriveSubSys_PP_T13toB2_Cmd(driveSubSys),
        
          new IntakeArm_PosHold_Cmd(
            intakeArmSubSys,
            RobotSettings.Intake.kArmIntakeAngle,
            ()-> 0),

          new IntakeInNOut_Spd_Cmd(
            intakeInNOutSubSys,
            RobotSettings.Intake.kIntakeVel,
            RobotSettings.Intake.kIntakeLwrVel))
      /*
        new ParallelDeadlineGroup(
          new DriveSubSys_PP_B2toShoot_Cmd(driveSubSys),
          
          new IntakeArm_PosHold_Cmd(
            intakeArmSubSys,
            RobotSettings.Shooting.kArmHighGoalAngle,
            ()-> 0)),

        new AutoShoot_Cmd(
          driveSubSys,
          intakeArmSubSys,
          intakeInNOutSubSys,
          limeLightSubSys,
          ()-> 0,
          ()-> 0)
            .withTimeout(2.0),
      
        new ParallelCommandGroup(
          new AutoShoot_Cmd(
            driveSubSys,
            intakeArmSubSys,
            intakeInNOutSubSys,
            limeLightSubSys,
            ()-> 0,
            ()-> 0)
              .withTimeout(1.0),
              
          new IntakeTriggers_Trigger_Cmd(
            intakeTriggersSubSys,
            true,
            true)
              .withTimeout(1))
    */
    );
  }
}
