package io.liftgate.ftc.scripting.opmode.editor

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.ftc.scripting.ScriptApplicationRunner

/**
 * An OpMode that starts up a web scripting
 * environment on the local network.
 *
 * @author GrowlyX
 * @since 8/20/2023
 */
abstract class StopEditorOpMode : LinearOpMode()
{
    private val scriptApp by lazy(::ScriptApplicationRunner)

    override fun runOpMode()
    {
        if (!scriptApp.isRunning())
        {
            throw IllegalStateException(
                "The script application is not yet running!"
            )
        }

        scriptApp.destroy()
        telemetry.addLine("Destroyed script application!")
        telemetry.update()
    }
}
