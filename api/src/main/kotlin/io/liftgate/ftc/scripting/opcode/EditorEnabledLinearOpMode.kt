package io.liftgate.ftc.scripting.opcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.ftc.scripting.ScriptApplicationRunner

/**
 * Creates an OpMode that starts up a web scripting
 * environment on the local network.
 *
 * @author GrowlyX
 * @since 8/20/2023
 */
abstract class EditorEnabledLinearOpMode : LinearOpMode()
{
    private val scriptApp by lazy(::ScriptApplicationRunner)

    override fun runOpMode()
    {
        scriptApp.startApplication()

        while (!isStopRequested)
        {
            // sleep while the opmode is running
            Thread.sleep(100L)
        }

        scriptApp.destroy()
    }
}
