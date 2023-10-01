package io.liftgate.ftc.scripting.opmode.editor

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

/**
 * An OpMode that starts up a web scripting
 * environment on the local network.
 *
 * @author GrowlyX
 * @since 8/20/2023
 */
abstract class StartEditorOpMode : LinearOpMode()
{
    override fun runOpMode()
    {
        if (scriptApp.isRunning())
        {
            throw IllegalStateException(
                "The script application is already running!"
            )
        }

        scriptApp.startApplication()
        telemetry.addLine("Started script application on port 6969!")
        telemetry.update()
    }
}
