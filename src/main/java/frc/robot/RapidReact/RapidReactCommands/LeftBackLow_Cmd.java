// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.RapidReact.RapidReactCommands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Components.DriveSubSys.DriveSubSys_Old;
import frc.robot.Components.DriveSubSys.SwerveDrives.AM_SwerveSubSys.drive_cmds.DriveSubSys_Drive4Time_Cmd;
import frc.robot.RapidReact.Intake.IntakeArm.IntakeArmSubSys;
import frc.robot.RapidReact.Intake.IntakeArm.IntakeArmCmds.IntakeArm_Pos_Cmd;
// import frc.robot.RapidReact.Intake.IntakeArm.IntakeArmCmds.IntakeArm_Spd_Cmd;
import frc.robot.RapidReact.Intake.IntakeInNOut.IntakeInNOutSubSys;
import frc.robot.RapidReact.Intake.IntakeInNOut.IntakeInNOutCmds.IntakeInNOut_Spd_Cmd;
import frc.robot.RapidReact.Intake.IntakeTriggers.IntakeTriggersSubSys;
import frc.robot.RapidReact.Intake.IntakeTriggers.IntakeTriggersCmds.IntakeTriggers_Trigger_Cmd;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class LeftBackLow_Cmd extends SequentialCommandGroup {
  /** Creates a new BasicAuto_Cmd. */
  public LeftBackLow_Cmd(
    DriveSubSys_Old driveSubSys,
    IntakeArmSubSys intakeArmSubSys,
    IntakeInNOutSubSys intakeInNOutSubSys,
    IntakeTriggersSubSys intakeTriggersSubSys) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());



    //use basic auto arm position command
    addCommands(
      new DriveSubSys_Drive4Time_Cmd(driveSubSys, 0.4, 0, 0, 2.2, true),
    
      new IntakeArm_Pos_Cmd(intakeArmSubSys,80)
        .withTimeout(0.2),

      new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys, 0.4, 0.0)
        .withTimeout(1),

      new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys, 0.6, 0.0)
        .withTimeout(0.75),

      new ParallelCommandGroup(
        new IntakeInNOut_Spd_Cmd(intakeInNOutSubSys, 0.6, 0.0)
        .withTimeout(1),
        
        new IntakeTriggers_Trigger_Cmd(intakeTriggersSubSys, true, true)
        .withTimeout(1)
      ),


      //should be -0.6 speed but I changed it so I didn't run into stuff
      new DriveSubSys_Drive4Time_Cmd(driveSubSys, -0.6, 0, 0, 4.0, true)

    );
  }
}
